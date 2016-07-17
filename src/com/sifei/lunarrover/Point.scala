package com.sifei.lunarrover

import scala.collection.mutable.ListBuffer
import com.sifei.lunarrover.Configuration._

class Point (xi: Int, yi: Int) {
  var x : Int = xi;
  var y : Int = yi;
  
  override def toString = ( "[x=" + x + ", y=" + y + "]");
  
  override def hashCode = this.toString().hashCode();
  
  override def equals(that: Any) : Boolean = {
    if (that == null) {
      return false;
    }
    
    return that.toString().equals(this.toString());
  }
  
  def getSiblings : List[Point] = {
    var buf = ListBuffer[Point]();
    
    
    //右
    if (isLegalColumn(x + 1) && isEnabled(x + 1, y)) {
      buf.+=(new Point(x + 1, y));
    }
    
    //下
    if (isLegalRow(y + 1) && isEnabled(x, y + 1)) {
      buf.+=(new Point(x, y + 1));
    }
    
    //左
    if (isLegalColumn(x - 1) && isEnabled(x - 1, y)) {
      buf.+=(new Point(x - 1, y));
    }
    
    //上
    if (isLegalRow(y - 1) && isEnabled(x, y - 1)) {
      buf.+=(new Point(x, y - 1));
    }
    
    
    return buf.toList;
  }
  
  def getTag : String = {
    return x + "," + y;
  }
  
  
}