package com.example.guillermouribe.pruebadb3;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText editText_fname;
    EditText editText_lname;
    EditText editText_phonenumber;
    ArrayList<Login> returnValues = new ArrayList<Login>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);

        editText_fname = (EditText) findViewById(R.id.firstName);
        editText_lname = (EditText) findViewById(R.id.lastName);
        editText_phonenumber = (EditText) findViewById(R.id.phoneNumber);

    }

    public void save(View v) {
        Login contact = new Login();

        contact.setNombre(editText_fname.getText().toString());
        contact.setApellido(editText_lname.getText().toString());
        contact.setNumerotelefono(editText_phonenumber.getText().toString());

        MongoLabSaveContact tsk = new MongoLabSaveContact();
        tsk.execute(contact);

        Toast.makeText(this, "Saved to MongoDB!!", Toast.LENGTH_SHORT).show();
    }

    public void fetch(View v){
        SincronizarContactos task = new SincronizarContactos();
        try {
            returnValues = task.execute().get();
            Login FetchedData = (Login) returnValues.toArray()[0];

            editText_fname.setText(FetchedData.getnombre());
            editText_lname.setText(FetchedData.getApellido());
            editText_phonenumber.setText(FetchedData.getNumeroTelefono());

            Toast.makeText(this, "Fetched from MongoDB!!", Toast.LENGTH_SHORT).show();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

final class MongoLabSaveContact extends AsyncTask<Object, Void, Boolean> {
    @Override
    protected Boolean doInBackground(Object... params) {
        Login contact = (Login) params[0];
        Log.d("contact", ""+contact);

        try {
            Database sd = new Database();
            URL url = new URL(sd.buildContactsSaveURL());

            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setRequestProperty("Accept", "application/json");

            OutputStreamWriter osw = new OutputStreamWriter(
                    connection.getOutputStream());

            osw.write(sd.createContact(contact));
            osw.flush();
            osw.close();

            if(connection.getResponseCode() <205)
            {
                return true;
            }
            else
            {
                return false;
            }

        } catch (Exception e) {
            e.getMessage();
            Log.d("Got error", e.getMessage());
            return false;
        }
    }
}
