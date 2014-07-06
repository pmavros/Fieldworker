public boolean exportDatabase()
{
  try 
   {
      String record = latitude + "," + longitude + "," + altitude + "," + accuracy+","+date+","+epoch+","+speed+","+event;
      printWriter.println(record); 
      //here the event is assigned as 0 again, because what it would o, it would keep logging the last event logged
      event="0";
   } 
   catch (Exception exc) 
    {
     return false;
    }     
    return true;
}

