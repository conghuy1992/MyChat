package com.conghuy.mychat._class;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.conghuy.mychat.dto.StatusAPI;
import com.conghuy.mychat.interfaces.UploadInterfaces;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by maidinh on 6/23/2017.
 */

public class UpLoadFile {
    public static String TAG = "UpLoadFile";

    public static class UploadFileAsync extends AsyncTask<String, String, String> {
        String upLoadServerUri = "";
        String sourceFileUri = "";
        int userId;
        UploadInterfaces callback;

        public UploadFileAsync(String sourceFileUri, String upLoadServerUri, int userId, UploadInterfaces callback) {
            this.sourceFileUri = sourceFileUri;
            this.upLoadServerUri = upLoadServerUri;
            this.userId = userId;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String responseString = null;
            String response = "error";
            int count;
            try {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(sourceFileUri);
                Log.d(TAG, "sourceFile:" + sourceFile.length());
                if (sourceFile.isFile()) {
                    try {
                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(
                                sourceFile);
                        URL url = new URL(upLoadServerUri);
                        // Open a HTTP connection to the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("image", sourceFileUri);

                        dos = new DataOutputStream(conn.getOutputStream());

                        // add parameter timeline_id
                        String user_id = "" + userId;
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"user_id\"" + lineEnd);
                        dos.writeBytes(lineEnd);

                        // assign value
                        dos.writeBytes(user_id);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + lineEnd);


                        String description = "hello word";
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"description\"" + lineEnd);
                        dos.writeBytes(lineEnd);

                        // assign value
                        dos.writeBytes(description);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + lineEnd);


                        // send image
                        String s = "Content-Disposition: form-data; name=\"image\";filename=\"" + sourceFileUri + "\"" + lineEnd;
                        Log.d(TAG, "s:" + s);
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes(s);
                        dos.writeBytes(lineEnd);


                        // create a buffer of maximum size
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];
                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                        while (bytesRead > 0) {
                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                        }
                        // send multipart form data necesssary after file
                        // data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens
                                + lineEnd);


//                        progressUpdate
//                        int progress = 0;
//                        int read = 0;
//                        byte buf[] = new byte[1024];
//                        BufferedInputStream bufInput = new BufferedInputStream(
//                                new FileInputStream(sourceFile));
//                        long lenghtOfFile = sourceFile.length();
//                        while ((read = bufInput.read(buf)) != -1) {
//                            // write output
//                            dos.write(buf, 0, read);
//                            dos.flush();
//                            progress += read;
//                            // update progress bar
//                            publishProgress("" + (int) ((progress * 100) / lenghtOfFile));
//                        }


                        // Responses from the server (code and message)
                        int serverResponseCode = conn.getResponseCode();
                        Log.d(TAG, "serverResponseCode:" + serverResponseCode);
                        String serverResponseMessage = conn
                                .getResponseMessage();

                        if (serverResponseCode == 200) {
                            // messageText.setText(msg);
                            //Toast.makeText(ctx, "File Upload Complete.",
                            //      Toast.LENGTH_SHORT).show();

                            // recursiveDelete(mDirectory1);
                            response = "true";
                        } else {
                            response = "false";
                        }
                        // close the streams //
                        fileInputStream.close();
                        dos.flush();

                        conn.getInputStream();
                        //for android InputStream is = connection.getInputStream();
                        java.io.InputStream is = conn.getInputStream();

                        int ch;
                        StringBuffer b = new StringBuffer();
                        while ((ch = is.read()) != -1) {
                            b.append((char) ch);
                        }
                        responseString = b.toString();
                        System.out.println("response string is" + responseString); //Here is the actual output
                        dos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } // End else block
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (response.equalsIgnoreCase("true"))
                return responseString;
            else {
                return "error ";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
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

        @Override
        protected void onProgressUpdate(String... values) {
            // setting progress percentage
//            Log.d(TAG, "value:" + Integer.parseInt(values[0]));
            callback.onProgressUpdate(Integer.parseInt(values[0]));
        }
    }

}
