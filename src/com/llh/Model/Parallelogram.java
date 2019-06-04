package com.llh.Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.Serializable;


/**
 * 平行四边形
 */

public class Parallelogram  extends Polygon implements Serializable {

    public Parallelogram(double x, double y){
        super(x+14.4,y,x+114.4,y,x+85.6,y+50,x-14.4,y+50);
        super.setStroke(Color.BLACK);
        super.setFill(Color.WHITE);
//        new SuperShape("Parallelogram",x,y,this,drawingArea);
    }
}
