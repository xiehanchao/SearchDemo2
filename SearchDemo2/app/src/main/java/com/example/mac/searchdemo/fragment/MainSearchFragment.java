package com.example.mac.searchdemo.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mac.searchdemo.R;
import com.example.mac.searchdemo.activity.MainSearch;
import com.example.mac.searchdemo.view.EditText_Clear;


/**
 * Created by MAC on 2017/8/29.
 */

public class MainSearchFragment extends Fragment {

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private BottomOneFragment test1F;
    private BottomTwoFragment test2F;
    private RadioButton btn1;
    private RadioButton btn2;
    private int price_sort = 1;
    private Drawable down;
    private Drawable up;
    String text;
    private String histoyry;
    private EditText_Clear editText_clear;
    private boolean isCheck = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_w, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        final RadioGroup tab = (RadioGroup) view.findViewById(R.id.sliding_tabs1);
        SearchHistory searchH = new SearchHistory();
        final MainSearch mainActivity = (MainSearch) getActivity();
        fragmentManager = getChildFragmentManager();
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            down = getResources().getDrawable(R.drawable.down, getContext().getTheme());
            up = getResources().getDrawable(R.drawable.up, getContext().getTheme());
        } else {
            down = getResources().getDrawable(R.drawable.down);
            up = getResources().getDrawable(R.drawable.up);
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, searchH);
        fragmentTransaction.commit();
        btn1 = (RadioButton) view.findViewById(R.id.btn1);
        btn1.setChecked(true);
        btn2 = (RadioButton) view.findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test2F = BottomTwoFragment.getInstance();
                if (isCheck) {
                    price_sort = 1;
                    up.setBounds(0, 0, up.getMinimumWidth(), up.getMinimumHeight());
                    btn2.setCompoundDrawables(null, null, up, null);
                    test2F.setInputString(1, text);
                    isCheck = false;
                    return;
                }
                if (price_sort == 1) {
                    price_sort = 2;
                    down.setBounds(0, 0, down.getMinimumWidth(), down.getMinimumHeight());
                    btn2.setCompoundDrawables(null, null, down, null);
                    test2F.setInputString(2, text);
                } else {
                    price_sort = 1;
                    up.setBounds(0, 0, up.getMinimumWidth(), up.getMinimumHeight());
                    btn2.setCompoundDrawables(null, null, up, null);
                    test2F.setInputString(1, text);
                }
            }
        });
        editText_clear = (EditText_Clear) view.findViewById(R.id.et_search1);

        editText_clear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                text = editText_clear.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    AppBarLayout appbarlayout = (AppBarLayout) view.findViewById(R.id.appbarlayout);
                    CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) appbarlayout.getChildAt(0);
                    AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
                    mParams.setScrollFlags(0);
                    collapsingToolbarLayout.setLayoutParams(mParams);

                    if (test2F == null) {
                        test2F = BottomTwoFragment.getInstance();

                    }
                    price_sort = 1;
                    test2F.sort = 1;
                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        up = getResources().getDrawable(R.drawable.up, getContext().getTheme());
                    } else {
                        up = getResources().getDrawable(R.drawable.up);
                    }
                    up.setBounds(0, 0, up.getMinimumWidth(), up.getMinimumHeight());
                    btn2.setCompoundDrawables(null, null, up, null);

                    btn1.setChecked(true);
                    tab.setVisibility(View.GONE);
                    mainActivity.jumpFragment(true, text);
                } else {
                    AppBarLayout appbarlayout = (AppBarLayout) view.findViewById(R.id.appbarlayout);
                    CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) appbarlayout.getChildAt(0);
                    AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
                    mParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                    collapsingToolbarLayout.setLayoutParams(mParams);
                    if (btn1.isChecked()) {
                        if (test1F == null) {
                            test1F = BottomOneFragment.getInstance();
                        }
                        test1F.setInputString(text);
                    }
                    if (btn2.isChecked()) {
                        if (test2F == null) {
                            test2F = BottomTwoFragment.getInstance();
                        }
                        test2F.setInputString(1, text);
                    }
                    tab.setVisibility(View.VISIBLE);
                    mainActivity.jumpFragment(false, text);
                }
            }
        });


        tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                text = editText_clear.getText().toString().trim();
                if (checkedId == btn1.getId()) {

                    isCheck = false;
                    btn1.setBackgroundResource(R.drawable.draw_line);
                    btn2.setBackgroundResource(R.drawable.white_back);

                    if (test1F == null) {
                        test1F = BottomOneFragment.getInstance();
                    }
                    fragmentTransaction.replace(R.id.frame, test1F);
                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        up = getResources().getDrawable(R.drawable.up, getContext().getTheme());
                    } else {
                        up = getResources().getDrawable(R.drawable.up);
                    }
                    test1F.setInputString(text);
                } else if (checkedId == btn2.getId()) {

                    btn2.setBackgroundResource(R.drawable.draw_line);
                    btn1.setBackgroundResource(R.drawable.white_back);
                    if (test2F == null) {
                        test2F = BottomTwoFragment.getInstance();
                    }
                    fragmentTransaction.replace(R.id.frame, test2F);
                    isCheck = true;
                }
                fragmentTransaction.commit();
            }

        });

    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }
    @Override
    public void onResume() {
        super.onResume();
    }

    public void setHistory(String history) {
        editText_clear.setText(history);
    }
}