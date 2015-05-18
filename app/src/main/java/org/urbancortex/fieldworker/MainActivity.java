package org.urbancortex.fieldworker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;

import static android.os.SystemClock.elapsedRealtime;


public class MainActivity extends Activity {

    public final static String EXTRA_MESSAGE = "org.urbancortex.fieldworker.MESSAGE";
    private File fileWriteDirectory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        System.out.println("main.oncreate");

        new locations(this, locations.ProviderType.GPS).start();

        if(Fieldworker.isRecording){
            Button b = (Button)findViewById(R.id.cont);
            b.setVisibility(View.VISIBLE);
        } else if (!Fieldworker.isRecording){
            Button b = (Button)findViewById(R.id.cont);
            b.setVisibility(View.INVISIBLE);
        }
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
    public void onResume(){
        super.onResume();

        System.out.println("main.onresume "+Fieldworker.isRecording);


        if(Fieldworker.isRecording){
            Button b = (Button)findViewById(R.id.cont);
            b.setVisibility(View.VISIBLE);
        } else if (!Fieldworker.isRecording){
            Button b = (Button)findViewById(R.id.cont);
            b.setVisibility(View.INVISIBLE);
        }
    }

    public void onStart(){
        super.onStart();

        System.out.println("main.onstart");

        if(Fieldworker.isRecording){
            Button b = (Button)findViewById(R.id.cont);
            b.setVisibility(View.VISIBLE);
        } else if (!Fieldworker.isRecording){
            Button b = (Button)findViewById(R.id.cont);
            b.setVisibility(View.INVISIBLE);
        }
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Buttons.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String participantID = editText.getText().toString();


        if(readWriteSettings.folderSettings()){
            Intent serviceIntent = new Intent(this,csv_logger.class);
            serviceIntent.putExtra("fileDir", readWriteSettings.fileWriteDirectory.toString());
            serviceIntent.putExtra("participantID", participantID);

            if(!participantID.isEmpty()){
                System.out.println(participantID);

                // start GPS logging

                Fieldworker.isRunning = false;
                startService(serviceIntent);


                intent.putExtra(EXTRA_MESSAGE, participantID);
                // Start button activity
                startActivity(intent);

            } else {
                Toast.makeText(this, "Hey, did you forget to enter a participant ID?", Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(this, "I couldn't find a fieldworker folder in the system!", Toast.LENGTH_LONG).show();
        }
    }
    /** Called when the user clicks the Send button */
    public void continueExperiment(View view) {

        EditText editText = (EditText) findViewById(R.id.edit_message);
        String participantID = editText.getText().toString();


        if(readWriteSettings.folderSettings()){


            // prepare logger settings
            final Intent serviceIntent = new Intent(this, csv_logger.class);
            serviceIntent.putExtra("fileDir", readWriteSettings.fileWriteDirectory.toString());
            serviceIntent.putExtra("participantID", participantID);

            if(!participantID.isEmpty()){

                // start second activity
                Intent intent = new Intent(this, Buttons.class);
                intent.putExtra(EXTRA_MESSAGE, participantID);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Hey, did you forget to enter your participant ID?", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Oops, I couldn't find the folders I need in the system!", Toast.LENGTH_LONG).show();
        }
    }

    /** Called when the user clicks the Send button */
    public void newExperiment(View view) {

        EditText editText = (EditText) findViewById(R.id.edit_message);
        final String participantID = editText.getText().toString();


        if(readWriteSettings.folderSettings()){

            // prepare logger settings
            final Intent serviceIntent = new Intent(this, csv_logger.class);
            serviceIntent.putExtra("fileDir", readWriteSettings.fileWriteDirectory.toString());
            serviceIntent.putExtra("participantID", participantID);

            if(!participantID.isEmpty()){
                System.out.println(participantID);

                // start GPS logging
                if(Fieldworker.isRecording){
                    // check if the want to start new recording
                    final Intent intent = new Intent(this, Buttons.class);
                    intent.putExtra(EXTRA_MESSAGE, participantID);

                    new AlertDialog.Builder(this)
                            .setTitle("New Recording")
                            .setMessage("Are you sure you want to start a new recording session?")
                            .setIcon(0)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    // if yes
                                    startNewRecording(serviceIntent);

                                    // start second activity
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .show();


                } else if (!Fieldworker.isRecording) {
                    // carry on with existing recording
                    startNewRecording(serviceIntent);

                    // start second activity
                    Intent intent = new Intent(this, Buttons.class);
                    intent.putExtra(EXTRA_MESSAGE, participantID);
                    startActivity(intent);

                }

            } else {
                Toast.makeText(this, "Hey, did you forget to enter your participant ID?", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Oops, I couldn't find the folders I need in the system!", Toast.LENGTH_LONG).show();
        }
    }

    public void startNewRecording(Intent startIntent){
        Fieldworker.isRecording = true;
        Fieldworker.eventsCounter = 0;
        Fieldworker.startTime = elapsedRealtime();
        startService(startIntent);
    }
}