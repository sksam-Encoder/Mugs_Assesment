package com.example.mugsassesment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mugsassesment.Models.Users;
import com.example.mugsassesment.Utility.WaitUser;
import com.example.mugsassesment.databinding.LoginMainActivityBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import io.paperdb.Paper;

public class Login_mainActivity extends AppCompatActivity {

    LoginMainActivityBinding binding;
    DatabaseReference dbRef;
    WaitUser waitUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginMainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        waitUser = new WaitUser(Login_mainActivity.this);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        Paper.init(getApplicationContext());
        Objects.requireNonNull(getSupportActionBar()).hide();
        setListeners();


    }


    private void setListeners() {

        binding.loginBtn.setOnClickListener(view -> {
            LoginData();

        });

        binding.joinBtn.setOnClickListener(view -> {

            startActivity(new Intent(Login_mainActivity.this,RegisterActivity.class));

        });



    }

    private void LoginData() {

        String emailtxt = String.valueOf(binding.emailtxt.getText());
        String pass = String.valueOf(binding.passwordtxt.getText());

        if (pass.isEmpty()) {
            binding.passwordtxt.setError("Please fill password");

        } else if (emailtxt.isEmpty()) {

            binding.emailtxt.setError("Please fill email");

        } else {

           if( validateEmail(emailtxt)) {


               waitUser.start_Dialog("Please Wait", "we are Checking for Details");

               String[] email = emailtxt.split("\\.");
               String emailDb = email[0] + email[1];

               if (binding.remember.isChecked()) {

                   Paper.book().write("email", emailtxt);
                   Paper.book().write("password", pass);

               }


               dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {

                       if (snapshot.child(emailDb).exists()) {

                           Users users = snapshot.child(emailDb).getValue(Users.class);
                           assert users != null;
                           if (users.getEmail().equals(emailtxt)) {

                               if (users.getPassword().equals(pass)) {

                                   waitUser.close_Dialog();
                                   //  go to Home Activity
                                   startActivity(new Intent(Login_mainActivity.this, HomeActivity.class));

                               } else {
                                   waitUser.close_Dialog();
                                   Toast.makeText(Login_mainActivity.this, "Invalid Details", Toast.LENGTH_SHORT).show();
                               }


                           } else {
                               waitUser.close_Dialog();
                               Toast.makeText(Login_mainActivity.this, "Invalid Details", Toast.LENGTH_SHORT).show();
                           }

                       } else {
                           waitUser.close_Dialog();
                           Toast.makeText(Login_mainActivity.this, "Invalid hellow Details", Toast.LENGTH_SHORT).show();
                       }

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });

           }
           else
           {

               binding.emailtxt.setError("email not valid");
               Toast.makeText(Login_mainActivity.this, "email not valid", Toast.LENGTH_SHORT).show();

           }
        }

    }

    private boolean validateEmail(String emailtxt) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (emailtxt.matches(emailPattern)) {
            // or
            return true;

        } else {
            //or

            return false;

        }

    }
}