package com.llh.Util;

import com.llh.Model.*;
import javafx.scene.Node;
import javafx.scene.shape.Shape;

public class NewUtil {

    public static Shape produceShape(String kind, double x, double y) {
        Node newShape = null;
        switch (kind) {
            case "MyRectangle":
                newShape = new MyRectangle("MyRectangle",x, y);
                break;
            case "ArcRectangle":
                newShape = new ArcRectangle(x, y);
                break;
            case "Rhombus":
                newShape = new Rhombus(x, y);
                break;
            case "Parallelogram":
                newShape = new Parallelogram(x,y);
                break;
            case "RurveRectangle":
                newShape = new RurveRectangle(x,y).getShape();
                break;
            case "ConnectCircle":
                newShape = new ConnectCircle(x,y);
                break;
        }
        return (Shape)newShape;
    }
}
