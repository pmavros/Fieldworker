package org.urbancortex.fieldworker;

import android.app.Application;

import static android.os.SystemClock.elapsedRealtime;

/**
 * Created by Panos on 14/05/2015.
 */
public class Fieldworker extends Application {
    // setup global variables
    public static boolean isRecording = false;
    public static int eventsCounter = 0;
    public static long startTime = 0;

}
