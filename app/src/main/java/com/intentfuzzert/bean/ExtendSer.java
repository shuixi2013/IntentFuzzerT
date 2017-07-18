package com.intentfuzzert.bean;

/**
 * Created by i-weiguobin on 2017/6/19.
 */

public class ExtendSer extends SerializableTest {


    public ExtendSer(Boolean a,int b,String c)
    {
        this.a=a;
        this.b=b;
        this.c=c;


    }

    private Boolean  a;
    private int b;
    private String c;
}
