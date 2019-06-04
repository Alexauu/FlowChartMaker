package com.llh.Model;

import java.io.Serializable;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

@SuppressWarnings({ "static-access", "serial" })

public class MyRectangle extends Rectangle implements Serializable{
	public MyRectangle() {

	}
	public MyRectangle(String kind, double x,double y){
		super(x,y,100,50);
		super.setStroke(Color.BLACK);
		super.setFill(Color.WHITE);
		if("ArcRectangle".equals(kind)){
            super.setArcHeight(60);
            super.setArcWidth(60);
        }
//		new SuperShape(kind,x,y,this,drawingArea);
	}


//	//圆角矩形 椭圆形
//	public MyRectangle(String ArcRectangle, double x, double y, AnchorPane drawingArea) {
//		this("ArcRectangle",x,y,drawingArea);
//		super.setArcHeight(60);
//        super.setArcWidth(60);
//	}




}