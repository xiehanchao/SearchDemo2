package com.example.mac.searchdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yaoyan on 2017/8/31.
 */

public class SearchHistory extends Fragment {

    private TagAdapter<String> tagAdapter;
    private String[] copylist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.searchfragment, null);
        initView(layout);
        return layout;
    }


    private void initView(LinearLayout layout) {
        final List<String> list = new ArrayList<String>();
        final TagFlowLayout tagFlowLayout = (TagFlowLayout) layout.findViewById(R.id.id_flowlayout);
        final RelativeLayout parent = (RelativeLayout) layout.findViewById(R.id.parent_search);
        ImageView search = (ImageView) layout.findViewById(R.id.dellall);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mmp = getActivity().getSharedPreferences("MMP", Context.MODE_PRIVATE);
                String history = mmp.getString("History", "");
                history = "";
                mmp.edit().putString("History", history).commit();
                list.clear();
                tagAdapter.notifyDataChanged();
                parent.setVisibility(View.INVISIBLE);
            }
        });
        final SharedPreferences mmp = getActivity().getSharedPreferences("MMP", Context.MODE_PRIVATE);
        String history = mmp.getString("History", "");

        if (TextUtils.isEmpty(history)) {
            parent.setVisibility(View.INVISIBLE);
            tagFlowLayout.setVisibility(View.INVISIBLE);
        } else {
            String[] split = history.split(",");
            copylist = split;
            if (split.length >= 9) {
                for (int i = 8; i >= 0; i--) {
                    String s = split[i];

                    list.add(s);
                }
            } else if (split.length > 0 && split.length < 9) {
                for (int i = split.length - 1; i >= 0; i--) {
                    String s = split[i];

                    list.add(s);
                }
            } else {

            }
            final LayoutInflater layoutInflater = getActivity().getLayoutInflater();

            tagFlowLayout.setAdapter(tagAdapter = new TagAdapter<String>(list) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) layoutInflater.inflate(R.layout.item_search_tv, tagFlowLayout, false);
                    String s1 = list.get(position);
                    tv.setText(s1);
                    return tv;
                }
            });
            tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {

                    Toast.makeText(getActivity(), list.get(position), Toast.LENGTH_SHORT).show();
                    FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                    MainSearchFragment myFragment = (MainSearchFragment) supportFragmentManager.findFragmentByTag("myFragment");
                    myFragment.setHistory(list.get(position));

                    return true;
                }
            });
        }


    }

    @Override
    public void onResume() {
        System.out.println("SearchHistory.onResume");
        super.onResume();
    }

//    public List<String> getList
}
