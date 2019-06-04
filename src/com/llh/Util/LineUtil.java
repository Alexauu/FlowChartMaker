package com.llh.Util;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

public class LineUtil {


    public static boolean isOverlap(Circle source,Circle target){
        double distance = Math.sqrt(Math.pow((source.getCenterX()-target.getCenterX()),2) + Math.pow((source.getCenterY()-target.getCenterY()),2));
        return distance <= 5?true:false;
        //两点距离小于5则初始化该点
    }

    public static Polygon newArrow(double endX, double endY){
        Polygon arrow = new Polygon(endX, endY, endX + 3.5, endY + 11, endX, endY + 9, endX - 3.5, endY + 11);
        arrow.setFill(Color.BLACK);
        arrow.getTransforms().add(new Rotate(90, endX, endY));
        arrow.setStrokeWidth(2);
        arrow.setStroke(Color.BLACK);
        return arrow;
    }







}
