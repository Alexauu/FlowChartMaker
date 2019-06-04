package com.llh.Model;
import com.llh.Util.BorderUtil;
import com.llh.Util.DistanceUtil;
import com.llh.Util.NewUtil;
import com.llh.Util.UpdateUtil;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.input.*;
import java.io.Serializable;
import java.util.List;


/**
 * 所有图形都由此容器装载
 *
 */

/**说明
 * 加入drawingArea 的都是SuperShape类
 * 所有图形的形状都由 shape 通过参数传入
 * 操作点（即选中状态下显示出的9个蓝色点）
 * 一个图形的文本、操作点、形状属性都装在SuperShape里面
 * 此类实现图形的拖拽移动，放大缩小功能在ControlPoint实现
 */

public class SuperShape extends Group implements Serializable {
    protected Shape shape; //图形
    private String kind;
    protected TextArea textArea; //输入区域
    protected Label label; //显示文本
    private ControlPoint points; //操作点
    private AnchorPane drawingArea; //画板
    protected SimpleBooleanProperty isSelected; //标记图形是否被选中

    protected static double initX;
    protected static double initY;
    protected Point2D dragAnchor; //鼠标拖拽起点
    protected Point2D xy;
    private int nearest;

    public SuperShape(){
        label = new Label();
        textArea = new TextArea();
        textArea.setVisible(false);
        textArea.setManaged(false);
        isSelected = new SimpleBooleanProperty(true);
    }


    public SuperShape(String kind, double x, double y, AnchorPane drawingArea) {
        this.shape = NewUtil.produceShape(kind,x,y);
        xy = new Point2D(x,y);
        this.kind = kind;
        this.drawingArea = drawingArea;
        this.getChildren().add(shape);
        drawingArea.getChildren().add(this);
        points = new ControlPoint(this, shape,drawingArea);
        isSelected = new SimpleBooleanProperty(true);
        addText(); //文本监听
        addShapeListen(); //图形类监听
    }
    private void addText(){
        textArea = new TextArea();
        label = new Label();
        UpdateUtil.updateTextArea(textArea,shape);
        textArea.setVisible(false);
        UpdateUtil.updateLabel(label,shape);
        label.setWrapText(true);
        label.setMouseTransparent(true); //设置label对鼠标事件透明（即鼠标无法点击），为了不让文本框遮挡shape
        label.setAlignment(Pos.CENTER);  //文本居中
        this.getChildren().addAll(label,textArea);
    }

    private void addShapeListen(){


        shape.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                shape.setCursor(Cursor.MOVE);
            }

        });

        shape.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                isSelected.set(true);
                if(event.getClickCount() == 2){ //双击显示文本输入框
                    UpdateUtil.updateTextArea(textArea,shape); //更新输入区域的位置，适应图形大小、位置
                        textArea.setVisible(true);
                        textArea.setManaged(true);
                }
            }
        });

        /*图形拖拽*/
        shape.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                isSelected.set(true);
                initX = shape.getTranslateX();
                initY = shape.getTranslateY();
                dragAnchor = new Point2D(event.getSceneX(), event.getSceneY());
                List<Node> nList = drawingArea.getChildren();
                if(!event.isControlDown())  //Ctrl没有按下的情况下，反之Ctrl+单击为多选
                for(Node s:nList){ //此循环为 取消图形选中状态、隐藏输入框

                    ((SuperShape)s).setIsSelected(false);
                }
                for(Node s:nList){
                    ((SuperShape)s).getTextArea().setVisible(false);
                    ((SuperShape)s).getTextArea().setManaged(false);
                    ((SuperShape)s).getLabel().setText(((SuperShape)s).getTextArea().getText());
                }
            }
        });

        shape.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double dragX = event.getSceneX() - dragAnchor.getX();
                double dragY = event.getSceneY() - dragAnchor.getY();
                double newXPosition = initX + dragX;
                double newYPosition = initY + dragY;
                BorderUtil.dragBoderJudge(shape, drawingArea, newXPosition, newYPosition);//越界判定
                points.movePoints(shape);//点随图形动
                UpdateUtil.updateTextArea(textArea,shape);
                UpdateUtil.updateLabel(label,shape);
            }
        });

        for(int i=1;i<=8;i++) { //开启放缩监听
            points.setOnScale(i, shape, label, textArea, drawingArea);
        }

        this.isSelected.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                points.setPointsVisible(newValue);  //监听IsSelected 改变，值改变
            }
        });

        matchingListen();
    } //总监听类

    private void matchingListen() {
        shape.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                String msg =  dragboard.getString();
                if("point".equals(msg)){
                    nearest = DistanceUtil.nearestPoint(points.getPoints(),
                            new Circle(event.getX()+shape.getTranslateX(),event.getY()+shape.getTranslateY(),0.5));
                                                    //这里很牛逼
                    for(int i=1;i<=8;i++){
                        if(i==nearest)
                            points.setPointFill(nearest, Color.RED);
                        else points.setPointFill(i, Color.BLUE);
                    }
                }
                event.acceptTransferModes(TransferMode.MOVE);
                setEnterVisable(true);
            }
        });

        shape.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                String msg =  dragboard.getString();
                if("point".equals(msg)){
                    ClipboardContent clipboardContent = new ClipboardContent();

                    clipboardContent.putString("nearest:"+points.getPoint(nearest).getCenterX()+":" +
                            points.getPoint(nearest).getCenterY());
                    dragboard.setContent(clipboardContent);
                    points.setPointFill(nearest, Color.BLUE);
                }
                setEnterVisable(false);
            }
        });
        shape.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                setEnterVisable(false);
                for(int i=1;i<=8;i++){
                    points.setPointFill(i, Color.BLUE);
                }
            }
        });
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected.set(isSelected);
        }

    public boolean isIsSelected() {
        return isSelected.get();
    }

    public SimpleBooleanProperty isSelectedProperty() {
        return isSelected;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label.setText(label);
        this.textArea.setText(label);
    }

    private void setEnterVisable(Boolean status){
        points.setEnterVisible(status);
    }

    public void setLineDotted(boolean is){
        if(is){
            shape.getStrokeDashArray().add(5.0);
        }
        else shape.getStrokeDashArray().clear();
    }
    public void setNOTDotted(){
        if (shape.getStrokeDashArray().size()!=0){
            shape.getStrokeDashArray().clear();
        }else shape.getStrokeDashArray().add(5.0);
    }
    public boolean isDotted(){
        if (shape.getStrokeDashArray().size()!=0)
            return true;
        return false;
    }


    @Override
    public String toString() { //基本图形的属性字符串
        return
        kind + "," + xy.getX() + "," + xy.getY() + "," +
        shape.getFill() + "," + shape.getTranslateX() + "," +
        shape.getTranslateY()  + "," + label.getText() + "," +
        points.getScaleXYXY() + "," + isDotted();

    }

    public void setShapeTranslateX(double x){shape.setTranslateX(x);}
    public void setShapeTranslateY(double y){shape.setTranslateY(y);}
    public void setShapeFill(Paint color){shape.setFill(color);}
    public void setShapeScale(double scX,double scY, double pX, double pY){points.setScaleXXYY(scX,scY,pX,pY);}
    public void refreshPoints(){points.refreshPoints(shape,label);}
}