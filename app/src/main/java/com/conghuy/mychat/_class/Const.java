package com.conghuy.mychat._class;

import android.app.NotificationManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.conghuy.mychat.PictureDetails;
import com.conghuy.mychat.R;
import com.conghuy.mychat.dto.GetRoomIdDetailsDto;
import com.conghuy.mychat.dto.LoginDto;
import com.conghuy.mychat.dto.MessageDtoDetails;
import com.conghuy.mychat.dto.RoomDtoDetails;
import com.conghuy.mychat.interfaces.Statics;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by maidinh on 5/25/2017.
 */

public class Const {
    public static String TAG = "Const";

    public static int TIME_OUT_UPLOAD_IMAGE = 20000;
    public static final String PDA_ORDER_IMAGE = "PDA_ORDER_IMAGE";
    public static final String DATE_FORMAT_PICTURE = "yyyyMMdd_HHmmss";
    public static final String IMAGE_JPG = ".jpg";
    // for API
    public static final int REQUEST_TIMEOUT_MS = 25000;
    public static final String ROOT_URL = "http://huynhconghuy.esy.es/android_connect/";
    public static final String IMAGE = "fileUpload.php";
    public static final String FILE = "UploadFileChat.php";

    public static final String UpLoadImage = ROOT_URL + IMAGE;

    public static final String UpLoadFILE = ROOT_URL + FILE;

    public static final String INSERT = "create_product.php";
    public static final String GET_ALL = "get_all_products.php";
    public static final String REGISTER = "register_user.php";
    public static final String LOGIN = "login.php";
    public static final String GET_ROOM = "get_all_room_chat.php";
    public static final String GET_MSG_OF_ROOM = "get_msg_of_room.php";
    public static final String SEND_MSG = "send_msg.php";
    public static final String UPDATE_KEY_FCM = "update_reg_id.php";
    public static final String GET_ALL_USER = "get_all_user.php";
    public static final String GET_ROOM_ID = "get_room_id.php";


    public static final String ACTION_RECEIVER_NOTIFICATION = "ACTION_RECEIVER_NOTIFICATION";
    public static final String ACTION_MENU = "ACTION_MENU";
    public static final String MENU_TYPE = "MENU_TYPE";
    public static final String ACTION_RECEIVER_LAST_MSG = "ACTION_RECEIVER_LAST_MSG";
    public static final String FCM_DATA_NOTIFICATOON = "FCM_DATA_NOTIFICATOON";
    public static final String FCM_DATA_OPEN_CHAT = "FCM_DATA_OPEN_CHAT";


    // for login
    public static int REGISTER_SUCCESS = 0;
    public static int MISSING_INPUT_USERNAME = 1;
    public static int MISSING_INPUT_EMAIL = 2;
    public static int MISSING_INPUT_PASSWORD = 3;
    public static int MISSING_INPUT_CONFIRM_PASSWORD = 4;
    public static int MISSING_INPUT_CONFIRM_PASS_DIFFERRENT = 5;

    //for key
    public static final String URL_AV = "URL_AV";
    public static final String TYPE_CHAT = "TYPE_CHAT";
    public static final String LOGIN_DTO = "LOGIN_DTO";
    public static final String ROOM_DTO = "ROOM_DTO";
    public static final String USER_ID = "USER_ID";
    public static final String USER_ID_2 = "USER_ID_2";

    // format time
    public static final String DATE_FORMAT_YY_MM_DD_DD = "yy-MM-dd-EEEEEEE";
    public static final String DATE_FORMAT_YY_MM_DD_DD_H_M = "yy-MM-dd-EEEEEEE hh:mm aa";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DD = "dd";
    public static final String DATE_FORMAT_DD_CHAR = "EEEEEEE";
    public static final String DATE_FORMAT_YYYY_MM_DD_H_M = "yyyy-MM-dd hh:mm aa";
    public static final String DATE_TOOLBAR_FORMAT_YY_MM = "yyyy-MM";
    public static final String DATE_TOOLBAR_FORMAT_DD = "DD";
    public static final String DATE_FORMAT_MONTH = "MM";
    public static final String DATE_FORMAT_YEAR = "yyyy";
    public static final String DATE_FORMAT_MONTH_NAME = "MMM";
    public static final String DATE_FORMAT_HH_MM_AA = "hh:mm aa";


    public static void showMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showMsg(Context context, int id) {
        Toast.makeText(context, getMsg(context, id), Toast.LENGTH_SHORT).show();
    }

    public static String getMsg(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static void showDialog(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getMsg(context, R.string.app_name));
        builder.setMessage(msg);

        String positiveText = getMsg(context, android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        dialog.dismiss();
                    }
                });

//        String negativeText = getMsg(context, android.R.string.cancel);
//        builder.setNegativeButton(negativeText,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // negative button logic
//                    }
//                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public static int _isNew(List<GetRoomIdDetailsDto> roomList) {
        for (int i = 0; i < roomList.size(); i++) {
            for (int j = 0; j <= i; j++) {
                if (roomList.get(i).getChat_room_id() == roomList.get(j).getChat_room_id()) {
                    return roomList.get(i).getChat_room_id();
                }
            }
        }
        return -1;
    }

    public static void setImage(Context context, final ImageView iv, String url) {
        if (url != null && url.trim().length() > 0) {
//            Log.d(TAG, "url:" + url);
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            // call callback when loading error

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            // call callback when loading success

                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            int srcWidth = resource.getWidth();
//                            int srcHeight = resource.getHeight();
//                            int dstWidth = (int) (srcWidth * ratio);
//                            int dstHeight = (int) (srcHeight * ratio);

//                            Bitmap putImage = createScaledBitmap(resource, Const.getDimenInPx(context, R.dimen.location_w), Const.getDimenInPx(context, R.dimen.location_h), true);
//                            iv.setImageBitmap(putImage);

                            iv.setImageBitmap(resource);

                        }
                    });
        } else {
            iv.setImageResource(R.drawable.avatar_l);
        }
    }

    public static void setImageChatting(Context context, final ImageView iv, String url) {
        if (url != null && url.trim().length() > 0) {
//            Log.d(TAG, "url:" + url);
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            // call callback when loading error

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            // call callback when loading success

                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            int srcWidth = resource.getWidth();
//                            int srcHeight = resource.getHeight();
//                            int dstWidth = (int) (srcWidth * ratio);
//                            int dstHeight = (int) (srcHeight * ratio);

//                            Bitmap putImage = createScaledBitmap(resource, Const.getDimenInPx(context, R.dimen.location_w), Const.getDimenInPx(context, R.dimen.location_h), true);
//                            iv.setImageBitmap(putImage);

                            iv.setImageBitmap(resource);

                        }
                    });
        } else {
            iv.setImageResource(R.drawable.picture_temp);
        }
    }

    public static void cancelAllNotification(Context ctx, int id) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(id);
    }

    public static String showTime(long date, String defaultPattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultPattern, Locale.getDefault());
        return simpleDateFormat.format(new Date(date));
    }

    public static Bitmap bitMapRotate(int exifToDegrees, Bitmap bmPhotoResult) {
        //Create object of new Matrix.
        Matrix matrix = new Matrix();
        //set image rotation value to 90 degrees in matrix.
        matrix.postRotate(exifToDegrees);
//        matrix.postScale(0.5f, 0.5f);
        int newWidth = bmPhotoResult.getWidth();
        int newHeight = bmPhotoResult.getHeight();
        //Create bitmap with new values.
        Bitmap bMapRotate = Bitmap.createBitmap(bmPhotoResult, 0, 0, newWidth, newHeight, matrix, true);
        return bMapRotate;
    }

    public static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static Bitmap createScaleBitmap(String pathFile, int reqSize) {
        try {
            // Get the dimensions of the View
            int targetW = reqSize;
            int targetH = reqSize;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathFile, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            Bitmap bmResult = BitmapFactory.decodeFile(pathFile, bmOptions);
            ExifInterface exifReader = new ExifInterface(pathFile);
            int exifOrientation = exifReader.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            int exifToDegrees = exifToDegrees(exifOrientation);

            return bitMapRotate(exifToDegrees, bmResult);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public static void pictureDetails(Context context, String s) {
        Intent intent = new Intent(context, PictureDetails.class);
        intent.putExtra(Const.URL_AV, s);
        context.startActivity(intent);
    }

    public static List<RoomDtoDetails> sort(List<RoomDtoDetails> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j <= i; j++) {
                if (list.get(i).milliseconds > list.get(j).milliseconds) {
                    RoomDtoDetails pp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, pp);
                }
            }
        }
//        for (RoomDtoDetails obj : list) {
//            Log.d(TAG, obj.milliseconds + " : " + obj.last_msg);
//        }
        return list;
    }

    public static MessageDtoDetails convertObject(MessageDtoDetails obj, LoginDto loginDto) {
        if (obj.type == Statics.TYPE_FILE) {
            // TYPE_FILE
        } else if (obj.type == Statics.TYPE_PICTURE) {
            // TYPE_PICTURE
            obj.VIEW_HOLDER = obj.user_id == loginDto.user_id ? Statics.ME_IMAGE : Statics.USER_IMAGE;
        } else {
            // TYPE_MESSAGE
            obj.VIEW_HOLDER = obj.user_id == loginDto.user_id ? Statics.ME_MSG : Statics.USER_MSG;
        }
        return obj;
    }

//    public static List<MessageDtoDetails> convertList(List<MessageDtoDetails> list, LoginDto loginDto) {
//        for (MessageDtoDetails obj : list) {
//
//            if (obj.type == Statics.TYPE_FILE) {
//                // TYPE_FILE
//            } else if (obj.type == Statics.TYPE_PICTURE) {
//                // TYPE_PICTURE
//            } else {
//                // TYPE_MESSAGE
//                obj.VIEW_HOLDER = obj.user_id == loginDto.user_id ? Statics.ME_MSG : Statics.USER_MSG;
//            }
//        }
//        return list;
//    }
}
