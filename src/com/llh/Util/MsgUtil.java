package com.llh.Util;

public class MsgUtil {

    public static String getMsg(String kind){
        String msg = null;
        switch(kind){
            case "MyRectangle":
                msg = "直角矩形，处理框，表示算法的一个步骤、一个处理环节。";
                break;
            case "ArcRectangle":
                msg = "圆角矩形，起止框，表示算法的开始和结束。";
                break;
            case "Rhombus":
                msg = "菱形，判断框，表示算法的一个判断、一个条件";
                break;
            case "Parallelogram":
                msg = "平行四边形，输入输出框，表示算法的一个输入或一个输出。";
                break;
            case "RurveRectangle":
                msg = "单边曲面矩形，说明符号，用以绘制对某个基本图形符号的说明和注释。使用虚线将说明符号与对应的图形符号连接起来。";
                break;
            case "ConnectCircle":
                msg = "小圆形，连接符号，用于需要分页绘制时，在前一页和后一页分别绘制一个连接符号，表示将流程图的两个部分连接起来。";
                break;
            case "ArrowLine":
                msg = "箭头实线，流程方向，用以连接流程图中的符号图形，表示算法的执行流程。";
                break;
        }
        return msg;
    }
}
