package com.onlinevoting;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.units.qual.C;

public class CustomDialog extends Dialog {
   Context context;
    private String data;
    private String name;
    private String functionName;
    Button submitButton;
    EditText editText;
    TextView dataTextView,nameTextView;
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String dobPattern ="\\b\\d{1,2}/\\d{1,2}/\\d{4}\\b";
    public CustomDialog(Context context, String data, String name,String funName) {
        super(context);
        this.context =context;
        this.data = data;
        this.name = name;
        this.functionName=funName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the custom layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_dialog_layout,null);
        setContentView(R.layout.custom_dialog_layout);

        // Get references to views
         dataTextView = findViewById(R.id.dataTextView);
         nameTextView= findViewById(R.id.nameTextView);
        editText = findViewById(R.id.editText);
        submitButton= findViewById(R.id.submitButton);

        // Set bold style to dataTextView
        dataTextView.setTypeface(dataTextView.getTypeface(), Typeface.BOLD);

        // Set data to views
        editText.setText("");
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forGenderAndDob();
            }
        });
        if(functionName.equals("name")){
            dataTextView.setText(data);
            nameTextView.setText(name);
            editText.setHint("enter your name");

        }else if(functionName.equals("email")){
            dataTextView.setText(data);
            nameTextView.setText(name);
            editText.setHint("enter your email");

        } else if (functionName.equals("dob")) {
            dataTextView.setText(data);
            nameTextView.setText(name);
            editText.setHint("12/3/2000");


        }else if(functionName.equals("gender")){
            dataTextView.setText(data);
            nameTextView.setText(name);
            editText.setHint("enter your gender");

        } else if (functionName.equals("address")) {
            dataTextView.setText(data);
            nameTextView.setText(name);
            editText.setHint("eneter your address");

        }else if(functionName.equals("password")){
            dataTextView.setText(data);
            nameTextView.setText(name);
            editText.setHint("eneter your password");
        }



        // Set click listener to submitButton
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(functionName.equals("name")){
                   changeName();

                }else if(functionName.equals("email")){
                    chaangeEmail();




                } else if (functionName.equals("dob")) {
                    changDob();




                }else if(functionName.equals("gender")){
                    changeGender();



                } else if (functionName.equals("address")) {
                  changeAddress();



                }else if(functionName.equals("password")){
                    changePassword();

                }

            }
        });
    }

    private void changePassword() {
        FirebaseUser user = auth.getCurrentUser();
        String password = editText.getText().toString().trim();
//        user.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Toast.makeText(getContext(),"Password Changed Sucessfully"
//                ,Toast.LENGTH_LONG).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e("error",e.getMessage());
//            }
//        });
        Toast.makeText(getContext(),"currently faceing some issue",Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private void changeAddress() {
        String address = editText.getText().toString();
        String userId = auth.getUid();
        if(address.equals("")){
            editText.setError("enter your address");
        }else if(address.length()<20){
            editText.setError("enter minimum of 20 character");
        }else {
        DatabaseReference userRef = databaseRef.child("users").child(userId);
        userRef.child("address").setValue(address).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(),"Gender Changed Sucessfully",Toast.LENGTH_LONG).show();
                Log.d("debug","dob get updated on database");
                dismiss();
            }
        });
        }

    }

    private void changeGender() {
        String gender = editText.getText().toString().toLowerCase();
        String userId = auth.getUid();
        if(gender.equals("")){
            editText.setError("enter your gender m/f/n");
        }else if(gender.equals("male")){
            funForGender( gender,userId);
        }else if(gender.equals("female")){

        }else if(gender.equals("none")){
            funForGender("none",userId);
        }else if(gender.equals("n")){
            funForGender("none",userId);
        }else if(gender.equals("m")){
            funForGender("male",userId);
        }else if(gender.equals("f")){
            funForGender("female",userId);
        }else {
            editText.setError("enter correct way of gender ");
        }



    }

    private void funForGender(String gender,String userId) {
        DatabaseReference userRef = databaseRef.child("users").child(userId);
        userRef.child("gender").setValue(gender).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(),"successfully changed your gender",Toast.LENGTH_LONG).show();
                Log.d("debug","dob get updated on database");
                dismiss();
            }
        });
    }

    private void changDob() {
        String dob = editText.getText().toString();
        String userId = auth.getUid();
        if(dob.equals("")){
            editText.setError("enter your dob like 12/3/2000");
        }else if(!dob.matches(dobPattern)){
            editText.setError("enter dob in this pattern 12/9/2002");
        }else if (dob.matches(dobPattern)) {
            DatabaseReference userRef = databaseRef.child("users").child(userId);
            userRef.child("dob").setValue(dob).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getContext(),"dob changed Sucessfully",Toast.LENGTH_LONG).show();
                    Log.d("debug", "dob get updated on database");
                    dismiss();
                }
            });
        }
    }

    private void chaangeEmail() {
        String email = editText.getText().toString();
        if(email.matches(emailPattern)){
//            FirebaseUser user = auth.getCurrentUser();
//            user.verifyBeforeUpdateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//                    String userId = auth.getUid();
//                    DatabaseReference userRef = databaseRef.child("users").child(userId);
//                    userRef.child("email").setValue(email).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Log.d("debug","email get updated on database");
//                        }
//                    });
//
//                    Log.d("debug","emailupdated of user email"+user.getEmail());
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
//                    Log.d("error","email do not updated of user email    "+e.getMessage());
//                }
//            });
//
         Toast.makeText(getContext(),"currently facing Some issuees",Toast.LENGTH_SHORT).show();
         dismiss();
        }else{
            Toast.makeText(getContext(),"currently facing Some issuees",Toast.LENGTH_SHORT).show();
            editText.setError("enter correct email");
            dismiss();

        }


    }

    private void changeName() {
        // Get a reference to the Firebase database


        // Assuming you have a "users" node in your database with a child node for each user
        // Here we are updating the "name" field of a specific user with ID "userId"
        String userId = auth.getUid();
        String newName = editText.getText().toString();
        if(name.equals("")){
            editText.setError("enter your name");
        }else if(name.length()<=5){
            editText.setError("enter minimum of 5 character name");
        }else {

            // Create a reference to the specific user node you want to update
            DatabaseReference userRef = databaseRef.child("users").child(userId);
            // Update the "name" field of the user
            userRef.child("name").setValue(newName)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // for doing thing when task is completed
                            if (task.isSuccessful()) {
                                // Toast.makeText(getContext(),"Name changed sucessfully",Toast.LENGTH_LONG).show();
                                Log.d("debug", "sucessfully changeed name");
                                Toast.makeText(getContext(),"name was sucessfully changed",Toast.LENGTH_LONG).show();
                                dismiss();
                                // CustomDialog.dismiss();
                            } else {
                                Log.e("error", task.getException().getMessage());
                            }

                        }
                    });
        }
    }
    private void forGenderAndDob() {
        if(functionName.equals("dob")){
            // Create a date picker dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // Display the selected date in the EditText
                    editText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }
            }, 2000, 8, 15);

            // Show the date picker dialog
            datePickerDialog.show();
        }else if(functionName.equals("gender")){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Select Your Gender");
            builder.setMessage("select your gender in these " +
                    "three none /male/female");

// Set positive button
            builder.setPositiveButton("Male", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle positive button click
                    // For example, you can perform an action or dismiss the dialog
                    editText.setText("male");
                    dialog.dismiss();
                }
            });

// Set neutral button
            builder.setNeutralButton("None", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle neutral button click
                    // For example, you can perform an action or dismiss the dialog
                    editText.setText("none");
                    dialog.dismiss();
                }
            });

// Set negative button
            builder.setNegativeButton("Female", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle negative button click
                    // For example, you can perform an action or dismiss the dialog
                    editText.setText("female");
                    dialog.dismiss();
                }
            });

// Create and show the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();

        }

    }


}

