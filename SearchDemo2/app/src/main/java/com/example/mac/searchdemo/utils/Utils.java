package com.example.mac.searchdemo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Yaoyan on 2017/9/12.
 */

public class Utils {

    //创建随机的数组
    public static List<String> getList() {
        List<String> list = new ArrayList<String>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            list.add(random.nextInt() + "");
        }
        return list;
    }

    public static List<String> LoadMore(int position) {

        if (position == 1) {
            List<String> list1 = new ArrayList<String>();
            for (int i = 21; i < 51; i++) {
                list1.add("i==" + i);
            }

            return list1;
        } else if (position == 2) {
            List<String> list2 = new ArrayList<String>();
            for (int i = 52; i < 82; i++) {
                list2.add("i==" + i);
            }

            return list2;
        } else if (position == 3) {
            List<String> list3 = new ArrayList<String>();
            for (int i = 83; i < 113; i++) {
                list3.add("i==" + i);
            }

            return list3;
        }
        return new ArrayList<String>();
    }

}
