package com.llh.View;
import com.llh.Model.SuperShape;
import com.llh.Util.EventUtil;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("static-access")
public class CenterArea {

    public static void centerArea(ScrollPane deepPane, FlowPane centerPane, AnchorPane drawingArea) {
        drawingArea.setOnMousePressed(EventUtil.cancelMark(drawingArea)); //点击空白处取消选中
        deepPane.setOnMousePressed(EventUtil.cancelMark(drawingArea));
        drawingArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if (event.getCode() == KeyCode.DELETE) {
                    List<Node> list = drawingArea.getChildren();
                    List<Node> deletelist = new ArrayList<>();
                    for(Node node:list)
                        if (((SuperShape) node).isIsSelected()) {
                            deletelist.add(node);
                    }
                    for(Node node:deletelist){
                        drawingArea.getChildren().remove(node);
                    }
                }
            }
        });






    }














}