package com.onlinevoting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {


    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance("https://online-voting-a8615-default-rtdb.firebaseio.com")
            .getReference("users");
    TextView name , email, gender,dob,address,namee , password;
    ImageView edit;
    CircleImageView image;
    StorageReference storageReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container,false);
        storageReference = FirebaseStorage.getInstance().getReference();
        name= rootView.findViewById(R.id.profile_name);
        email =rootView.findViewById(R.id.profile_email);
        gender =rootView.findViewById(R.id.profile_gender);
        dob = rootView.findViewById(R.id.profile_dob);
        address = rootView.findViewById(R.id.profile_address);
        namee = rootView.findViewById(R.id.profile_profile_name);
        image = rootView.findViewById(R.id.profile_image);
        password = rootView.findViewById(R.id.profile_password_change);
        edit = rootView.findViewById(R.id.profile_edit);
        setData();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataWhenEditClicked();

            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int redColor=getResources().getColor(R.color.red);
                if(redColor==name.getCurrentTextColor()){
                HashMap<String,Object> map = UserDataHolder.getInstance().getSharedData();
                CustomDialog dialog = new CustomDialog(getContext(), "Change Your Name",map.get("name").toString() ,"name");
                dialog.show();
                }else{
                    Toast.makeText(getContext(),"for edit name please click on edit button",Toast.LENGTH_LONG).show();
                }

            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int redColor=getResources().getColor(R.color.red);
                if(redColor==email.getCurrentTextColor()){
                HashMap<String,Object> map = UserDataHolder.getInstance().getSharedData();
                CustomDialog dialog = new CustomDialog(getContext(), "Change Your Email",map.get("email").toString(),"email");
                dialog.show();
                }else{
                    Toast.makeText(getContext(),"for edit name please click on edit button",Toast.LENGTH_LONG).show();
                }

            }
        });
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int redColor=getResources().getColor(R.color.red);
                if(redColor==gender.getCurrentTextColor()){
                HashMap<String,Object> map = UserDataHolder.getInstance().getSharedData();
                CustomDialog dialog = new CustomDialog(getContext(), "Change Your Gender", map.get("gender").toString(),"gender");
                dialog.show();
                }else{
                    Toast.makeText(getContext(),"for edit gender please click on edit button",Toast.LENGTH_LONG).show();
                }

            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int redColor=getResources().getColor(R.color.red);
                if(redColor==dob.getCurrentTextColor()){
                HashMap<String,Object> map = UserDataHolder.getInstance().getSharedData();
                CustomDialog dialog = new CustomDialog(getContext(), "Change Your Dob", map.get("dob").toString(),"dob");
                dialog.show();
                }else{
                    Toast.makeText(getContext(),"for edit dob please click on edit button",Toast.LENGTH_LONG).show();
                }

            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int redColor=getResources().getColor(R.color.red);
                if(redColor==address.getCurrentTextColor()){
                    HashMap<String,Object> map = UserDataHolder.getInstance().getSharedData();
                    Object address = map.get("address");
                    if(address==null){
                        CustomDialog dialog = new CustomDialog(getContext(), "Change Your Address", "change your address like this home nearest by town/city distric " +
                                "state pincode","address");
                        dialog.show();

                    }else {
                        CustomDialog dialog = new CustomDialog(getContext(), "Change Your Address", map.get("address").toString(),"address");
                        dialog.show();
                    }

                }else{
                    Toast.makeText(getContext(),"for address name please click on edit button",Toast.LENGTH_LONG).show();
                }


            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int redColor=getResources().getColor(R.color.red);
                if(redColor==password.getCurrentTextColor()){
                    CustomDialog dialog = new CustomDialog(getContext(), "Change Your Password", " create secured password","password");
                    dialog.show();
                }
            }
        });

        TextView emailTextView = rootView.findViewById(R.id.profile_profile_email);
        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
      //  DatabaseReference ref = FirebaseDatabase.getInstance("https://online-voting-a8615-default-rtdb.firebaseio.com/").getReference("users");
        String email = currentUser.getEmail();
        String userId = currentUser.getUid();
        emailTextView.setText(email);
        AppCompatButton addVotingSample = rootView.findViewById(R.id.profile_add_voting_samples);
        AppCompatButton addedVotes = rootView.findViewById(R.id.profile_added_votes_samples);
        AppCompatButton logout = rootView.findViewById(R.id.fragment_profile_logout_button);


       image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ProfileFragment.this)
                        .crop()//Crop image(Optional), Check Customization for more option
                        .cropSquare()
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
       logout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FirebaseAuth auth = FirebaseAuth.getInstance();
               auth.signOut();

           }
       });

        addedVotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AddedVotes.class));
            }
        });
        addVotingSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AdminActivity.class));
            }
        });

      



        return rootView;


    }

    private void setDataWhenEditClicked() {
        int redColor=getResources().getColor(R.color.red);
        int blacColor=getResources().getColor(R.color.black);
        if(blacColor==name.getCurrentTextColor()&&
                blacColor==email.getCurrentTextColor() &&
                blacColor==gender.getCurrentTextColor()&&
                blacColor==dob.getCurrentTextColor()&&
                blacColor==address.getCurrentTextColor()&&
                getResources().getColor(R.color.dark_grey)== password.getCurrentTextColor()){
            name.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
            edit.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);

        }else {

            if (redColor == name.getCurrentTextColor()) {
                name.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                email.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            } else if (redColor == email.getCurrentTextColor()) {
                email.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                gender.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            } else if (redColor == gender.getCurrentTextColor()) {
                gender.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                dob.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            } else if (redColor == dob.getCurrentTextColor()) {
                dob.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                address.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            } else if(redColor==address.getCurrentTextColor()){
                address.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                password.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                password.setText("<<--click me to change your password-->>");
            } else {
                password.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_grey));
                password.setText("click to change account info -------->");
                edit.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
            }

        }


    }

    private void setData() {
        HashMap<String,Object> map = UserDataHolder.getInstance().getSharedData();
        if(map!=null) {
            name.setText(map.get("name").toString());
            namee.setText(map.get("name").toString());
            email.setText(map.get("email").toString());
            gender.setText(map.get("gender").toString());
            dob.setText(map.get("dob").toString());
            if (map.get("address") != null) {
                address.setText(map.get("address").toString());
            }
        }
        Bitmap bitmapImage = UserDataHolder.getInstance().getBitmapUserImage();
        if(bitmapImage!=null){
            image.setImageBitmap(bitmapImage);
        }else{

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setData();
                }
            },100);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        if(uri!=null) {
            image.setImageURI(uri);
            storageReference.child("userspic/" + FirebaseAuth.getInstance().getUid()).putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully
                        Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(exception -> {
                        // Handle any errors
                        Toast.makeText(getContext(), "Error uploading image: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("error", exception.getMessage());
                    });
        }
    }

}