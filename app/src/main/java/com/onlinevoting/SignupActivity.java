package com.onlinevoting;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onlinevoting.databinding.ActivitySignupBinding;

import java.util.ArrayList;
import java.util.Arrays;


public class SignupActivity extends AppCompatActivity {
    // private FirebaseAuth auth
    ActivitySignupBinding binding;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String dobPattern ="^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[0-2])/(19|20)\\d{2}$";
    FirebaseAuth auth;
    String selectedGender ;
    DatabaseReference ref = FirebaseDatabase.getInstance("https://online-voting-a8615-default-rtdb.firebaseio.com/").getReference("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        spinner();

        binding.signupSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               signup();
            }


        });
        binding.signupLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });


    }

    private void signup() {

        String firstName = binding.signupFirstName.getText().toString();
        String secondName = binding.signupSecondName.getText().toString();
        String dob = binding.signupDob.getText().toString();
        String email = binding.signupEmailAddress.getText().toString();
        String password = binding.signupPassword.getText().toString();
        String confirmPassword = binding.signupConfirmPassword.getText().toString();


        if(TextUtils.isEmpty(firstName)){
            binding.signupFirstName.setError("First Name is required");

        } else if(TextUtils.isEmpty(secondName)){
            binding.signupSecondName.setError("Second Name is required");

        }else if(selectedGender.trim().isEmpty()){
            Toast.makeText(SignupActivity.this,"empty",Toast.LENGTH_LONG).show();

        }else if(selectedGender.trim().equals("none")){
            Toast.makeText(SignupActivity.this,"select gender",Toast.LENGTH_LONG).show();

        }else if(TextUtils.isEmpty(dob)){
            binding.signupDob.setText("");
            binding.signupDob.setHint("ente your date of birth birth");

        } else if(!dob.matches(dobPattern)){
            binding.signupDob.setText("");
            binding.signupDob.setHint("day/month/year");

        } else if(TextUtils.isEmpty(email)){
            binding.signupEmailAddress.setError("Email is required");

        } else if(!email.matches(emailPattern)){
            binding.signupEmailAddress.setError("Enter correct email");

        } else if (password.isEmpty() || password.length() < 6 ){
            binding.signupPassword.setError("Enter proper password");

        } else if ( !password.equals(confirmPassword)){
            binding.signupConfirmPassword.setError("Password not matched");


        }else{auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(SignupActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            FirebaseUser currentUser = auth.getCurrentUser();
                            String userId = currentUser.getUid();

                            ref.child(userId).child("name").setValue(firstName+secondName);
                            ref.child(userId).child("email").setValue(email);
                            ref.child(userId).child("password").setValue(password);
                            ref.child(userId).child("dob").setValue(dob);
                            ref.child(userId).child("gender").setValue(selectedGender);
                            UserDataHolder.getInstance().setSharedData();
                            UserDataHolder.getInstance().setBitmapUserImage();

                            Toast.makeText(SignupActivity.this, "Sucessfully Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));


                        }else{
                            Toast.makeText(SignupActivity.this, "Not Registered "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        }
    }

    private void spinner() {

         String[] gender = getResources().getStringArray(R.array.dropDowmMaleOrFemale);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignupActivity.this, android.R.layout.simple_spinner_item, gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.singupGenderSpinner.setAdapter(adapter);

        binding.singupGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String value = parent.getItemAtPosition(position).toString();
                    selectedGender = value;
                    Toast.makeText(SignupActivity.this,value,Toast.LENGTH_LONG).show();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
        });


    }

    public void onClick(View v){
        if(v.getId()==R.id.singup_dob_picker){
            new DatePickerDialog(this, R.style.dobPicker,new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    binding.signupDob.setText(dayOfMonth+"/"+month+"/"+year);
                }
            },2000,0,0).show();

        }
    }
}