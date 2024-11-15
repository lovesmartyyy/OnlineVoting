package com.onlinevoting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class SplashActivity extends AppCompatActivity {
  FirebaseAuth auth;
  FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance("https://online-voting-a8615-default-rtdb.firebaseio.com")
            .getReference("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        auth = FirebaseAuth.getInstance();
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        if(auth.getCurrentUser()!=null){
            UserDataHolder.getInstance().setSharedData();
            UserDataHolder.getInstance().setBitmapUserImage();

        handler();
        }else{
            Intent  intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }
    private void setData() {

        String userId = FirebaseAuth.getInstance().getUid();
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,Object> map = (HashMap<String, Object>) snapshot.getValue();
                if(map!=null){
                 UserDataHolder.getInstance().setSharedData();
                 UserDataHolder.getInstance().setBitmapUserImage();
                 MyStorageManager.saveHashMap(SplashActivity.this,map);
                 methodX();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void methodX() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String userId= FirebaseAuth.getInstance().getUid();
        try {
            AtomicReference<Bitmap> bitmapp = new AtomicReference<>();
            storageRef.child("userspic/" + userId).getBytes(Long.MAX_VALUE)
                    .addOnSuccessListener(bytes -> {
                        // Image data downloaded successfully
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        // Use the bitmap as needed (e.g., display in an ImageView)
                        bitmapp.set(bitmap);
                       MyStorageManager.saveBitmap(SplashActivity.this,bitmap,"profileImage");
                        Log.e("error", "IMage loaded sucessfully");

                    })
                    .addOnFailureListener(exception -> {
                        // Handle any errors
                        Log.e("error", exception.getMessage());

                    });
            if(bitmapp.get() ==null){
                throw new Exception("Object does not exist at location");
            }
        }catch (Exception e){
            Log.e("errorr",e.getMessage());

        }
    }

    private void handler() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();

            }
        },100);
    }
}