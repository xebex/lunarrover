package com.sifei.lunarrover

import scala.collection.mutable.Map;

object Main {

  
  def main(args: Array[String]) {
    var lunaRover = new LunaRover("1");
    new Thread(lunaRover).start();
    
//    var generator = new Generator();
//    generator.generate();
    
  }
  
  
  
}