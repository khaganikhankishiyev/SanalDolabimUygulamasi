package com.example.sanaldolabmuygulamas;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CustomAdapter_kiyaferler extends RecyclerView.Adapter<CustomAdapter_kiyaferler.MyViewHolder> {
    private ArrayList<String> kiyafet_turu;
    private ArrayList<String>  renk;
    private ArrayList<String>  fiyat;
    private ArrayList<String>  alinmatarihi;
    private ArrayList<String>  desen;
    private ArrayList<String> Uri;
    private View view;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView_kiyafetturu;
        TextView textView_renk;
        TextView textView_desen;
        TextView textView_fiyat;
        TextView textView_alinmatarihi;
        ImageView imageVIew;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textView_kiyafetturu = (TextView) itemView.findViewById(R.id.textView_kiyafetturu);
            this.textView_renk = (TextView) itemView.findViewById(R.id.textView_renk);
            this.textView_desen =  (TextView) itemView.findViewById(R.id.textView_desen_kiyafetler);
            this.textView_fiyat =  (TextView) itemView.findViewById(R.id.textView_fiyat);
            this.textView_alinmatarihi =  (TextView) itemView.findViewById(R.id.textView_alinmatarihi);
            this.imageVIew=itemView.findViewById(R.id.imageView4);
        }
    }

    public CustomAdapter_kiyaferler(ArrayList<String> kiyafet_turu, ArrayList<String> renk, ArrayList<String> fiyat, ArrayList<String> alinmatarihi, ArrayList<String> desen, ArrayList<String> Uri) {
        this.kiyafet_turu = kiyafet_turu;
        this.renk = renk;
        this.fiyat = fiyat;
        this.alinmatarihi = alinmatarihi;
        this.desen = desen;
        this.Uri=Uri;
    }
    @NonNull
    @Override
    public CustomAdapter_kiyaferler.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.kiyafetler_cards,parent,false);
        view.setOnClickListener(KiyafetFragment.myOnClickListener);

        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter_kiyaferler.MyViewHolder holder, int position) {
        System.out.println("position "+position);
        TextView textView_kiyafetturu=holder.textView_kiyafetturu;
        TextView textView_renk=holder.textView_renk;
        TextView textView_desen=holder.textView_desen;
        TextView textView_fiyat=holder.textView_fiyat;
        TextView textView_alinmatarihi=holder.textView_alinmatarihi;
        ImageView imageVIew=holder.imageVIew;
        Glide.with(view).load(Uri.get(position)).into(imageVIew);
        textView_kiyafetturu.setText(kiyafet_turu.get(position));
        textView_renk.setText(renk.get(position));
        textView_fiyat.setText(fiyat.get(position));
        textView_alinmatarihi.setText(alinmatarihi.get(position));
        textView_desen.setText(desen.get(position));


    }

    @Override
    public int getItemCount() {
        return desen.size();
    }
}
