package com.example.bigoder.motionandoperation_new;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.DismissOverlayView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    int n_target = 36;

    private DismissOverlayView mDismissOverlay;
    public int[] obj=new int[3*n_target];
    GoogleApiClient mGoogleAppiClient;

    int start_tf = 1;
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    float mPosX,mPosY;
    String mPosXY;

    long t1 = 0;
    long t2 = 0;
    int targetnum = 0;
    int viewpagernum = 0;
    String l_b = "0,0";
    String[] l_bo = new String[n_target];

    LinearLayout mNumLayout;
    ImageView mPreSelectedBt;

    // 1.37英寸360×325分辨率屏幕（像素密度为263ppi)
    // 1dp = 2.21259124px
    int n_1 = 15;
    int height = 360;
    int width = 325;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obj = testRandomNew();
        setContentView(R.layout.activity_main);
        initViewPager();
        mGoogleAppiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        // Obtain the DismissOverlayView element
        mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlay.setIntroText(R.string.long_press_intro);
        mDismissOverlay.showIntroIfNecessary();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    public void buildEndDialog(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("—————————")
                .setMessage("请放下手臂")
                .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for(DataEvent event: dataEventBuffer){
            if(event.getType() == DataEvent.TYPE_DELETED){

            }else if(event.getType() == DataEvent.TYPE_CHANGED){
                DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                if(event.getDataItem().getUri().getPath().equals("/phone_to_watch")) {
                    String content = dataMap.get("content");
                    if (content.equals("3_voice")) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, SpeakActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }
    }

    private int[] testRandomNew(){
        Random random = new Random();
        int a[] = new int[3*n_target];
        int a1[] = new int[n_target];
        int a2[] = new int[n_target];
        int a3[] = new int[n_target];
        int b1[][] = new int[3][n_target/3];
        int b2[][] = new int[3][n_target/3];
        int b3[][] = new int[3][n_target/3];

        for(int i=0; i<a1.length; i++){
            a1[i] = random.nextInt(n_target)+1;
            for(int j=0; j<i; j++){
                if(a1[i]==a1[j]){
                    i--;
                }
            }
        }
        for(int i1=0; i1<3; i1++){
            for(int j1=0; j1<(n_target/3); j1++){
                b1[i1][j1] = a1[12*i1+j1];
            }
        }
        for(int i=0; i<a1.length; i++){
            a2[i] = random.nextInt(n_target)+n_target+1;
            for(int j=0; j<i; j++){
                if(a2[i]==a2[j]){
                    i--;
                }
            }
        }
        for(int i2=0;i2<3;i2++){
            for(int j2=0;j2<n_target/3;j2++){
                b2[i2][j2] = a2[12*i2+j2];
            }
        }
        for(int i=0; i<a1.length; i++){
            a3[i] = random.nextInt(n_target)+2*n_target+1;
            for(int j=0; j<i; j++){
                if (a3[i] == a3[j]) {
                    i--;
                }
            }
        }
        for(int i3=0;i3<3;i3++){
            for(int j3=0;j3<n_target/3;j3++){
                b3[i3][j3] = a3[12*i3+j3];
            }
        }

        for(int i=0;i<3;i++){
            for(int j=0;j<n_target/3;j++){
                a[n_target/3*(3*i)+j] = b1[i][j];
                a[n_target/3*(3*i+1)+j] = b2[i][j];
                a[n_target/3*(3*i+2)+j] = b3[i][j];
            }
        }
        return a;
    }

    private void sendTextToPhone(String content){
        PutDataMapRequest request1 = PutDataMapRequest.create("/watch_to_phone");
        DataMap dataMap1 = request1.getDataMap();
        dataMap1.putLong("time", System.currentTimeMillis());
        dataMap1.putString("content", content);
        Wearable.DataApi.putDataItem(mGoogleAppiClient, request1.asPutDataRequest());
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        try{
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //当手指按下的时候
                x1 = event.getX();
                y1 = event.getY();
                t1 = event.getEventTime();
                mPosXY = String.valueOf(x1) + "," + String.valueOf(y1) + "," + String.valueOf(t1);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //当手指离开的时候
                x2 = event.getX();
                y2 = event.getY();
                t2 = event.getEventTime();
                String xy = "slip," + mPosXY + "," + String.valueOf(t2);
                if(l_b == null){l_b = "0,0";}
                String xo = "click," + l_b + "," + String.valueOf(x1) + "," + String.valueOf(y1) + "," + String.valueOf(t1) +
                        "," + String.valueOf(x2) + "," + String.valueOf(y2) + "," + String.valueOf(t2);
                if(Math.abs(y1 - y2) < 30 && Math.abs(x1 - x2) < 30 && t2 - t1 > 3000){
                    mDismissOverlay.show();
                }
                if(Math.abs(y1 - y2) < 30 && Math.abs(x1 - x2) < 30 && t2 - t1 <= 3000){
                    sendTextToPhone(xo);
                    Log.e("click", xo);
                    if(targetnum < n_target*3-1){
                        targetnum = targetnum+1;
                    }else{
                        buildEndDialog();
                        sendTextToPhone("finish_point");
                    }
                    initViewPager();
                }
                if (Math.abs(y1 - y2) > 30) {
                    sendTextToPhone(xy);
                    Log.e("slip", xy);
                } else if (Math.abs(x1 - x2) > 30) {
                    sendTextToPhone(xy);
                    Log.e("slip", xy);
                }
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                mPosX = event.getX();
                mPosY = event.getY();
                mPosXY = mPosXY + "," + String.valueOf(mPosX) + "," + String.valueOf(mPosY);
                Log.e("move", mPosXY);
            }
            return super.dispatchTouchEvent(event);
        }catch(Exception e){
            return false;
        }
    }

    protected void onStart() {
        mGoogleAppiClient.connect();
        super.onStart();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleAppiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop(){
        if(null != mGoogleAppiClient && mGoogleAppiClient.isConnected()){
            Wearable.DataApi.removeListener(mGoogleAppiClient,this);
            mGoogleAppiClient.disconnect();
        }
        super.onStop();
    }

    private void initViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        View view1 = LayoutInflater.from(this).inflate(R.layout.activity_1st, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.activity_2nd, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.activity_3rd, null);

        ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);

        MYViewPagerAdapter adapter = new MYViewPagerAdapter();
        adapter.setViews(views);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(viewpagernum, false);

        mNumLayout = (LinearLayout) findViewById(R.id.main_dot);

        if (start_tf == 1){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zzz_page_dot_normal);
            for (int i = 0; i < adapter.getCount(); i++) {
                ImageView bt = new ImageView(this);
                bt.setLayoutParams(new ViewGroup.LayoutParams(bitmap.getWidth(),bitmap.getHeight()));
                bt.setImageResource(R.drawable.zzz_page_dot_normal);
                mNumLayout.addView(bt);
            }
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (mPreSelectedBt != null) {
                    mPreSelectedBt.setImageResource(R.drawable.zzz_page_dot_normal);
                }
                ImageView currentBt = (ImageView) mNumLayout.getChildAt(position);
                currentBt.setImageResource(R.drawable.zzz_page_dot_select);
                mPreSelectedBt = currentBt;
                //Log.i("INFO", "current item:"+position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });


        final View linearlayout1 = (View) view1.findViewById(R.id.linearLayout2);
        final ImageButton button1 = (ImageButton) view1.findViewById(R.id.button1);
        final ImageButton button2 = (ImageButton) view1.findViewById(R.id.button2);
        final ImageButton button3 = (ImageButton) view1.findViewById(R.id.button3);
        final ImageButton button4 = (ImageButton) view1.findViewById(R.id.button4);
        final ImageButton button5 = (ImageButton) view1.findViewById(R.id.button5);
        final ImageButton button6 = (ImageButton) view1.findViewById(R.id.button6);
        final ImageButton button7 = (ImageButton) view1.findViewById(R.id.button7);
        final ImageButton button8 = (ImageButton) view1.findViewById(R.id.button8);
        final ImageButton button9 = (ImageButton) view1.findViewById(R.id.button9);
        final ImageButton button10 = (ImageButton) view1.findViewById(R.id.button10);
        final ImageButton button11 = (ImageButton) view1.findViewById(R.id.button11);
        final ImageButton button12 = (ImageButton) view1.findViewById(R.id.button12);
        final ImageButton button13 = (ImageButton) view1.findViewById(R.id.button13);
        final ImageButton button14 = (ImageButton) view1.findViewById(R.id.button14);
        final ImageButton button15 = (ImageButton) view1.findViewById(R.id.button15);
        final ImageButton button16 = (ImageButton) view1.findViewById(R.id.button16);
        final ImageButton button17 = (ImageButton) view1.findViewById(R.id.button17);
        final ImageButton button18 = (ImageButton) view1.findViewById(R.id.button18);
        final ImageButton button19 = (ImageButton) view1.findViewById(R.id.button19);
        final ImageButton button20 = (ImageButton) view1.findViewById(R.id.button20);
        final ImageButton button21 = (ImageButton) view1.findViewById(R.id.button21);
        final ImageButton button22 = (ImageButton) view1.findViewById(R.id.button22);
        final ImageButton button23 = (ImageButton) view1.findViewById(R.id.button23);
        final ImageButton button24 = (ImageButton) view1.findViewById(R.id.button24);
        final ImageButton button25 = (ImageButton) view1.findViewById(R.id.button25);
        final ImageButton button26 = (ImageButton) view1.findViewById(R.id.button26);
        final ImageButton button27 = (ImageButton) view1.findViewById(R.id.button27);
        final ImageButton button28 = (ImageButton) view1.findViewById(R.id.button28);
        final ImageButton button29 = (ImageButton) view1.findViewById(R.id.button29);
        final ImageButton button30 = (ImageButton) view1.findViewById(R.id.button30);
        final ImageButton button31 = (ImageButton) view1.findViewById(R.id.button31);
        final ImageButton button32 = (ImageButton) view1.findViewById(R.id.button32);
        final ImageButton button33 = (ImageButton) view1.findViewById(R.id.button33);
        final ImageButton button34 = (ImageButton) view1.findViewById(R.id.button34);
        final ImageButton button35 = (ImageButton) view1.findViewById(R.id.button35);
        final ImageButton button36 = (ImageButton) view1.findViewById(R.id.button36);
        //final ImageButton button37 = (ImageButton) view1.findViewById(R.id.button37);

        final ImageButton button1_2 = (ImageButton) view2.findViewById(R.id.button1_2);
        final ImageButton button2_2 = (ImageButton) view2.findViewById(R.id.button2_2);
        final ImageButton button3_2 = (ImageButton) view2.findViewById(R.id.button3_2);
        final ImageButton button4_2 = (ImageButton) view2.findViewById(R.id.button4_2);
        final ImageButton button5_2 = (ImageButton) view2.findViewById(R.id.button5_2);
        final ImageButton button6_2 = (ImageButton) view2.findViewById(R.id.button6_2);
        final ImageButton button7_2 = (ImageButton) view2.findViewById(R.id.button7_2);
        final ImageButton button8_2 = (ImageButton) view2.findViewById(R.id.button8_2);
        final ImageButton button9_2 = (ImageButton) view2.findViewById(R.id.button9_2);
        final ImageButton button10_2 = (ImageButton) view2.findViewById(R.id.button10_2);
        final ImageButton button11_2 = (ImageButton) view2.findViewById(R.id.button11_2);
        final ImageButton button12_2 = (ImageButton) view2.findViewById(R.id.button12_2);
        final ImageButton button13_2 = (ImageButton) view2.findViewById(R.id.button13_2);
        final ImageButton button14_2 = (ImageButton) view2.findViewById(R.id.button14_2);
        final ImageButton button15_2 = (ImageButton) view2.findViewById(R.id.button15_2);
        final ImageButton button16_2 = (ImageButton) view2.findViewById(R.id.button16_2);
        final ImageButton button17_2 = (ImageButton) view2.findViewById(R.id.button17_2);
        final ImageButton button18_2 = (ImageButton) view2.findViewById(R.id.button18_2);
        final ImageButton button19_2 = (ImageButton) view2.findViewById(R.id.button19_2);
        final ImageButton button20_2 = (ImageButton) view2.findViewById(R.id.button20_2);
        final ImageButton button21_2 = (ImageButton) view2.findViewById(R.id.button21_2);
        final ImageButton button22_2 = (ImageButton) view2.findViewById(R.id.button22_2);
        final ImageButton button23_2 = (ImageButton) view2.findViewById(R.id.button23_2);
        final ImageButton button24_2 = (ImageButton) view2.findViewById(R.id.button24_2);
        final ImageButton button25_2 = (ImageButton) view2.findViewById(R.id.button25_2);
        final ImageButton button26_2 = (ImageButton) view2.findViewById(R.id.button26_2);
        final ImageButton button27_2 = (ImageButton) view2.findViewById(R.id.button27_2);
        final ImageButton button28_2 = (ImageButton) view2.findViewById(R.id.button28_2);
        final ImageButton button29_2 = (ImageButton) view2.findViewById(R.id.button29_2);
        final ImageButton button30_2 = (ImageButton) view2.findViewById(R.id.button30_2);
        final ImageButton button31_2 = (ImageButton) view2.findViewById(R.id.button31_2);
        final ImageButton button32_2 = (ImageButton) view2.findViewById(R.id.button32_2);
        final ImageButton button33_2 = (ImageButton) view2.findViewById(R.id.button33_2);
        final ImageButton button34_2 = (ImageButton) view2.findViewById(R.id.button34_2);
        final ImageButton button35_2 = (ImageButton) view2.findViewById(R.id.button35_2);
        final ImageButton button36_2 = (ImageButton) view2.findViewById(R.id.button36_2);
        //final ImageButton button37_2 = (ImageButton) view2.findViewById(R.id.button37_2);

        final ImageButton button1_3 = (ImageButton) view3.findViewById(R.id.button1_3);
        final ImageButton button2_3 = (ImageButton) view3.findViewById(R.id.button2_3);
        final ImageButton button3_3 = (ImageButton) view3.findViewById(R.id.button3_3);
        final ImageButton button4_3 = (ImageButton) view3.findViewById(R.id.button4_3);
        final ImageButton button5_3 = (ImageButton) view3.findViewById(R.id.button5_3);
        final ImageButton button6_3 = (ImageButton) view3.findViewById(R.id.button6_3);
        final ImageButton button7_3 = (ImageButton) view3.findViewById(R.id.button7_3);
        final ImageButton button8_3 = (ImageButton) view3.findViewById(R.id.button8_3);
        final ImageButton button9_3 = (ImageButton) view3.findViewById(R.id.button9_3);
        final ImageButton button10_3 = (ImageButton) view3.findViewById(R.id.button10_3);
        final ImageButton button11_3 = (ImageButton) view3.findViewById(R.id.button11_3);
        final ImageButton button12_3 = (ImageButton) view3.findViewById(R.id.button12_3);
        final ImageButton button13_3 = (ImageButton) view3.findViewById(R.id.button13_3);
        final ImageButton button14_3 = (ImageButton) view3.findViewById(R.id.button14_3);
        final ImageButton button15_3 = (ImageButton) view3.findViewById(R.id.button15_3);
        final ImageButton button16_3 = (ImageButton) view3.findViewById(R.id.button16_3);
        final ImageButton button17_3 = (ImageButton) view3.findViewById(R.id.button17_3);
        final ImageButton button18_3 = (ImageButton) view3.findViewById(R.id.button18_3);
        final ImageButton button19_3 = (ImageButton) view3.findViewById(R.id.button19_3);
        final ImageButton button20_3 = (ImageButton) view3.findViewById(R.id.button20_3);
        final ImageButton button21_3 = (ImageButton) view3.findViewById(R.id.button21_3);
        final ImageButton button22_3 = (ImageButton) view3.findViewById(R.id.button22_3);
        final ImageButton button23_3 = (ImageButton) view3.findViewById(R.id.button23_3);
        final ImageButton button24_3 = (ImageButton) view3.findViewById(R.id.button24_3);
        final ImageButton button25_3 = (ImageButton) view3.findViewById(R.id.button25_3);
        final ImageButton button26_3 = (ImageButton) view3.findViewById(R.id.button26_3);
        final ImageButton button27_3 = (ImageButton) view3.findViewById(R.id.button27_3);
        final ImageButton button28_3 = (ImageButton) view3.findViewById(R.id.button28_3);
        final ImageButton button29_3 = (ImageButton) view3.findViewById(R.id.button29_3);
        final ImageButton button30_3 = (ImageButton) view3.findViewById(R.id.button30_3);
        final ImageButton button31_3 = (ImageButton) view3.findViewById(R.id.button31_3);
        final ImageButton button32_3 = (ImageButton) view3.findViewById(R.id.button32_3);
        final ImageButton button33_3 = (ImageButton) view3.findViewById(R.id.button33_3);
        final ImageButton button34_3 = (ImageButton) view3.findViewById(R.id.button34_3);
        final ImageButton button35_3 = (ImageButton) view3.findViewById(R.id.button35_3);
        final ImageButton button36_3 = (ImageButton) view3.findViewById(R.id.button36_3);
        //final ImageButton button37_3 = (ImageButton) view3.findViewById(R.id.button37_3);


        if (start_tf == 1) {
            start_tf = 0;
            new Thread() {
                public void run() {
                    synchronized (this) {
                        try {
                            wait(1000);// 1秒
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                  }
                    double lly = linearlayout1.getY()+1.5;
                    double llh = linearlayout1.getHeight();
                    l_bo[0] = String.valueOf(button1.getX() + button1.getWidth() / 2) + "," + String.valueOf(lly+ llh * 1/2);
                    l_bo[1] = String.valueOf(button2.getX() + button2.getWidth() / 2) + "," + String.valueOf(lly+ llh * 3/2);
                    l_bo[2] = String.valueOf(button3.getX() + button3.getWidth() / 2) + "," + String.valueOf(lly+ llh * 3/2);

                    l_bo[3] = String.valueOf(button4.getX() + button4.getWidth() / 2) + "," + String.valueOf(lly+ llh * 5/2);
                    l_bo[4] = String.valueOf(button5.getX() + button5.getWidth() / 2) + "," + String.valueOf(lly+ llh * 5/2);
                    l_bo[5] = String.valueOf(button6.getX() + button6.getWidth() / 2) + "," + String.valueOf(lly+ llh * 5/2);
                    l_bo[6] = String.valueOf(button7.getX() + button7.getWidth() / 2) + "," + String.valueOf(lly+ llh * 7/2);
                    l_bo[7] = String.valueOf(button8.getX() + button8.getWidth() / 2) + "," + String.valueOf(lly+ llh * 7/2);
                    l_bo[8] = String.valueOf(button9.getX() + button9.getWidth() / 2) + "," + String.valueOf(lly+ llh * 7/2);
                    l_bo[9] = String.valueOf(button10.getX() + button10.getWidth() / 2) + "," + String.valueOf(lly+ llh * 7/2);

                    l_bo[10] = String.valueOf(button11.getX() + button11.getWidth() / 2) + "," + String.valueOf(lly+ llh * 9/2);
                    l_bo[11] = String.valueOf(button12.getX() + button12.getWidth() / 2) + "," + String.valueOf(lly+ llh * 9/2);
                    l_bo[12] = String.valueOf(button13.getX() + button13.getWidth() / 2) + "," + String.valueOf(lly+ llh * 9/2);
                    l_bo[13] = String.valueOf(button14.getX() + button14.getWidth() / 2) + "," + String.valueOf(lly+ llh * 11/2);
                    l_bo[14] = String.valueOf(button15.getX() + button15.getWidth() / 2) + "," + String.valueOf(lly+ llh * 11/2);
                    l_bo[15] = String.valueOf(button16.getX() + button16.getWidth() / 2) + "," + String.valueOf(lly+ llh * 11/2);
                    l_bo[16] = String.valueOf(button17.getX() + button17.getWidth() / 2) + "," + String.valueOf(lly+ llh * 11/2);

                    l_bo[17] = String.valueOf(button18.getX() + button18.getWidth() / 2) + "," + String.valueOf(lly+ llh * 13/2);
                    l_bo[18] = String.valueOf(button19.getX() + button19.getWidth() / 2) + "," + String.valueOf(lly+ llh * 13/2);
                    l_bo[19] = String.valueOf(button20.getX() + button20.getWidth() / 2) + "," + String.valueOf(lly+ llh * 13/2);
                    l_bo[20] = String.valueOf(button21.getX() + button21.getWidth() / 2) + "," + String.valueOf(lly+ llh * 15/2);
                    l_bo[21] = String.valueOf(button22.getX() + button22.getWidth() / 2) + "," + String.valueOf(lly+ llh * 15/2);
                    l_bo[22] = String.valueOf(button23.getX() + button23.getWidth() / 2) + "," + String.valueOf(lly+ llh * 15/2);
                    l_bo[23] = String.valueOf(button24.getX() + button24.getWidth() / 2) + "," + String.valueOf(lly+ llh * 15/2);

                    l_bo[24] = String.valueOf(button25.getX() + button25.getWidth() / 2) + "," + String.valueOf(lly+ llh * 17/2);
                    l_bo[25] = String.valueOf(button26.getX() + button26.getWidth() / 2) + "," + String.valueOf(lly+ llh * 17/2);
                    l_bo[26] = String.valueOf(button27.getX() + button27.getWidth() / 2) + "," + String.valueOf(lly+ llh * 17/2);
                    l_bo[27] = String.valueOf(button28.getX() + button28.getWidth() / 2) + "," + String.valueOf(lly+ llh * 19/2);
                    l_bo[28] = String.valueOf(button29.getX() + button29.getWidth() / 2) + "," + String.valueOf(lly+ llh * 19/2);
                    l_bo[29] = String.valueOf(button30.getX() + button30.getWidth() / 2) + "," + String.valueOf(lly+ llh * 19/2);
                    l_bo[30] = String.valueOf(button31.getX() + button31.getWidth() / 2) + "," + String.valueOf(lly+ llh * 19/2);

                    l_bo[31] = String.valueOf(button32.getX() + button32.getWidth() / 2) + "," + String.valueOf(lly+ llh * 21/2);
                    l_bo[32] = String.valueOf(button33.getX() + button33.getWidth() / 2) + "," + String.valueOf(lly+ llh * 21/2);
                    l_bo[33] = String.valueOf(button34.getX() + button34.getWidth() / 2) + "," + String.valueOf(lly+ llh * 21/2);
                    l_bo[34] = String.valueOf(button35.getX() + button35.getWidth() / 2) + "," + String.valueOf(lly+ llh * 23/2);
                    l_bo[35] = String.valueOf(button36.getX() + button36.getWidth() / 2) + "," + String.valueOf(lly+ llh * 23/2);
                    //l_bo[36] = String.valueOf(button37.getX() + button37.getWidth() / 2) + "," + String.valueOf(lly+ llh * 25/2);

                }
            }.start();
        }

        if(obj[targetnum]==1){button1.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[0];}
        if(obj[targetnum]==2){button2.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[1];}
        if(obj[targetnum]==3){button3.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[2];}
        if(obj[targetnum]==4){button4.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[3];}
        if(obj[targetnum]==5){button5.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[4];}
        if(obj[targetnum]==6){button6.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[5];}
        if(obj[targetnum]==7){button7.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[6];}
        if(obj[targetnum]==8){button8.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[7];}
        if(obj[targetnum]==9){button9.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[8];}
        if(obj[targetnum]==10){button10.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[9];}
        if(obj[targetnum]==11){button11.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[10];}
        if(obj[targetnum]==12){button12.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[11];}
        if(obj[targetnum]==13){button13.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[12];}
        if(obj[targetnum]==14){button14.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[13];}
        if(obj[targetnum]==15){button15.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[14];}
        if(obj[targetnum]==16){button16.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[15];}
        if(obj[targetnum]==17){button17.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[16];}
        if(obj[targetnum]==18){button18.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[17];}
        if(obj[targetnum]==19){button19.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[18];}
        if(obj[targetnum]==20){button20.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[19];}
        if(obj[targetnum]==21){button21.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[20];}
        if(obj[targetnum]==22){button22.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[21];}
        if(obj[targetnum]==23){button23.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[22];}
        if(obj[targetnum]==24){button24.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[23];}
        if(obj[targetnum]==25){button25.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[24];}
        if(obj[targetnum]==26){button26.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[25];}
        if(obj[targetnum]==27){button27.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[26];}
        if(obj[targetnum]==28){button28.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[27];}
        if(obj[targetnum]==29){button29.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[28];}
        if(obj[targetnum]==30){button30.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[29];}
        if(obj[targetnum]==31){button31.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[30];}
        if(obj[targetnum]==32){button32.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[31];}
        if(obj[targetnum]==33){button33.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[32];}
        if(obj[targetnum]==34){button34.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[33];}
        if(obj[targetnum]==35){button35.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[34];}
        if(obj[targetnum]==36){button36.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[35];}
        //if(obj[targetnum]==37){button37.setVisibility(View.VISIBLE); viewpagernum = 0;l_b = l_bo[36];}

        if(obj[targetnum]==(1+n_target)){button1_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[0];}
        if(obj[targetnum]==(2+n_target)){button2_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[1];}
        if(obj[targetnum]==(3+n_target)){button3_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[2];}
        if(obj[targetnum]==(4+n_target)){button4_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[3];}
        if(obj[targetnum]==(5+n_target)){button5_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[4];}
        if(obj[targetnum]==(6+n_target)){button6_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[5];}
        if(obj[targetnum]==(7+n_target)){button7_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[6];}
        if(obj[targetnum]==(8+n_target)){button8_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[7];}
        if(obj[targetnum]==(9+n_target)){button9_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[8];}
        if(obj[targetnum]==(10+n_target)){button10_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[9];}
        if(obj[targetnum]==(11+n_target)){button11_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[10];}
        if(obj[targetnum]==(12+n_target)){button12_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[11];}
        if(obj[targetnum]==(13+n_target)){button13_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[12];}
        if(obj[targetnum]==(14+n_target)){button14_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[13];}
        if(obj[targetnum]==(15+n_target)){button15_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[14];}
        if(obj[targetnum]==(16+n_target)){button16_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[15];}
        if(obj[targetnum]==(17+n_target)){button17_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[16];}
        if(obj[targetnum]==(18+n_target)){button18_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[17];}
        if(obj[targetnum]==(19+n_target)){button19_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[18];}
        if(obj[targetnum]==(20+n_target)){button20_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[19];}
        if(obj[targetnum]==(21+n_target)){button21_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[20];}
        if(obj[targetnum]==(22+n_target)){button22_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[21];}
        if(obj[targetnum]==(23+n_target)){button23_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[22];}
        if(obj[targetnum]==(24+n_target)){button24_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[23];}
        if(obj[targetnum]==(25+n_target)){button25_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[24];}
        if(obj[targetnum]==(26+n_target)){button26_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[25];}
        if(obj[targetnum]==(27+n_target)){button27_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[26];}
        if(obj[targetnum]==(28+n_target)){button28_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[27];}
        if(obj[targetnum]==(29+n_target)){button29_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[28];}
        if(obj[targetnum]==(30+n_target)){button30_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[29];}
        if(obj[targetnum]==(31+n_target)){button31_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[30];}
        if(obj[targetnum]==(32+n_target)){button32_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[31];}
        if(obj[targetnum]==(33+n_target)){button33_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[32];}
        if(obj[targetnum]==(34+n_target)){button34_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[33];}
        if(obj[targetnum]==(35+n_target)){button35_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[34];}
        if(obj[targetnum]==(36+n_target)){button36_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[35];}
        //if(obj[targetnum]==(37+n_target)){button37_2.setVisibility(View.VISIBLE); viewpagernum = 1;l_b = l_bo[36];}

        if(obj[targetnum]==(1+2*n_target)){button1_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[0];}
        if(obj[targetnum]==(2+2*n_target)){button2_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[1];}
        if(obj[targetnum]==(3+2*n_target)){button3_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[2];}
        if(obj[targetnum]==(4+2*n_target)){button4_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[3];}
        if(obj[targetnum]==(5+2*n_target)){button5_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[4];}
        if(obj[targetnum]==(6+2*n_target)){button6_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[5];}
        if(obj[targetnum]==(7+2*n_target)){button7_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[6];}
        if(obj[targetnum]==(8+2*n_target)){button8_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[7];}
        if(obj[targetnum]==(9+2*n_target)){button9_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[8];}
        if(obj[targetnum]==(10+2*n_target)){button10_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[9];}
        if(obj[targetnum]==(11+2*n_target)){button11_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[10];}
        if(obj[targetnum]==(12+2*n_target)){button12_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[11];}
        if(obj[targetnum]==(13+2*n_target)){button13_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[12];}
        if(obj[targetnum]==(14+2*n_target)){button14_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[13];}
        if(obj[targetnum]==(15+2*n_target)){button15_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[14];}
        if(obj[targetnum]==(16+2*n_target)){button16_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[15];}
        if(obj[targetnum]==(17+2*n_target)){button17_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[16];}
        if(obj[targetnum]==(18+2*n_target)){button18_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[17];}
        if(obj[targetnum]==(19+2*n_target)){button19_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[18];}
        if(obj[targetnum]==(20+2*n_target)){button20_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[19];}
        if(obj[targetnum]==(21+2*n_target)){button21_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[20];}
        if(obj[targetnum]==(22+2*n_target)){button22_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[21];}
        if(obj[targetnum]==(23+2*n_target)){button23_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[22];}
        if(obj[targetnum]==(24+2*n_target)){button24_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[23];}
        if(obj[targetnum]==(25+2*n_target)){button25_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[24];}
        if(obj[targetnum]==(26+2*n_target)){button26_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[25];}
        if(obj[targetnum]==(27+2*n_target)){button27_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[26];}
        if(obj[targetnum]==(28+2*n_target)){button28_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[27];}
        if(obj[targetnum]==(29+2*n_target)){button29_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[28];}
        if(obj[targetnum]==(30+2*n_target)){button30_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[29];}
        if(obj[targetnum]==(31+2*n_target)){button31_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[30];}
        if(obj[targetnum]==(32+2*n_target)){button32_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[31];}
        if(obj[targetnum]==(33+2*n_target)){button33_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[32];}
        if(obj[targetnum]==(34+2*n_target)){button34_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[33];}
        if(obj[targetnum]==(35+2*n_target)){button35_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[34];}
        if(obj[targetnum]==(36+2*n_target)){button36_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[35];}
        //if(obj[targetnum]==(37+2*n_target)){button37_3.setVisibility(View.VISIBLE); viewpagernum = 2;l_b = l_bo[36];}
    }
}





class MYViewPagerAdapter extends PagerAdapter {
    private ArrayList<View> views;

    public void setViews(ArrayList<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(views.get(position));
        return views.get(position);
    }
}
