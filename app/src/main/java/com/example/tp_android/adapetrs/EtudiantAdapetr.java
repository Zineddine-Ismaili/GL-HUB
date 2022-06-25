package com.example.tp_android.adapetrs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tp_android.R;
import com.example.tp_android.model.Etudiant;
import com.example.tp_android.model.Professeur;

import java.util.ArrayList;
import java.util.LinkedList;

public class EtudiantAdapetr extends BaseAdapter {

    private Context context;
    private ArrayList<Etudiant> etudiants;
    private LayoutInflater inflater;

    public EtudiantAdapetr(Context context, ArrayList<Etudiant> etudiants) {
        this.context = context;
        this.etudiants = etudiants;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return etudiants.size();
    }

    @Override
    public Etudiant getItem(int position) {
        return etudiants.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.adapter_view_etudiant,null);

        Etudiant currentEtd = getItem(i);
        String itemName= currentEtd.getNom();
        String itemPrenom= currentEtd.getPrenom();
        String itemTel=currentEtd.getTel();

        TextView itemNameView = view.findViewById(R.id.name_etudiant);
        itemNameView.setText(itemName);

        TextView itemPrenomView = view.findViewById(R.id.prenom_etudiant);
        itemPrenomView.setText(itemPrenom);


        return view;

    }

}
