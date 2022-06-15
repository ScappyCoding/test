package com.codeofscappy.messanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private EditText setUsername, setStatus;
    private Button updateButton;

    private String currentUserId;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Firebase Instance
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        initialFields();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();
            }
        });
        
        
        RetrieveUserInfo();

        setUsername.setVisibility(View.INVISIBLE);


    }




    private void initialFields() {
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        setUsername = (EditText) findViewById(R.id.set_username);
        setStatus = (EditText) findViewById(R.id.set_status);
        updateButton = (Button) findViewById(R.id.update_settings_button);
    }


    // Methode to Set User Stats in Database
    private void UpdateSettings() {
        String setUserName = setUsername.getText().toString();
        String setUserStatus = setStatus.getText().toString();


        if (TextUtils.isEmpty(setUserName)) {
            Toast.makeText(this, "Please write your Username...", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(setUserStatus)) {
            Toast.makeText(this, "Please write your Status...", Toast.LENGTH_SHORT).show();
        } else {
            // Set NewUser [User Info] push into Database and create a Profile-Map,
            // Root from Map  "USERS"-->"UID"---> in [UID] set the UserInfo.
            HashMap<String, String> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserId);
            profileMap.put("name", setUserName);
            profileMap.put("status", setUserStatus);
            databaseReference.child("Users").child(currentUserId).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                SendUserToMainActivity();
                                Toast.makeText(SettingsActivity.this, "Profile Update Successfully...", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error :" + message, Toast.LENGTH_SHORT).show();
                            }
                        }


                    });
        }


    }


    // Methode to set UserInfo into the "Username" and "Status" Field
    private void RetrieveUserInfo()
    {
        databaseReference.child("Users").child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image"))))
                        {
                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrieveStatus = dataSnapshot.child("status").getValue().toString();
                            String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();

                            setUsername.setText(retrieveUserName);
                            setStatus.setText(retrieveStatus);
                            //TODO profileImage Set

                        }
                        else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")))
                        {
                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrieveStatus = dataSnapshot.child("status").getValue().toString();


                            setUsername.setText(retrieveUserName);
                            setStatus.setText(retrieveStatus);

                        }
                        else
                        {
                            setUsername.setVisibility(View.VISIBLE);
                            Toast.makeText(SettingsActivity.this, "Please set & update your Profile Information", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });
    }

    // Methode [SendUserToMain]
    private void SendUserToMainActivity() {
        Intent mainintent = new Intent(SettingsActivity.this, MainActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();

    }



}






