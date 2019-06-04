package com.llh.Util;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Shape;


public class UpdateUtil {

    public static void updateLabel(Label label, Shape shape){
        Bounds bounds = shape.getBoundsInParent();
        label.setLayoutX(bounds.getMinX());
        label.setLayoutY(bounds.getMinY());
        label.setPrefSize(bounds.getWidth(), bounds.getHeight());
}

    public static void updateTextArea(TextArea textArea, Shape shape){
        Bounds bounds = shape.getBoundsInParent();
        textArea.setManaged(true);
        textArea.setLayoutX(bounds.getMinX()+2);
        textArea.setLayoutY(bounds.getMinY()+2);
        textArea.setPrefSize(bounds.getWidth()-4, bounds.getHeight()-4);
    }


}
