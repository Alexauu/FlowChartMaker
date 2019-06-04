package com.llh.Util;

import com.llh.Model.ArrowLine;
import com.llh.Model.ControlPoint;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;

import java.util.List;

public class BorderUtil {

    public static String newBorderJudge( double x, double y, AnchorPane drawingArea){

        double currentH = drawingArea.getPrefHeight();
        double currentW = drawingArea.getPrefWidth();
        if(x <= 0 ){
            if(currentW == 600){
                drawingArea.setPrefWidth(1200);
                List<Node> list = drawingArea.getChildren();
                for(Node n:list){ //2019/5/23 增
                    if(n instanceof ArrowLine)
                        ((ArrowLine)n).translateLine(600.0);
                   else n.setTranslateX(600);
                } x = 1;
            }
            else x = 1;
        }
        if( x >=  currentW - 100 ){
            if(currentW == 600){
                drawingArea.setPrefWidth(600+currentW);
            }
            else x = 1099;
        }
        if(y <= 0) y = 1;
        if( y >= currentH-50) {
            if(currentH == 800) drawingArea.setPrefHeight(800+currentH);
            else y = 1549;
        }
        return x + ":" + y;
    }

    public static void dragBoderJudge(Shape shape, AnchorPane drawingArea, double newXPosition, double newYPosition) {
        double currentH = drawingArea.getPrefHeight();
        double currentW = drawingArea.getPrefWidth();
        double lastTranX = shape.getTranslateX();
        double lastTranY = shape.getTranslateY();
        shape.setTranslateX(newXPosition);
        shape.setTranslateY(newYPosition);
        Bounds bounds = shape.getBoundsInParent();
        double bW = bounds.getWidth();
        double bH = bounds.getHeight();
        double minX = bounds.getMinX();
        double minY = bounds.getMinY();

            if (minX + bW >= currentW-0.5) {
                if (currentW == 600) {
                    drawingArea.setPrefWidth(1200);
                }
                else shape.setTranslateX(lastTranX);
            }
        else if(minX < 0.5)
                shape.setTranslateX(lastTranX);
        //Y判定
        if(minY + bH >= currentH-0.5){
            if(currentH == 800){
                drawingArea.setPrefHeight(1600);
            }else shape.setTranslateY(lastTranY);
        }else if(minY < 0.5){
            shape.setTranslateY(lastTranY);
        }
    }

    public static void scaleBorderJudge(Shape shape, Scale scale, double scX, double scY, AnchorPane drawingArea){

        double currentH = drawingArea.getPrefHeight();
        double currentW = drawingArea.getPrefWidth();

        double lastScaleX = scale.getX();
        double lastScaleY = scale.getY();

        scale.setX(scX);
        scale.setY(scY);

        Bounds bounds = shape.getBoundsInParent();
        double bW = bounds.getWidth();
        double minX = bounds.getMinX();
        double minY = bounds.getMinY();
        double bH = bounds.getHeight();

        if (minX + bW >= currentW-0.5) {
            if (currentW == 600) {
                drawingArea.setPrefWidth(1200);
            }
            else scale.setX(lastScaleX);
        }
        else if(minX < 0.5)
            shape.setTranslateX(lastScaleX);

        //Y判定
        if(minY + bH >= currentH-0.5){
            if(currentH == 800){
                drawingArea.setPrefHeight(1600);
            }else scale.setY(lastScaleY);
        }else if(minY < 0.5){
            shape.setTranslateY(lastScaleY);
        }
    }

    public static void movePointBorderJudge(Circle point, double newX, double newY, AnchorPane drawingArea){

        double currentH = drawingArea.getPrefHeight();
        double currentW = drawingArea.getPrefWidth();
        if(newX > currentW - 0.5) {
            if (currentW == 600) {
                drawingArea.setPrefWidth(1200);
                point.setCenterX(newX);
            }
        }else if(newX > 0.5) point.setCenterX(newX);


        if(newY > currentH - 0.5){
            if(currentH == 800){
                drawingArea.setPrefHeight(1600);
                point.setCenterY(newY);
            }
        }else if(newY > 0.5) point.setCenterY(newY);
    }



    public static int setPointsBorderJudge(ControlPoint point, double stdX, double stdY,
                                           double newW, double newH,AnchorPane drawingArea){

        double currentH = drawingArea.getPrefHeight();
        double currentW = drawingArea.getPrefWidth();
        int flag1 = 0;
        int flag2 = 0;

        if(stdY<0.5) return 0;
        if(stdX<0.5) return 0;
        if(stdX + newW + 0.5 > currentW){
            if(currentW == 600) flag1 = 1;
            else return 0;
        }
        if(stdY + newH + 0.5 > currentH){
            if(currentH == 800) flag2 = 1;
            else return 0;
        }

        if(flag1 == 1) drawingArea.setPrefWidth(1200);
        if(flag2 == 1) drawingArea.setPrefHeight(1600);

        point.setPoints(stdX,stdY,newW,newH);
        return 1;
    }

}//类