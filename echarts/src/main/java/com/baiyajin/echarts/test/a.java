package com.baiyajin.echarts.test;

public class a {
    public static void main(String[] args){
        doubleNum(1237);
    }
     static void doubleNum(int n)

    {

//        System.out.println(n);

        if(n<=5000)

            doubleNum(n*2);

        System.out.println(n);

    }
}
