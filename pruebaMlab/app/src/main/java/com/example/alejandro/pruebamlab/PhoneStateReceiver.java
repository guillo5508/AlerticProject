package com.example.alejandro.pruebamlab;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStreamWriter;
import java.util.zip.Inflater;

import static java.security.AccessController.getContext;

public class PhoneStateReceiver extends BroadcastReceiver {

    public static String ultimoDesconocido="";

    static boolean lastCall;

    public void showToast(String msg,int imagen,int color,Context context,int colortext){
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.toast_layout, null);
        //View layout = inflater.inflate(R.layout.toast_layout,(ViewGroup)findViewById(R.id.toast_root));

        LinearLayout l=layout.findViewById(R.id.toast_root);
        //color= Color.el_color_que_quiera
        l.setBackgroundColor(color);

        TextView toastText=layout.findViewById(R.id.toast_text);
        ImageView toastImage=layout.findViewById(R.id.toast_image);

        toastText.setText(msg);
        toastText.setTextColor(colortext);
        //imagen debe ser R.drawable.nombre_del_icono
        toastImage.setImageResource(imagen);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        try {

            System.out.println("Receiver start");
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);


           // Toast.makeText(context,incomingNumber,Toast.LENGTH_SHORT).show();


            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                //Toast.makeText(context,"Ringing State Number is -"+incomingNumber,Toast.LENGTH_SHORT).show();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                boolean isreported=false;

                if(DB.isConnected(context)){
                    isreported=DB.verifyLocal(incomingNumber,context);
                    if(!isreported){
                        isreported=DB.verifyDB(incomingNumber,context);
                        DB.localSync(context);
                    }

                }else{
                    isreported=DB.verifyLocal(incomingNumber,context);
                }

                if(isreported){
                    //Toast.makeText(context,"¡Cuidado!, Este número ha sido reportado como sospechoso",Toast.LENGTH_LONG).show();
                    showToast("¡Cuidado!, Este número ha sido reportado como sospechoso",R.drawable.ic_warning, Color.rgb(219,29,29),context,Color.WHITE);
                }

                ContentResolver resolver=context.getContentResolver();
                Cursor cursor=resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

                boolean found=false;
                while(cursor.moveToNext() && !found){
                    String id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name =cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    Cursor phoneCursor=resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?",new String[]{id},null);


                    while(phoneCursor.moveToNext() && !found){
                        String phoneNumber=phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    /*if(phoneNumber.length()>10){
                        phoneNumber=phoneNumber.substring(3,phoneNumber.length());
                    }*/
                        phoneNumber=phoneNumber.replace(" ","");
                        phoneNumber=phoneNumber.replace("(","");
                        phoneNumber=phoneNumber.replace(")","");
                        phoneNumber=phoneNumber.replace("-","");
                        phoneNumber=phoneNumber.replace("+","");

                        if(phoneNumber.length()>10){
                            phoneNumber=phoneNumber.substring(phoneNumber.length()-10,phoneNumber.length());
                        }
                        if(phoneNumber.compareTo(incomingNumber)==0){
                            //Toast.makeText(context,name+" "+phoneNumber,Toast.LENGTH_SHORT).show();
                            found=true;
                            lastCall=true;
                        }
                    }

                }
                if(found==false){
                    //Toast.makeText(context,"Desconocido "+incomingNumber,Toast.LENGTH_SHORT).show();
                    ultimoDesconocido=incomingNumber;
                    lastCall=false;
                }

            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
               // Toast.makeText(context,"In call",Toast.LENGTH_LONG).show();
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                if(lastCall==false){
                    showToast("¿Notaste algo sospechoso? repórtalo en Alertic.",R.drawable.ic_alertic, Color.rgb(232,232,232),context,Color.BLACK);
                    //Toast.makeText(context,"¿Notaste algo sospechoso? repórtalo en Alertic.",Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(context,"Cal ended",Toast.LENGTH_SHORT).show();
            }







        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}