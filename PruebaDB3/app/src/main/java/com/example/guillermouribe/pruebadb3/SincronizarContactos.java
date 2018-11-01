package com.example.guillermouribe.pruebadb3;

import android.os.AsyncTask;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import com.mongodb.*;



public class SincronizarContactos extends AsyncTask<Login, Void, ArrayList<Login>> {
    static String server_output = null;
    static String temp_output = null;

    @Override
    protected ArrayList<Login> doInBackground(Login... arg0) {

        ArrayList<Login> mycontacts = new ArrayList<Login>();
        try {
            Database sd = new Database();
            URL url = new URL(sd.buildContactsFetchURL());
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            while ((temp_output = br.readLine()) != null) {
                server_output = temp_output;
            }

            String mongoarray = "{ DB_output: "+server_output+"}";
            Object o = com.mongodb.util.JSON.parse(mongoarray);

            DBObject dbObj = (DBObject) o;
            BasicDBList contacts = (BasicDBList) dbObj.get("DB_output");
            for (Object obj : contacts) {
                DBObject userObj = (DBObject) obj;

                Login temp = new Login();
                temp.setNombre(userObj.get("first_name").toString());
                temp.setApellido(userObj.get("last_name").toString());
                temp.setNumerotelefono(userObj.get("phone").toString());
                mycontacts.add(temp);

            }

        }catch (Exception e) {
            e.getMessage();
        }

        return mycontacts;
    }
}
