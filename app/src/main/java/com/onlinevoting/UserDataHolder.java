package com.onlinevoting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class UserDataHolder {
    private static UserDataHolder instance;

    private HashMap<String,Object> userData;
    private Bitmap bitmapUserImage;

    public Bitmap getBitmapUserImage() {
        return bitmapUserImage;
    }

    public void setBitmapUserImage() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String userId= FirebaseAuth.getInstance().getUid();
        try {
            storageRef.child("userspic/" + userId).getBytes(Long.MAX_VALUE)
                    .addOnSuccessListener(bytes -> {
                        // Image data downloaded successfully
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        // Use the bitmap as needed (e.g., display in an ImageView)
                        bitmapUserImage = bitmap;
                        Log.e("error", "IMage loaded sucessfully");

                    })
                    .addOnFailureListener(exception -> {
                        // Handle any errors
                        Log.e("error", exception.getMessage());

                    });
            if(bitmapUserImage==null){
                throw new Exception("Object does not exist at location");
            }
        }catch (Exception e){
            Log.e("errorr",e.getMessage());

        }

    }


    private UserDataHolder() {
        // Private constructor to prevent instantiation
    }

    public static synchronized UserDataHolder getInstance() {
        if (instance == null) {
            instance = new UserDataHolder();
        }
        return instance;
    }

    public void setSharedData(HashMap<String,Object> data) {
        this.userData = data;
    }
    public void setSharedData() {
        String userId = FirebaseAuth.getInstance().getUid();
        DatabaseReference reference= FirebaseDatabase.getInstance("https://online-voting-a8615-default-rtdb.firebaseio.com/").getReference("users");
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,Object> map = (HashMap<String, Object>) snapshot.getValue();
                if(map!=null){
                    userData = map;
                    Log.e("error","data gettting to firebase successfully");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("error",error.getDetails());
            }
        });
    }

    public HashMap<String, Object> getSharedData() {
        return userData;
    }
}
