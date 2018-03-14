package com.conghuy.mychat._class;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.conghuy.mychat.MyApplication;
import com.conghuy.mychat.dto.ErrorDto;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by maidinh on 5/25/2017.
 */

public class WebServiceManager {
    String TAG = "WebServiceManager";

    public interface RequestListener<T> {
        void onSuccess(T response);

        void onFailure(ErrorDto error);
    }

    public void doJsonObjectRequest(int requestMethod, final String url, final JSONObject bodyParam, final RequestListener<String> listener) {
        Log.d(TAG, "bodyParam:" + new Gson().toJson(bodyParam));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestMethod, url, bodyParam, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "response:" + response);
                listener.onSuccess(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorDto errorDto = new ErrorDto();
                listener.onFailure(errorDto);
            }
        });
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest, url);
    }
}
