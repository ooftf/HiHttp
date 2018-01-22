package com.ooftf.hihttp;

/**
 * Created by 99474 on 2018/1/22 0022.
 */

public class DemoJava {
    interface A1<T>{
        void f1(T t);
    }
    static class B1 implements A1{

        @Override
        public void f1(Object o) {

        }
    }
    interface A2<T extends String> extends A1<T>{
        void f2(T t);
    }
    static class B2 extends B1 implements A2<String> {

        @Override
        public void f1(String s) {

        }
        @Override
        public void f2(String s) {

        }
    }

}
