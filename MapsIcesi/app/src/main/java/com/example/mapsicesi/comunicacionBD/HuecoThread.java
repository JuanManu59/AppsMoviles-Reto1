package com.example.mapsicesi.comunicacionBD;

import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.mapsicesi.MapsActivity;
import com.example.mapsicesi.modelo.Hueco;
import com.example.mapsicesi.modelo.User;
import com.example.mapsicesi.util.Constants;
import com.example.mapsicesi.util.HTTPSWebUtilDomi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Console;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.logging.LogRecord;

public class HuecoThread extends  Thread {

    private MapsActivity mapsActivity;
    private boolean isAlive;
    private HTTPSWebUtilDomi httpsWebUtilDomi;
    private Gson gson;

    public HuecoThread (MapsActivity mapsActivity){
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
                Thread.sleep(1500);
                consultarHuecos();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void consultarHuecos(){
        String response = httpsWebUtilDomi.GETrequest(Constants.BASEURL+"huecos.json");
        Type tipo = new TypeToken<HashMap<String, Hueco>>(){}.getType();

        Log.e("response",response);

        HashMap<String, Hueco> huecos = gson.fromJson(response, tipo);

        mapsActivity.actualizarHuecos(huecos);
    }
}
