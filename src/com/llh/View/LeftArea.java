package com.llh.View;


import com.llh.Model.*;
import com.llh.Util.BorderUtil;
import com.llh.Util.EventUtil;
import com.llh.Util.MsgUtil;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


@SuppressWarnings("static-access")
public class LeftArea {
	static double relativeX;
	static double relativeY;
	static boolean isSingleClick = false;
	static String buildKind = null;

	@SuppressWarnings("static-access")
	public static void leftArea(VBox leftPane, AnchorPane drawingArea) {
		pictureDrawing(leftPane, drawingArea);
		isSelectedListen(leftPane, drawingArea);
//		newRec(leftPane, drawingArea);
	}

	// 左侧选择栏显示图形
	private static void pictureDrawing(VBox leftPane, AnchorPane drawingArea) {


        //不应该使用在类路径上加载资源。直接使用getResource（）加载资源
		ImageView MyRectangleImageView = new ImageView(LeftArea.class.getResource("/image/MyRectangle.png").toExternalForm());
        newShape(MyRectangleImageView, drawingArea, "MyRectangle");

        ImageView ArcRectangleImageView = new ImageView(LeftArea.class.getResource("/image/ArcRectangle.png").toExternalForm());
        newShape(ArcRectangleImageView, drawingArea, "ArcRectangle");

        ImageView RhombusImageView = new ImageView(LeftArea.class.getResource("/image/Rhombus.png").toExternalForm());
        newShape(RhombusImageView, drawingArea, "Rhombus");

        ImageView ParallelogramImageView = new ImageView(LeftArea.class.getResource("/image/Parallelogram.png").toExternalForm());
        newShape(ParallelogramImageView, drawingArea, "Parallelogram");

        ImageView RurveRectangleImageView = new ImageView(LeftArea.class.getResource("/image/RurveRectangle.png").toExternalForm());
        newShape(RurveRectangleImageView, drawingArea, "RurveRectangle");

        ImageView ConnectCircleImageView = new ImageView(LeftArea.class.getResource("/image/ConnectCircle.png").toExternalForm());
        newShape(ConnectCircleImageView, drawingArea, "ConnectCircle");

        ImageView ArrowLineImageView = new ImageView(LeftArea.class.getResource("/image/ArrowLine.png").toExternalForm());
        newShape(ArrowLineImageView, drawingArea, "ArrowLine");


        //加入左边选择栏
		leftPane.getChildren().addAll(ArcRectangleImageView, MyRectangleImageView,
                RhombusImageView,ParallelogramImageView,ArrowLineImageView,ConnectCircleImageView,RurveRectangleImageView);

		leftPane.setAlignment(Pos.TOP_CENTER);
		leftPane.setMargin(ArcRectangleImageView, new Insets(20.0));
		leftPane.setMargin(RhombusImageView, new Insets(20.0));
		leftPane.setMargin(ArrowLineImageView, new Insets(20.0));
		leftPane.setMargin(RurveRectangleImageView, new Insets(20.0));
	}

	// 图形制作
	public static Node produceShape(String kind, double x, double y, AnchorPane drawingArea) {
		Node newShape = null;
		switch (kind) {
			case "MyRectangle":
            case "ArcRectangle":
            case "Rhombus":
            case "Parallelogram":
            case "RurveRectangle":
            case "ConnectCircle":
                newShape = new SuperShape(kind,x, y, drawingArea);
				break;
            case "ArrowLine":
                newShape = new ArrowLine(x+50,y+10,drawingArea);
                break;
		}
		return newShape;
	}

    // 新建图形
    public static void newShape(ImageView shapeImageView, AnchorPane drawingArea, String kind) {

            shapeImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    isSingleClick = false;
                    //双击新建
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        isSingleClick = false;
                        produceShape(kind, 250, 375, drawingArea);
                    }
                    //单击新建
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                        isSingleClick = true;
                        drawingArea.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1 && isSingleClick == true) {
                                    double currectX = event.getX() - 50 ;
                                    double currectY = event.getY() - 25 ;
                                    String[] xAndy = (BorderUtil.newBorderJudge(currectX, currectY, drawingArea)).split(":");
                                    currectY = Double.valueOf(xAndy[1]);
                                    currectX = Double.valueOf(xAndy[0]);
                                    produceShape(kind, currectX, currectY, drawingArea);
                                    isSingleClick = false;
                                } else if (event.getButton() == MouseButton.SECONDARY) isSingleClick = false;
                            }
                        });
                    }
                }
            });

            //拖拽新建（以下三个类）
       shapeImageView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                shapeImageView.setCursor(Cursor.CLOSED_HAND);
                isSingleClick = false;
            }
        });
        shapeImageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                RightArea.setIntroduction(MsgUtil.getMsg(kind));
                shapeImageView.setCursor(Cursor.OPEN_HAND);
            }
        });
        shapeImageView.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                RightArea.setIntroduction("");
            }
        });
            shapeImageView.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    isSingleClick = false;
                    Dragboard dragboard = shapeImageView.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent clipboardContent = new ClipboardContent();
                    clipboardContent.putString("shape");
                    dragboard.setContent(clipboardContent);
                    dragboard.setDragView(new Image("/image/" + kind + ".png"));
                    buildKind = kind;
                    dragboard.setDragViewOffsetX((relativeX = event.getSceneX() - shapeImageView.getLayoutX())); //鼠标指针矫正
                    dragboard.setDragViewOffsetY((relativeY = event.getSceneY() - shapeImageView.getLayoutY() - 20));
                }
            });

            drawingArea.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            });

            drawingArea.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    event.consume();
                    Dragboard dragboard = event.getDragboard();
                   String msg = dragboard.getString();
                    if("shape".equals(msg)){
                    double currectX = event.getX() - relativeX ; //151  以画布左上角为原点
                    double currectY = event.getY() - relativeY;
                    //越界判定及自动调整
                    String[] xAndy = (BorderUtil.newBorderJudge(currectX, currectY, drawingArea)).split(":");
                    currectX = Double.valueOf(xAndy[0]);
                    currectY = Double.valueOf(xAndy[1]);
                    produceShape(buildKind, currectX, currectY, drawingArea);
                    }
                }
            });
        }

    private static void isSelectedListen(VBox leftPane, AnchorPane drawingArea){
	    leftPane.setOnMousePressed(EventUtil.cancelMark(drawingArea));
    }
}