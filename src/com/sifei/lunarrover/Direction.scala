package com.sifei.lunarrover

object Direction extends Enumeration {
  type Direction = Value;
  val N = Value(0);
  val E = Value(90);
  val S = Value(180);
  val W = Value(270);
  
  def rotate(source: Direction, angle: Int) : Direction = {
    return Direction((source.id + angle) % 360);
  }
}