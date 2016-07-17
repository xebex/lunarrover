package com.sifei.lunarrover

import scala.collection.mutable.Map;

object Main {

  
  def main(args: Array[String]) {
    var lunarRover = new LunarRover("1");
    new Thread(lunarRover).start();
    
//    var generator = new Generator();
//    generator.generate();
    
  }
  
  
  
}