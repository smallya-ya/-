package com.ruoyi.common.utils.battle;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * WGS-84、GCJ-02、BD-09坐标系转换
 * @author hongjiasen
 */
public class GpsCoordinateUtils {

    /**
     * 圆周率
     */
    private static final double PI = 3.1415926535897932384626433832795;
    private static final double X_PI = PI * 3000.0 / 180.0;
    /**
     * 克拉索夫斯基椭球参数长半轴
     */
    private static final double A = 6378245.0;
    /**
     * 克拉索夫斯基椭球参数第一偏心率平方
     */
    private static final double EE = 0.00669342162296594323;

    /**
     * 地球半径
     */
    private static final double R = 6371e3;

    /*
     * 大地坐标系资料WGS-84 长半径a=6378137 短半径b=6356752.3142 扁率f=1/298.2572236
     */
    /** 长半径a=6378137 */
    private static final double a = 6378137;
    /** 短半径b=6356752.3142 */
    private static final double b = 6356752.3142;
    /** 扁率f=1/298.2572236 */
    private static final double f = 1 / 298.2572236;

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
     * 即 百度 转 谷歌、高德
     * @param bd_lon
     * @param bd_lat
     * @return [lat, lng]
     */
    public double[] bd09togcj02(double bd_lon, double bd_lat){
        double x = bd_lon - 0.0065;
        double y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new double[] {gg_lat, gg_lng};
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
     * 即谷歌、高德 转 百度
     * @param lng
     * @param lat
     * @returns [lat, lng]
     */
    public static double[] gcj02tobd09(double lng, double lat){
        double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * X_PI);
        double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * X_PI);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new double[] {bd_lat, bd_lng};
    }

    /**
     * WGS84转GCj02
     * @param lng
     * @param lat
     * @returns [lat, lng]
     */
    public static double[] wgs84togcj02(double lng, double lat){
        double dlat = transformlat(lng - 105.0, lat - 35.0);
        double dlng = transformlng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1 - EE * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((A * (1 - EE)) / (magic * sqrtmagic) * PI);
        dlng = (dlng * 180.0) / (A / sqrtmagic * Math.cos(radlat) * PI);
        double mglat = lat + dlat;
        double mglng = lng + dlng;
        return new double[] {mglat, mglng};
    }

    /**
     * GCJ02 转换为 WGS84
     * @param lng
     * @param lat
     * @returns [lat, lng]
     */
    public static double[] gcj02towgs84(double lng, double lat){
        double dlat = transformlat(lng - 105.0, lat - 35.0);
        double dlng = transformlng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1 - EE * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((A * (1 - EE)) / (magic * sqrtmagic) * PI);
        dlng = (dlng * 180.0) / (A / sqrtmagic * Math.cos(radlat) * PI);
        double mglat = lat + dlat;
        double mglng = lng + dlng;
        return new double[] {mglat, mglng};
    }

    /**
     * WGS84 转换为 BD-09
     * @param lng
     * @param lat
     * @returns [lat, lng]
     */
    private static double[] wgs84tobd09(double lng, double lat){
        //第一次转换
        double dlat = transformlat(lng - 105.0, lat - 35.0);
        double dlng = transformlng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1 - EE * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((A * (1 - EE)) / (magic * sqrtmagic) * PI);
        dlng = (dlng * 180.0) / (A / sqrtmagic * Math.cos(radlat) * PI);
        double mglat = lat + dlat;
        double mglng = lng + dlng;

        //第二次转换
        double z = Math.sqrt(mglng * mglng + mglat * mglat) + 0.00002 * Math.sin(mglat * X_PI);
        double theta = Math.atan2(mglat, mglng) + 0.000003 * Math.cos(mglng * X_PI);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new double[] {bd_lat, bd_lng};
    }

    private static double transformlat(double lng,double lat){
        double ret= -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformlng(double lng,double lat){
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 根据一个经纬度，角度，距离，粗略计算另一个经纬
     * 纬度：φ2 = asin( sin φ1 ⋅ cos δ + cos φ1 ⋅ sin δ ⋅ cos θ )
     * 经度：λ2 = λ1 + atan2( sin θ ⋅ sin δ ⋅ cos φ1, cos δ − sin φ1 ⋅ sin φ2 )
     * λ2结果经度
     * λ1起始经度
     * φ2结果纬度
     * φ1起始纬度
     * θ角度，从正北顺时针方向
     * R地球半径
     * δ表示角距离，即d/R
     * d距离（米）
     * @param lng 起始经度
     * @param lat 起始纬度
     * @param degree 角度
     * @param distance 距离
     * @return
     */
    public static double[] calLocationByDistanceAndDegree(double lng, double lat, double degree, double distance) {
        double δ = distance / R;
        double angle = Math.toRadians(degree);
        double srcLng = Math.toRadians(lng);
        double srcLat = Math.toRadians(lat);

        double desLat = Math.asin(Math.sin(srcLat) * Math.cos(δ) + Math.cos(srcLat) * Math.sin(δ) * Math.cos(angle));
        double desLng = srcLng + Math.atan2(Math.sin(angle) * Math.sin(δ) * Math.cos(srcLat), Math.cos(δ) - Math.sin(srcLat) * Math.sin(desLat));

        double finalLat = Math.toDegrees(desLat);
        double finalLng = Math.toDegrees(desLng);

        return new double[] {finalLat, finalLng};
    }

    /**
     * 度换成弧度
     * @param d
     * @return
     */
    public static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 弧度换成度
     * @param x
     * @return
     */
    public static double deg(double x) {
        return x * 180 / Math.PI;
    }

    /**
     * 计算两个经纬度的距离
     * 先转换为WGS84坐标再进行计算
     * @param gpsFrom
     * @param gpsTo
     * @return
     */
    public static double getDistanceMeter(GlobalCoordinates gpsFrom, GlobalCoordinates gpsTo){
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.WGS84, gpsFrom, gpsTo);
        return geoCurve.getEllipsoidalDistance();
    }
}
