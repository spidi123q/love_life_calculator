package net.sf.icefeast.lovelifecalculator;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;

import android.provider.ContactsContract;

import android.os.Bundle;

import android.view.KeyEvent;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import java.lang.Math;


import java.util.HashSet;

import java.util.Set;
import java.util.TreeSet;


public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private EditText yourNumber,partnerNumber,yourName,partnerName;
    AdView mAdView;










    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
         yourNumber = (EditText) findViewById(R.id.yourNumber);
        yourName = (EditText) findViewById(R.id.yourName);
        partnerNumber = (EditText) findViewById(R.id.partnerNumber);
        partnerName = (EditText) findViewById(R.id.partnerName);

        //setting add view
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //hides add when softkeyboard appears
        partnerNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override

                public void onFocusChange (View v,boolean hasFocus){
                    if (hasFocus) {
                        mAdView.setVisibility(View.GONE);
                    } else {
                        mAdView.setVisibility(View.VISIBLE);
                    }


            }


        });
        partnerName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override

            public void onFocusChange (View v,boolean hasFocus){
                if (hasFocus) {
                    mAdView.setVisibility(View.GONE);
                } else {
                    mAdView.setVisibility(View.VISIBLE);
                }


            }


        });











        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            //filling your details
            //setting import contact button
            final Button contactButton1 = (Button) findViewById(R.id.contactButton1);
            contactButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //performing action
                    Intent i = new Intent(Intent.ACTION_PICK,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(i, 1);

                }


            });
            //filling partner details
            Button contactButton2 = (Button) findViewById(R.id.contactButton2);
            contactButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_PICK,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(i, 2);

                }
            });

        }
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }


    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.


        float x =  event.values[0];
        float y =  event.values[1];
        float z =  event.values[2];
        double magnitude = Math.sqrt(x*x+y*y+z*z);
        if(magnitude>16) {


            String num1 = yourNumber.getText().toString();
            String num2 = partnerNumber.getText().toString();
            String name1 = yourName.getText().toString();
            String name2 = partnerName.getText().toString();
            boolean val = num2.isEmpty() || num1.isEmpty();
            int len;
            //for avoiding duplicate entries
            Set<String> set = new HashSet<String>();


            if (!val) {

                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.shake);
                mp.start();


                /*
                for(int i = 0;i < num1.length();i++){
                    for(int j =0;j < num2.length();j++){
                        if(num1.charAt(i) == num2.charAt(j)){
                            set.add(num1.charAt(i)+"");
                        }
                    }
                }
              */
                Set<Character> characters1 = new TreeSet<>();
                for (int i = 0; i < num1.length(); i++) {
                    characters1.add(num1.charAt(i));
                }

                Set<Character> characters2 = new TreeSet<>();
                for (int i = 0; i < num2.length(); i++) {
                    characters2.add(num2.charAt(i));
                }
                //finding length of smallest string
                if (characters1.size() >= characters2.size()) {

                    len = characters1.size();


                } else {
                    len = characters2.size();
                }

                characters1.retainAll(characters2);


                //finding percentage
                int per = (characters1.size() * 100) / len;

                String percentage = Integer.toString(per);


                Intent myIntent = new Intent(MainActivity.this, ResultActivity.class);

                //sending data to ResultActivity
                myIntent.putExtra("name1", name1);
                myIntent.putExtra("name2", name2);
                myIntent.putExtra("percentage", percentage);
                //playing shake sound


                MainActivity.this.startActivity(myIntent);

            }

            else {
                Toast.makeText(this, "Please enter phone numbers ", Toast.LENGTH_SHORT).show();
            }


        }

        // Do something with this sensor value.
    }





    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }




        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                if (requestCode == 1) {
                    //for your details

                    Cursor cursor = null;


                    Uri result = data.getData();
                    String id = result.getLastPathSegment();

                    //Get Name
                    cursor = getContentResolver().query(result, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        //setting name
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        EditText yourName = (EditText) findViewById(R.id.yourName);
                        yourName.setText(name);
                        //setting number
                        String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        EditText yourNumber = (EditText) findViewById(R.id.yourNumber);
                        yourNumber.setText(number);


                    }

                }
                if (requestCode == 2) {
                    Cursor cursor2 = null;


                    Uri result2 = data.getData();
                    String id2 = result2.getLastPathSegment();

                    //Get Name
                    cursor2 = getContentResolver().query(result2, null, null, null, null);
                    if (cursor2.moveToFirst()) {
                        //setting name
                        String name2 = cursor2.getString(cursor2.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        EditText partnerName = (EditText) findViewById(R.id.partnerName);
                        partnerName.setText(name2);
                        //setting number
                        String number2 = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        EditText partnerNumber = (EditText) findViewById(R.id.partnerNumber);
                        partnerNumber.setText(number2);
                    }
                }


            }
        }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    }


