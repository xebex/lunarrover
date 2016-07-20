package com.sifei.lunarrover

import scala.collection.mutable.Map;

/**
 * 入口程序
 */
object Main {
  def main(args: Array[String]) {
    //控制中心
    var lunarController = new LunarController();
    new Thread(lunarController).start();
    
    //月球车
    for (i <- 1 to 5) {
      var lunarRover = new LunarRover(i, lunarController);
      new Thread(lunarRover).start();
    }
    
//    //路线生成器
//    var generator = new Generator();
//    generator.generate("src/6.txt");
    
  }
}