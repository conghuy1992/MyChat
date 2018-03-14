package com.conghuy.mychat.interfaces;

import com.conghuy.mychat.dto.StatusAPI;

/**
 * Created by Huy on 24/6/2017.
 */

public interface UploadInterfaces {
    void onProgressUpdate(int values);
    void onSuccess(StatusAPI statusAPI);
    void onFail();
}
