package com.example.mac.searchdemo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mac.searchdemo.R;
import com.example.mac.searchdemo.activity.MainSearch;
import com.example.mac.searchdemo.databinding.ContextText1Binding;
import com.example.mac.searchdemo.databinding.XhcItemlayoutBinding;
import com.example.mac.searchdemo.utils.Utils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yaoyan on 2017/8/30.
 */

public class BottomTwoFragment extends Fragment {
    private static List<String> list = new ArrayList<String>();
    private BottomTwoFragment.HomeAdapter mAdapter;
    private ContextText1Binding binding;
    private int page = 0;
    private XRecyclerView view;
    private Context context;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.context_text1, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String item = bundle.getString("text");
            System.out.println("1111itemtwo = " + item);
        }
        setView(binding.out);
        return binding.out;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setView(final XRecyclerView view) {
        this.view = view;
        for (int i = 10; i <= 40; i++) {
            list.add("one->" + i);
        }
        view.setLayoutManager(new GridLayoutManager(context, 2));
        if (mAdapter == null) {
            mAdapter = new BottomTwoFragment.HomeAdapter();
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
                System.out.println("istory222 = " + history);
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
                System.out.println("page = " + page);
                List<String> list = Utils.LoadMore(page);
                if (list.size() > 0) {
                    BottomTwoFragment.this.list.addAll(list);
                    handle.sendEmptyMessageDelayed(1, 5000);
                } else {
                    handle.sendEmptyMessageDelayed(2, 5000);

                }
            }

        });

    }


    public void setInputString(int i, String inputString) {
        //请求网络
        //Adapter进行刷新处理
        System.out.println("BottomTwoFragment " + inputString + "==" + i);
        view.setNoMore(false);
        page = 0;
        List<String> list = Utils.getList();
        if (list.size() > 0) {
            //请求网络成功
            this.list = list;
            mAdapter.notifyDataSetChanged();
        }
    }

    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mAdapter.notifyDataSetChanged();
                    view.loadMoreComplete();
                    break;
                case 2:
                    view.setNoMore(true);
                    break;
            }
        }
    };
    static class HomeAdapter extends RecyclerView.Adapter<BottomTwoFragment.HomeAdapter.MyViewHolder> {

        private OnItemClickLitener mOnItemClickLitener;

        interface OnItemClickLitener {
            void onItemClick(HomeAdapter.MyViewHolder view, int position);
        }

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public BottomTwoFragment.HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
//            System.out.println("xxxHomeAdapter.getItemCount==" + list.size());
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
    public void onDestroyView() {
        System.out.println("BottomTwoFragment.onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mAdapter=null;
        list.clear();
        view.setNoMore(false);
        page = 0;
        view=null;
        System.out.println("BottomTwoFragment.onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        System.out.println("BottomTwoFragment.onDetach");
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
