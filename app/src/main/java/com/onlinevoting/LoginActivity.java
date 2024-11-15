package com.onlinevoting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onlinevoting.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth auth;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();


        binding.loginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.loginEmailAddress.getText().toString();
                String password = binding.loginPassword.getText().toString();

                if(email.isEmpty()){
                    binding.loginEmailAddress.setError("Email is required");


                }else if(!email.matches(emailPattern)){
                    binding.loginEmailAddress.setError("Enter proper email");

                }else if (password.isEmpty()){
                    binding.loginPassword.setError("Password is required");

                }else if (password.length()<6){
                    binding.loginPassword.setError("password is atleast 6 character");

                }else{
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(  AuthResult authResult) {
                                    UserDataHolder.getInstance().setSharedData();
                                    UserDataHolder.getInstance().setBitmapUserImage();
                                    Toast.makeText(LoginActivity.this, "Login Sucessful",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();

                                }
                            });
                }

            }
        });

        binding.loginSignupHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup= new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signup);
                finish();
            }
        });


    }
}






