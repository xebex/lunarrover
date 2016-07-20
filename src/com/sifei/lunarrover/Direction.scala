package com.sifei.lunarrover

/**
 * 方向
 */
object Direction extends Enumeration {
  type Direction = Value;
  val N = Value(0);
  val E = Value(90);
  val S = Value(180);
  val W = Value(270);
  
  //根据原始方向和旋转角度，计算新方向
  def rotate(source: Direction, rotation: Int) : Direction = {
    return Direction((source.id + rotation) % 360);
  }
  
  //根据起始点和结束点，计算转向
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
  
  //根据两个朝向，计算旋转角度
  def getRotation(from: Direction, to: Direction) : Int = {
    var value = to.id - from.id;
    if (value < 0) {
      value += 360;
    }
    
    return value;
  }
  
  //根据当前朝向、当前点和目标点，计算旋转角度
  def getRotation(from: Direction, fromPoint: Point, toPoint: Point) : Int = {
    var to = getDirection(fromPoint, toPoint);
    if (to == null) {
      return -1;
    } else {
      return getRotation(from, to);
    }
  }
}