package com.example.mapsicesi.comunicacionBD;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mapsicesi.MapsActivity;
import com.example.mapsicesi.modelo.Hueco;
import com.example.mapsicesi.modelo.User;
import com.example.mapsicesi.util.Constants;
import com.example.mapsicesi.util.HTTPSWebUtilDomi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class UserThread extends Thread {

    private MapsActivity mapsActivity;
    private boolean isAlive;
    private HTTPSWebUtilDomi httpsWebUtilDomi;
    private Gson gson;

    public UserThread (MapsActivity mapsActivity){
        this.mapsActivity = mapsActivity;
        isAlive = true;
        httpsWebUtilDomi = new HTTPSWebUtilDomi();
        gson = new Gson();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        super.run();
        while(isAlive){
            try {
                Thread.sleep(2500);
                consultarUsuarios();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void consultarUsuarios(){
        String response = httpsWebUtilDomi.GETrequest(Constants.BASEURL+"users.json");
        Type tipo = new TypeToken<HashMap<String, User>>(){}.getType();
        Gson gson = new Gson();
        HashMap<String, User> users = gson.fromJson(response, tipo);

        mapsActivity.actualizarUsuarios(users);
    }
}
