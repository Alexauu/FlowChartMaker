package com.llh.Model;


import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.Serializable;

/**
 * 菱形
 */

public class Rhombus extends Polygon implements Serializable {

    public Rhombus(double x, double y){
        super(x+50,y,x+100,y+25,x+50,y+50,x,y+25);
        super.setStroke(Color.BLACK);
        super.setFill(Color.WHITE);
//        new SuperShape("Rhombus",x,y,this,drawingArea);
    }
}
