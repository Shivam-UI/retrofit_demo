package com.createdinam.glaucusdemo;

import android.provider.ContactsContract;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EmailListApiHolder {

    @GET("email/")
    Call<List<Emails>> getEmail();

    @POST("email/")
    Call<Emails> createPost(@Body Emails email);

    @PUT("email/{idtableEmail}")
    Call<Emails> putEmail(@Path("idtableEmail") int idtableEmail,@Body Emails emails);

    @DELETE("email/{idtableEmail}")
    Call<Void> deleteEmail(@Path("idtableEmail") int id);
}
