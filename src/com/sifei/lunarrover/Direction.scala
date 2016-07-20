package com.sifei.lunarrover

object Direction extends Enumeration {
  type Direction = Value;
  val N = Value(0);
  val E = Value(90);
  val S = Value(180);
  val W = Value(270);
  
  def rotate(source: Direction, rotation: Int) : Direction = {
    return Direction((source.id + rotation) % 360);
  }
  
  def getDirection(fromPoint: Point, toPoint: Point) : Direction = {
    var xLength = toPoint.x - fromPoint.x;
    var yLength = toPoint.y - fromPoint.y
    
    if (xLength > 0) {
      return E;
    } else if (xLength < 0) {
      return W;
    } else if (yLength > 0) {
      return S;
    } else if (yLength < 0) {
      return N;
    }
    
    return null;
  }
  
  def getRotation(from: Direction, to: Direction) : Int = {
    var value = to.id - from.id;
    if (value < 0) {
      value += 360;
    }
    
    return value;
  }
  
  def getRotation(from: Direction, fromPoint: Point, toPoint: Point) : Int = {
    var to = getDirection(fromPoint, toPoint);
    if (to == null) {
      return -1;
    } else {
      return getRotation(from, to);
    }
  }
}