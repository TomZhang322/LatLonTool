package com.adcc.util;

import cn.adcc.eagles.commons.geom.LatLon;
import cn.adcc.eagles.commons.geom.Line;
import cn.adcc.eagles.commons.geom.Vec4;
import cn.adcc.eagles.commons.geom2.GeoUtils;
import cn.adcc.eagles.commons.geom2.VecTools;
import cn.adcc.eagles.commons.globes.EarthBasic;
import cn.adcc.eagles.commons.globes.GlobeBasic;
import com.google.common.base.Strings;

import java.text.DecimalFormat;
import java.util.List;

/**
 * GIS工具类
 */
public final class GISUtil {

    // 角度转换弧度值
    private final static double DEGREES_TO_RADIANS = Math.PI / 180d;

    // 弧度转换角度值
    private final static double RADIANS_TO_DEGREES = 180d / Math.PI;

    // DecimalFormat2
    private static DecimalFormat df2 = new DecimalFormat("00");

    // DecimalFormat3
    private static DecimalFormat df3 = new DecimalFormat("000");

    // DecimalFormat4
    private static DecimalFormat df4 = new DecimalFormat("0000");

    /**
     * 拆解报文中的经纬度
     * @param value
     * @return
     * @throws Exception
     */
    public static String parseENSWValue(String value) throws Exception{
        StringBuffer sb = new StringBuffer();
        String firstChar = new String(value.substring(0, 1));
        Double dou = Double.parseDouble(value.substring(2));
        Double dou1 = Double.parseDouble(value.substring(1));
        String du =((firstChar.equals("E") || firstChar.equals("W"))) ?  df3.format(dou1.intValue()) : df2.format(dou1.intValue());
        Double tempFen = (dou - dou.intValue()) * 60;
        String fen = df2.format(tempFen.intValue());
        Double tempMi = (tempFen - tempFen.intValue()) * 60;
        String mi = df4.format(((Double) (tempMi * 100)).intValue());
        sb.append(firstChar).append(du).append(fen).append(mi);
        return  sb.toString();
    }

    /**
     * 拆解经纬度
     * @param value
     * @return
     */
    public static double parseEWNSValue2(String value) {
        String temp = value;
        String firstChar = value.substring(0, 1);
        switch(firstChar){
            case "S":
                temp = temp.replace("S", "N");
                return parseENValue2(temp);
            case "W":
                temp = temp.replace("W", "E");
                return parseENValue2(temp);
            default:
                return parseENValue2(temp);
        }
    }

    /**
     * 拆解经纬度
     * @param value
     * @return
     */
    private static double parseENValue2(String value){
        double redou = 0.0d;
        String firstChar = value.substring(0, 1);
        int step = -1;
        switch(firstChar){
            case "N":
                step = 2;
                break;
            case "E":
                step = 3;
                break;
            default:
                redou = Double.parseDouble(value);
                break;
        }
        if (step != -1) {
            String str = value.substring(1, value.length());
            Double du = Double.parseDouble(str.substring(0, step));
            Double fen = Double.parseDouble(str.substring(step, step + 2));
            Double miao = Double.parseDouble(str.substring(step + 2, step + 4) + "." + str.substring(step + 4));
            redou = (du * 3600 + fen * 60 + miao) / 3600;
        }
        return redou;
    }

    /**
     * 格式化纬度(N41405566)
     * @param lat
     * @return
     */
    public static float formatLatitudeDMS(String lat){

        // 经纬度单位符号(1为N,-1为S)
        int sign = 1;
        if(Strings.isNullOrEmpty(lat)){
            return 0.f;
        }
        if(lat.startsWith("S")){
            sign = -1;
        }
        String strDegree = lat.substring(1,3);
        String strMinute = lat.substring(3,5);
        String strSecond = lat.substring(5);
        float degree = Float.valueOf(strDegree);
        float minute = Float.valueOf(strMinute);
        float second = Float.valueOf(strSecond)/100;
        return sign * (degree + minute/60 + second/3600);
    }

    /**
     * 格式化经度(E110585386)
     * @param lon
     * @return
     */
    public static float formatLongitudeDMS(String lon){

        // 经纬度单位符号(1为E,-1为W)
        int sign = 1;
        if(Strings.isNullOrEmpty(lon)){
            return 0.f;
        }
        if(lon.startsWith("W")){
            sign = -1;
        }
        String strDegree = lon.substring(1,4);
        String strMinute = lon.substring(4,6);
        String strSecond = lon.substring(6);
        float degree = Float.valueOf(strDegree);
        float minute = Float.valueOf(strMinute);
        float second = Float.valueOf(strSecond)/100;
        return sign * (degree + minute/60 + second/3600);
    }

    /**
     * 计算两点之间的距离
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @param alt
     * @return
     */
    public static double getDistance(double lat1,double lon1,double lat2,double lon2,double alt){
        LatLon latLon1 = LatLon.fromDegrees(lat1,lon1);
        LatLon latLon2 = LatLon.fromDegrees(lat2,lon2);
        return GeoUtils.getDistanceFromLatLon(latLon1,latLon2,alt);
    }

    /**
     * 根据两点经纬度计算方位角
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    public static double getAngle(double lat1,double lon1,double lat2,double lon2) {
        if (lat1 == lat2 && lon1 == lon2){
            return 0.0f;
        }
        if (lon1 == lon2){
            return lat1 > lat2 ? 180.0f : 0.0f;
        }

        // 转成弧度
        double dLat1 = DEGREES_TO_RADIANS * lat1;
        double dLon1 = DEGREES_TO_RADIANS * lon1;
        double dLat2 = DEGREES_TO_RADIANS * lat2;
        double dLon2 = DEGREES_TO_RADIANS * lon2;
        double y = Math.cos(dLat2) * Math.sin(dLon2 - dLon1);
        double x = Math.cos(dLat1) * Math.sin(dLat2) - Math.sin(dLat1)
                * Math.cos(dLat2) * Math.cos(dLon2 - dLon1);
        double azimuthRadians = Math.atan2(y, x);
        return RADIANS_TO_DEGREES * azimuthRadians;
    }

    /**
     * 计算两点之间的直线方位角(正北为0度)
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    public static double getAngle2(double lat1,double lon1,double lat2,double lon2){
        LatLon latLon1 = LatLon.fromDegrees(lat1,lon1);
        LatLon latLon2 = LatLon.fromDegrees(lat2,lon2);
        return GeoUtils.getAngleFromLatLonPair(latLon1,latLon2).degrees;
    }

    /**
     * 取得距离最近的位置点
     * @param latLon
     * @param latLonList
     * @param alt
     * @return
     */
    public static int getNearestPosPoint(LatLon latLon,List<LatLon> latLonList,double alt){
        int index = -1;
        double distance = -1;
        if(latLonList != null && latLonList.size() > 0){
            for(int i=0;i<latLonList.size();i++){
                double tempDistance = GeoUtils.getDistanceFromLatLon(latLon,latLonList.get(i),alt);
                if(i == 0){
                    distance = tempDistance;
                    index = 0;
                }else{
                    if(tempDistance < distance){
                        distance = tempDistance;
                        index = i;
                    }
                }
            }
        }
        return index;
    }

    /**
     * 判断在指定距离包含位置点
     * @param latLon
     * @param latLonList
     * @param distance
     * @return
     */
    public static boolean isIncludePosPoint(LatLon latLon,List<LatLon> latLonList, double distance) {
        GlobeBasic globeBasic = new EarthBasic();
        Vec4 vec = globeBasic.computePointFromLocation(latLon);

        // 以PosPoint为中心，将其他点创建为
        for (int i = 0; i < latLonList.size() - 1; i++) {
            LatLon latLonStart = latLonList.get(i);
            LatLon latLonEnd = latLonList.get(i + 1);

            // 计算这俩个点的Vec值
            Vec4 vec1 = globeBasic.computePointFromLocation(latLonStart);
            Vec4 vec2 = globeBasic.computePointFromLocation(latLonEnd);
            Line line = Line.fromSegment(vec1, vec2);
            if (VecTools.isPtBetweenSeg(vec, vec1, vec2)) {
                double dis = line.distanceTo(vec);
                if (dis < distance) {
                    return true;
                }
            }else{
                if(GeoUtils.getDistanceFromLatLon(latLon,latLonStart,0.0) < distance){
                    return true;
                }
                if(GeoUtils.getDistanceFromLatLon(latLon,latLonEnd,0.0) < distance){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断在两点间包含位置点
     * @param latLon
     * @param latLon1
     * @param latLon2
     * @return
     */
    public static boolean isIncludePosPoint(LatLon latLon, LatLon latLon1, LatLon latLon2) {
        GlobeBasic globeBasic = new EarthBasic();
        Vec4 vec = globeBasic.computePointFromLocation(latLon);
        Vec4 vec1 = globeBasic.computePointFromLocation(latLon1);
        Vec4 vec2 = globeBasic.computePointFromLocation(latLon2);
        return VecTools.isPtBetweenSeg(vec, vec1, vec2);
    }
}
