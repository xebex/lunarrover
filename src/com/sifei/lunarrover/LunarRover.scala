package com.sifei.lunarrover

import com.sifei.lunarrover.Configuration._
import com.sifei.lunarrover.Direction._
import scala.io.Source
import scala.collection.mutable.ListBuffer


class LunarRover (name: String) extends Runnable {

  
  override def run(){
    var instructions = parseFile(name);
    //println(instructions);

    var i : Int = 0;
    while (i < instructions.length - 1) {
      var startPoint = instructions(i);
      var paths : List[Point] = null;
      
      i += 1;
      var found = false;
      while (i < instructions.length && !found) {
        var endPoint = instructions(i);
        paths = bst(startPoint.point, endPoint.point);
        if (paths == null || paths.length == 0) {
          i += 1;
        } else {
          found = true;
        }
      }
      
      println(paths);

    }
    

  }
  
  private def findPathsToNearestPoint(instructions : List[Instruction], start : Int) : List[Point] = {
    var startPoint = instructions(start);
    var i = start + 1;
    while (i < instructions.length) {
      var endPoint = instructions(i);
      var paths = bst(startPoint.point, endPoint.point);
      if (paths.length > 0) {
        return paths;
      }
      
      i += 1;
    }
    
    return null;
  }
  
  private def parseFile(name: String) : List[Instruction] = {
    var buf = ListBuffer[Instruction]();
    for (line <- Source.fromFile(new java.io.File("src/" + name + ".txt")).getLines()) {
      
      if (!line.startsWith("#")) {
        var instruction = parseLine(line);
        println(instruction.toTag());
        buf.+=(instruction);
      }
    }
    
    return buf.toList;
  }
  
  private def parseLine(line : String) : Instruction = {
    if (line == null) {
      return null;
    }
    
    var arr = line.split(",");
    if (arr.length < 5) {
      return null;
    }
    
    var point = new Point(arr(0).toInt, arr(1).toInt);
    var direction = Direction.withName(arr(2));
    var speed = arr(3).toInt;
    var rotation = arr(4).toInt;
    
    return new Instruction(point, direction, speed, rotation);
  }


  
}