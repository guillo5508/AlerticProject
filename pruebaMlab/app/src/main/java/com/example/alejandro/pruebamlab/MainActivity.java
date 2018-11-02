package com.example.alejandro.pruebamlab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button b1=findViewById(R.id.button13);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText textcc = (EditText) findViewById(R.id.editTextCc);
                String cc=textcc.getText().toString();

                EditText textname = (EditText) findViewById(R.id.editTextName);
                String name=textname.getText().toString();

                EditText textnum = (EditText) findViewById(R.id.editTextNum);
                String num=textnum.getText().toString();

                EditText textdate = (EditText) findViewById(R.id.editTextDate);
                String date=textdate.getText().toString();

                EditText textnums = (EditText) findViewById(R.id.editTextNumS);
                String nums=textnums.getText().toString();

                EditText textdes = (EditText) findViewById(R.id.editTextDes);
                String des=textdes.getText().toString();

                //cedula, numD, nombreD, fecha, descripcion, numSospechoso

                denuncia den = new denuncia(cc,num,name,date,des,nums);
                try {
                    den.Save();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Toast notificacion = Toast.makeText(MainActivity.this, "Reportado", Toast.LENGTH_SHORT);
                notificacion.show();
            }
        });

    }

}
