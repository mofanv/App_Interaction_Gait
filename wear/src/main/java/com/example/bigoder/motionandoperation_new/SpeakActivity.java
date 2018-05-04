package com.example.bigoder.motionandoperation_new;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.util.Random;

public class SpeakActivity extends Activity implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    GoogleApiClient mGoogleAppiClient;
    public int n = 5;
    public int[] num =new int[n];

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speak);
        num = testRandom();
        mGoogleAppiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        notice();
        final Button button001 = (Button) findViewById(R.id.button001);
        final ImageButton button002 = (ImageButton) findViewById(R.id.imageView5);
        button001.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.speak_record);
                final Button button003 = (Button) findViewById(R.id.button002);
                button003.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendTextToPhone("finish_voice");
                        final TextView text = (TextView) findViewById(R.id.textView_final);
                        text.setText("已发送");
                        button003.setVisibility(View.INVISIBLE);
                    }
                });


            }
        });

        button002.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.speak_record);
                final Button button003 = (Button) findViewById(R.id.button002);
                button003.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendTextToPhone("finish_voice");
                        final TextView text = (TextView) findViewById(R.id.textView_final);
                        text.setText("已发送");
                        button003.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

    }

    private void sendTextToPhone(String content){
        PutDataMapRequest request1 = PutDataMapRequest.create("/watch_to_phone");
        DataMap dataMap1 = request1.getDataMap();
        dataMap1.putLong("time", System.currentTimeMillis());
        dataMap1.putString("content", content);
        Wearable.DataApi.putDataItem(mGoogleAppiClient, request1.asPutDataRequest());
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for(DataEvent event: dataEventBuffer){
            if(event.getType() == DataEvent.TYPE_DELETED){

            }else if(event.getType() == DataEvent.TYPE_CHANGED){
                DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                if(event.getDataItem().getUri().getPath().equals("/phone_to_watch")) {
                    String content = dataMap.get("content");
                    if (content.equals("4_wrist")) {
                        Log.e("4_wrist", "4_wrist");
                        notice();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }
                }
            }
        }
    }


    private int[] testRandom(){
        Random random = new Random();
        int a[] = new int[5];
        for(int i=0; i<a.length; i++){
            a[i] = random.nextInt(34)+1;
            for(int j=0; j<i; j++){
                while(a[i]==a[j]){
                    i--;
                }
            }
        }
        return a;
    }

    protected void notice() {

        final int NOTIFICATION_ID1 = 1;
        final int NOTIFICATION_ID2 = 2;
        final int NOTIFICATION_ID3 = 3;
        final int NOTIFICATION_ID4 = 4;
        final int NOTIFICATION_ID5 = 5;
        final int NOTIFICATION_ID6 = 6;

// Build intent for notification content
        String[] bigtext = new String[1];
        String[] title = new String[5];
        String[] text = new String[5];
        int[] icon = new int[5];
        String[][] store = new String[2][40];

        //weibo
        store[0][0] = "沿江高铁规划";
        store[1][0] = "沿江高铁起于上海市…";

        store[0][1] = "重庆最小西瓜";
        store[1][1] = "重庆石柱县出现世界…";

        store[0][2] = "代写小学作业";
        store[1][2] = "武汉各大中小学即将…";

        store[0][3] = "台湾的大停电";
        store[1][3] = "当局前副领导人吕秀…";

        store[0][4] = "三星手机爆燃";
        store[1][4] = "哈尔滨，突然妻子挎…";

        store[0][5] = "急需三孩家庭";
        store[1][5] = "中国急需三孩家庭但…";

        store[0][6] = "麻辣鲜口水鸡";
        store[1][6] = "经典川菜口水鸡怎样…";

        store[0][7] = "全新创作单曲";
        store[1][7] = "古风与现代音乐结合…";

        store[0][8] = "秋招即将开启";
        store[1][8] = "高校的新一届毕业生…";

        store[0][9] = "呵护差评权利";
        store[1][9] = "人民微评：呵护公民…";

        store[0][10] = "魅族新品发布";
        store[1][10] = "魅蓝君的送分题手机…";

        //wechat
        store[0][11] = "还是男神懂我";
        store[1][11] = "男神虽然还嘲笑我但…";

        store[0][12] = "华为首席作家";
        store[1][12] = "今天值得纪念，华为…";

        store[0][13] = "感情心得体会";
        store[1][13] = "维持一段感情，不能…";

        store[0][14] = "住青旅的感觉";
        store[1][14] = "第一次住青旅环境挺…";

        store[0][15] = "带小孩去海边";
        store[1][15] = "看到大海，萌妹毫不…";

        store[0][16] = "晚上加班有感";
        store[1][16] = "一个人一层楼好害怕…";

        store[0][17] = "给老长辈红包";
        store[1][17] = "首月工资，给爷爷奶…";

        store[0][18] = "大叔拯救牙齿";
        store[1][18] = "梦到牙齿掉了四颗一…";

        store[0][19] = "张家口的清晨";
        store[1][19] = "自由愈新鲜，远山和…";

        store[0][20] = "读霍乱时爱情";
        store[1][20] = "给自己一本书的时间…";

        //system
        store[0][21] = "蓝牙接收照片";
        store[1][21] = "蓝牙状态即将打开了…";

        store[0][22] = "起飞安全保护";
        store[1][22] = "检测到您的飞机即将…";

        store[0][23] = "建议进行备份";
        store[1][23] = "您已经10周没有进行…";

        store[0][24] = "手机电量不足";
        store[1][24] = "手表只剩20%的电量…";

        store[0][25] = "未读信息提示";
        store[1][25] = "您家中的座机收到语…";

        //shedule
        store[0][26] = "机票出票成功";
        store[1][26] = "您于下个月二号将从…";

        store[0][27] = "父亲节的提示";
        store[1][27] = "父亲节还有10天，小…";

        store[0][28] = "开会材料提示";
        store[1][28] = "小助手提示您，明天…";

        store[0][29] = "预购准备开始";
        store[1][29] = "您在京东预定的黑胶…";

        store[0][30] = "添加议程提醒";
        store[1][30] = "您与下周一有会议安…";

        //weather
        store[0][31] = "明日天气预报";
        store[1][31] = "明天有雷暴雨。中午…";

        store[0][32] = "重庆天气质量";
        store[1][32] = "明日的天气质量指数…";

        store[0][33] = "台风红色预警";
        store[1][33] = "后天晚有较强对流天…";

        //shopping
        store[0][34] = "好友前来请客";
        store[1][34] = "你的好友发了信息给…";

        store[0][35] = "购物点评邀请";
        store[1][35] = "小白发起了购物邀请…";


        title[0] = store[0][num[0]];
        title[1] = store[0][num[1]];
        title[2] = store[0][num[2]];
        title[3] = store[0][num[3]];
        title[4] = store[0][num[4]];

        text[0] = store[1][num[0]];
        text[1] = store[1][num[1]];
        text[2] = store[1][num[2]];
        text[3] = store[1][num[3]];
        text[4] = store[1][num[4]];

        for(int i=0; i<5; i++){
            if(0<num[0] & num[i]<=10){
                icon[i] = R.drawable.cnn;
            }else if(10<num[i] & num[i]<=20){
                icon[i] = R.drawable.wechat;
            }else if(20<num[i] & num[i]<=25){
                icon[i] = R.drawable.settings;
            }else if(25<num[i] & num[i]<=30){
                icon[i] = R.drawable.calendar;
            }else if(30<num[i] & num[i]<=33){
                icon[i] = R.drawable.weather;
            }else if(33<num[i] & num[i]<=35){
                icon[i] = R.drawable.twiter;
            }
        }

        Notification.BigTextStyle bigStyle1 = new Notification.BigTextStyle();
        bigStyle1.bigText(bigtext[0]);
        Notification.BigTextStyle bigStyle2 = new Notification.BigTextStyle();
        bigStyle2.bigText(bigtext[0]);
        Notification.BigTextStyle bigStyle3 = new Notification.BigTextStyle();
        bigStyle3.bigText(bigtext[0]);
        Notification.BigTextStyle bigStyle4 = new Notification.BigTextStyle();
        bigStyle4.bigText(bigtext[0]);
        Notification.BigTextStyle bigStyle5 = new Notification.BigTextStyle();
        bigStyle5.bigText(bigtext[0]);
        Notification.BigTextStyle bigStyle6 = new Notification.BigTextStyle();
        bigStyle5.bigText(bigtext[0]);


        Notification.Builder nb6;
        nb6 = new Notification.Builder(this) // notification content
                .setContentTitle("结束！")
                .setContentText("请放下手臂")
                .setStyle(bigStyle6)
                .setSmallIcon(R.drawable.target);
        Notification n6 = nb6.build(); // create notification object
        // launch notification
        NotificationManager nm6 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm6.notify(NOTIFICATION_ID6, n6);

        Notification.Builder nb1 = new Notification.Builder(this) // notification content
                .setContentTitle(title[0])
                .setContentText(text[0])
                .setStyle(bigStyle1)
                .setSmallIcon(icon[0]);
        Notification n1 = nb1.build(); // create notification object
        // launch notification
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID1, n1);

        Notification.Builder nb2 = new Notification.Builder(this) // notification content
                .setContentTitle(title[1])
                .setContentText(text[1])
                .setStyle(bigStyle2)
                .setSmallIcon(icon[1])
                ;
        Notification n2 = nb2.build(); // create notification object
        // launch notification
        NotificationManager nm2 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm2.notify(NOTIFICATION_ID2, n2);

        Notification.Builder nb3 = new Notification.Builder(this) // notification content
                .setContentTitle(title[2])
                .setContentText(text[2])
                .setStyle(bigStyle3)
                .setSmallIcon(icon[2])
                ;
        Notification n3 = nb3.build(); // create notification object
        // launch notification
        NotificationManager nm3 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm3.notify(NOTIFICATION_ID3, n3);

        Notification.Builder nb4 = new Notification.Builder(this) // notification content
                .setContentTitle(title[3])
                .setContentText(text[3])
                .setStyle(bigStyle4)
                .setSmallIcon(icon[3])
                ;
        Notification n4 = nb4.build(); // create notification object
        // launch notification
        NotificationManager nm4 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm4.notify(NOTIFICATION_ID4, n4);

        Notification.Builder nb5;
        nb5 = new Notification.Builder(this) // notification content
                .setContentTitle(title[4])
                .setContentText(text[4])
                .setStyle(bigStyle5)
                .setSmallIcon(icon[4]);
        Notification n5 = nb5.build(); // create notification object
        // launch notification
        NotificationManager nm5 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm5.notify(NOTIFICATION_ID5, n5);
    }

    @Override
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

    protected void onStop(){
        if(null != mGoogleAppiClient && mGoogleAppiClient.isConnected()){
            Wearable.DataApi.removeListener(mGoogleAppiClient,this);
            mGoogleAppiClient.disconnect();
        }
        super.onStop();
    }

}