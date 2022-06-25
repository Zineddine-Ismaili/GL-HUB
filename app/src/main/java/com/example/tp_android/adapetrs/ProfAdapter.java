package com.example.tp_android.adapetrs;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tp_android.R;
import com.example.tp_android.model.Professeur;

import java.util.ArrayList;
import java.util.LinkedList;

public class ProfAdapter extends BaseAdapter {

    private Context context;
    private LinkedList<Professeur> professeurs;
    private LayoutInflater inflater;

    public ProfAdapter(Context context, LinkedList<Professeur> professeurs) {
        this.context = context;
        this.professeurs = professeurs;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return professeurs.size();
    }

    @Override
    public Professeur getItem(int position) {
        return professeurs.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
         view = inflater.inflate(R.layout.adpter_item,null);

         Professeur currentProf = getItem(i);
         String itemName= currentProf.getNom();
         String itemPrenom= currentProf.getPrenom();
         String itemDepatement=currentProf.getDepartement();
         String itemPhoto=currentProf.getPhoto();
         String itemTel=currentProf.getTel();

        TextView itemNameView = view.findViewById(R.id.name_prof);
        itemNameView.setText(itemName);

        TextView itemDepatementView = view.findViewById(R.id.departement_prof);
        itemDepatementView.setText(itemDepatement);


        return view;

    }

}

//public class ProfAdapter extends ArrayAdapter<Professeur> {
//
//    public ProfAdapter(Context context,LinkedList<Professeur> professeurList){
//        super(context,R.layout.adpter_item,professeurList);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        Professeur prof = getItem(position);
//
//        if(convertView==null){
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adpter_item,parent,false);
//        }
//
//        ImageView imageView = convertView.findViewById(R.id.prof_icon);
//        TextView itemNameView= convertView.findViewById(R.id.name_prof);
//        TextView itemDepatementView= convertView.findViewById(R.id.departement_prof);
//
//        itemNameView.setText(prof.getNom());
//        itemDepatementView.setText(prof.getDepartement());
//
//
//        return super.getView(position, convertView, parent);
//    }
//}
