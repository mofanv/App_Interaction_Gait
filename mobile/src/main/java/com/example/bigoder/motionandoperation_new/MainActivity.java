package com.example.bigoder.motionandoperation_new;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class MainActivity extends ActionBarActivity implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;

    GoogleApiClient mGoogleAppiClient;
    public String obj[]=new String[24];
    private SoundPool sp;//声明一个SoundPool
    private int music;//定义一个整型用load（）；来设置suondID
    int sum = 0;
    int count = 0;
    String correctNum;
    String compareNum;
    int aBooleanStart = 0;

    String user_num = "0";
    String user_speed = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleAppiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        processSetting_page1();
    }

    void processSetting_page1(){
        setContentView(R.layout.activity_main);
        final Button button1 = (Button) findViewById(R.id.button0);
        final TextView editText = (TextView) findViewById(R.id.editText);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_num = String.valueOf(editText.getText());
                processSetting_page2();
            }
        });
        final Button button2 = (Button) findViewById(R.id.button_t);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_num = String.valueOf(editText.getText());
                user_speed = "t";
                processSetting_page3();
            }
        });
    }


    void processSetting_page2(){
        setContentView(R.layout.activity_main2);
        final Button button0 = (Button) findViewById(R.id.button00);
        final Button button1 = (Button) findViewById(R.id.button2);
        final Button button2 = (Button) findViewById(R.id.button5);
        final Button button3 = (Button) findViewById(R.id.button6);
        final Button button4 = (Button) findViewById(R.id.button7);
        //final Button button5 = (Button) findViewById(R.id.button3);
        final Button button6 = (Button) findViewById(R.id.button4);
        //final Button button7 = (Button) findViewById(R.id.button8);
        final TextView textView2 = (TextView) findViewById(R.id.textView111);
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSetting_page3();
            }
        });
        button1.setOnClickListener(new View.OnClickListener(){public void onClick(View v) {user_speed = "0";textView2.setText("速度为 1");sendTextToWatch("speed_1");}});
        button2.setOnClickListener(new View.OnClickListener(){public void onClick(View v) {user_speed = "2";textView2.setText("速度为 2");sendTextToWatch("speed_2");}});
        button3.setOnClickListener(new View.OnClickListener(){public void onClick(View v) {user_speed = "3";textView2.setText("速度为 3");sendTextToWatch("speed_3");}});
        button4.setOnClickListener(new View.OnClickListener(){public void onClick(View v) {user_speed = "4";textView2.setText("速度为 4");sendTextToWatch("speed_4");}});
        button6.setOnClickListener(new View.OnClickListener(){public void onClick(View v) {user_speed = "5";textView2.setText("速度为 5");sendTextToWatch("speed_5");}});
    }

    void processSetting_page3(){
        String text_temp;
        text_temp = "编号：" + user_num + "    速度：" + user_speed;
        setContentView(R.layout.activity_main3);
        final Button button1 = (Button) findViewById(R.id.button9);
        final Button button2 = (Button) findViewById(R.id.button10);
        final Button button3 = (Button) findViewById(R.id.button11);
        final Button button4 = (Button) findViewById(R.id.button12);
        final Button button5 = (Button) findViewById(R.id.button13);
        final Button button6 = (Button) findViewById(R.id.button14);
        final Button button7 = (Button) findViewById(R.id.button15);
        final Button button8 = (Button) findViewById(R.id.button16);
        final TextView textView1 = (TextView) findViewById(R.id.textView6);

        //final Button button9 = (Button) findViewById(R.id.button);
        //final Button button10 = (Button) findViewById(R.id.button18);
        //final Button button11 = (Button) findViewById(R.id.button19);
        final Button button12 = (Button) findViewById(R.id.button20);
        //final Button button13 = (Button) findViewById(R.id.button21);
        final Button button14 = (Button) findViewById(R.id.button17);
        textView1.setText(text_temp);

        button1.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.INVISIBLE);button3.setVisibility(View.INVISIBLE);
        button4.setVisibility(View.INVISIBLE);button5.setVisibility(View.INVISIBLE);button6.setVisibility(View.INVISIBLE);
        button12.setVisibility(View.INVISIBLE);


        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendTextToWatch("1_1_look");
                try {
                    textPrint_phase("1look_number_steps," + String.valueOf(System.currentTimeMillis()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                playSounds(2);
                button2.setVisibility(View.VISIBLE);
                button1.setVisibility(View.INVISIBLE);
            }
        });
        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendTextToWatch("1_2_look");
                try {
                    textPrint_phase("2look_walk_time,"+String.valueOf(System.currentTimeMillis()));
                } catch (IOException e) {e.printStackTrace();}
                playSounds(3);
                button3.setVisibility(View.VISIBLE);
                button2.setVisibility(View.INVISIBLE);
            }
        });
        button3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendTextToWatch("1_3_look");
                try {
                    textPrint_phase("3look_SMS,"+String.valueOf(System.currentTimeMillis()));
                } catch (IOException e) {e.printStackTrace();}
                playSounds(4);
                button4.setVisibility(View.VISIBLE);
                button3.setVisibility(View.INVISIBLE);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendTextToWatch("2_click");
                for(int i=0;i <= 200;i++){Log.e("info","2_click");}
                try {
                    textPrint_phase("4swipe_touch_look_light,"+String.valueOf(System.currentTimeMillis()));
                } catch (IOException e) {e.printStackTrace();}
                playSounds(5);
                button5.setVisibility(View.VISIBLE);
                button4.setVisibility(View.INVISIBLE);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendTextToWatch("3_voice");
                try {
                    textPrint_phase("5voice,"+String.valueOf(System.currentTimeMillis()));
                } catch (IOException e) {e.printStackTrace();}
                playSounds(6);
                button6.setVisibility(View.VISIBLE);
                button5.setVisibility(View.INVISIBLE);
            }
        });
        button7.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                processSetting_page2();
            }
        });
        button8.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                processSetting_page1();
            }
        });

        button12.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                try {
                    textPrint_phase("5_6_7finish,"+String.valueOf(System.currentTimeMillis()));
                } catch (IOException e) {e.printStackTrace();}
                playSounds(10);
                button1.setVisibility(View.VISIBLE);button2.setVisibility(View.VISIBLE);button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);button5.setVisibility(View.VISIBLE);button6.setVisibility(View.VISIBLE);
                button12.setVisibility(View.VISIBLE);button14.setVisibility(View.VISIBLE);
            }
        });
        button6.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendTextToWatch("4_wrist");
                try {
                    textPrint_phase("6find_title,"+String.valueOf(System.currentTimeMillis()));
                } catch (IOException e) {e.printStackTrace();}
                playSounds(17);
                for(int i = 1;i<200;i++){Log.e("info","6find_title");}
                button12.setVisibility(View.VISIBLE);
                button6.setVisibility(View.INVISIBLE);
            }
        });
        button14.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                try {
                    textPrint_phase("0begin," + String.valueOf(System.currentTimeMillis()));
                } catch (IOException e) {e.printStackTrace();}

                if(user_speed.equals("0")){
                    playSounds(12);
                } else if(user_speed.equals("5")){
                    playSounds(13);
                } else{
                    playSounds(1);
                }

                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                button1.setVisibility(View.VISIBLE);
                button14.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void sendTextToWatch(String content){
        PutDataMapRequest request1 = PutDataMapRequest.create("/phone_to_watch");
        DataMap dataMap1 = request1.getDataMap();
        dataMap1.putLong("time", System.currentTimeMillis());
        dataMap1.putString("content", content);
        Wearable.DataApi.putDataItem(mGoogleAppiClient, request1.asPutDataRequest());
    }

    public void playSounds(int r){
        sp= new SoundPool(1, AudioManager.STREAM_SYSTEM,5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        int mResId = R.raw.recording_1;
        if(r == 1){mResId = R.raw.recording_1;}
        else if(r == 12){mResId = R.raw.recording_1_2;}
        else if(r == 13){mResId = R.raw.recording_1_3;}
        else if(r == 2){mResId = R.raw.recording_2;}
        else if(r == 3){mResId = R.raw.recording_3;}
        else if(r == 4){mResId = R.raw.recording_4;}
        else if(r == 5){mResId = R.raw.recording_5;}
        else if(r == 6){mResId = R.raw.recording_6_0;}
        else if(r == 7){mResId = R.raw.recording_7;}
        else if(r == 8){mResId = R.raw.recording_8;}
        else if(r == 10){mResId = R.raw.recording_10;}
        else if(r == 17){mResId = R.raw.recording_7_bak;}
        else if(r == 18){mResId = R.raw.recording_8_bak;}
        music = sp.load(this, mResId, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sp.play(music, 1, 1, 0, 0, 1);
    }

    public void playMedia(int r){
        try {
            MediaPlayer mMediaPlayer = new MediaPlayer();
            if (r == 1){mMediaPlayer = MediaPlayer.create(this, R.raw.recording_7);}
            if (r == 2){mMediaPlayer = MediaPlayer.create(this, R.raw.recording_8);}
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    private void textPrint_click(String content) throws IOException {
        String user_num_temp = user_num;
        if (user_speed.equals("t")){user_num =  "t";}
        String NAME_OF_FILE = "/storage/emulated/0/aaa_data/No" + user_num + "_Sp" + user_speed + "_" + "click.txt";
        FileOutputStream phone_outStream = new FileOutputStream (new File(NAME_OF_FILE),true);
        phone_outStream.write((content + "\r\n").getBytes());
        phone_outStream.flush();
        phone_outStream.close();
        if (user_speed.equals("t")){user_num = user_num_temp;}
    }

    private void textPrint_slip(String content) throws IOException {
        String user_num_temp = user_num;
        if (user_speed.equals("t")){user_num =  "t";}
        String NAME_OF_FILE = "/storage/emulated/0/aaa_data/No" + user_num + "_Sp" + user_speed + "_" + "slip.txt";
        FileOutputStream phone_outStream = new FileOutputStream (new File(NAME_OF_FILE),true);
        phone_outStream.write((content + "\r\n").getBytes());
        phone_outStream.flush();
        phone_outStream.close();
        if (user_speed.equals("t")){user_num = user_num_temp;}
    }

    private void textPrint_phase(String content) throws IOException {
        String user_num_temp = user_num;
        if (user_speed.equals("t")){user_num =  "t";}
        String NAME_OF_FILE = "/storage/emulated/0/aaa_data/No" + user_num + "_Sp" + user_speed + "_" + "phase.txt";
        FileOutputStream phone_outStream = new FileOutputStream (new File(NAME_OF_FILE),true);
        phone_outStream.write((content + "\r\n").getBytes());
        phone_outStream.flush();
        phone_outStream.close();
        if (user_speed.equals("t")){user_num = user_num_temp;}
    }

    private void textPrint_phase_task(String content) throws IOException {
        String user_num_temp = user_num;
        if (user_speed.equals("t")){user_num =  "t";}
        String NAME_OF_FILE = "/storage/emulated/0/aaa_data/No" + user_num + "_Sp" + user_speed + "_" + "task.txt";
        FileOutputStream phone_outStream = new FileOutputStream (new File(NAME_OF_FILE),true);
        phone_outStream.write((content + "\r\n").getBytes());
        phone_outStream.flush();
        phone_outStream.close();
        if (user_speed.equals("t")){user_num = user_num_temp;}
    }


    @Override
    protected void onStart() {
        Log.e("phone", "onStart");
        mGoogleAppiClient.connect();
        super.onStart();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for(DataEvent event: dataEventBuffer){
            if(event.getType() == DataEvent.TYPE_DELETED){

            }else if(event.getType() == DataEvent.TYPE_CHANGED){
                DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                if(event.getDataItem().getUri().getPath().equals("/watch_to_phone")){
                    String content = dataMap.get("content");
                    String time_watch = String.valueOf(dataMap.get("time"));
                    long currentTimeMillis = System.currentTimeMillis();
                    Date date = new Date(currentTimeMillis);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss SSS");
                    String content_mark = content.substring(0,4);
                    if (content.equals("finish_voice")){
                        playSounds(10);
                    }
                    if (content.equals("finish_point")){
                        playSounds(10);
                    }
                    if(content_mark.equals("slip")) {
                        try {
                            textPrint_slip(content + "," + time_watch + "," + String.valueOf(currentTimeMillis));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if(content_mark.equals("clic")) {
                        try {
                            textPrint_click(content + "," + time_watch + "," + String.valueOf(currentTimeMillis));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (content_mark.equals("look")){
                        try {
                            textPrint_phase_task(content);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.e("phone", "onConnected");
        Wearable.DataApi.addListener(mGoogleAppiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("phone", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("phone", "onConnectionFailed" + connectionResult.getErrorCode());
    }


    protected void onStop(){
        if(null != mGoogleAppiClient && mGoogleAppiClient.isConnected()){
            Wearable.DataApi.removeListener(mGoogleAppiClient,this);
            mGoogleAppiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}