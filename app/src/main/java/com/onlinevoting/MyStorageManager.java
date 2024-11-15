package com.onlinevoting;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class MyStorageManager {

    private static final String PREF_NAME = "MyHashMapData";
    private static final String TAG = "BitmapStorageManager";

    public static void saveHashMap(Context context, HashMap<String, Object> hashMap) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(hashMap);
        editor.putString("hashMap", json);
        editor.apply();
    }

    public static HashMap<String, Object> getHashMap(Context context) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("hashMap", null);
            if (json != null) {
                Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
                hashMap = gson.fromJson(json, type);
            }
        } catch (Exception e) {
            Log.e("MyStorageManager", "Error loading HashMap: " + e.getMessage());
        }
        return hashMap;
    }


    public static void saveBitmap(Context context, Bitmap bitmap, String filename) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (IOException e) {
            Log.e(TAG, "Error saving bitmap: " + e.getMessage());
        }
    }

    public static Bitmap loadBitmap(Context context, String filename) {
        Bitmap bitmap = null;
        try {
            File filePath = context.getFileStreamPath(filename);
            FileInputStream fis = new FileInputStream(filePath);
            bitmap = BitmapFactory.decodeStream(fis);
            fis.close();
        } catch (IOException e) {
            Log.e(TAG, "Error loading bitmap: " + e.getMessage());
        }
        return bitmap;
    }

}
