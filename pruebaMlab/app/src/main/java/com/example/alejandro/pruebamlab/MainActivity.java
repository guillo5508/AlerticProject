package com.example.alejandro.pruebamlab;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button btn;
    int year_x,month_x,day_x;
    static final int DIALOG_ID=0;



    public void showDialogOnButtonClick(){
        btn =(Button)findViewById(R.id.button4);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_ID);
                    }
                }
        );
    }
    @Override
    protected Dialog onCreateDialog(int id){
        if(id==DIALOG_ID){
            return new DatePickerDialog(this,dpickerListener,year_x,month_x,day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x=year;
            month_x=month + 1;
            day_x=dayOfMonth;
            //Toast.makeText(MainActivity.this,year_x + "/" + month_x + "/" + day_x,Toast.LENGTH_LONG).show();
            String fecha=year_x + "/" + month_x + "/" + day_x;


            Calendar cal1=Calendar.getInstance();
            int year_x1=cal1.get(Calendar.YEAR);
            int month_x1=cal1.get(Calendar.MONTH);
            month_x1=month_x1 +1;
            int day_x1=cal1.get(Calendar.DAY_OF_MONTH);

            if(year_x>year_x1)
                mostrarAlerta("Ingrese una fecha valida","Ok");
            else if(year_x1==year_x){
                if(month_x>month_x1)
                    mostrarAlerta("Ingrese una fecha valida","Ok");
                else if(month_x==month_x1){
                    if(day_x>day_x1)
                        mostrarAlerta("Ingrese una fecha valida","Ok");
                    else{
                        EditText textdate = (EditText) findViewById(R.id.editTextDate);
                        textdate.setText(fecha);
                    }
                }else{
                    EditText textdate = (EditText) findViewById(R.id.editTextDate);
                    textdate.setText(fecha);
                }
            }else{
                EditText textdate = (EditText) findViewById(R.id.editTextDate);
                textdate.setText(fecha);
            }

        }
    };

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public void mostrarAlerta(String title,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(title)
                .setCancelable(false)
                .setPositiveButton(msg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PERMISSION_PHONE_STATE);

        }*/

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.READ_PHONE_STATE
        };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        final Calendar cal=Calendar.getInstance();
        year_x=cal.get(Calendar.YEAR);
        month_x=cal.get(Calendar.MONTH);
        day_x=cal.get(Calendar.DAY_OF_MONTH);
        showDialogOnButtonClick();


        TextView tv =findViewById(R.id.textView);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://alertic.ml/legal/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });



        Button b3 =findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a=PhoneStateReceiver.ultimoDesconocido;
                EditText textnums = (EditText) findViewById(R.id.editTextNumS);
                textnums.setText(a);
            }
        });



        Button b1=findViewById(R.id.button13);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText textcc = (EditText) findViewById(R.id.editTextCc);
                String cc = textcc.getText().toString();

                EditText textname = (EditText) findViewById(R.id.editTextName);
                String name = textname.getText().toString();

                EditText textnum = (EditText) findViewById(R.id.editTextNum);
                String num = textnum.getText().toString();

                EditText textdate = (EditText) findViewById(R.id.editTextDate);
                String date = textdate.getText().toString();

                EditText textnums = (EditText) findViewById(R.id.editTextNumS);
                String nums = textnums.getText().toString();

                EditText textdes = (EditText) findViewById(R.id.editTextDes);
                String des = textdes.getText().toString();

                if (cc.compareTo("") == 0 || name.compareTo("") == 0 || num.compareTo("") == 0 || date.compareTo("") == 0
                        || nums.compareTo("") == 0 || des.compareTo("") == 0){
                    mostrarAlerta("Faltan campos por llenar","Ok");
                }else{
                    //cedula, numD, nombreD, fecha, descripcion, numSospechoso

                    Calendar cal1=Calendar.getInstance();
                    int year_x1=cal1.get(Calendar.YEAR);
                    int month_x1=cal1.get(Calendar.MONTH);
                    month_x1=month_x1 +1;
                    int day_x1=cal1.get(Calendar.DAY_OF_MONTH);
                    String fecha1=year_x1 + "/" + month_x1 + "/" + day_x1;

                    denuncia den = new denuncia(cc,num,name,fecha1,date,des,nums);

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    try {
                        DB db=new DB();
                        db.save(den);
                        //denuncia.GetDenunciados();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Toast notificacion = Toast.makeText(MainActivity.this, "Tu denuncia ha sido recibida, gracias por tu contribución", Toast.LENGTH_LONG);
                    notificacion.show();

                    textcc.setText("");
                    textname.setText("");
                    textnum.setText("");
                    textdate.setText("");
                    textnums.setText("");
                    textdes.setText("");
                }



            }
        });

    }

}
