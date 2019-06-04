package com.llh.View;

import com.llh.Model.ArrowLine;
import com.llh.Model.SuperShape;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import java.util.List;
public class RightArea {
    static Text text ;
    static Label label;
    static ColorPicker colorPicker;

    public static void rightArea(AnchorPane drawingArea, VBox rightPane){
        rightPane.setPadding(new Insets(10.0));
        rightPane.setSpacing(10);
        setColorPainter(drawingArea, rightPane);
        setLabel(rightPane);
        setColorPicker(rightPane,drawingArea);
        setDotted(drawingArea,rightPane);
        addIntroduction(rightPane);

    }

    public static void setDotted(AnchorPane drawingArea,VBox rightPane){
        Button button = new Button("设置虚线");
        button.setPrefSize(110,30);
        rightPane.getChildren().add(button);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<Node> nodeList = drawingArea.getChildren();
                for (Node node:nodeList){
                    if(((SuperShape)node).isIsSelected()) {
                        if (node instanceof ArrowLine)
                            ((ArrowLine) node).setNOTDotted();
                        else ((SuperShape) node).setNOTDotted();
                    }
                }
            }
        });
    }

    public static void addIntroduction(VBox rightPane){
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        Text inText = new Text("图 形 介 绍");
        vBox.setPadding(new Insets(10,0,30,0));
        inText.setUnderline(true);
        inText.setFontSmoothingType(FontSmoothingType.LCD);
        label = new Label();
        label.setFont(Font.font("KaiTi",FontWeight.BLACK,FontPosture.ITALIC,20.0));
        label.setMouseTransparent(true);
        label.setWrapText(true);
        label.setPrefSize(100,250);
        label.setLayoutX(2);
        label.setLayoutY(2);
        setIntroduction("Nice To Meet You！");
        Rectangle rectangle = new Rectangle(104,254);
        rectangle.setFill(null);
        rectangle.setStroke(Color.BLACK);
        Group group = new Group();
        group.getChildren().addAll(rectangle,label);
        vBox.getChildren().addAll(inText,group);
        rightPane.getChildren().add(vBox);

    }
    public static void setIntroduction(String string){
        label.setText(string);
    }


    public static void setColorPicker(VBox rightPane, AnchorPane drawingArea){

        colorPicker = new ColorPicker(Color.DEEPPINK);
        rightPane.getChildren().add(colorPicker);

        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Color color = colorPicker.getValue();
                List<Node> list = drawingArea.getChildren();
                text.setStroke(color);
                text.setFill(color);
                for (Node node:list){
                    if( ((SuperShape)node).isIsSelected() ){
                        if(!(node instanceof ArrowLine))
                            ((SuperShape)node).setShapeFill(color);
                    }
                }
            }
        });

    }

    public static void setLabel(VBox rightPane) {

        text= new Text("调 色 板");
        text.setFont(Font.font("KaiTi", FontWeight.BLACK,FontPosture.ITALIC,20));
        text.setUnderline(true);
        text.setFontSmoothingType(FontSmoothingType.LCD);
        rightPane.getChildren().add(0,text);
        rightPane.setAlignment(Pos.TOP_CENTER);
    }

    public static void setColorPainter(AnchorPane drawingArea, VBox rightPane){

        Rectangle[] rectangles = new Rectangle[36];
        double[] hue = new double[]{0.0,30.0,60.0,120.0,160.0,200.0,240.0,280.0,320.0};//2019/5/23 改


        VBox vBox = new VBox();
        vBox.setSpacing(6);

        for (int i = 0;i<9;i++){
            rectangles[i] =  createRectangle(Color.hsb(hue[i%9], 1.0, 1.0)); //2019/5/23 改i%9
            vBox.getChildren().add(rectangles[i]);
        }

        VBox vBox2 = new VBox();
        vBox2.setSpacing(6);
        for (int i = 9;i<18;i++){
            rectangles[i] =  createRectangle(Color.hsb(hue[i%9], 0.5, 1.0));
            vBox2.getChildren().add(rectangles[i]);
        }

        VBox vBox3 = new VBox();
        vBox3.setSpacing(6);
        for (int i = 18;i<27;i++){
            rectangles[i] =  createRectangle(Color.hsb(hue[i%9], 1.0, 0.5));
            vBox3.getChildren().add(rectangles[i]);
        }

        VBox vBox4 = new VBox();
        vBox4.setSpacing(6);

        rectangles[27] = createRectangle(Color.BLACK);
        rectangles[28] = createRectangle(Color.hsb(0, 0, 0.1));
        rectangles[29] = createRectangle(new Color(0.2, 0.2, 0.2, 1));
        rectangles[30] = createRectangle(Color.color(0.3, 0.3, 0.3));
        rectangles[31] = createRectangle(Color.rgb(102, 102, 102));
        rectangles[32] = createRectangle(Color.web("#777777"));
        rectangles[33] = createRectangle(Color.gray(0.6));
        rectangles[34] =  createRectangle(Color.grayRgb(179));
        rectangles[35] =  createRectangle(Color.grayRgb(179, 0.5));
        for (int i = 27;i<=35;i++)
        vBox4.getChildren().add(rectangles[i]);

        HBox palette = new HBox();
        palette.setSpacing(6);
        palette.getChildren().addAll(vBox,vBox2,vBox3,vBox4);
        rightPane.getChildren().add(palette);

        for (int i = 0;i <= 35;i++){
            rectangles[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    event.consume();
                    Paint color =((Rectangle)event.getSource()).getFill();
                    List<Node> list = drawingArea.getChildren();
                    for (Node node:list){
                        if( ((SuperShape)node).isIsSelected() ){
                            if(!(node instanceof ArrowLine))
                            ((SuperShape)node).setShapeFill( color );
                        }
                    }
                    text.setStroke(color);
                    text.setFill(color);
                    colorPicker.setValue((Color) color);
                }
            });
        }
    }
    public static Rectangle createRectangle(Color color) {
        Rectangle rectangle = new Rectangle(0, 45, 20, 20);
        //Fill rectangle with color
        rectangle.setFill(color);
        return rectangle;
    }

}
