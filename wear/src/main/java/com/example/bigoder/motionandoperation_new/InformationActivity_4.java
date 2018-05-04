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

public class InformationActivity_4 extends Activity implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    GoogleApiClient mGoogleAppiClient;
    double x1 = 0;
    double x2 = 0;
    double y1 = 0;
    double y2 = 0;
    double t1 = 0;
    double t2 = 0;
    String a[] = new String[2];
    String pws = "0PWS";
    String look_text = "null";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testRandom();
        setContentView(R.layout.information_3rd);
        final Button button3 = (Button) findViewById(R.id.button301);
        button3.setText(look_text);

        mGoogleAppiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void testRandom(){
        int num;
        Random random = new Random();
        num = random.nextInt(7) + 1;
        if(num==1){pws = "0PWS";look_text="今早是阴天，记得带雨伞.";}
        if(num==2){pws = "0.5PWS";look_text="今早是雨天，记得带外衣.";}
        if(num==3){pws = "0.75PWS";look_text="今早是雪天，记得带衣服.";}
        if(num==4){pws = "1PWS";look_text="今晚有雷雨，记得带雨伞.";}
        if(num==5){pws = "1.25PWS";look_text="今天是晴天，记得少穿点.";}
        if(num==6){pws = "1.5PWS";look_text="今晚有大风，最好别出门.";}
        if(num==7){pws = "2PWS";look_text="今天空气差，最好别出门.";}
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

                    if(content.equals("2_click")){
                        sendTextToPhone("look_SMS:" + look_text);
                        //for(int i=0;i <= 200;i++){Log.e("info","2_click");}
                        Intent intent = new Intent();
                        intent.setClass(InformationActivity_4.this, MainActivity.class);
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