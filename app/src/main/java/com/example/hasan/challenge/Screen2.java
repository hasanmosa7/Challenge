package com.example.hasan.challenge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Screen2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);
        TextView textViewSN = (TextView)findViewById(R.id.textViewSNinfo);
        TextView textViewIMEI = (TextView)findViewById(R.id.textViewIMEIinfo);
        TextView textViewEmail = (TextView)findViewById(R.id.textViewEmailinfo);
        Button screen3 = (Button) findViewById(R.id.buttonScreen3);
        Button screen4 = (Button) findViewById(R.id.buttonScreen4);
        Button screen5 = (Button) findViewById(R.id.buttonScreen5);
        screen3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Screen3.class));
            }
        });
        screen4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Screen4.class));
            }
        });
        screen5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Screen5.class));
            }
        });
        String IMEI = getDeviceIMEI(this);
        String SN = getDeviceSN();
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String friends = intent.getStringExtra("friends");
        JSONArray friendsJArray;
        ArrayList<String> friends_list = new ArrayList<String>();

        try {
            friendsJArray = new JSONArray(friends);
            for (int l=0; l < friendsJArray.length(); l++) {
                friends_list.add(friendsJArray.getJSONObject(l).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, friends_list);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        textViewSN.setText(SN);
        textViewIMEI.setText(IMEI);
        textViewEmail.setText(email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_screen2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getDeviceIMEI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public String getDeviceSN(){
        return Build.SERIAL;
    }
}
