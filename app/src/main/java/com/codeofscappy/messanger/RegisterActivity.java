package com.codeofscappy.messanger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity
{
    // XML UI-Elements
    private Button btnGoogle, btnFacebook, btnPhone, btnRegister;
    private EditText rg_Username, rg_userEmail, rg_userPassword;
    private TextView alreadyHaveAccountLink;
    // Firebase Elements
    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootRef;
    //User Info PopUp
    private ProgressDialog loadingBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initial Firebase Instance
        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance("https://scappy-messanger-598d7-default-rtdb.europe-west1.firebasedatabase.app/").getReference();





        initializeFields();

        // Switch User to LoginActivity
        alreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLoginActivity();
            }
        });

        //Button to [Register]
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CreateAccount();
            }
        });
    }





    // Methode NewAccount
    private void CreateAccount()
    {
        String username = rg_Username.getText().toString();
        String email = rg_userEmail.getText().toString();
        String password = rg_userPassword.getText().toString();

        // Field can´t be empty
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please Enter your Email", Toast.LENGTH_SHORT).show();
        }
        // Field can´t be Empty
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter your Password", Toast.LENGTH_SHORT).show();
        }
        else
        {

            // Dialog-PopUp -->  [ User Info ]
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait, while we are Creating new Account...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            
            // Create a User with input from fields [email] [psw]
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                // Create a Database [Table] for User Data Route ("users"-->"ID"-->"Value")
                                String currentUserId = firebaseAuth.getCurrentUser().getUid();
                                rootRef.child("Users").child(currentUserId).setValue("");

                                SendUserToMainActivity();
                                Toast.makeText(RegisterActivity.this, "Account Created Successfully !", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }




    // XML UI-Elements where the roots is
    private void initializeFields()
    {
        btnRegister = findViewById(R.id.btn_register);
        btnGoogle = findViewById(R.id.btn_Google);
        btnFacebook = findViewById(R.id.btn_facebook);
        btnPhone = findViewById(R.id.btn_phone);
        rg_Username = findViewById(R.id.rg_userName);
        rg_userEmail = findViewById(R.id.rg_Email);
        rg_userPassword = findViewById(R.id.rg_Password);
        alreadyHaveAccountLink = findViewById(R.id.alreadyHaveAccountLink);

        loadingBar = new ProgressDialog(this);
    }


    // Methode [SendUserToLogin]
    private void SendUserToLoginActivity()
    {
        Intent loginintent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginintent);
    }

    // Methode [SendUserToMain]
    private void SendUserToMainActivity()
    {
        Intent mainintent = new Intent(RegisterActivity.this, MainActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();
    }










}
