package com.conghuy.mychat._class;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.conghuy.mychat.MyApplication;
import com.conghuy.mychat.dto.HttpCallback;
import com.conghuy.mychat.dto.StatusAPI;
import com.conghuy.mychat.interfaces.Statics;
import com.conghuy.mychat.interfaces.UploadInterfaces;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maidinh on 5/25/2017.
 */

public class HttpRequest {
    public static String TAG = "HttpRequest";

    public static void Register(String part, final String name, final String pass, final String email, final HttpCallback callback) {
        String url = Const.ROOT_URL + part;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) response = "";
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFail(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("name", name);
                params.put("password", pass);
                params.put("email", email);
                //returning parameter
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Statics.TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
        requestQueue.add(stringRequest);
    }

    public static void Login(String part, final String name, final String pass, final HttpCallback callback) {
        String url = Const.ROOT_URL + part;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) response = "";
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFail(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("name", name);
                params.put("password", pass);

                //returning parameter
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Statics.TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
        requestQueue.add(stringRequest);
    }

    public static void getRoom(String part, final int user_id, final HttpCallback callback) {
        String url = Const.ROOT_URL + part;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) response = "";
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFail(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_id", "" + user_id);
                //returning parameter
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Statics.TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
        requestQueue.add(stringRequest);
    }

    public static void getMsgOfRoom(String part, final int msgId, final int chat_room_id, final HttpCallback callback) {
        String url = Const.ROOT_URL + part;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) response = "";
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFail(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("chat_room_id", "" + chat_room_id);
                params.put("msgId", "" + msgId);

                //returning parameter
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Statics.TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
        requestQueue.add(stringRequest);
    }

    public static void SendMsg(String part, final String user_name_send, final int chat_room_id, final int user_id, final String message, final HttpCallback callback, final int type) {

        String url = Const.ROOT_URL + part;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) response = "";
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFail(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("user_id", "" + user_id);
                params.put("chat_room_id", "" + chat_room_id);
                params.put("message", message);
                params.put("user_name_send", user_name_send);
                params.put("type", "" + type);

                //returning parameter
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Statics.TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
        requestQueue.add(stringRequest);
    }

    public static void updateKeyFCM(String part, final int user_id, final String regId, final HttpCallback callback) {
        String url = Const.ROOT_URL + part;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) response = "";
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFail(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("user_id", "" + user_id);
                params.put("regId", "" + regId);

                //returning parameter
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Statics.TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
        requestQueue.add(stringRequest);
    }

    public static void GetAllUser(String part, final int user_id, final HttpCallback callback) {
        String url = Const.ROOT_URL + part;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) response = "";
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFail(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("user_id", "" + user_id);


                //returning parameter
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Statics.TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
        requestQueue.add(stringRequest);
    }

    public static void getRoomId(String part, final int my_id, final int user_id, final HttpCallback callback) {
        String url = Const.ROOT_URL + part;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) response = "";
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFail(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("user_id", "" + user_id);
                params.put("my_id", "" + my_id);

                //returning parameter
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Statics.TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
        requestQueue.add(stringRequest);
    }

    public class Upload extends AsyncTask<String, String, String> {
        String url;
        String path;
        UploadInterfaces callback;

        public Upload(String url, String path, UploadInterfaces callback) {
            this.url = url;
            this.path = path;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            MultipartUtility multipart = null;
            try {
//                param(user_id : INT, location_id : INT ,title : TEXT,reason : LONGTEXT,description : LONGTEXT, images : ARRAY, report_status : BOOLEAN [1 : true,  0 : false])
                multipart = new MultipartUtility(url);
//                multipart.addFormField("query", "location_reports");
//                multipart.addFormField("user_id", user_id);
//                multipart.addFormField("location_id", location_id);
//                multipart.addFormField("description", description);
//                multipart.addFormField("report_status", report_status);

                multipart.addFilePart("file", new File(path));

                response = multipart.finish(); // response from server.
//                Log.d(TAG, "response:" + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute:" + result);
            Log.d(TAG, "result:" + result);
            if (result.equals("error")) {
                // error
                callback.onFail();
            } else {
                // check success = 1 -> ok
                if (result.startsWith("{")) {
                    StatusAPI statusAPI = new Gson().fromJson(result, StatusAPI.class);
                    if (statusAPI != null && statusAPI.getSuccess() == 1)
                        callback.onSuccess(statusAPI);
                    else callback.onFail();
                } else {
                    callback.onFail();
                }
            }
        }
    }
}
