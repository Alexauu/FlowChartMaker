package com.llh.View;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane borderPane = new BorderPane();
		Scene scence = new Scene(borderPane);
		primaryStage.setScene(scence);
		primaryStage.setMinHeight(800);
		primaryStage.setMinWidth(910);
		primaryStage.setHeight(800);
		primaryStage.setWidth(1024);
		primaryStage.getIcons().add(new Image(Main.class.getResource("/icon/Mainicon.png").toExternalForm())); //设置图标
		primaryStage.setTitle("流程图绘制"); //设置标题
		AnchorPane anchorPane = Layout.layout(borderPane); //显示布局
		primaryStage.show();
		
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"是否保存后退出？",new ButtonType("取消", ButtonBar.ButtonData.NO),
		                new ButtonType("确定", ButtonBar.ButtonData.YES), new ButtonType("继续绘制", ButtonBar.ButtonData.CANCEL_CLOSE));
//		        设置窗口的标题
		        alert.setTitle("确认");
//		        alert.setHeaderText("关闭");
		        alert.setHeaderText(null);
//		        设置对话框的 icon 图标，参数是主窗口的 stage
		        alert.initOwner(primaryStage);	
		        List<Node> list = anchorPane.getChildren();
		        if(list.size() > 0) {
//			        showAndWait() 将在对话框消失以前不会执行之后的代码
			       Optional<ButtonType> _buttonType = alert.showAndWait();
//				        根据点击结果返回
				   if(_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)){
				          TopArea.saveMethod();
				   }
				   else if(_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.NO)){
				            
				   }
				   else {
					   event.consume();
					}
			        
		        }
			}
		});
	}
}
