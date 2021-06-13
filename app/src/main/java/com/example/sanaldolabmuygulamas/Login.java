package com.example.sanaldolabmuygulamas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    private EditText editTextemail,editTextpassword;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private boolean permission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkAndRequestPermissions();
        System.out.println(permission);
            editTextemail = findViewById(R.id.editTextEmailAddress);
            editTextpassword = findViewById(R.id.editTextPasswordd);

    }

    public void OnRegister(View view) {
        Intent intent = new Intent(Login.this,Register.class);
        startActivity(intent);
    }

    public void clickLogin(View view) {
        if(!editTextemail.getText().toString().matches("")&&!editTextpassword.getText().toString().matches("")){
            if(editTextemail.getText().toString().contains("@")){
                if(editTextpassword.getText().toString().length()>=6){
                    FirebaseAuth auth= FirebaseAuth.getInstance();
                    auth.signInWithEmailAndPassword(editTextemail.getText().toString(),editTextpassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if(authResult.getUser()!=null){
                                 Toast.makeText(Login.this, "login succesfull", Toast.LENGTH_SHORT).show();
                                 User user = new User(authResult.getUser().getEmail(),authResult.getUser().getUid());
                                 Intent intent= new Intent(Login.this,SanalDolabim_navigation.class);
                                 System.out.println(user.getEmail()+" "+user.getUid());
                                 intent.putExtra("User",  user);
                                 startActivity(intent);
                            }
                            else
                                Toast.makeText(Login.this, "Email ve sifrenizi yanlis girdiniz", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText(this, "Password must be at 6 characte", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "email formatini dogru girmeniz gerek", Toast.LENGTH_SHORT).show();
        }
        else
        Toast.makeText(this, "butun haneleri doldurmaniz gerek", Toast.LENGTH_SHORT).show();
    }
    protected void checkAndRequestPermissions() {
        int READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (WRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (READ_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            System.out.println("false");
        }
        System.out.println("true");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            onDestroy();
        }
    }
}