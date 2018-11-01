package com.example.alejandro.prueba;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void save(View v) {
        Login contact = new Login();

        contact.setNombre(editText_fname.getText().toString());
        contact.setLast_name(editText_lname.getText().toString());
        contact.setPhone_number(editText_phonenumber.getText().toString());

        MongoLabSaveContact tsk = new MongoLabSaveContact();
        tsk.execute(contact);

        Toast.makeText(this, "Saved to MongoDB!!", Toast.LENGTH_SHORT).show();
    }
    final class MongoLabSaveContact extends AsyncTask<Object, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Object... params) {
            MyContact contact = (MyContact) params[0];
            Log.d("contact", ""+contact);

            try {
                SupportData sd = new SupportData();
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
