package com.llh.Model;

import com.llh.Util.BorderUtil;
import com.llh.Util.LineUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.input.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 */

public class ArrowLine extends SuperShape implements Serializable {

    private Polyline line;
    private Polygon arrow;
    private String kind;
    private AnchorPane drawingArea;
    private Circle[] lpoints = new Circle[4];
    private int[] status = new int[]{1,0,0,1};
    private Group myself = this;


    public ArrowLine(double x, double y, AnchorPane drawingArea) {
        super();
        this.kind = "ArrowLine";
        this.drawingArea = drawingArea;
        initLine(x, y);
        this.getChildren().addAll(line, arrow,textArea,label);
        drawingArea.getChildren().add(0,this);
        textArea.setMaxSize(30,30);
        onListen(this);
        for (int i = 0; i < 4; i++) {
            if(i == 0 || i == 3) aPlanListen(i,this);
            else bPlanListen(i,this);
        }
    }

    private void initLine(double x, double y) {
        double startX = x - 50;
        double startY = y;
        double endX = x + 50;
        double endY = y;
        line = new Polyline(startX, startY, endX, endY);
        line.setStroke(Color.BLACK);
        line.setFill(null);
        arrow = LineUtil.newArrow(endX,endY);
        line.setStrokeWidth(2);
        line.setPickOnBounds(true);
        initPoints(startX, startY, endX, endY);
    }

    private void initPoints(double startX, double startY, double endX, double endY) {
        lpoints[0] = new Circle(startX, startY, 4, Color.BLUE); // 箭尾
        lpoints[3] = new Circle(endX, endY, 4, Color.BLUE); // 箭头
        lpoints[1] = new Circle((endX - startX) / 3 + startX, (endY - startY) / 3 + startY, 4, Color.BLUE); //
        lpoints[1].setOpacity(0.3);
        lpoints[2] = new Circle((endX - startX) * 2 / 3 + startX, (endY - startY) * 2 / 3 + startY, 4, Color.BLUE); //
        lpoints[2].setOpacity(0.3);
        for (Circle p : lpoints) p.setStroke(Color.WHITE);
        this.getChildren().addAll(lpoints[0], lpoints[1], lpoints[2], lpoints[3]);

    }

    public void onListen(Group group) {
        line.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                cancelSelect();
                isSelected.set(true);
            }
        });
        line.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                if(event.getClickCount() == 2){
                    Label newlabel = new Label();
                    textArea.setManaged(true);
                    textArea.setVisible(true);
                    label.setVisible(false);
                    textArea.setText(label.getText());
                    textArea.setLayoutX(event.getX() - 30);
                    textArea.setLayoutY(event.getY() - 30);
                    label.setLayoutX(event.getX() - 20);
                    label.setLayoutY(event.getY() - 20);
                }
            }
        });

        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                if(event.getClickCount() == 2){
                    textArea.setManaged(true);
                    textArea.setVisible(true);
                    textArea.setLayoutX(label.getLayoutX());
                    textArea.setLayoutY(label.getLayoutY());
                }
            }
        });

        line.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                line.setCursor(Cursor.MOVE);
            }
        });
        arrow.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                cancelSelect();
                isSelected.set(true);

            }
        });
            matchListen(0);//首尾两个点的监听
            matchListen(3);//主要功能是自动连接图形

        isSelected.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                for (Circle c : lpoints) {
                    c.setVisible(newValue);
                }
                textArea.setVisible(false);
                textArea.setManaged(false);
                label.setText(textArea.getText());
                label.setVisible(true);
            }
        });

    } // onListen结束

    public void unionMove(double newXPosition, double newYPosition){

    }

    public void matchListen(int i){
        lpoints[i].setOnDragDetected(event -> {
            Dragboard dragboard = lpoints[i].startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString("point");
            dragboard.setContent(clipboardContent);
            cancelSelect();
            isSelected.set(true);

            drawingArea.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    event.acceptTransferModes(TransferMode.MOVE);
                    Dragboard dragboard = event.getDragboard();
                    String msg = dragboard.getString();
                    if("point".equals(msg)) {
                        double newX = event.getX();
                        double newY = event.getY();
                        BorderUtil.movePointBorderJudge(lpoints[i], newX, newY, drawingArea);
                        if (i == 3) {
                            updateArrow(lpoints[i].getCenterX(),lpoints[i].getCenterY());
                        }
                        upDateLine();
                    }
                }
            });
        });
        lpoints[i].setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                Dragboard dragboard = event.getDragboard();
                String msg = dragboard.getString();
                String msgs[] = msg.split(":");
                if("nearest".equals(msgs[0])) {
                    lpoints[i].setCenterX(Double.valueOf(msgs[1]));
                    lpoints[i].setCenterY(Double.valueOf(msgs[2]));
                    if (i == 3) {
                        updateArrow(Double.valueOf(msgs[1]),Double.valueOf(msgs[2]));
                    }
                    upDateLine();
                    lpoints[i].setRadius(4);
                    lpoints[i].setFill(Color.BLUE);
                }
            }
        });



    }

    private void aPlanListen(int i, Group group) {
        lpoints[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lpoints[i].setCursor(Cursor.OPEN_HAND);
            }
        });
        lpoints[i].setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lpoints[i].setCursor(Cursor.CLOSED_HAND);
                event.consume();
                isSelected.set(true);
                for (Circle p : lpoints) {
                    group.getChildren().remove(p);
                    group.getChildren().add(p);
                }
            }
        });

    }

    public void bPlanListen(int i,Group group) {
        lpoints[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lpoints[i].setCursor(Cursor.OPEN_HAND);
            }
        });

        lpoints[i].setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                lpoints[i].setCursor(Cursor.CLOSED_HAND);
                status[i] = 1;
                lpoints[i].setOpacity(1);
                isSelected.set(true);
                for (Circle p : lpoints) {
                    group.getChildren().remove(p);
                    group.getChildren().add(p);
                }
                lpoints[i].setRadius(6);
                lpoints[i].setFill(Color.RED);

                cancelSelect();
                isSelected.set(true);
            }
        });


        lpoints[i].setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double newY = event.getY();
                double newX = event.getX();
                BorderUtil.movePointBorderJudge(lpoints[i],newX,newY,drawingArea);
                for (int j = 0; j < 4; j++) {
                    if (j != i) {
                        if (LineUtil.isOverlap(lpoints[i], lpoints[j])) {
                            status[i] = 0;
                            lpoints[i].setOpacity(0.3);
                            lpoints[i].setFill(Color.BLUE);
                            break;
                        }
                    }
                    upDateLine();
                }
            }
        });

        lpoints[i].setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lpoints[i].setFill(Color.BLUE);
                lpoints[i].setRadius(4);
            }
        });
    }

    public  void upDateLine(){
        line.getPoints().clear();
        Circle left,right;
        for(int i=1; i <= 2;i++){//此循环实现虚点位置自动调整
            if(status[i] == 0){ //判定是虚点进入循环
                if(status[i-1] == 1) left = lpoints[i-1];
                else left = lpoints[i-2];
                if(status[i+1] == 1) right = lpoints[i+1];
                else right = lpoints[i+2];
                double endX = right.getCenterX();
                double endY = right.getCenterY();
                double startX = left.getCenterX();
                double startY = left.getCenterY();
                lpoints[i].setCenterX((endX-startX)*i/3+startX);
                lpoints[i].setCenterY((endY-startY)*i/3+startY);
            }
        }
            for(int i=0;i<4;i++) { //实线根据四个点的位置确认
                line.getPoints().addAll(lpoints[i].getCenterX(),
                        lpoints[i].getCenterY());
            }
        //updateDirection
        Circle anchor = new Circle();
        for(int i =2;i>=0;i--){ //找到最接近箭头的点
            if(status[i] == 1){
                anchor = lpoints[i];
                break;
            }
        }

        //根据两个点的相对位置，通过三角函数算出箭头指向方向
        double angle = Math.atan((lpoints[3].getCenterX()- anchor.getCenterX())
               / (anchor.getCenterY()-lpoints[3].getCenterY()) ) ;
        angle = Math.toDegrees(angle); //弧度转成角度
        arrow.getTransforms().clear();
        if(lpoints[3].getCenterY()>anchor.getCenterY()) angle+=180;
        arrow.getTransforms().add(new Rotate(angle,lpoints[3].getCenterX(),lpoints[3].getCenterY()));
    }

    public void updateArrow(double newX, double newY){

        this.getChildren().remove(arrow);
        arrow = LineUtil.newArrow(newX, newY);
        this.getChildren().add(arrow);
        upDateLine();
    }

    public void cancelSelect(){

        List<Node> nList = drawingArea.getChildren();
        for(Node node:nList){
            ((SuperShape)node).setIsSelected(false);
//                    ((SuperShape)node).getTextArea().setVisible(false);
            ((SuperShape)node).getTextArea().setManaged(false);
            ((SuperShape)node).getLabel().setText(((SuperShape)node).getTextArea().getText());
        }

    }


    public void setLinePoints(double x1, double y1,double x2, double y2,
                              double x3, double y3,double x4, double y4){
            lpoints[0].setCenterX(x1);
            lpoints[0].setCenterY(y1);
            lpoints[1].setCenterX(x2);
            lpoints[1].setCenterY(y2);
            lpoints[2].setCenterX(x3);
            lpoints[2].setCenterY(y3);
            lpoints[3].setCenterX(x4);
            lpoints[3].setCenterY(y4);
    }
    public void setPointStatus(int s1, int s2){
        status[1] = s1;
        status[2] = s2;
    }

    public void setLineLabel(String label,double x,double y){
        this.label.setText(label);
        this.label.setLayoutX(x);
        this.label.setLayoutY(y);
        this.textArea.setText(label);
        this.textArea.setLayoutX(x);
        this.textArea.setLayoutY(y);
    }
    private String getPointXyString(){
        String str = "";
        for (Circle c:lpoints) {
            str += c.getCenterX()+","+c.getCenterY()+ ",";
        }
        return str;
    }

    @Override
    public String toString() { //箭头实线的属性字符串
        return
        kind + "," + getPointXyString()  +status[1] + "," +
        status[2] + "," + label.getText() + "," +
        label.getLayoutX() + "," + label.getLayoutY() + "," + isDotted();

    }
    @Override
    public void setLineDotted(boolean is){
        if(is){
            line.getStrokeDashArray().add(5.0);
        }
        else line.getStrokeDashArray().clear();
    }

    public void setNOTDotted(){
        if (line.getStrokeDashArray().size()!=0){
            line.getStrokeDashArray().clear();
        }else line.getStrokeDashArray().add(5.0);
    } //虚实互换

    public boolean isDotted(){
        if (line.getStrokeDashArray().size() != 0)
            return true;
        return false;
    } //判断虚线

    public void translateLine(double dis){
        for(int i=0;i<4;i++){
            lpoints[i].setCenterX(lpoints[i].getCenterX()+dis);
        }
        refreshLine();
    }

    public void refreshLine(){
        upDateLine();
        updateArrow(lpoints[3].getCenterX(),lpoints[3].getCenterY());
    } //刷新线

}
