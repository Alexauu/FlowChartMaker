package com.llh.Model;


import com.llh.Util.BorderUtil;
import com.llh.Util.UpdateUtil;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;

import java.io.Serializable;

/** 操作点的排序序号
 *   0
 * 1 2 3
 * 4   5
 * 6 7 8
 */

public class ControlPoint extends Circle implements Serializable {
    private Circle[] points = new Circle[9];
    private Rectangle rectangle;
    private Scale scale = new Scale();
    private Bounds originalBounds;
    private AnchorPane drawingArea;
    private ControlPoint myself = this;

    public ControlPoint(Group group, Shape shape, AnchorPane drawingArea) {
        this.drawingArea = drawingArea;
        initPoints( group, shape);
    }


    public void initPoints(Group group, Shape shape) {
        Bounds bounds = shape.getBoundsInParent();
        double firstX = bounds.getMinX();
        double firstY = bounds.getMinY();
        double bW = bounds.getWidth();
        double bH = bounds.getHeight();
        points[0] = new Circle(firstX + bW/2, firstY - 20, 8);
        points[0].setFill(new ImagePattern(new Image("/icon/rotate.png")));
        points[0].setCursor(Cursor.OPEN_HAND);
        points[1] = new Circle(firstX, firstY, 4.0, Color.BLUE);
        points[1].setCursor(Cursor.NW_RESIZE);
        points[2] = new Circle(firstX + bW/2, firstY, 4.0, Color.BLUE);
        points[2].setCursor(Cursor.N_RESIZE);
        points[3] = new Circle(firstX + bW, firstY, 4.0, Color.BLUE);
        points[3].setCursor(Cursor.NE_RESIZE);
        points[4] = new Circle(firstX, firstY + bH/2, 4.0, Color.BLUE);
        points[4].setCursor(Cursor.W_RESIZE);
        points[5] = new Circle(firstX + bW, firstY + bH/2, 4.0, Color.BLUE);
        points[5].setCursor(Cursor.E_RESIZE);
        points[6] = new Circle(firstX, firstY + bH, 4.0, Color.BLUE);
        points[6].setCursor(Cursor.SW_RESIZE);
        points[7] = new Circle(firstX + bW/2, firstY + bH, 4.0, Color.BLUE);
        points[7].setCursor(Cursor.S_RESIZE);
        points[8] = new Circle(firstX + bW, firstY + bH, 4.0, Color.BLUE);
        points[8].setCursor(Cursor.SE_RESIZE);

        for (int i = 0; i < 9; i++) {
            group.getChildren().add(points[i]);
        }

        rectangle = new Rectangle(firstX,firstY,bW,bH);
        rectangle.getStrokeDashArray().add(5.0);
        rectangle.setStroke(Color.GRAY);
        rectangle.setFill(null);
        group.getChildren().add(rectangle);

        scale = new Scale();
        shape.getTransforms().add(scale);

    }

    public void movePoints(Shape shape) {
        Bounds bounds = shape.getBoundsInParent();
        setPoints(bounds.getMinX(),bounds.getMinY(),bounds.getWidth(),bounds.getHeight());
    }

    public void setPoints(double bX, double bY, double bW, double bH) {

        movePoint(points[0],bX+bW/2,bY -20);
        movePoint(points[1],bX,bY);
        movePoint(points[2],bX+bW/2,bY);
        movePoint(points[4],bX,bY+bH/2);
        movePoint(points[3],bX+bW,bY);
        movePoint(points[6],bX,bY+bH);
        movePoint(points[5],bX+bW,bY+bH/2);
        movePoint(points[7],bX+bW/2,bY+bH);
        movePoint(points[8],bX+bW,bY+bH);
        rectangle.setX(bX);
        rectangle.setY(bY);
        rectangle.setWidth(bW);
        rectangle.setHeight(bH);
    }


    public void movePoint(Circle point, double newX, double newY) {
        point.setCenterX(newX);

        point.setCenterY(newY);


    }

    public void shapeScale(Shape shape,int i,Bounds originalBounds,AnchorPane drawingArea){
        shape.setTranslateX(0);
        shape.setTranslateY(0);
        double moveX = rectangle.getX();
        double moveY = rectangle.getY();
        double initX = originalBounds.getMinX();
        double initY = originalBounds.getMinY();

        shape.setTranslateX(moveX-initX); //图形移动到线框的左上角
        shape.setTranslateY(moveY-initY);
        Bounds newBounds = rectangle.getBoundsInParent();
        double newWidth = newBounds.getWidth(); //得到放大后的线框的宽和高
        double newHeight = newBounds.getHeight();
        double oldWidth = originalBounds.getWidth();//图形原始宽和高
        double oldHeight = originalBounds.getHeight();
        scale.setPivotX(originalBounds.getMinX());//始终以线框的左上角一点作为定点进行缩放
        scale.setPivotY(originalBounds.getMinY());
        double scX = newWidth/oldWidth;
        double scY = newHeight/oldHeight; //得到缩放比例
        BorderUtil.scaleBorderJudge(shape,scale,scX,scY,drawingArea); //越界判定并设置比例
    }

    public Circle getPoint(int index) {
        return points[index];
    }

    public Circle[] getPoints() {
        return points;
    }

    public void setPointsVisible(boolean isSelected) {
        for (Circle c : points) {
            c.setVisible(isSelected);
        }
        rectangle.setVisible(isSelected);
    }

    public void setOnScale(int i, Shape shape, Label label, TextArea textArea, AnchorPane drawingArea) {

        Shape tempShape = shape;
        tempShape.setTranslateY(0);
        tempShape.setTranslateY(0);
        originalBounds = tempShape.getBoundsInParent(); //得到最初图形的边框

        points[i].setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
        });

        points[i].setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double newX = event.getX();
                double newY = event.getY();
                double stdX = points[1].getCenterX(); //左上角的点 默认为第一点
                double stdY = points[1].getCenterY(); //实际上拖拽时会改变
                double newW = 0;
                double newH = 0;
                switch (i) {
                    case 1:
                        newW = points[9-i].getCenterX() - newX;
                        newH = points[9-i].getCenterY() - newY;
                        stdX = newX;
                        stdY = newY;
                        break;
                    case 2:
                        newW = points[3].getCenterX() - points[1].getCenterX();
                        newH =  points[9-i].getCenterY()-newY ;
                        stdY = newY;
                        break;
                    case 4:
                        newW =points[9-i].getCenterX() - newX ;
                        newH = points[6].getCenterY() - points[1].getCenterY();
                        stdX = newX;
                        break;
                    case 3:
                        newW = newX -points[9-i].getCenterX();
                        newH = points[9-i].getCenterY() - newY;
                        stdY = newY;
                        break;
                    case 6:
                        newW = points[9-i].getCenterX() - newX;
                        newH = newY - points[9-i].getCenterY();
                        stdX = newX;
                        break;
                    case 5:
                        newW =  newX - points[9-i].getCenterX();
                        newH = points[6].getCenterY() - points[1].getCenterY();
                        break;
                    case 7:
                        newW = points[3].getCenterX() - points[1].getCenterX();
                        newH = newY - points[9-i].getCenterY();
                        break;
                    case 8:
                        newW = newX - points[9-i].getCenterX();
                        newH = newY - points[9-i].getCenterY();
                        break;
                }
                if(newW > 0.2*originalBounds.getWidth() && newH > 0.2*originalBounds.getHeight()) {
                    BorderUtil.setPointsBorderJudge(myself, stdX, stdY, newW, newH,drawingArea);
                    shapeScale(shape,i, originalBounds,drawingArea);
                }

                UpdateUtil.updateLabel(label, shape );
                UpdateUtil.updateTextArea(textArea,shape);
            }
        });


        points[i].setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (Circle p:points){
                    p.setCenterX(p.getCenterX()+p.getTranslateX());
                    p.setCenterY(p.getCenterY()+p.getTranslateY());
                }
            }
        });
    }

    protected void setEnterVisible(Boolean status){
        points[2].setVisible(status);
        points[4].setVisible(status);
        points[5].setVisible(status);
        points[7].setVisible(status);
    }

    public void setPointFill(int i,Paint color){
        points[i].setFill(color);
    }

    public String getScaleXYXY(){
        return scale.getX()+","+scale.getY()+","+scale.getPivotX()+","+scale.getPivotY();
    }
    public void setScaleXXYY(double scX,double scY, double pX, double pY){
        scale.setX(scX);
        scale.setY(scY);
        scale.setPivotX(pX);
        scale.setPivotY(pY);
    }
    public void refreshPoints(Shape shape, Label label){
        Bounds bounds = shape.getBoundsInParent();
        setPoints(bounds.getMinX(),bounds.getMinY(),bounds.getWidth(),bounds.getHeight());
        label.setLayoutX(bounds.getMinX());
        label.setLayoutY(bounds.getMinY());
    }
}
