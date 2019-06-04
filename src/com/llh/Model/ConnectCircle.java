package com.llh.Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.Serializable;

/**
 * 连接圆  ○
 */

public class ConnectCircle extends Circle implements Serializable {

    public ConnectCircle(double x, double y){
        super(x,y,20, Color.WHITE);
        super.setStroke(Color.BLACK);
//        new SuperShape("ConnectCircle",x,y,this,drawingArea);
    }
}
