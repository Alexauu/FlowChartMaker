package com.llh.Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.io.Serializable;

/**
 * 单边曲面矩形
 */

public class RurveRectangle extends Rectangle implements Serializable {

    private Arc concave; //凹
    private Arc convex;  //凸
    private Shape shape;

    public void init(double x, double y){
        concave = new Arc(x+25,y,50,50,240,60);
        convex = new Arc(x+75,y+87,50,50,60,60);
    }

    public RurveRectangle(double x, double y){
        super(x,y,100,43.5);
        init(x,y);
        shape = union(this,concave);
        shape = subtract(shape,convex);
        shape.setFill(Color.WHITE);
        shape.setStroke(Color.BLACK);
    }
    public Shape getShape(){
        return this.shape;
    }

}
