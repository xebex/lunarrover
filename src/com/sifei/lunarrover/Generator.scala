package com.sifei.lunarrover

import scala.util.Random
import java.io._
import com.sifei.lunarrover.Atlas._
import com.sifei.lunarrover.Direction._

/**
 * 规划线路生成器
 */
class Generator {
  //
  def generate(fileName: String) = {
    val writer = new PrintWriter(new File(fileName));
    
    val rotations = Array(0, 90, 270);
    
    var previous = new Instruction(new Point(0, 0), E, 1, 0);   
    println(previous.toTag());
    writer.write(previous.toTag() + "\n");

    
    while (!previous.point.equals(getBottomRightPoint())) {
      //direction
      var direction = getDirection(previous.direction, previous.rotation);
      
      //x, y
      var point = getPoint(previous.point.x, previous.point.y, previous.speed, direction);
      
      //
      var isLegal : Boolean = false;
      while (!isLegal) {
        //speed
        var speed = Random.nextInt(3) + 1;
        
        //rotation
        //var i = Random.nextInt(360);
        //var rotation = i - i % 90;
        var i = Random.nextInt(3);
        var rotation = rotations(i);
        
        //
        var newDirection = getDirection(direction, rotation);
        var newPoint = getPoint(point.x, point.y, speed, newDirection);
        if (isLegalRow(newPoint.x) && isLegalColumn(newPoint.y)) {
          isLegal = true;
          previous = new Instruction(point, direction, speed, rotation);
          println(previous.toTag());
          writer.write(previous.toTag() + "\n");
        }
      } 
    }
    
    writer.close();
  }
  
  private def getDirection(oldDirection : Direction, rotation : Int) : Direction = {
    return rotate(oldDirection, rotation);
  }
  
  private def getPoint(oldX : Int, oldY : Int, speed : Int, direction : Direction) : Point = {
    var x : Int = 0;
    var y : Int = 0;
    if (direction == E) {
      x = oldX + speed;
      y = oldY;
    } else if (direction == W) {
      x = oldX - speed;
      y = oldY;
    } else if (direction == S) {
      x = oldX;
      y = oldY + speed;
    } else {
      x = oldX;
      y = oldY - speed;
    }
    
    return new Point(x, y);
  }
}