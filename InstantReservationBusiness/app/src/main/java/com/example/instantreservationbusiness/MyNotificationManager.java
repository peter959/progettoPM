package com.example.instantreservationbusiness;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyNotificationManager {
    RequestQueue mRequestQueue;
    String URL = "https://fcm.googleapis.com/fcm/send";
    String topicDestination;
    String queueName;
    String queueBusiness;


    public MyNotificationManager(Context context, String topicDestination, String queueName, String queueBusiness ) {
        this.topicDestination = topicDestination;
        this.queueName = queueName;
        this.queueBusiness = queueBusiness;
        mRequestQueue = Volley.newRequestQueue(context);
        //CLIENT
        //FirebaseMessaging.getInstance().subscribeToTopic(topicDestination);
    }

    public void sendNotification() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+topicDestination);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","It's your turn for the queue: " + queueName);
            notificationObj.put("body",queueBusiness);

            json.put("notification",notificationObj);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAJ9QVTnE:APA91bFTVoDSyFqeVqG70atokpsG4s24FRKFSazw-VYlNnopatehRWRv3sNy5mXUxlwz7FWOiW2iFNbLmdXaSpACi9tVS-PeyO2BkOcbWgw82vNhjIrT6i4BngE5XflhCtj6YJB_Lqh-");
                    return header;
                }
            };
            mRequestQueue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }

}
