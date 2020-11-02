package com.example.mapsicesi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mapsicesi.modelo.Hueco;
import com.example.mapsicesi.util.Constants;
import com.example.mapsicesi.util.HTTPSWebUtilDomi;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.UUID;

public class MyDialogFragment extends DialogFragment implements  View.OnClickListener{

    private TextView coordenadaTV;
    private TextView direccionTV;
    private Button okBtn;

    private Listener listener;

    private String dataCoordenada;
    private String dataDireccion;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dialog_fragment, container, false);
        coordenadaTV = view.findViewById(R.id.coordenadaTV);
        direccionTV = view.findViewById(R.id.direccionTV);
        okBtn = view.findViewById(R.id.okBtn);

        coordenadaTV.setText(dataCoordenada);
        direccionTV.setText(dataDireccion);

        okBtn.setOnClickListener(this);
        return view;
    }

    public void setListener (Listener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.okBtn:
                    listener.buttonOK();
                break;
        }
    }

    public interface Listener{
        void buttonOK();
    }

    public void setCoordenadaTV(String data){
        this.dataCoordenada = data;
    }

    public void setDireccionTV(String data){
        this.dataDireccion = data;
    }

}
