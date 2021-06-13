package com.example.sanaldolabmuygulamas;

import android.app.Dialog;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class KiyafetFragment extends Fragment {
    private Button button_upload;
    private View view;
    private static final int PICK_IMAGE = 1;
    protected static View.OnClickListener myOnClickListener;
    private  ImageView imageView;
    private Dialog myDialog;
    private Uri selectedImage;
    private Spinner spinner_Kiyafetturu,spinner_renk,spinner_desen;
    private User user;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView.Adapter adapter_recycler;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    public KiyafetFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDialog= new Dialog(getContext());

        firebaseFirestore=FirebaseFirestore.getInstance();
        Intent intent=getActivity().getIntent();
        user= (User) intent.getSerializableExtra("User");
        System.out.println(user.getUid());
        view= inflater.inflate(R.layout.fragment_kiyafet, container, false);
        recyclerView=view.findViewById(R.id.recyclerView_kiyafets);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getkiyafets();
        FloatingActionButton fb =view.findViewById(R.id.fab2);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("fab2");
                kiyafetekle();
            }
        });
        return view;
    }





    private void getkiyafets() {
        firebaseFirestore.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    ArrayList<HashMap> kiyafetler_array=new ArrayList<>();
                    kiyafetler_array= (ArrayList<HashMap>) documentSnapshot.get("kiyafetler");
                    ArrayList<String>kiyafetturu_array=new ArrayList<>();
                    ArrayList<String>renk_array=new ArrayList<>();
                    ArrayList<String>fiyat_array=new ArrayList<>();
                    ArrayList<String>alinmatarihi_array=new ArrayList<>();
                    ArrayList<String>desen_array=new ArrayList<>();
                    ArrayList<String>Uri_array=new ArrayList<>();
                    if(kiyafetler_array!=null) {
                        for (int i = 0; i < kiyafetler_array.size(); i++) {
                            kiyafetturu_array.add(kiyafetler_array.get(i).get("kiyafet turu").toString());
                            renk_array.add(kiyafetler_array.get(i).get("renk").toString());
                            fiyat_array.add(kiyafetler_array.get(i).get("fiyat").toString());
                            alinmatarihi_array.add(kiyafetler_array.get(i).get("Alinma tarihi").toString());
                            desen_array.add(kiyafetler_array.get(i).get("desen").toString());
                            Uri_array.add(kiyafetler_array.get(i).get("imageURL").toString());
                        }

                        adapter_recycler = new CustomAdapter_kiyaferler(kiyafetturu_array, renk_array, fiyat_array, alinmatarihi_array, desen_array,Uri_array);
                        recyclerView.setAdapter(adapter_recycler);
                        System.out.println("arraysize " + desen_array.size() + " " + kiyafetler_array.size());
                    }
                }
            }
        });
    }

    private void kiyafetekle() {
        myDialog.setContentView(R.layout.kiyafetekle_cards);
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        myDialog.getWindow().setLayout(metrics.widthPixels,metrics.heightPixels);
        setspinners(myDialog);
        Button btn_upload,btn_insert;
        TextView textview_close;
        textview_close=myDialog.findViewById(R.id.textView_closecekmece);
        btn_upload=myDialog.findViewById(R.id.button_uploadcard);
        btn_insert=myDialog.findViewById(R.id.button_add);
        textview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToFirebase(myDialog);
            }
        });

        myDialog.show();

    }

    private void setToFirebase(Dialog myDialog) {
        EditText editText_Date,editText_Fiyat;
        editText_Date=myDialog.findViewById(R.id.editText_Date);
        editText_Fiyat=myDialog.findViewById(R.id.editText_Fiyat);
        System.out.println(spinner_desen.getSelectedItem()+" "+spinner_renk.getSelectedItem()+" "+spinner_Kiyafetturu.getSelectedItem()+" "+editText_Date.getText().toString()+" "+editText_Fiyat.getText().toString());
        if(selectedImage!=null){
            if(!editText_Date.getText().toString().matches("")&& !editText_Fiyat.getText().toString().matches("")){
                String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName=timeStamp+"."+getFileExt(selectedImage);
                FirebaseStorage storage=FirebaseStorage.getInstance();
                StorageReference storageReference=storage.getReference().child("images").child(imageFileName);
                storageReference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    System.out.println("URI "+uri);
                                    HashMap<String,Object> kiyafet_map=new HashMap<>();
                                    kiyafet_map.put("kiyafet turu",spinner_Kiyafetturu.getSelectedItem().toString());
                                    kiyafet_map.put("desen",spinner_desen.getSelectedItem().toString());
                                    kiyafet_map.put("renk",spinner_renk.getSelectedItem().toString());
                                    kiyafet_map.put("Alinma tarihi",editText_Date.getText().toString());
                                    kiyafet_map.put("fiyat",editText_Fiyat.getText().toString());
                                    kiyafet_map.put("imageURL",uri.toString());
                                    firebaseFirestore.collection("Users").document(user.getUid()).update("kiyafetler", FieldValue.arrayUnion(kiyafet_map)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                                Toast.makeText(getContext(), "Kiyafet eklendi", Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(getContext(), "Kiyafet eklenemedi", Toast.LENGTH_SHORT).show();
                                            myDialog.dismiss();
                                            getkiyafets();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            System.out.println(e);
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println(e);
                                }
                            });

                        }
                        else
                            Toast.makeText(getActivity(), "Image can`t uploaded", Toast.LENGTH_SHORT).show();

                    }
                });
            }
            else
                Toast.makeText(getContext(), "Tarih ve Fiyat bilgilerini doldurmaniz gerek", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getContext(), "Resim eklemeniz gerek", Toast.LENGTH_SHORT).show();

    }

    private void setspinners(Dialog myDialog) {
        spinner_Kiyafetturu= myDialog.findViewById(R.id.spinner_Kiyafetturu);
        spinner_renk=myDialog.findViewById(R.id.spinner_renk);
        spinner_desen=myDialog.findViewById(R.id.spinner_desen);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(getContext(),R.array.Turu, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Kiyafetturu.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter_renk= ArrayAdapter.createFromResource(getContext(),R.array.Renk, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_renk.setAdapter(adapter_renk);
        ArrayAdapter<CharSequence> adapter_desen= ArrayAdapter.createFromResource(getContext(),R.array.Desen, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_desen.setAdapter(adapter_desen);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE){
            System.out.println("if");
            selectedImage = data.getData();
            String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName=timeStamp+"."+getFileExt(selectedImage);
            System.out.println(imageFileName);
            Bitmap bm=null;
            try {
                 bm=MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(),data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView=myDialog.findViewById(R.id.imageView3);
            imageView.setImageBitmap(bm);
        }
        else
            System.out.println("else");
    }
    private String getFileExt(Uri selectedImage){
        ContentResolver c=getActivity().getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(selectedImage));

    }

}