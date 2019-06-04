package com.llh.FileManager;

import com.llh.Util.EventUtil;
import com.llh.View.LeftArea;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class NewChart {


	public static void newChart(AnchorPane drawingArea) {
        drawingArea.getChildren().clear();
        drawingArea.setPrefSize(600,800);
    }

}	
