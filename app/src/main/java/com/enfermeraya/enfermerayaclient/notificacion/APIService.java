package com.enfermeraya.enfermerayaclient.notificacion;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAjam3JLE:APA91bGNpDYH4v7oL2Ik8plXaKi60l8pyuWucBoAxmjZ0-TZlWSamGvF5r6Sjg3snoPNlSd7ugQtlK18-Rbzs5ISoUprY_-v200uIrjdsuWyE6fBkwYyjyq_MlHreD-MwAOf6o57iCvo" //Clave del servidor 
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

