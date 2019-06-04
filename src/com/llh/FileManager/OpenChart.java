package com.llh.FileManager;

import com.llh.Model.ArrowLine;
import com.llh.Model.SuperShape;
import com.llh.View.LeftArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class OpenChart {


	public static void openChart(File file, AnchorPane drawingArea) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));

        String[] nodeList = (String[])objectInputStream.readObject();
        shapeFactory(nodeList,drawingArea);
	}

	public static void shapeFactory(String[] shapeCode,AnchorPane drawingArea){
	    double size = shapeCode.length;
        SuperShape shape = null;
        String[] data;
	    int i,j;
	    for(i=0;i<size;i++) {
	        data = shapeCode[i].split(",");
	        double length = data.length;
	        String type = data[0];
	        if(!"ArrowLine".equals(type)){
                shape = (SuperShape) LeftArea.produceShape(type,Double.valueOf(data[1]),
                        Double.valueOf(data[2]),drawingArea);
                shape.setShapeFill(Paint.valueOf(data[3]));
                shape.setShapeTranslateX(Double.valueOf(data[4]));
                shape.setShapeTranslateY(Double.valueOf(data[5]));
                shape.setLabel(data[6]);
                shape.setShapeScale(Double.valueOf(data[7]),Double.valueOf(data[8]),Double.valueOf(data[9]),
                        Double.valueOf(data[10]));
                shape.setLineDotted(Boolean.valueOf(data[11]));
                shape.setIsSelected(false);
                shape.refreshPoints();
            }
	        else{
	            shape = (SuperShape) LeftArea.produceShape(type,0,0,drawingArea);
                ((ArrowLine)shape).setLinePoints(Double.valueOf(data[1]),Double.valueOf(data[2]),Double.valueOf(data[3]),Double.valueOf(data[4]),
                        Double.valueOf(data[5]),Double.valueOf(data[6]),Double.valueOf(data[7]),Double.valueOf(data[8]));
                ((ArrowLine)shape).setPointStatus(Integer.valueOf(data[9]), Integer.valueOf(data[10]));
                ((ArrowLine)shape).setLineLabel(data[11],Double.valueOf(data[12]),Double.valueOf(data[13]));
                ((ArrowLine)shape).setLineDotted(Boolean.valueOf(data[14]));
                ((ArrowLine)shape).refreshLine();
                shape.setIsSelected(false);
            } //线条设置属性部分
        }//大循环


    }











}
