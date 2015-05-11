package org.urbancortex.fieldworker;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.SystemClock.elapsedRealtime;
import static java.lang.System.*;


public class Buttons extends Activity  {

    static String participantID = null;
    static long time = System.currentTimeMillis();
    String [] colours = {"#33B5E5", "#AA66CC", "#99CC00","#FFBB33","#FF4444","#0099CC", "#9933CC","#669900", "#FF8800","#CC0000"};

    static long now;
    static long startTime = elapsedRealtime();



    SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss.SSS");
    private String date;
    private String currentTime;
    String eventInfo;

    Vibrator v;

    private int mInterval = 1000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    csv_logger mService;
    boolean mBound = false;

    DecimalFormat df = new DecimalFormat("#.#");

    public static int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);

        // Get the message from the intent
        Intent intent = getIntent();
        participantID = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
    }


    ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            csv_logger.LocalBinder mLocalBinder = (csv_logger.LocalBinder)service;
            mService = mLocalBinder.getService();
            mBound = true;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();

        // Bind to LocalService
        Intent intent = new Intent(this, csv_logger.class);
        bindService(intent, mConnection, 0);

        setContentView(R.layout.activity_buttons);
        v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);



        // Create the text view
        TextView textViewName = new TextView(this);
        textViewName.setTextSize(20);
        textViewName.setText("Participant:");

        // Create the text view
        TextView textView = (TextView) findViewById(R.id.textView4);
        textView.setTextSize(20);
        textView.setText(participantID);

        // load button names from file
        renameButtons();
        updateCounter(csv_logger.counter);

        // start timer
        mHandler = new Handler();

        startTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buttons, menu);
        return true;
    }

//    @Override
//    public void onStart(){
//        super.onStart();
//        // put your code here...
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            System.out.println("pressed action bar ");

            stopService(new Intent(Buttons.this, csv_logger.class));
            MainActivity.exit = true;
            finish();
            return true;
        }   else if (id == R.id.action_camera){
            Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }


    public void logEvent(View view) throws IOException, ParseException {
        Button b = (Button) view;

        csv_logger.counter++;
        updateCounter(csv_logger.counter);

        String buttonPressed = b.getText().toString();
        time = System.currentTimeMillis();
        date = formatterDate.format(new Date(time));
        currentTime = formatterTime.format(new Date(time));
        v.vibrate(100);

        Toast.makeText(this, buttonPressed + " pressed", Toast.LENGTH_SHORT).show();

        eventInfo = buttonPressed.toString() + ", " +
                time + ", " +
                date + ", " +
                currentTime + ", " +
                locations.lat + ", " +
                locations.lon + ", " +
                locations.speed + ", " +
                locations.bearing + ", " +
                locations.elevation + ", " +
                locations.accuracy;

        System.out.println(eventInfo);
        mService.writeStringToFile(eventInfo);



    }

    private void renameButtons (){

        String [] events = readWriteSettings.getButtonSettings();

        for (int i = 0; i < events.length; i++) {
            out.println(events[i]);

            String buttonID = "button" + i;
            int resID = getResources().getIdentifier(buttonID, "id", "org.urbancortex.fieldworker");
            Button text = (Button)findViewById(resID);
            text.setText(events[i]);
            text.setEnabled(true);

            text.getBackground().setColorFilter(Color.parseColor(colours[i]), PorterDuff.Mode.MULTIPLY);

        }
    }



    protected String[] getEventNames(String s){
            out.println(s);
            String[] events = s.split("\n");
            String[] eventNames = new String[events.length];
            for (int i = 0; i < events.length; i++) {
                try {
                    eventNames[i] = events[i];
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    //TODO
                }
            }
        return eventNames;
    }

    private Runnable mTimer = new Runnable() {
        @Override
        public void run() {

            now = elapsedRealtime();

            updateTime(now);
            mHandler.postDelayed(mTimer, mInterval);
        }
    };

    void startTimer() {
        mTimer.run();
    }

    void stopTimer() {
        mHandler.removeCallbacks(mTimer);
    }

        private void updateCounter(int count){

            TextView textView = (TextView) findViewById(R.id.textView7);
            textView.setTextSize(20);
            textView.setText(String.valueOf(count));
        }

        void updateTime(long now){

            long millisElapsed;
            String textTimeElasped;
            millisElapsed = now - startTime;
            double timeElapsed = millisElapsed / 1000;



            if (timeElapsed < 60 ){
                textTimeElasped = String.valueOf(timeElapsed) + " seconds";
            } else if (timeElapsed < 3600){
                timeElapsed = timeElapsed/60;
                textTimeElasped = df.format(timeElapsed) + " minutes";
            } else {
                timeElapsed = timeElapsed/3600;
                textTimeElasped = df.format(timeElapsed) + " hours";
            }

            TextView textView = (TextView) findViewById(R.id.textView9);
            textView.setTextSize(20);
            textView.setText(textTimeElasped);
        }
}

