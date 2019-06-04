package com.llh.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.llh.FileManager.NewChart;
import com.llh.FileManager.OpenChart;
import com.llh.FileManager.SaveChart;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.swing.*;

public class TopArea{
	
	public static void topArea(MenuBar menuBar, AnchorPane drawingArea) {
		
		
		Menu menuFile = new Menu("菜单");
		MenuItem newItem = new  MenuItem("新建", new ImageView(TopArea.class.getResource("/icon/new.png").toExternalForm()));
		newItem.setAccelerator(KeyCombination.valueOf("ctrl+N"));
		MenuItem saveItem = new MenuItem("保存",new ImageView(TopArea.class.getResource("/icon/save.png").toExternalForm()));
		saveItem.setAccelerator(KeyCombination.valueOf("ctrl+S"));
		MenuItem openItem = new MenuItem("打开",new ImageView(TopArea.class.getResource("/icon/open.png").toExternalForm()));
		openItem.setAccelerator(KeyCombination.valueOf("ctrl+O"));

		SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
		menuFile.getItems().addAll(newItem,saveItem,openItem,separatorMenuItem);
		Menu menuFomat = new Menu("格式");
		MenuItem superStar = new MenuItem("会员版", new ImageView(TopArea.class.getResource("/icon/superStar.png").toExternalForm()));
		menuFomat.getItems().add(superStar);
		Menu menuHelp  = new Menu("帮助");
		MenuItem helpItem = new MenuItem("注意事项", new ImageView(TopArea.class.getResource("/icon/help.png").toExternalForm()));
		helpItem.setAccelerator(KeyCombination.valueOf("ctrl+H"));
		menuHelp.getItems().add(helpItem);
		
		MenuItem exportItem = new MenuItem("导出图片", new ImageView(TopArea.class.getResource("/icon/export.png").toExternalForm()));
		exportItem.setAccelerator(KeyCombination.valueOf("ctrl+E"));
		menuFile.getItems().add(exportItem);
		menuBar.getMenus().addAll(menuFile,menuFomat,menuHelp);
		
		superStar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Dialog<ButtonType> dialog = new Dialog<ButtonType>();
				dialog.setTitle("哈哈哈~");
				dialog.setHeaderText("嘻嘻嘻~");
				dialog.setContentText("未完待续......");
//				 Get the Stage.
                Stage tmpStage = (Stage) dialog.getDialogPane().getScene().getWindow();
//              Add a custom icon.
                tmpStage.getIcons().add(new Image(TopArea.class.getResource("/icon/superStar.png").toExternalForm()));
                dialog.getDialogPane().setPrefSize(200, 100);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                Button ok = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
                dialog.show();
                ok.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						dialog.close();
					}
				});
			}
		});
		
		//新建
		newItem.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Dialog<ButtonType> dialog = alertDialog(drawingArea/*, true*/);
				if(dialog == null) {
					NewChart.newChart(drawingArea);
				}
				else {
					dialog.setTitle("流程图新建");
					dialog.show();
				}
			}
		});
		
		//导出单击事件
		exportItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				SaveChart.exportPic();
			}
		});
		//保存单击事件
		saveItem.setOnAction(new EventHandler<ActionEvent>() {			
			
			@Override
			public void handle(ActionEvent event) {
				saveMethod();
			}
		});
		
		//打开单击事件
		openItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

					List<Node> list = drawingArea.getChildren();
					if(list.size() > 0) {
						Dialog<ButtonType> dialog = new Dialog<ButtonType>();
						dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
						dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);
						dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
						
						Button ok = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
						ok.setText("是");
						Button no = (Button) dialog.getDialogPane().lookupButton(ButtonType.NO);
						no.setText("否");
						Button cancel = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
						cancel.setText("继续绘制");
						dialog.setContentText("是否保存正在绘制的流程图到文件里面？");
						dialog.show();
						ok.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								saveMethod();
								drawingArea.getChildren().clear();
								drawingArea.setStyle("-fx-background-color:#EAEAEA");
								try {
									File file = openMethod();
									if(file != null)
									OpenChart.openChart(file,drawingArea);
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						});

						cancel.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
							}
						});
						
						no.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent arg0) {
//								if(flag == true)
								try {
									drawingArea.getChildren().clear();
									drawingArea.setStyle("-fx-background-color:#EAEAEA");
									File file = openMethod();
									if(file != null) {
										OpenChart.openChart(file,drawingArea);
									}
									
								} catch (ClassNotFoundException | IOException e) {
									e.printStackTrace();
								}
//								}
							}
						});
					}
					else {
						try {
							
							File file = openMethod();
							if(file != null) {
								OpenChart.openChart(file,drawingArea);
							}
						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
						}
					}
			}
		});
		
		//帮助单击事件	
		helpItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Dialog<ButtonType> dialog = new Dialog<ButtonType>();
				dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
				dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);
				
				Button ok = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
				Button no = (Button) dialog.getDialogPane().lookupButton(ButtonType.NO);
				ok.setText("已支付完成，进入下一页");
				no.setText("残忍拒绝");
				dialog.setTitle("为我们充电");

                // Get the Stage.
                Stage tmpStage = (Stage) dialog.getDialogPane().getScene().getWindow();

//              Add a custom icon.
                tmpStage.getIcons().add(new Image(TopArea.class.getResource("/icon/open.png").toExternalForm()));
				dialog.setHeaderText("点击右边按钮生成二维码");
				Button button = new Button("生成二维码");
				ImageView imageView = new ImageView(TopArea.class.getResource("/image/payQR.png").toExternalForm());
				imageView.setFitWidth(200);
				imageView.setFitHeight(200);
				button.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						dialog.setGraphic(imageView);
						button.setText(null);
						dialog.setHeaderText("觉得好用就打开支付宝扫一扫支持我们吧");
					}
				});
				dialog.setGraphic(button);
				dialog.getDialogPane().setPrefSize(400, 350);

				Optional<ButtonType> optional = dialog.showAndWait();
				optional.ifPresent(new Consumer<ButtonType>() {

					@Override
					public void accept(ButtonType buttonType) {
						if(buttonType.getButtonData() == ButtonData.OK_DONE) {
							button.setVisible(false);
							imageView.setVisible(false);
							imageView.setFitWidth(1);
							imageView.setFitHeight(1);
							ok.setVisible(false);
							no.setVisible(false);
							dialog.setTitle("谢谢您");
							dialog.setHeaderText("本流程图绘制软件将竭力为你提供优质服务，使用本软件，您可以随心所欲的构造你心中的流程图。流程图左边，" +
                                    "我们为您提供一系列基本图形，您可以拖拽他们，点击他们来新建一个全新图形。" +
                                    "右边，我们提供专业调色板，您可以所以设置心仪的颜色，还可以点击按钮将图形设置为虚线。" +
                                    "上边，我们有萌萌哒菜单栏，您可以保存或者导入自己文件，这样就不用害怕突然死机了！~");
							dialog.setContentText(null);
//							dialog.setContentText("此软件为程序流程图绘制软件，左边为图形符号选择区，工具栏可实现新建，保存，打开，导出图形文件的功能");
						}
						else {
							imageView.setVisible(false);
							imageView.setFitWidth(1);
							imageView.setFitHeight(1);
							button.setVisible(false);
							ok.setVisible(false);
							no.setVisible(false);
							dialog.setTitle("帮助");
							dialog.setHeaderText("本流程图绘制软件将竭力为你提供优质服务，使用本软件，您可以随心所欲的构造你心中的流程图。流程图左边，" +
                                    "我们为您提供一系列基本图形，您可以拖拽他们，点击他们来新建一个全新图形。" +
                                    "右边，我们提供专业调色板，您可以所以设置心仪的颜色，还可以点击按钮将图形设置为虚线。" +
                                    "上边，我们有萌萌哒菜单栏，您可以保存或者导入自己文件，这样就不用害怕突然死机了！~");
							dialog.setContentText(null);
//							dialog.setContentText("此软件为程序流程图绘制软件，左边为图形符号选择区，工具栏可实现新建，保存，打开，导出图形文件的功能");
						}
					}
				});
				
				dialog.getDialogPane().setPrefSize(400, 300);

				dialog.show();
			}
		});
		
		
	}
	
	public static File openMethod() {
		Stage stage = new Stage();
		
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.setTitle("打开");

        File f = new File("D:\\FlowChartData");
        if(!f.exists()) f.mkdir();
		fileChooser.setInitialDirectory(new File("D:" + File.separator + "FlowChartData"));
		
		fileChooser.getExtensionFilters().add(new ExtensionFilter("文本类型", "*.chart"));	
		
		File file = fileChooser.showOpenDialog(stage);
		if(file == null ) {
			return null;
		}
		return file;
	}
	
	public static void saveMethod() {
		Stage stage = new Stage();
		
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.setTitle("保存");
		
		fileChooser.setInitialFileName("新建流程图");
		
		fileChooser.setInitialDirectory(new File("D:" + File.separator + "FlowChartData"));
		
		fileChooser.getExtensionFilters().add(new ExtensionFilter("文本类型", "*.chart"));

		File file = fileChooser.showSaveDialog(stage);
		if(file == null ) {
			return;
		}
		

		try {
			SaveChart.saveChart(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Dialog<ButtonType> alertDialog(AnchorPane drawingArea/*, boolean flag*/) {
		List<Node> list = drawingArea.getChildren();
		if(list.size() > 0) {
			Dialog<ButtonType> dialog = new Dialog<ButtonType>();
			dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
			dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);
			dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
			
			Button ok = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
			ok.setText("是");
			Button no = (Button) dialog.getDialogPane().lookupButton(ButtonType.NO);
			no.setText("否");
			Button cancel = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
			cancel.setText("继续绘制");
			
			ok.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					saveMethod();
					NewChart.newChart(drawingArea);
				}
			});

			cancel.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
				}
			});
			
			no.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
//					if(flag == true)
					NewChart.newChart(drawingArea);
//					else {
//						dialog.close();
//					}
				}
			});
			
//			dialog.setTitle("流程图新建");
			dialog.setContentText("是否保存正在绘制的流程图到文件里面？");
			return dialog;
//			dialog.show();
		}		
		else {
			return null;
		}
	}
}
