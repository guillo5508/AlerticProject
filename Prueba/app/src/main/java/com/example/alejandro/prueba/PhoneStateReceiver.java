package com.example.alejandro.prueba;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.OutputStreamWriter;

public class PhoneStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        try {

            System.out.println("Receiver start");
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);


            //Toast.makeText(context,incomingNumber,Toast.LENGTH_SHORT).show();


            /*if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                Toast.makeText(context,"Ringing State Number is -"+incomingNumber,Toast.LENGTH_SHORT).show();
            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
                Toast.makeText(context,"Received State",Toast.LENGTH_SHORT).show();
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Toast.makeText(context,"Idle State",Toast.LENGTH_SHORT).show();
            }*/


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
                    if(phoneNumber.compareTo(incomingNumber)==0){
                        Toast.makeText(context,name + " "+phoneNumber,Toast.LENGTH_SHORT).show();
                        found=true;
                    }
                }

            }
            if(found==false){
                Toast.makeText(context,"Desconocido "+incomingNumber,Toast.LENGTH_SHORT).show();

                /*OutputStreamWriter fout=
                        new OutputStreamWriter(
                                context.openFileOutput("prueba_int.txt", Context.MODE_PRIVATE));

                fout.write("Texto de prueba.");
                fout.close();*/
            }






        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
