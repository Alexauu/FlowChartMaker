package com.llh.Util;

import javafx.scene.shape.Circle;

public class DistanceUtil {


    public static int nearestPoint(Circle[] target, Circle sourse){
        int[] num = new int[]{4,5,7};
        int nearestNum = 2;
        double nearest = distance(sourse,target[2]);
        for(int i=0;i<=2;i++){
            double dis = distance(target[num[i]],sourse);
            if(dis <= nearest) {
                nearest = dis;
                nearestNum = num[i];
            }
        }
        return nearestNum;
    }

    public static double distance(Circle a, Circle b){
        return Math.sqrt( Math.pow((a.getCenterX()-b.getCenterX()),2) + Math.pow((a.getCenterY()-b.getCenterY()),2) );
    }
}
