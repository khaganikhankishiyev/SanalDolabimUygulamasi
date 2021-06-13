package com.example.sanaldolabmuygulamas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private EditText editText_name,editText_surname,editText_email,editText_password;
    private ActionCodeSettings actionCodeSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editText_name=findViewById(R.id.editTextName);
        editText_surname=findViewById(R.id.editTextSurname);
        editText_email=findViewById(R.id.editTextEmail);
        editText_password=findViewById(R.id.editTextPassword);
    }

    public void ClickEkle(View view) {
        if(!editText_name.getText().toString().matches("")&& !editText_surname.getText().toString().matches("") && !editText_password.getText().toString().matches(" ")&&!editText_email.getText().toString().matches("")) {
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
            if (editText_email.getText().toString().contains("@")) {
                if(editText_password.getText().toString().length()>=6) {

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(editText_email.getText().toString(), editText_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                System.out.println(task.getResult().getUser().getUid());
                                User user = new User(editText_email.getText().toString(),task.getResult().getUser().getUid());
                                Toast.makeText(Register.this, "Register succesfully done", Toast.LENGTH_SHORT).show();
                                creatcollection(user);
                            } else {
                                Toast.makeText(Register.this, "Can not register", Toast.LENGTH_SHORT).show();
                                System.out.println("test2");

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "" + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText(this, "Password must be at 6 character", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Email addres must be contains @", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "butun alanlari doldurmaniz gerek", Toast.LENGTH_SHORT).show();
    }
    public void creatcollection(User user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data =new HashMap<>();
        data.put("Name",editText_name.getText().toString());
        data.put("Surname",editText_surname.getText().toString());
        db.collection("Users").document(user.getUid()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("collection created");
            }
        });
    }
}