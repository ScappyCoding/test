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

public class LoginActivity extends AppCompatActivity
{
    // XML UI-Elements
    private Button btnGoogle, btnFacebook, btnPhone, btnLogin;
    private EditText ln_userEmail, ln_userPassword;
    private TextView newAccountLink;
    // Firebase Elements
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;

    // Google Login
    private GoogleSignInClient client;



    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initial Firebase-Instance
        firebaseAuth = FirebaseAuth.getInstance();






        initializeFields();



        //Button --> (TexView)
        newAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               SendUserToRegisterActivity();
            }
        });
        //Button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AllowUserToLogin();
            }
        });
    }




    //Methode [UserLogin-Auth]
    private void AllowUserToLogin()
    {

        String email = ln_userEmail.getText().toString();
        String password = ln_userPassword.getText().toString();

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
            loadingBar.setTitle("Sign In");
            loadingBar.setMessage("Please wait... ");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            //User Auth-Check to Login
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                SendUserToMainActivity();
                                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }





    private void initializeFields() {
        // XML UI-Elements
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnPhone = (Button) findViewById(R.id.btn_phone);
        btnFacebook = (Button) findViewById(R.id.btn_facebook);
        ln_userEmail = (EditText) findViewById(R.id.ln_Email);
        ln_userPassword = (EditText) findViewById(R.id.ln_Password);
        newAccountLink = (TextView) findViewById(R.id.needNewAccountLink);
        loadingBar = new ProgressDialog(this);

    }






     // Methode [SendUserToMain]
     private void SendUserToMainActivity()
     {
         Intent mainintent = new Intent(LoginActivity.this, MainActivity.class);
         mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(mainintent);
         finish();
     }
     // Methode [SendUserToRegister]
    private void SendUserToRegisterActivity()
    {
        Intent registerintent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerintent);
    }



}