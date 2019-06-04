package com.llh.FileManager;

import com.llh.Model.ArrowLine;
import com.llh.Model.SuperShape;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class SaveChart implements Serializable{
	private static FlowPane flowPane;
	private  static VBox leftPane;
	private static AnchorPane drawingArea;
	
	public SaveChart(FlowPane flowPane, VBox vBox, AnchorPane anchorPane) {
	    this.flowPane = flowPane;
	    this.leftPane = vBox;
	    this.drawingArea = anchorPane;
	}
	
	public static void saveChart(File file) throws FileNotFoundException, IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        List<Node> list = drawingArea.getChildren();
        String[]  saveShape = new String[list.size()];
        for(int i=0;i<list.size();i++){
            if(list.get(i) instanceof ArrowLine) {
                saveShape[i] = ((ArrowLine) list.get(i)).toString();
            }else saveShape[i] = ((SuperShape) list.get(i)).toString();
        }
		objectOutputStream.writeObject(saveShape);
		objectOutputStream.close();
	}

	public static void exportPic(){
            int width =  (int)drawingArea.getPrefWidth();
            int height =  (int)drawingArea.getPrefHeight();
            WritableImage writableImage = new WritableImage(width, height);
            drawingArea.snapshot(new SnapshotParameters(), writableImage);
            File file = exportPicStage();
            if(file == null) {
            	return;
            }
            String[] fileName =  file.getName().split("\\.");
            int Index = fileName.length-1;
            String kind = fileName[Index];
            try {
            	switch (kind) {
				case "jpg":
					ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "jpg", file);
					BufferedImage bufferedImage = ImageIO.read(file);
					BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
					newBufferedImage.getGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE,null);
					ImageIO.write(newBufferedImage, "jpg", file);
					break;
				case "png":
//					File file = exportPicStage();
					ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
					break;
				default:
					System.out.println("出错啦~~~~~虽然不可能");
					break;
				}
            } catch (IOException e) {
                System.out.println("有异常？？？？");
                e.printStackTrace();
            }
    }
	
	public static File exportPicStage() {
		Stage stage = new Stage();
		
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.setTitle("保存图片");
		
		fileChooser.setInitialFileName("请输入图片的名称");

		File f = new File("D:\\FlowChartData");
		if(!f.exists()) f.mkdir();
		fileChooser.setInitialDirectory(new File("D:" + File.separator + "FlowChartData"));

		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("图片类型", "*.jpg"), new ExtensionFilter("图片类型", "*.png"));

		File file = fileChooser.showSaveDialog(stage);
		if(file == null ) {
			return null;
		}
		return file;
	}
}
