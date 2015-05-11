void mousePressed()
{
  pushStyle();
  rectMode(CENTER);
  if (mouseY < 100)
  { 
    confirmation = new KetaiList(this, yesno);   
  }
  else if (mouseX<width/2)
  {
   if (((mouseY<height/5+buttonsize/2)&&(mouseY>height/4-buttonsize/2))) 
   {
    // ch=1;
     println(button1);
     event=button1;
     vibe.vibrate(200);
     //changes color to show that a selection has been made
     fill(c);
     rect(width/3-buffer, height/5, buttonsize, buttonsize);
   }
   else if (((mouseY<2*height/5+buttonsize/2)&&(mouseY>2*height/5-buttonsize/2))) 
   {
  //   ch=2;
     println(button2);
     event=button2;
     vibe.vibrate(200);
     fill(c);
     rect(width/3-buffer, 2*height/5, buttonsize, buttonsize); 
   }
   else if (((mouseY<3*height/5+buttonsize/2)&&(mouseY>3*height/5-buttonsize/2))) 
   {
     println(button3);
     event=button3;
     vibe.vibrate(200);
   //  ch=3;
     fill(c);
     rect(width/3-buffer, 3*height/5, buttonsize, buttonsize);
   }
   else if (((mouseY<4*height/5+buttonsize/2)&&(mouseY>4*height/5-buttonsize/2)))
   {
    println(button7);
    event=button7;
    vibe.vibrate(200);
   //  ch=7;
     fill(c);
     rect(width/3-buffer, 4*height/5, buttonsize, buttonsize);
   }
  }
  else if (mouseX>width/2)
  {
   if (((mouseY<height/5+buttonsize/2)&&(mouseY>height/5-buttonsize/2)))
   {   
    // ch=4;
     println(button4);
     event=button4;
     vibe.vibrate(200);
     fill(c);
     rect(2*width/3+buffer, height/5, buttonsize, buttonsize);
   }
   else if (((mouseY<2*height/5+buttonsize/2)&&(mouseY>2*height/5-buttonsize/2))) 
   {
     //ch=5;
      println(button5);
      event=button5;
      vibe.vibrate(200);
      fill(c);
      rect(2*width/3+buffer, 2*height/5, buttonsize, buttonsize);
   }
    else if (((mouseY<3*height/5+buttonsize/2)&&(mouseY>3*height/5-buttonsize/2)))  
   {
     println(button6);
     event=button6;
     vibe.vibrate(200);
     fill(c);
     rect(2*width/3+buffer, 3*height/5, buttonsize, buttonsize);
    // ch=6;
   }
   else if (((mouseY<4*height/5+buttonsize/2)&&(mouseY>4*height/5-buttonsize/2)))
   {
    println(button8);
    event=button8;
    vibe.vibrate(200);
   //  ch=8;
    fill(c);
    rect(2*width/3+buffer, 4*height/5, buttonsize, buttonsize);
   }
  }
  popStyle();
}

