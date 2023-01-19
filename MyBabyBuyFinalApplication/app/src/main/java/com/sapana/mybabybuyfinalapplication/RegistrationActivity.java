package com.sapana.mybabybuyfinalapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    EditText inputUsername, inputEmail,inputPassword,inputConfirmPassword;
    Button btnRegister;
    TextView login;
    dBHelper DB;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        inputUsername = (EditText) findViewById(R.id.inputUsername);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputConfirmPassword = (EditText) findViewById(R.id.inputConformPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        login =(TextView) findViewById(R.id.login);
        DB = new dBHelper(this);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String user = inputUsername.getText().toString();
                String email = inputEmail.getText().toString();
                String pass = inputPassword.getText().toString();
                String repass = inputConfirmPassword.getText().toString();
                try {
                    pass = EncPass(pass);
                    repass = EncPass(repass);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(user.equals("") || pass.equals("") || email.equals(""))
                    Toast.makeText(RegistrationActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else
                {

                    if(pass.equals(repass))
                    {
                        if (!ValidateEmailAddress(inputEmail)) {
                            return;
                        }

                        validatePassword(inputPassword);

                            Boolean checkuser = DB.checkusername(user);

                            if (!checkuser)

                            {
                                boolean insert = DB.insertData(user, email, pass);
                                if (insert)
                                {

                                    Toast.makeText(RegistrationActivity.this, "Registered successfully!! Now login", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                } else
                                {
                                    Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                }
                            } else
                            {
                                Toast.makeText(RegistrationActivity.this, "User already exists!!", Toast.LENGTH_SHORT).show();
                            }

                    }
                    else
                    {
                        Toast.makeText(RegistrationActivity.this, "password does not match", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private  boolean ValidateEmailAddress(EditText inputEmail){
        String Email = inputEmail.getText().toString();
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(DB.checkemail(Email)) {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }
    private boolean validatePassword(EditText inputPassword) {
        String passwordInput = inputPassword.getText().toString();
        if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            Toast.makeText(this, "Password too weak", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public String EncPass(String password) throws Exception{
        String Encrypted =Encryption.encryptAES(password);
        return  Encrypted;
    }
}