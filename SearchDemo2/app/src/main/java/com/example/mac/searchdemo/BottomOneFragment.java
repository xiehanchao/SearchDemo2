package com.example.mac.searchdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mac.searchdemo.databinding.ContextText1Binding;
import com.example.mac.searchdemo.databinding.XhcItemlayoutBinding;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Yaoyan on 2017/8/30.
 */

public class BottomOneFragment extends Fragment {
    private static List<String> list = new ArrayList<String>();
    private HomeAdapter mAdapter;
    private String inputString;
    private ContextText1Binding binding;
    private int page = 1;
    private int pageCount = 1;
    //类加载时就初始化
    private static final BottomOneFragment instance = new BottomOneFragment();
    private XRecyclerView view;

    private BottomOneFragment() {

    }

    public static BottomOneFragment getInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.context_text1, container, false);
        setView(binding.out);
        return binding.out;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (int i = 10; i < 100; i++) {
            list.add("one->" + i);
        }
    }

    public void setView(final XRecyclerView view) {
        this.view = view;
        view.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        if (mAdapter == null) {
            mAdapter = new HomeAdapter();
        }
        mAdapter.setOnItemClickLitener(new HomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(HomeAdapter.MyViewHolder view, int position) {


                XhcItemlayoutBinding xhcItemlayoutBinding = (XhcItemlayoutBinding) view.getBinding();
                String title = xhcItemlayoutBinding.tv.getText().toString();
                SharedPreferences mmp = getActivity().getSharedPreferences("MMP", Context.MODE_PRIVATE);
                String history = mmp.getString("History", "");
                if (TextUtils.isEmpty(history)) {
                    history = title;
                } else {
                    int flag = -1;
                    String[] split = history.split(",");
                    for (int i = 0; i < split.length; i++) {
                        if (title.equals(split[i])) {
                            flag = i;
                            break;
                        } else {

                        }
                    }
                    if (flag != -1) {
                        history = "";
                        String temp = "";
                        for (int j = 0; j < split.length; j++) {
                            if (j == flag) {
                                temp = split[j];
                                continue;
                            } else {
                                history = history + "," + split[j];
                            }
                        }
                        history = history.substring(1, history.length());
                        history = history + "," + temp;
                    } else {
                        history = history + "," + title;
                    }
                }
                System.out.println("istory111 = " + history);
                mmp.edit().putString("History", history).commit();
            }
        });
        view.setAdapter(mAdapter);
        view.setHasFixedSize(true);
        view.setPullRefreshEnabled(false);
        view.setLoadingMoreEnabled(true);
        view.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);

        view.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                ++page;
//                loadDate(BottomOneFragment.this.inputString);
            }

        });

    }

    private Context context;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public void setInputString(String inputString) {

        if (page > 1) {

            page = 1;
            BottomOneFragment.this.view.setNoMore(false);
        }
        System.out.println("xxxinputString = " + inputString);
        this.inputString = inputString;
        if (mAdapter == null) {
            mAdapter = new HomeAdapter();
        }
//        loadDate(this.inputString);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    BottomOneFragment.this.view.loadMoreComplete();
                    mAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    BottomOneFragment.this.view.setNoMore(true);
                    mAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    mAdapter.notifyDataSetChanged();
                    break;

            }

        }
    };

    static class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        private OnItemClickLitener mOnItemClickLitener;

        interface OnItemClickLitener {
            void onItemClick(HomeAdapter.MyViewHolder view, int position);
        }

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }


        @Override
        public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.xhc_itemlayout, null, false);
            MyViewHolder holder = new MyViewHolder(binding);
            return holder;
        }

        @Override
        public void onBindViewHolder(final HomeAdapter.MyViewHolder holder, final int position) {

            if (mOnItemClickLitener != null) {

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(holder, position);
                    }
                });
            }

            XhcItemlayoutBinding binding = (XhcItemlayoutBinding) holder.getBinding();
            String listBean = list.get(position);
            binding.tv.setText(listBean);
            binding.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            System.out.println("xxxHomeAdapter.getItemCount==" + list.size());
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private ViewDataBinding binding;

            public MyViewHolder(ViewDataBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public ViewDataBinding getBinding() {
                return binding;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("xxxtest1Fragment.onResume");
        Fragment fragment = ((MainSearch) getActivity()).getSupportFragmentManager().findFragmentById(R.id.frame);
        if (fragment instanceof BottomOneFragment) {
            System.out.println("MainSearchFragment.queryData1111111111=");
        }
        if (fragment instanceof BottomTwoFragment) {
            System.out.println("MainSearchFragment.queryData22222222222");
        }
    }

    @Override
    public void onDestroyView() {
        System.out.println("BottomOneFragment.onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        list.clear();
        page = 1;
        System.out.println("BottomOneFragment.onDestroy");
        super.onDestroy();
    }
}
