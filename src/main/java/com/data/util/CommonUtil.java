package com.data.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class CommonUtil {

    public static final String TOKEN = "token";

    // 图片文件地址
    private static final String IMAGE_DIR_NAME = "/images";

    public static String getAbsoluteImagePath() {
        return System.getProperty("user.dir") + IMAGE_DIR_NAME;
    }

    public static String getRelativeImagePath() {
        return IMAGE_DIR_NAME;
    }

    public static String getAbsoluteProjectPath() {
        return System.getProperty("user.dir");
    }

    //微信小程序
    // 请求的网址  session_key
    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    // 你的appid
    public static final String WX_LOFIN_APPID_NAME = "appid";
    public static final String WX_LOGIN_APPID_VALUE = "wxceed7e7e03289e18";

    //你的code
    public static final String WX_LOGIN_JS_CODE_NAME = "js_code";
    // 你的密匙
    public static final String WX_LOGIN_SECRET_NAME = "secret";
    public static final String WX_LOGIN_SECRET_VALUE = "c07deba8b78a1f800abad9cb8d430f45";
    // 固定参数
    public static final String WWX_LOGIN_GRANT_TYPE_NAME = "grant_type";
    public static final String WX_LOGIN_GRANT_TYPE_VALUE = "authorization_code";

    public static final String SANWEI_CEFUYI_TOKEN = "YYY00001-API-SANWEI";

    public static String randomStrInt(int count) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public enum ReturnCode {
        FAIL(0, "FAIL"),
        SUCCESS(1, "SUCCESS");

        private Integer code;
        private String name;

        // 构造方法
        private ReturnCode(Integer code, String name) {

            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        //覆盖方法
        @Override
        public String toString() {
            return this.code.toString() + "_" + this.name;
        }

    }

    public static int getBetweenDay(Long startTime, Long endTime) {
        if (endTime <= startTime) {
            return 0;
        }

        if (Long.valueOf(endTime - startTime).compareTo(Long.valueOf(24 * 3600 * 1000)) < 0) {
            return 1;
        }

        final BigDecimal dayDecimal = (new BigDecimal(endTime).subtract(new BigDecimal(startTime))).divide(new BigDecimal(24 * 3600 * 1000), 0);
        return dayDecimal.intValue();
    }

    private static final double EARTH_RADIUS = 6378137;//赤道半径

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double getDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return (new BigDecimal(s).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP)).doubleValue();
    }
}
