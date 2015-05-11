package org.urbancortex.fieldworker;


import android.os.Environment;
import android.text.format.Time;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



/**
 * Created by Panos on 21/04/2015.
 */
public class logger {

    static String participantID = null;
    static long time = System.currentTimeMillis();

    static Time now = new Time();
    static String outputFileName;
    static File fileDirectory = null;
    static BufferedWriter buf;

//    public static void createFile(String participantID) throws IOException {
//        now.setToNow();
//        outputFileName =  participantID +"_" + now.format("%d.%m.%Y_%H.%M.%S")+"fieldworker.csv";
//        System.out.println(outputFileName);
//
//        System.out.println(readSettings.fileWriteDirectory);
//        buf = new BufferedWriter(new FileWriter(new File(readSettings.fileWriteDirectory, outputFileName) ));
//
//        String record = "event, date, time, epoch, lat, lon, speed, bearing, elevation, accuracy";
//
//        buf.write(record);
//        buf.append("\n");
//    }
//
//    public static void writeStringAsFile(final String eventInfo) throws IOException {
//        System.out.println(eventInfo);
//
//        buf.append(eventInfo);
//        buf.append("\n");
//    }
//
//    public static void closeFile () throws IOException {
//        buf.flush();
//        buf.close();
//
//    }
//
//    /* Checks if external storage is available for read and write */
//    public static boolean isExternalStorageWritable() {
//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            return true;
//        }
//        return false;
//    }


}
