package com.example.mac.searchdemo.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mac.searchdemo.R;
import com.example.mac.searchdemo.activity.MainSearch;
import com.example.mac.searchdemo.utils.Utils;
import com.example.mac.searchdemo.view.EditText_Clear;


/**
 * Created by MAC on 2017/8/29.
 */

/**
 * 1.当多次add之后，currentFragment为最后一次的Fragment
 */
public class MainSearchFragment extends Fragment {

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private BottomOneFragment test1F;
    private BottomTwoFragment test2F;
    private RadioButton btn1;
    private RadioButton btn2;
    private Drawable down;
    private Drawable up;
    String text;
    private EditText_Clear editText_clear;
    //1为one
    //2为two
    private int currentFragment = 1;
    //1为btn2箭头向上
    //-1为btn2箭头向下
    private int btn2Stats = 1;
    //文字是否改变了，如果文字改变了，切换btn1和btn2标签应该更新数据。
    private boolean isTextChange = false;
    private Context context;
    private AppBarLayout appbarlayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_w, container, false);
        return view;
    }

    public Fragment currentFragment() {
        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
        return childFragmentManager.findFragmentById(R.id.frame);

    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        System.out.println("MainSearchFragment.onViewCreated");
        final RadioGroup tab = (RadioGroup) view.findViewById(R.id.sliding_tabs1);
        appbarlayout = (AppBarLayout) view.findViewById(R.id.appbarlayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) appbarlayout.getChildAt(0);
        SearchHistory searchH = new SearchHistory();
        final MainSearch mainActivity = (MainSearch) getActivity();
        fragmentManager = getChildFragmentManager();
        down = Utils.getDownDrawable(this.context);
        up = Utils.getUpDrawable(this.context);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, searchH);
        fragmentTransaction.commitAllowingStateLoss();
        btn1 = (RadioButton) view.findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFragment = 1;
                FragmentManager childFragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
                BottomOneFragment one = (BottomOneFragment) childFragmentManager.findFragmentByTag("one");
                BottomTwoFragment two = (BottomTwoFragment) childFragmentManager.findFragmentByTag("two");
                if (isTextChange) {
                    one.setInputString(text);
                    isTextChange = false;
                }
                //one是隐藏的，将其显示
                if (one.isHidden()) {
                    fragmentTransaction.show(one);
                    //two不是隐藏，将其隐藏
                    if (!two.isHidden()) {
                        fragmentTransaction.hide(two);
                    }
                }
                fragmentTransaction.commitAllowingStateLoss();
            }
        });


        btn2 = (RadioButton) view.findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager childFragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
                BottomOneFragment one = (BottomOneFragment) childFragmentManager.findFragmentByTag("one");
                BottomTwoFragment two = (BottomTwoFragment) childFragmentManager.findFragmentByTag("two");
                if (isTextChange && currentFragment != 2) {
                    two.setInputString(btn2Stats, text);
                    isTextChange = false;
                }
                if (currentFragment == 2) {
                    if (btn2Stats == 1) {
                        down.setBounds(0, 0, down.getMinimumWidth(), down.getMinimumHeight());
                        btn2.setCompoundDrawables(null, null, down, null);
                        btn2Stats = -1;
                        two.setInputString(btn2Stats, text);
                    } else {
                        up.setBounds(0, 0, up.getMinimumWidth(), up.getMinimumHeight());
                        btn2.setCompoundDrawables(null, null, up, null);
                        btn2Stats = 1;
                        two.setInputString(btn2Stats, text);
                    }
                }
                currentFragment = 2;
                //two是隐藏的，将其显示
                if (two.isHidden()) {
                    fragmentTransaction.show(two);
                    //one不是隐藏，将其隐藏
                    if (!one.isHidden()) {
                        fragmentTransaction.hide(one);
                    }
                }

                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        editText_clear = (EditText_Clear) view.findViewById(R.id.et_search1);

        if (editText_clear.getTag() == null) {
            editText_clear.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    System.out.println("MainSearchFragment.beforeTextChanged");
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    System.out.println("MainSearchFragment.onTextChanged");
                }

                @Override
                public void afterTextChanged(Editable s) {
                    System.out.println("MainSearchFragment.afterTextChanged=================");
                    text = editText_clear.getText().toString().trim();
                    Fragment fragment = currentFragment();
                    if (!(fragment instanceof SearchHistory)) {
                        isTextChange = true;
                    }
                    if (TextUtils.isEmpty(text)) {
                        currentFragment = 1;
                        btn2Stats = 1;
                        btn1.setChecked(true);
                        AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
                        mParams.setScrollFlags(0);
                        collapsingToolbarLayout.setLayoutParams(mParams);

                        up = Utils.getUpDrawable(MainSearchFragment.this.context);
                        up.setBounds(0, 0, up.getMinimumWidth(), up.getMinimumHeight());
                        btn2.setCompoundDrawables(null, null, up, null);
                        tab.setVisibility(View.GONE);
                        mainActivity.jumpFragment(true, text, MainSearchFragment.this, 0, 0);
                    } else {
                        AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
                        mParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                        collapsingToolbarLayout.setLayoutParams(mParams);
                        tab.setVisibility(View.VISIBLE);
                        mainActivity.jumpFragment(false, text, MainSearchFragment.this, currentFragment, btn2Stats);
                    }
                }
            });
        }
        editText_clear.setTag("first");

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroy() {
        System.out.println("MainSearchFragment.onDestroy");
        currentFragment = 1;
        btn2Stats = 1;
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setHistory(String history) {
        editText_clear.setText(history);
    }
}