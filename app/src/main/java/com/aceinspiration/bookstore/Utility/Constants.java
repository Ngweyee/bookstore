package com.aceinspiration.bookstore.Utility;

import android.os.Environment;

import com.aceinspiration.bookstore.R;

import java.io.File;

public class Constants {

    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_CUST_ID = "customer_id";


    public static final String COLUMN_CUSTOMER_ID = "_id";
    public static final String COLUMN_IMAGE_PATH = "imagePath";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_STREET = "street";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_ZIP_CODE = "zipCode";

    public static final String KEY_IMAGE_URI = "image_uri";

    public static final String KEY_IMAGE_PATH = "key_image_path";
    public static int ACTION_REQUEST_IMAGE = 1000;
    public static int SELECT_IMAGE = 1001;
    public static final String TAKE_PHOTO = "Take Photo";
    public static final String CHOOSE_FROM_GALLERY = "Choose from gallery";
    public static final String CANCEL = "Cancel";

    public static final int DEFAULT_IMAGE_RESOURCE = R.drawable.default_customer_picture;
    public static final String PICTURE_DIRECTORY = Environment.getExternalStorageDirectory()
            + File.separator + "DCIM" + File.separator + "ProfilePicture" + File.separator;

}
