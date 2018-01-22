package com.ooftf.hihttp

/**
 * Created by 99474 on 2018/1/22 0022.
 */
class Demo {
    interface  A1<T:Any>{
        fun f1(t:T)
    }
    interface A2<T:String>:A1<T>{
        fun f2(t:T)
        override fun f1(t:T)
    }
    class B1:A1<Any>{
        override fun f1(t: Any) {

        }
    }
    class B2:B1,A2<String>{
        override fun f1(t: String) {

        }
        override fun f2(t: String) {

        }
    }
}