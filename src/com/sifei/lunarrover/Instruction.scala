package com.sifei.lunarrover

import com.sifei.lunarrover.Direction._

class Instruction(thePoint: Point, theDirection: Direction, theSpeed: Int, theRotation: Int) {
  var point : Point = thePoint;
  var direction : Direction = theDirection;
  var speed : Int = theSpeed;
  var rotation : Int = theRotation;
  
  def toTag() : String = {
    return point.getTag + "," + direction + "," + speed + "," + rotation;
  }
  
  override def toString() : String = {
    return "[" + toTag() + "]";
  }
  
}