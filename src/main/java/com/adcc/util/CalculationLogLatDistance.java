package com.adcc.util;

/**
 *
 * 计算经纬度、距离、方位角
 *
 * @author lillian.he
 * @time 2016-06-02
 * */
public class CalculationLogLatDistance {
    /**
     * 地球赤道半径(km)
     * */
    public final static double EARTH_RADIUS = 6378.137;
    /**
     * 地球每度的弧长(km)
     * */
    public final static double EARTH_ARC = 111.199;

    /**
     * 转化为弧度(rad)
     * */
    public static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 求两经纬度距离
     *
     * @param lon1
     *            第一点的经度
     * @param lat1
     *            第一点的纬度
     * @param lon2
     *            第二点的经度
     * @param lat2
     *            第二点的纬度
     * @return 两点距离，单位km
     * */
    public static double GetDistanceOne(double lon1, double lat1, double lon2,
                                        double lat2) {
        double r1 = rad(lat1);
        double r2 = rad(lon1);
        double a = rad(lat2);
        double b = rad(lon2);
        double s = Math.acos(Math.cos(r1) * Math.cos(a) * Math.cos(r2 - b)
                + Math.sin(r1) * Math.sin(a))
                * EARTH_RADIUS;
        return s;
    }

    /**
     * 求两经纬度距离(google maps源码中)
     *
     * @param lon1
     *            第一点的经度
     * @param lat1
     *            第一点的纬度
     * @param lon2
     *            第二点的经度
     * @param lat2
     *            第二点的纬度
     * @return 两点距离，单位km
     * */
    public static double GetDistanceTwo(double lon1, double lat1, double lon2,
                                        double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;
    }

    /**
     * 求两经纬度距离
     *
     * @param lon1
     *            第一点的经度
     * @param lat1
     *            第一点的纬度
     * @param lon2
     *            第二点的经度
     * @param lat2
     *            第二点的纬度
     * @return 两点距离，单位km
     * */
    public static double GetDistanceThree(double lon1, double lat1,
                                          double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double radLon1 = rad(lon1);
        double radLon2 = rad(lon2);
        if (radLat1 < 0)
            radLat1 = Math.PI / 2 + Math.abs(radLat1);// south
        if (radLat1 > 0)
            radLat1 = Math.PI / 2 - Math.abs(radLat1);// north
        if (radLon1 < 0)
            radLon1 = Math.PI * 2 - Math.abs(radLon1);// west
        if (radLat2 < 0)
            radLat2 = Math.PI / 2 + Math.abs(radLat2);// south
        if (radLat2 > 0)
            radLat2 = Math.PI / 2 - Math.abs(radLat2);// north
        if (radLon2 < 0)
            radLon2 = Math.PI * 2 - Math.abs(radLon2);// west
        double x1 = Math.cos(radLon1) * Math.sin(radLat1);
        double y1 = Math.sin(radLon1) * Math.sin(radLat1);
        double z1 = Math.cos(radLat1);

        double x2 = Math.cos(radLon2) * Math.sin(radLat2);
        double y2 = Math.sin(radLon2) * Math.sin(radLat2);
        double z2 = Math.cos(radLat2);

        double d = Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)
                + Math.pow((z1 - z2), 2);
        // // 余弦定理求夹角
        // double theta = Math.acos((2 - d) / 2);

        d = Math.pow(EARTH_RADIUS, 2) * d;
        // //余弦定理求夹角
        double theta = Math.acos((2 * Math.pow(EARTH_RADIUS, 2) - d)
                / (2 * Math.pow(EARTH_RADIUS, 2)));

        double dist = theta * EARTH_RADIUS;
        return dist;
    }

    /**
     * 求两经纬度方向角
     *
     * @param lon1
     *            第一点的经度
     * @param lat1
     *            第一点的纬度
     * @param lon2
     *            第二点的经度
     * @param lat2
     *            第二点的纬度
     * @return 方位角，角度（单位：°）
     * */
    public static double GetAzimuth(double lon1, double lat1, double lon2,
                                    double lat2) {
        lat1 = rad(lat1);
        lat2 = rad(lat2);
        lon1 = rad(lon1);
        lon2 = rad(lon2);
        double azimuth = Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)
                * Math.cos(lat2) * Math.cos(lon2 - lon1);
        azimuth = Math.sqrt(1 - azimuth * azimuth);
        azimuth = Math.cos(lat2) * Math.sin(lon2 - lon1) / azimuth;
        azimuth = Math.asin(azimuth) * 180 / Math.PI;
        if (Double.isNaN(azimuth)) {
            if (lon1 < lon2) {
                azimuth = 90.0;
            } else {
                azimuth = 270.0;
            }
        }
        return azimuth;
    }

    /**
     * 已知一点经纬度A，和与另一点B的距离和方位角，求B的经纬度(计算结果有误)
     *
     * @param lon1
     *            A的经度
     * @param lat1
     *            A的纬度
     * @param distance
     *            AB距离（单位：米）
     * @param azimuth
     *            AB方位角
     * @return B的经纬度
     * */
    public static String GetOtherPoint(double lon1, double lat1,
                                       double distance, double azimuth) {
        azimuth = rad(azimuth);
        double ab = distance / EARTH_ARC;// AB间弧线长
        ab = rad(ab);
        double Lat = Math.asin(Math.sin(lat1) * Math.cos(ab) + Math.cos(lat1)
                * Math.sin(ab) * Math.cos(azimuth));
        double Lon = lon1
                + Math.asin(Math.sin(azimuth) * Math.sin(ab) / Math.cos(Lat));
        System.out.println(Lon + "," + Lat);

        double a = Math.acos(Math.cos(90 - lon1) * Math.cos(ab)
                + Math.sin(90 - lon1) * Math.sin(ab) * Math.cos(azimuth));
        double C = Math.asin(Math.sin(ab) * Math.sin(azimuth) / Math.sin(a));
        System.out.println("c=" + C);
        double lon2 = lon1 + C;
        double lat2 = 90 - a;
        return lon2 + "," + lat2;
    }

    /**
     * 已知一点经纬度A，和与另一点B的距离和方位角，求B的经纬度
     *
     * @param lng1
     *            A的经度
     * @param lat1
     *            A的纬度
     * @param distance
     *            AB距离（单位：米）
     * @param azimuth
     *            AB方位角
     * @return B的经纬度
     * */
    public static String ConvertDistanceToLogLat(double lng1, double lat1,
                                                 double distance, double azimuth) {
        azimuth = rad(azimuth);
        // 将距离转换成经度的计算公式
        double lon = lng1 + (distance * Math.sin(azimuth))
                / (EARTH_ARC * Math.cos(rad(lat1)));
        // 将距离转换成纬度的计算公式
        double lat = lat1 + (distance * Math.cos(azimuth)) / EARTH_ARC;
        return lon + "," + lat;
    }

    public static void main(String[] args) {
        double lon1 = 121.469156;
        double lat1 = 31.232307;
        double lon2 = 121.469156;
        double lat2 = 31.233205;
        double distance = GetDistanceTwo(lon1, lat1, lon2, lat2);
        double azimuth = GetAzimuth(lon1, lat1, lon2, lat2);
        System.out.println("经纬度为(" + lon1 + "," + lat1 + ")的点与经纬度为(" + lon2
                + "," + lat2 + ")相距：" + distance + "千米," + "方位角：" + azimuth
                + "°");
        System.out.println("距经纬度为(" + lon1 + "," + lat1 + ")的点" + distance
                + "千米,方位角为" + azimuth + "°的另一点经纬度为("
                + ConvertDistanceToLogLat(lon1, lat1, distance, azimuth) + ")");
    }
}