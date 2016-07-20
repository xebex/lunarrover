package com.sifei.lunarrover

import com.sifei.lunarrover.Atlas._
import com.sifei.lunarrover.Direction._
import scala.io.Source
import scala.collection.mutable.ListBuffer

/**
 * 月球车
 */
class LunarRover (name: Int, controller: LunarController) extends Runnable {

  override def run(){
    var zero = System.currentTimeMillis();
    
    //根据路线图，获取对应的Point的列表
    var paths = parsePlannedInstructions();

    //根据paths生成directions
    var instructions = generateRealInstructions(paths);
    
    printf("LunarRover%d will start, startPoint:(%d, %d), endPoint:(%d, %d), initialSpeed=%d\n", 
        name,
        paths(0).x,
        paths(0).y,
        paths(paths.length - 1).x,
        paths(paths.length - 1).y,
        instructions(0).speed);

    
    //向控制中心每隔1秒发送数据
    for (instruction <- instructions) {
      //println(instruction);
      controller.enqueue(name, instruction);
      Thread.sleep(1000L);
    }
    
    var secondsSpent = (System.currentTimeMillis() - zero) / 1000;
    if (getBottomRightPoint().equals(instructions(instructions.length - 1).point)) {
      printf("LunarRover%d has reached the destination. Time spent (in seconds): %d\n", 
          name,
          secondsSpent);
    } else {
      printf("LunarRover%d got stuck... Time spent (in seconds): %d\n", 
          name,
          secondsSpent);
    }
  }
  
  private def generateRealInstructions(paths : List[Point]) : List[Instruction] = {
    var instructions = ListBuffer[Instruction]();
    var mergedInstructions = ListBuffer[Instruction]();
    
    if (paths != null && paths.length > 0) {
      var instruction = new Instruction(new Point(0, 0), E, 1, 0);
      if (paths.length < 2) {
        instructions.+=(instruction);
      } else {
        var i : Int = 0;
        while (i < paths.length - 1) {
          var firstPoint = paths(i);
          var secondPoint = paths(i + 1);
          
          if (i == 0) {
            instruction = generateFirstInstruction(firstPoint, secondPoint);
          } else {
            instruction = generateInstruction(instructions(i - 1), firstPoint, secondPoint);
          }
          
          instructions.+=(instruction);
          
          i += 1;
        }
        
        instructions.+=(generateLastInstruction(instructions(i - 1), paths(i)));
      }
    }
    
    
    
    //merge
    var i = 1;
    var speed = 0;
    mergedInstructions.+=(instructions(0));
    while (i < instructions.length) {
      speed += 1;
      
      if (instructions(i).rotation != 0) {
        var instruction = instructions(i);
        instruction.speed = speed;
        mergedInstructions.+=(instruction);
        speed = 0;
      }
      
      i += 1;
    }
    var instruction = instructions(instructions.length - 1);
    instruction.speed = speed;
    mergedInstructions.+=(instruction);
    
    //for (inst <- mergedInstructions) {
    //  println(inst);
    //}
    //println("=====================================");

    return mergedInstructions.toList;
  }
  
  private def generateFirstInstruction(firstPoint : Point, secondPoint: Point) : Instruction = {
    var xLength = secondPoint.x - firstPoint.x;
    var yLength = secondPoint.y - firstPoint.y;
    
    var point = new Point(firstPoint.x, firstPoint.y);
    var direction : Direction = E;
    var speed : Int = 0;
    var rotation = 0;
    
    if (xLength > 0) {
      direction = E;
      speed = xLength;
    } else if (xLength < 0) {
      direction = W;
      speed = 0 - xLength;
    } else if (yLength > 0) {
      direction = S;
      speed = yLength;
    } else if (yLength < 0) {
      direction = N;
      speed = 0 - yLength;
    }
    
    var instruction = new Instruction(point, direction, speed, rotation);
    return instruction;
  }
  
  private def generateInstruction(lastInstruction : Instruction, currentPoint: Point, nextPoint: Point) : Instruction = {
    var direction = Direction.rotate(lastInstruction.direction, lastInstruction.rotation);
    var speed : Int = 1;
    var rotation = Direction.getRotation(direction, currentPoint, nextPoint);
    
    return new Instruction(currentPoint, direction, speed, rotation);
  }
  
  private def generateLastInstruction(lastInstruction : Instruction, currentPoint: Point) : Instruction = {
    var instruction = new Instruction(currentPoint, Direction.getDirection(lastInstruction.point, currentPoint), 1, 0);
    return instruction;
  }
  
  private def parsePlannedInstructions() : List[Point] = {
    //从控制中心读取线路
    var instructions = parseFile(String.valueOf(name));
    //println(instructions);

    //根据线路中的各个Point与地图上的障碍信息，计算实际的线路
    var paths = ListBuffer[Point]();
    var i : Int = 0;
    while (i < instructions.length - 1) {
      var startPoint = instructions(i);
      var points : List[Point] = null;
      
      i += 1;
      var found = false;
      while (i < instructions.length && !found) {
        var endPoint = instructions(i);
        points = bst(startPoint.point, endPoint.point);
        if (points == null || points.length == 0) {
          i += 1;
        } else {
          found = true;
        }
      }
      
      //println(points);
      
      //去除points中的重复点，生成实际线路paths
      if (points != null && points.length > 0) {
        if (paths.isEmpty) {
          paths.++=(points);
        } else {
          paths.++=(points.slice(1, points.length));
        }        
      }
    }
      
    return paths.toList;
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
        //println(instruction.toTag());
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