package com.sifei.lunarrover

import scala.collection.mutable.Map
import scala.collection.mutable.Queue
import com.sifei.lunarrover.Atlas._
import com.sifei.lunarrover.Direction._

/**
 * 控制中心
 */
class LunarController extends Runnable {
  //每个月球车对应一个队列
  var map = Map[Int, Queue[Instruction]]();
  for (i <- 1 to 5) {
    var queue = Queue[Instruction]();
    map.put(i, queue);
  }
  
  //每个月球车会将上报的运行数据放入自己的队列中，由控制中心线程消费
  def enqueue(key: Int, value: Instruction) {
    map.synchronized {
      var queue = map.get(key);
      queue.get.+=(value);
    }
  }
  
  //根据月球车的当前上报数据，预测2秒后月球车的位置
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