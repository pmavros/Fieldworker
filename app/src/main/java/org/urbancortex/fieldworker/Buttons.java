package org.urbancortex.fieldworker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.os.Bundle;
import org.urbancortex.fieldworker.*;

import static android.os.SystemClock.elapsedRealtime;


public class Buttons extends Activity  {

    static String participantID = null;
    static long time = System.currentTimeMillis();
    String [] colours = {"#33B5E5", "#AA66CC", "#99CC00","#FFBB33","#FF4444","#0099CC", "#9933CC","#669900", "#FF8800","#CC0000"};

    long now;

    SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss.SSS");
    private String date;
    private String currentTime;
    String eventInfo;
    private Menu menu;
    Vibrator v;

    // this is the time elapsed display for the ui thread
    private int mInterval = 1000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    csv_logger mService;
    boolean mBound = false;

    DecimalFormat df = new DecimalFormat("#.#");

    private static Drawable gpsNo;
    private static Drawable gpsYes;
    private static View view;

    public static View getView() {
        return view;
    }




/*
    create a new streaminfo to describe your stream and create a new outlet with that info.
    Push samples into the outlet as your app produces them. Destroy the outlet when you're done.

  */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("buttons on create");
        setContentView(R.layout.activity_buttons);

        // Get the message from the intent
        Intent intent = getIntent();
        participantID = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

     //   System.out.println("The LSL clock reads: " + Double.toString(lsl.local_clock()) );
    }


    ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            mService = null;
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

        System.out.println("buttons on start");
        // Bind to LocalService
        Intent intent = new Intent(this, csv_logger.class);
        bindService(intent, mConnection, 0);

        setContentView(R.layout.activity_buttons);
        v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);

        // Create the text view
        TextView textView = (TextView) findViewById(R.id.participantId);
        textView.setTextSize(12);
        textView.setText(participantID);

        gpsNo = getResources().getDrawable(R.drawable.ic_gps_no);
        gpsYes =  getResources().getDrawable(R.drawable.ic_gps_yes);

        // load button names from file
        renameButtons();
        updateCounter(Fieldworker.eventsCounter);

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
    protected void onDestroy() {

        System.out.println("Buttons onDestroy");
        super.onDestroy();

        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    MenuItem shareItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buttons, menu);

        shareItem = menu.findItem(R.id.gps_fix);
        updateGPS();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            System.out.println("pressed action bar ");

            final Intent stopIntent = new Intent(Buttons.this, csv_logger.class);

            new AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to stop recording?")
                    .setIcon(0)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with exit

                            Fieldworker.isRecording = false;
                            stopService(stopIntent);
                            stopTimer();
                            // Unbind from the service
                            if (mBound) {
                                unbindService(mConnection);
                                mBound = false;
                            }
//                            MainActivity.exit = true;
                            finish();

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();

        }   else if (id == R.id.action_camera){
//        if (id == R.id.action_camera) {
            Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void logEvent(View view) throws IOException, ParseException {
        Button b = (Button) view;

        Fieldworker.eventsCounter++;
        updateCounter(Fieldworker.eventsCounter);

        String buttonPressed = b.getText().toString();

        long millisElapsed = now - Fieldworker.startMillis;

        time = Fieldworker.startTime + millisElapsed;

        date = formatterDate.format(new Date(time));
        currentTime = formatterTime.format(new Date(time));
        v.vibrate(20);

        Toast.makeText(this, buttonPressed + " pressed", Toast.LENGTH_SHORT).show();

        eventInfo = buttonPressed.toString() + ", " +
                date + ", " +
                time + ", " +
                currentTime + ", " +
                locations.lat + ", " +
                locations.lon + ", " +
                locations.speed + ", " +
                locations.bearing + ", " +
                locations.elevation + ", " +
                locations.accuracy;

        mService.writeStringToFile(eventInfo);
    }

    private void renameButtons (){

        String [] events = readWriteSettings.getButtonSettings();

        for (int i = 0; i < events.length; i++) {

            String buttonID = "button" + i;
            int resID = getResources().getIdentifier(buttonID, "id", "org.urbancortex.fieldworker");
            Button text = (Button)findViewById(resID);
            text.setText(events[i]);
            text.setEnabled(true);

            text.getBackground().setColorFilter(Color.parseColor(colours[i]), PorterDuff.Mode.MULTIPLY);

        }
    }

    private Runnable mReminder = new Runnable() {
        @Override
        public void run() {

            v.vibrate(200);
            mHandler.postDelayed(mReminder, 20000);
        }
    };

    private Runnable mTimer = new Runnable() {
        @Override
        public void run() {

            long now = elapsedRealtime();
                updateTime(now);
                updateGPS();
                mHandler.postDelayed(mTimer, mInterval);
        }
    };



    void startTimer() {
        mTimer.run();
        if(Fieldworker.Reminder){
            mReminder.run();
        }
    }

    void stopTimer() {
        mHandler.removeCallbacks(mTimer);

        if(Fieldworker.Reminder){
            mHandler.removeCallbacks(mReminder);
        }

    }

    private void updateCounter(int count){

            TextView textView = (TextView) findViewById(R.id.eventCounter);
            textView.setText(String.valueOf(count));
    }

    void updateTime(long now){

            long millisElapsed = now - Fieldworker.startMillis;
            double timeElapsed = millisElapsed/1000 ;

            TextView textView = (TextView) findViewById(R.id.timer);
            textView.setTextSize(20);
            textView.setText(convertSecondsToHMmSs((long) timeElapsed));
    }

    public static String convertSecondsToHMmSs(long seconds) {
        /* http://stackoverflow.com/a/9027362 */
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", h,m,s);
    }

    public void updateGPS() {

        double accuracy = locations.accuracy;
        TextView textView = (TextView) findViewById(R.id.gpsaccuracy);
        textView.setTextSize(12);
        textView.setText(String.valueOf(accuracy));

        if(true){
            if(shareItem !=null){
                if (accuracy > 0 && accuracy < 100) {
                    shareItem.setIcon(gpsYes);
                } else {
                    shareItem.setIcon(gpsNo);

                }
            }
        }

    }


}
