void drawButtons()
{
  noStroke();
  pushStyle();
  textAlign(CENTER);
  rectMode(CENTER);
  //imageMode(CENTER);
  fill(bu);
  rect(width/2, 50, width, 100);
  //button1
 rect(width/3-buffer, height/5, buttonsize, buttonsize); 
  //button4
  rect(2*width/3+buffer, height/5, buttonsize, buttonsize);
  //button2
  rect(width/3-buffer, 2*height/5, buttonsize, buttonsize); 
   //button5
  rect(2*width/3+buffer, 2*height/5, buttonsize, buttonsize);
   //button3
  rect(width/3-buffer, 3*height/5, buttonsize, buttonsize);
  //button6
  rect(2*width/3+buffer, 3*height/5, buttonsize, buttonsize);
  //button7
  rect(width/3-buffer, 4*height/5, buttonsize, buttonsize);
  //BUTTON8
  rect(2*width/3+buffer, 4*height/5, buttonsize, buttonsize);
  fill(0);
  text("CLOSE", width/2, 50);
  text(button1, width/3-buffer, height/5);
  text(button4, 2*width/3+buffer, height/5);
  text(button2, width/3-buffer, 2*height/5); 
  text(button5, 2*width/3+buffer, 2*height/5); 
  text(button3, width/3-buffer, 3*height/5); 
  text(button6, 2*width/3+buffer, 3*height/5); 
  text(button7, width/3-buffer, 4*height/5); 
  text(button8, 2*width/3+buffer, 4*height/5); 
  popStyle();
}

