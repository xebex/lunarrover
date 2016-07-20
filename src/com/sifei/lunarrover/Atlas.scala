package com.sifei.lunarrover

import Array._
import scala.collection.mutable.Queue
import scala.collection.mutable.Map
import scala.collection.mutable.ListBuffer
import com.sifei.lunarrover.Direction._

/**
 * 地图对象
 */
object Atlas {
  //地图的行数
  val ROWS : Int = 30;
  
  //地图的列数
  val COLS : Int = 30;
  
  //初始化地图数据，使用二维数组保存，true表示是通的，false表示是障碍
  val MATRIX = ofDim[Boolean](ROWS, COLS);
  for (i <- 0 until ROWS) {
    for (j <- 0 until COLS) {
      MATRIX(i)(j) = true;
    }
  }
  
  //添加障碍
//  MATRIX(1)(3) = false;
  MATRIX(1)(1) = false;
  MATRIX(2)(1) = false;
//  MATRIX(3)(1) = false;
  
  //判断某个点是否为障碍点
  def isEnabled(p : Point) : Boolean = {
    return MATRIX(p.x)(p.y);
  }
  
  //判断某个点是否为障碍点
  def isEnabled(x : Int, y : Int) : Boolean = {
    return MATRIX(x)(y);
  }
  
  //判断行值是否合法
  def isLegalRow(row : Int) : Boolean = {
    return row >= 0 && row < ROWS;
  }
  
  //判断列值是否合法
  def isLegalColumn(col : Int) : Boolean = {
    return col >= 0 && col < COLS;
  }
  
  //获取右下角节点对应的Point
  def getBottomRightPoint() : Point = {
    return new Point(ROWS - 1, COLS - 1);
  }
  
  //广度优先搜索，用于重新规划路径
  def bst(begin: Point, end: Point) : List[Point] = {
    var q = Queue[Point]();
    var visitedMap : Map[Point, Boolean] = Map();
    var lastPointMap : Map[Point, Point] = Map();
    var found : Boolean = false;
    var buf = ListBuffer[Point]();
    
    if (isEnabled(begin) && isEnabled(end)) {
      visitedMap.put(begin, true);
      q.+=(begin);
      
      while (!q.isEmpty && !found) {
        var point = q.dequeue();
        if (point != null) {
          var siblings = point.getSiblings;
          for (sibling <- siblings) {
            
            if (!visitedMap.contains(sibling)) {
              q.+=(sibling);
              lastPointMap.put(sibling, point);
              visitedMap.put(sibling, true);
              
              if (sibling.equals(end)) {
                found = true;
              }
            }
          }
        }
      }
  
      var option : Option[Point] = Some(end);
      while (!option.isEmpty) {
        var p = option.get;
        buf.+=:(p);
        option = lastPointMap.get(p);
      }
      if (buf.length == 1) {
        buf.remove(0);
      }
    }

    
    return buf.toList;
  }
  
  //根据起始点、方向和步长，计算运动后的新点
  def getNewPoint(from: Point, direction: Direction, step: Int) : Point = {
    var x = from.x;
    var y = from.y;
    if (direction == E) {
      x += step;
      if (!isLegalRow(x)) {
        x = ROWS - 1;
      }
    } else if (direction == W) {
      x -= step;
      if (!isLegalRow(x)) {
        x = 0;
      }
    } else if (direction == S) {
      y += step;
      if (!isLegalColumn(y)) {
        y = COLS - 1;
      }
    } else if (direction == N) {
      y -= step;
      if (!isLegalColumn(y)) {
        y = 0;
      }
    }
    
    return new Point(x, y);
  }
}