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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainSearchFragment myFragment_1 = new MainSearchFragment();
        fragmentTransaction.add(R.id.id_content, myFragment_1, "myFragment");
        fragmentTransaction.commit();
    }

    public void jumpFragment(boolean b, String text) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        Fragment current = supportFragmentManager.findFragmentById(R.id.frame);
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

            if (current instanceof BottomOneFragment || current instanceof BottomTwoFragment) {
                System.out.println("current = " + current);
                return;
            } else {
                if (test1F != null) {
                    fragmentTransaction.replace(R.id.frame, test1F);
                } else {
                    test1F = BottomOneFragment.getInstance();
                    fragmentTransaction.replace(R.id.frame, test1F);
                }

            }
        }
        fragmentTransaction.commit();
    }
}
