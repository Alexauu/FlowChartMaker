package com.llh.Util;

import com.llh.Model.SuperShape;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class EventUtil {

    public static EventHandler<MouseEvent > cancelMark(AnchorPane drawingArea){
        return new EventHandler<MouseEvent >() {
            @Override
            public void handle(MouseEvent event) {
                List<Node> nList = drawingArea.getChildren();
                drawingArea.requestFocus();
                for(Node s:nList){
                    ((SuperShape)s).setIsSelected(false);
                    ((SuperShape)s).getTextArea().setManaged(false);
                    ((SuperShape)s).getTextArea().setVisible(false); //必须
                    ((SuperShape)s).getLabel().setText(((SuperShape)s).getTextArea().getText());
                }
            }
        };
    }
}
