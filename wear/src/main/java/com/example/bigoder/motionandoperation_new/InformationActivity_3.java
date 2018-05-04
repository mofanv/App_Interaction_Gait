package com.example.bigoder.motionandoperation_new;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Random;

public class InformationActivity_3 extends Activity implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    GoogleApiClient mGoogleAppiClient;
    String a[] = new String[2];
    String pws = "0PWS";
    String look_text = "null";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testRandom();
        setContentView(R.layout.information_2nd);
        final Button button2 = (Button) findViewById(R.id.button201);
        button2.setText(a[1]);

        mGoogleAppiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    private void testRandom(){
        int num[] = new int[3];
        Random random = new Random();
        num[0] = random.nextInt(10000) + 20000;
        num[1] = random.nextInt(85) + 10;
        a[0] = num[0] + "步";
        a[1] = num[1] + "分钟";
    }

    @Override
    protected void onStart() {
        Log.e("watch","onStart");
        mGoogleAppiClient.connect();
        super.onStart();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for(DataEvent event: dataEventBuffer){
            if(event.getType() == DataEvent.TYPE_DELETED){

            }else if(event.getType() == DataEvent.TYPE_CHANGED){
                DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                if(event.getDataItem().getUri().getPath().equals("/phone_to_watch")){
                    String content = dataMap.get("content");

                    if(content.equals("1_3_look")){
                        sendTextToPhone("look_walk_time:" + a[1]);
                        //for(int i=0;i <= 200;i++){Log.e("info","look_3");}
                        Intent intent = new Intent();
                        intent.setClass(InformationActivity_3.this, InformationActivity_4.class);
                        startActivity(intent);
                        finish();

                    }
                    if(content.equals("2_click")){
                        sendTextToPhone("look_walk_time:" + a[1]);
                        //for(int i=0;i <= 200;i++){Log.e("info","2_click");}
                        Intent intent = new Intent();
                        intent.setClass(InformationActivity_3.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }
    }


    private void sendTextToPhone(String content){
        PutDataMapRequest request1 = PutDataMapRequest.create("/watch_to_phone");
        DataMap dataMap1 = request1.getDataMap();
        dataMap1.putLong("time", System.currentTimeMillis());
        dataMap1.putString("content", content);
        Wearable.DataApi.putDataItem(mGoogleAppiClient, request1.asPutDataRequest());
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleAppiClient, this);
        Log.e("watch", "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("watch", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("watch","FAILED TO CONNECT: 16");
    }

    protected void onStop(){
        if(null != mGoogleAppiClient && mGoogleAppiClient.isConnected()){
            Wearable.DataApi.removeListener(mGoogleAppiClient,this);
            mGoogleAppiClient.disconnect();
        }
        super.onStop();
    }

}