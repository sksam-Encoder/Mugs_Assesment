package com.example.mugsassesment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mugsassesment.Models.Users;
import com.example.mugsassesment.Utility.WaitUser;
import com.example.mugsassesment.databinding.RegisterActivityBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import io.paperdb.Paper;

public class RegisterActivity extends AppCompatActivity {

    RegisterActivityBinding binding;
    String name, phone, email, password, confirmPassword;
    WaitUser waitUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RegisterActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initVars();
        setListeners();
        checkAuth();
    }

    private void checkAuth() {

        String email, password;
        email = Paper.book().read("email");
        password = Paper.book().read("password");

        if (email != null && password != null) {

            if (!email.isEmpty() && !password.isEmpty()) {

                waitUser.start_Dialog("please wait", "you are already logged in ");
                AllowAccess(email, password);

            }


        }

    }

    private void AllowAccess(String email, String password) {

        String[] emailarr = email.split("\\.");
        String emailDb = emailarr[0] + emailarr[1];


        DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference();

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("Users").child(emailDb).exists()) {

                    Users userdata = snapshot.child("Users").child(emailDb).getValue(Users.class);

                    assert userdata != null;

                    if (userdata.getPassword().equals(password)) {

                        if (userdata.getEmail().equals(email)) {

//                                went Home Activity
                            waitUser.close_Dialog();
                            finish();
                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                        }
                    }

                } else {

                    Toast.makeText(RegisterActivity.this, "Login Again", Toast.LENGTH_SHORT).show();
                    waitUser.close_Dialog();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


                Toast.makeText(RegisterActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();


            }
        });


    }

    private void initVars() {
        Paper.init(getApplicationContext());
        waitUser = new WaitUser(this);
        Objects.requireNonNull(getSupportActionBar()).hide();

    }

    private void setListeners() {
        binding.signUpBtn.setOnClickListener(view -> SignUp());
        binding.logBtn.setOnClickListener(view -> {

            startActivity(new Intent(RegisterActivity.this,Login_mainActivity.class));

        });
    }

    private void SignUp() {

        //      if all condition satisfies insert data into db
        boolean isOk = checkConditions();

        if (isOk) {

            UploadUser();

        } else {
            Toast.makeText(this, "Please check all details correctly", Toast.LENGTH_SHORT).show();
        }


    }

    private void UploadUser() {

        waitUser.start_Dialog("Please Wait", "we are checking for credentials");
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String[] em = email.split("\\.");
                String emaildb = em[0] + em[1];
                if (snapshot.child(emaildb).exists()) {

                    waitUser.close_Dialog();
                    Toast.makeText(RegisterActivity.this, "User Already exists" +
                            "Try with another email", Toast.LENGTH_SHORT).show();
                } else {
//                      insert User with email in database...

                    HashMap<String, Object> usrData = new HashMap<>();
                    usrData.put("name", name);
                    usrData.put("email", email);
                    usrData.put("phone", phone);
                    usrData.put("password", password);


                    dbRef.child(email).updateChildren(usrData).
                            addOnCompleteListener(task -> {

                                waitUser.close_Dialog();
                                Toast.makeText(RegisterActivity.this, "Register" +
                                        "Successfull", Toast.LENGTH_SHORT).show();
//                                        start a new activity

                            }).
                            addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Error" +
                                    "Occurred" + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "An Database error" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean checkConditions() {

        name = Objects.requireNonNull(binding.nametxt.getText()).toString();
        email = Objects.requireNonNull(binding.emailtxt.getText()).toString();
        phone = Objects.requireNonNull(binding.phonetxt.getText()).toString();
        password = Objects.requireNonNull(binding.passwordtxt.getText()).toString();
        confirmPassword = Objects.requireNonNull(binding.Cpasswordtxt.getText()).toString();

        if (name.length() < 2) {

            binding.nametxt.setError("Fill the name correctly");

        } else if (email.isEmpty()) {


            binding.emailtxt.setError("fill email box ");


        } else if (phone.length() < 10) {

            binding.phonetxt.setError("Phonenumbers Should be equals to 10");


        } else if (password.isEmpty()) {

            binding.passwordtxt.setError("Fill Password");


        } else if (confirmPassword.isEmpty()) {

            binding.Cpasswordtxt.setError("Fill Confirm Password");

        } else if (!password.equals(confirmPassword)) {

            binding.Cpasswordtxt.setError("password not matched");

        } else {

           return validateEmail();

        }
        return false;
    }

    private boolean validateEmail() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (email.matches(emailPattern)) {
            // or
            return true;

        } else {
            //or

            return false;

        }
    }
}
