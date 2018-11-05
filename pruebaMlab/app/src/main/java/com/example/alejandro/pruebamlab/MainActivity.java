package com.example.alejandro.pruebamlab;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.alejandro.pruebamlab.DB.getDenunciados;
import static com.example.alejandro.pruebamlab.DB.localSync;
import static com.example.alejandro.pruebamlab.DB.localpath;

public class MainActivity extends AppCompatActivity {

    Button btn;
    int year_x,month_x,day_x;
    static final int DIALOG_ID=0;
    public static final String localpath = "prueba.txt";


    public void showToast(String msg,int imagen,int color){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,(ViewGroup)findViewById(R.id.toast_root));

        LinearLayout l=layout.findViewById(R.id.toast_root);
        //color= Color.el_color_que_quiera
        l.setBackgroundColor(color);

        TextView toastText=layout.findViewById(R.id.toast_text);
        ImageView toastImage=layout.findViewById(R.id.toast_image);

        toastText.setText(msg);
        //imagen debe ser R.drawable.nombre_del_icono
        toastImage.setImageResource(imagen);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


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


    public void recurrentLocalSync() throws IOException {


        String temp = DB.getDenunciados();

        File path = getApplicationContext().getFilesDir();
        File file = new File(path, localpath);
        ;
        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(temp.getBytes());
        } finally {
            stream.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context=this;
        // Create the Handler object (on the main thread by default)
        final Handler handler = new Handler();
        // Define the code block to be executed
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(MainActivity.this, "Its been 2 seconds", Toast.LENGTH_SHORT).show();
                try{
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    recurrentLocalSync();
                }catch (IOException e){
                    e.printStackTrace();
                }

                handler.postDelayed(this, 1000*60*5);
            }
        };
        // Start the initial runnable task by posting through the handler
        handler.post(runnableCode);



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
                //showToast("hola soy un error",R.drawable.ic_toasticon,Color.argb(255,255,0,0));
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
                        //DB db=new DB();
                        if(DB.isConnected(getApplicationContext())){
                            DB.save(den,getApplicationContext());

                            //Toast.makeText(MainActivity.this, "Tu denuncia ha sido recibida, gracias por tu contribución", Toast.LENGTH_LONG).show();
                            showToast("Tu denuncia ha sido recibida, gracias por tu contribución",R.drawable.ic_done,Color.rgb(113,194,58));

                            textcc.setText("");
                            textname.setText("");
                            textnum.setText("");
                            textdate.setText("");
                            textnums.setText("");
                            textdes.setText("");
                        }else{
                            //Toast.makeText(MainActivity.this, "No pudimos recibir tu reporte, verifica tu conexión a internet e inténtalo de nuevo", Toast.LENGTH_LONG).show();
                            showToast("No pudimos recibir tu reporte, verifica tu conexión a internet e inténtalo de nuevo",R.drawable.ic_desconnection,Color.rgb(219,29,29));
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                }



            }
        });

    }

}
