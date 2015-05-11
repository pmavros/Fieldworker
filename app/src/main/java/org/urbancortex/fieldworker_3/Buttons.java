package org.urbancortex.fieldworker_3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
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
    static long startTime;

    static String outputFileName;

    SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss.SSS");
    private String date;
    private String currenttime;
    String eventInfo;

    Vibrator v;

    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    public static int counter = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       setContentView(R.layout.activity_buttons);

        // Get the message from the intent
        Intent intent = getIntent();
        participantID = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);


        // Create the text view
        TextView textViewName = new TextView(this);
        textViewName.setTextSize(20);
        textViewName.setText("Participant:");

        // Create the text view
        //TextView textView = new TextView(this);
        TextView textView = (TextView) findViewById(R.id.textView4);
        textView.setTextSize(20);
        textView.setText(participantID);

        // Set the text view as the activity layout
        //setContentView(textView);

        renameButtons();
        mHandler = new Handler();

        v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);


//        System.out.println()
        try {
            logger.createFile(participantID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        startTime = elapsedRealtime();
        startRepeatingTask();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buttons, menu);
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
            System.out.println("pressed action bar ");

            try {
                logger.closeFile();

                if (getIntent().getBooleanExtra("EXIT", false)) {
                    finish();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logEvent(View view) throws IOException, ParseException {
        Button b = (Button) view;

        counter++;
        updateCounter();

        String buttonPressed = b.getText().toString();
        time = System.currentTimeMillis();
        date = formatterDate.format(new Date(time));


        currenttime = formatterTime.format(new Date(time));


        v.vibrate(100);

        Toast.makeText(this, buttonPressed + " pressed", Toast.LENGTH_SHORT).show();

        eventInfo = buttonPressed.toString() + ", " +
                time + ", " +
                date + ", " +
                currenttime + ", " +
                locations.lat + ", " +
                locations.lon + ", " +
                locations.speed + ", " +
                locations.bearing + ", " +
                locations.elevation + ", " +
                locations.accuracy;

        logger.writeStringAsFile(eventInfo);



    }

    private void renameButtons (){

        String [] events = readSettings.getButtonSettings();

        for (int i = 0; i < events.length; i++) {
            out.println(events[i]);

            String buttonID = "button" + i;
            int resID = getResources().getIdentifier(buttonID, "id", "org.urbancortex.fieldworker_3");
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

    private Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            now = elapsedRealtime();

            updateTime(now);
            updateLog(); //this function can change value of mInterval.
            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);



    }

    public void updateLog(){

        time = System.currentTimeMillis();
        date = formatterDate.format(new Date(time));
        currenttime = formatterTime.format(new Date(time));

        eventInfo = "NA" + ", " +
                time + ", " +
                date + ", " +
                currenttime + ", " +
                locations.lat + ", " +
                locations.lon + ", " +
                locations.speed + ", " +
                locations.bearing + ", " +
                locations.elevation + ", " +
                locations.accuracy;

        try {
            logger.writeStringAsFile(eventInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        private void updateCounter(){

            TextView textView = (TextView) findViewById(R.id.textView7);
            textView.setTextSize(20);
            textView.setText(String.valueOf(counter));
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
                textTimeElasped = String.valueOf(timeElapsed) + " minutes";
            } else {
                timeElapsed = timeElapsed/3600;
                textTimeElasped = String.valueOf(timeElapsed) + " hours";
            }

            TextView textView = (TextView) findViewById(R.id.textView9);
            textView.setTextSize(20);
            textView.setText(textTimeElasped);
        }

    }

