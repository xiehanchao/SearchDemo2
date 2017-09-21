package com.example.mac.searchdemo.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mac.searchdemo.fragment.BottomOneFragment;
import com.example.mac.searchdemo.fragment.BottomTwoFragment;
import com.example.mac.searchdemo.fragment.MainSearchFragment;
import com.example.mac.searchdemo.R;
import com.example.mac.searchdemo.fragment.SearchHistory;

public class MainSearch extends AppCompatActivity {
    private SearchHistory searchFragment;
    private BottomOneFragment test1F;
    private BottomTwoFragment test2F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainSearchFragment myFragment_1 = new MainSearchFragment();
        fragmentTransaction.add(R.id.id_content, myFragment_1, "myFragment");
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void jumpFragment(boolean b, String text, MainSearchFragment mainSearchFragment, int currentFragment, int btn2Stats) {
        FragmentManager childFragmentManager = mainSearchFragment.getChildFragmentManager();
        FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
        Fragment current = childFragmentManager.findFragmentById(R.id.frame);


        if (b) {

            if (current instanceof SearchHistory) {

                return;
            } else {
                if (searchFragment != null) {
                    fragmentTransaction.replace(R.id.frame, searchFragment);
                } else {
                    searchFragment = new SearchHistory();
                    fragmentTransaction.replace(R.id.frame, searchFragment);
                }
            }
        }
        //请求网络
        else {

            if (!(current instanceof SearchHistory)) {
                if (currentFragment == 1) {
                    BottomOneFragment one = (BottomOneFragment) childFragmentManager.findFragmentByTag("one");
                    one.setInputString(text);
                } else if (currentFragment == 2) {
                    BottomTwoFragment two = (BottomTwoFragment) childFragmentManager.findFragmentByTag("two");
                    two.setInputString(btn2Stats, text);
                }

                return;
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("text", text);


                if (test2F == null) {
                    test2F = new BottomTwoFragment();
                }

                test2F.setArguments(bundle);

                fragmentTransaction.add(R.id.frame, test2F, "two");
                fragmentTransaction.hide(test2F);

                Fragment current1 = childFragmentManager.findFragmentById(R.id.frame);
                System.out.println("current1 = " + current1);
                //初始化A
                if (test1F == null) {
                    test1F = new BottomOneFragment();
                }

                test1F.setArguments(bundle);
                fragmentTransaction.add(R.id.frame, test1F, "one");


            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }
}
