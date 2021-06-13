package com.example.sanaldolabmuygulamas;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CekmeceFragment extends Fragment {
    private View view;
    private User user;
    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private Dialog myDialog;
    private Spinner spinner_cekmeceler;
    private Button btn_sil;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_cekmece, container, false);
        myDialog= new Dialog(getContext());
        Intent intent=getActivity().getIntent();
        btn_sil=view.findViewById(R.id.button_delete);
        btn_sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        spinner_cekmeceler=view.findViewById(R.id.spinner_cekmeceler);
        user= (User) intent.getSerializableExtra("User");
        getcekmeceler();
        FloatingActionButton fb =view.findViewById(R.id.fab2);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("fab2");
                setcekmece();
            }
        });
        return view;
    }

    private void delete() {
        firebaseFirestore.collection("Users").document(user.getUid()).collection("cekmeceler").document(spinner_cekmeceler.getSelectedItem().toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Cekmece silindi", Toast.LENGTH_SHORT).show();
                getcekmeceler();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "cekmece silenemedi", Toast.LENGTH_SHORT).show();
                getcekmeceler();
            }
        });
    }

    private void getcekmeceler() {
        firebaseFirestore.collection("Users").document(user.getUid()).collection("cekmeceler").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<String> documentsname =new ArrayList<>();
                for(int i=0; i<queryDocumentSnapshots.getDocuments().size(); i++){
                    documentsname.add(queryDocumentSnapshots.getDocuments().get(i).getId());

                }
                if(documentsname.size()>0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, documentsname);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_cekmeceler.setAdapter(adapter);
                }
            }
        });
    }

    private void setcekmece() {
        myDialog.setContentView(R.layout.cekmece);
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        myDialog.getWindow().setLayout(metrics.widthPixels,metrics.heightPixels);
        TextView textview_close,textView_name;
        textView_name=myDialog.findViewById(R.id.editText_cekmeceismi);
        textview_close=myDialog.findViewById(R.id.textView_closecekmece);
        Button btn_insert;
        btn_insert=myDialog.findViewById(R.id.button_addcekmece);
        textview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToFirebase(myDialog,textView_name);
            }
        });

        myDialog.show();
    }

    private void setToFirebase(Dialog myDialog,TextView textView_name) {
        HashMap<String,String> map=new HashMap<>();
        map.put("name",textView_name.getText().toString());
        firebaseFirestore.collection("Users").document(user.getUid()).collection("cekmeceler").document(textView_name.getText().toString()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Cekmece eklendi", Toast.LENGTH_SHORT).show();
                myDialog.dismiss();
                getcekmeceler();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("cekmece eklenemedi");
                myDialog.dismiss();
            }
        });
    }
}