package org.urbancortex.fieldworker;

import android.app.Application;

/**
 * Created by Panos on 14/05/2015.
 */
public class Fieldworker extends Application {
    // setup global variables
    public static boolean isRecording = false;
    public static int eventsCounter = 0;
    public static long startMillis = 0;
    public static long startTime = System.currentTimeMillis();
    public static boolean isRunning = false;
    public static boolean Reminder = false;
    public static boolean streamToLSL = false;


}
