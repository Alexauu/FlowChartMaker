package com.llh.View;



import com.llh.FileManager.SaveChart;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class Layout {

	@SuppressWarnings("static-access")
	public static AnchorPane layout(BorderPane bor) {
		
		MenuBar menuBar = new MenuBar();
		menuBar.setPrefHeight(20);
		
		VBox leftPane = new VBox(); //左
		leftPane.setMaxWidth(100);

		VBox rightPane = new VBox(); //右
		rightPane.setPrefWidth(100);

		FlowPane centerPane = new FlowPane();
		ScrollPane deepPane = new ScrollPane(centerPane);//中间底层
        AnchorPane drawingArea = new AnchorPane();
		drawingArea.setPrefSize(600, 800);
		drawingArea.setStyle("-fx-background-color:#EAEAEA");
		centerPane.getChildren().addAll(drawingArea);
        new SaveChart(centerPane,leftPane,drawingArea);
		bor.setTop(menuBar);
		bor.setLeft(leftPane);
		bor.setRight(rightPane);
		bor.setCenter(deepPane);
		
		LeftArea.leftArea(leftPane, drawingArea);
		TopArea.topArea(menuBar,drawingArea);
		CenterArea.centerArea(deepPane,centerPane,drawingArea);
		RightArea.rightArea(drawingArea,rightPane);
		
		return drawingArea;
	}
	
	

}
