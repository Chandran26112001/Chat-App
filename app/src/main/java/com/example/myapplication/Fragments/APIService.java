package com.example.myapplication.Fragments;

import com.example.myapplication.Notifications.MyResponse;
import com.example.myapplication.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAADn-bOCc:APA91bGx_PwIhTKrPiXdzViX_nM7pSbZvJEQf7Q0PoG2cqXr46ahbiyRnmoQgfK3uBG5JWc24w0aojnR32Xsl0nZtn0vR7T7cDTRoRMb1QgkDwIrn9iD3rTOcKq6s9Nm6p24aCBMHw9t"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
