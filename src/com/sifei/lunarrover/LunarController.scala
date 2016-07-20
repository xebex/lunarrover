package com.sifei.lunarrover

import scala.collection.mutable.Map
import scala.collection.mutable.Queue
import com.sifei.lunarrover.Atlas._
import com.sifei.lunarrover.Direction._

class LunarController extends Runnable {
  var map = Map[Int, Queue[Instruction]]();
  for (i <- 1 to 5) {
    var queue = Queue[Instruction]();
    map.put(i, queue);
  }
  
  
  def enqueue(key: Int, value: Instruction) {
    map.synchronized {
      var queue = map.get(key);
      queue.get.+=(value);
    }
  }
  
  def getPredictionAfterTwoSeconds(instruction : Instruction) : Point = {
    var from = instruction.point;
    var direction = rotate(instruction.direction, instruction.rotation);
    var step = 2 * instruction.speed;
    
    return getNewPoint(from, direction, step);
  }
  
  override def run() {
    while (true) {
      map.synchronized {
        map.keys.foreach { key => 
          var queue = map.get(key);
          if (!queue.isEmpty && !queue.get.isEmpty) {
            var instruction = queue.get.dequeue()
            var predicted = getPredictionAfterTwoSeconds(instruction);
            printf("LunarRover%d: Reported: [Position=(%d,%d), Direction=%s, Speed=%d, Rotation=%d], Predicted: [Position=(%d,%d)]\n",
                key,
                instruction.point.x,
                instruction.point.y,
                instruction.direction,
                instruction.speed,
                instruction.rotation,
                predicted.x,
                predicted.y);
          }
          
        }
      }
      
      Thread.sleep(500L);
    }
  }
  

}