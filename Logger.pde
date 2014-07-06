import ketai.data.*;
import ketai.sensors.*;
import ketai.ui.*;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Date;
import android.os.Environment;
import java.io.PrintWriter;
import java.io.FileWriter;
import android.view.MotionEvent;

double longitude, latitude, altitude, accuracy, speed;
KetaiLocation location;
KetaiVibrate vibe;
long epoch, epoch1;
String date, epcount;
int ch=0;

String [] eventlist;
String button1, button2, button3, button4, button5, button6, button7, button8;



color c = color(255, 255, 255, 150);
color b= color(0,0,0);
color bu= color(255,158,174);

float buttonsize, buffer;

KetaiList confirmation;

ArrayList<String> yesno = new ArrayList<String>();

File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);      
File file;

PrintWriter printWriter;

String event;

Date d= new Date();

long startTime, estimatedTime;
int secs;

void setup() 
{
  orientation(PORTRAIT);
  textAlign(CENTER, CENTER);
  textSize(36);
  vibe = new KetaiVibrate(this);
  eventlist = loadStrings("EventList.csv");
  button1=eventlist[1];
  button2=eventlist[2];
  button3=eventlist[3];
  button4=eventlist[4];
  button5=eventlist[5];
  button6=eventlist[6];
  button7=eventlist[7];
  button8=eventlist[8];
  buttonsize=height/5-10;
  startTime = System.nanoTime();
  try
  {
  epoch1 = System.currentTimeMillis();
  epcount = new java.text.SimpleDateFormat("yyyyMMdd'_'HHmmss").format(new java.util.Date (epoch1));
  String filename = "Log"+epcount+".csv";
  file = new File(exportDir, filename);
  file.createNewFile(); 
  printWriter = new PrintWriter(new FileWriter(file));
  printWriter.println("Latitude,Longitude,Altitude,Accuracy,Date,Epoch,Speed,Event"); 
  fill(0);
  text("Recording data...",width/2,6*height/7);   
  }
  catch (Exception exc)
  {
   println("Exception#$%");
  }
  event="0";
  yesno.add("Quit");
  yesno.add("Don't quit");
  frameRate(10);
  buffer=((width-2*buttonsize)/3)/2;
}

void draw() {
  background(b);
  drawButtons();
  epoch = System.currentTimeMillis();
  date = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new java.util.Date (epoch));
  if (location.getProvider() == "none")
    text("Location data is unavailable. \n" +
      "Please check your location settings.", 0, 0, width, height);
 //we have a frameRate of 10, so that is 10 frames per second, so if framecount modulo 10 is 0 then every 10 frames, which means every second
 //a line is written in the file
 //why is the frameRate 10? I unfortunatelly don't remember
  if (frameCount%10==0) exportDatabase();
  //event="0";
  estimatedTime = (System.nanoTime() - startTime)/1000000000;
  secs=int(estimatedTime);
  pushStyle();
  fill(255);
  textSize(70);
  text("Seconds Elapsed: "+secs, width/2, height-50);
  popStyle();
}

void onResume()
{
  location = new KetaiLocation(this);
  super.onResume();
}

void onLocationEvent(Location _location)
{
  //print out the location object
  println("onLocation event: " + _location.toString());
  longitude = _location.getLongitude();
  latitude = _location.getLatitude();
  altitude = _location.getAltitude();
  accuracy = _location.getAccuracy();
  speed = _location.getSpeed();
}



