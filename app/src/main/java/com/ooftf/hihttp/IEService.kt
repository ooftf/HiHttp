package com.ooftf.hihttp
import retrofit2.http.*
import io.reactivex.Observable
/**
 * Created by master on 2017/1/19.
 */

interface IEService {
    @FormUrlEncoded
    @POST("service/user/login")
    fun signIn(@Field("useLoginName") username: String,
                        @Field("useLoginPswd") password: String,
                        @Field("identify") PIN: String,
                        @Field("uuid") uuid: String): Observable<BaseBean>
    @POST("service/system/identify")
    fun picCaptcha(): Observable<PicCaptchaBean>
}
