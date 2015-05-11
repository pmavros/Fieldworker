package org.urbancortex.fieldworker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class MainActivity extends Activity {

    public final static String EXTRA_MESSAGE = "org.urbancortex.fieldworker.MESSAGE";
    private File fileWriteDirectory;
    protected static boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new locations(this, locations.ProviderType.GPS).start();



    }

    @Override
    protected void onStart() {
        if(exit){
            finish();
        }

        super.onStart();
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
                startService(serviceIntent);

                intent.putExtra(EXTRA_MESSAGE, participantID);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Hey, did you forget to enter a participant ID?", Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(this, "I couldn't find a fieldworker folder in the system!", Toast.LENGTH_LONG).show();
        }





    }




}



