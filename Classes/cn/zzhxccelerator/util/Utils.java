package cn.zzhxccelerator.util;

import java.io.File;
import java.util.StringJoiner;

public class Utils {
    /**
     * calculate the distance with two points.
     *
     * @param p1 point1
     * @param p2 point2
     * @return the distance between two points
     */
    public static double distance(Pos p1, Pos p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    /**
     * calculate the average of the given values
     *
     * @param values an array of values
     * @return the average of the given values
     */
    public static double average(double... values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    /**
     * calculate the slope of the line with two points.
     *
     * @param p1 point1
     * @param p2 point2
     * @return the slope of the line with two points
     */
    public static double slope(Pos p1, Pos p2) {
        return (p2.y - p1.y) / (p2.x - p1.x);
    }


    /**
     * calculate the quadratic function with three points.
     * y = ax^2 + bx + c
     *
     * @param p1 point1
     * @param p2 point2
     * @param p3 point3
     * @return 'a' of the quadratic function
     */
    public static double getQuadraticA(Pos p1, Pos p2, Pos p3) {
        double temp1 = (p2.y - p1.y) / (p2.x - p1.x);
        double temp2 = (p3.y - p1.y) / (p3.x - p1.x);
        return (temp1 - temp2) / (p2.x - p3.x);
    }

    /**
     * combine the path of given paths, using {@link File#separator} to separate them.
     */
    public static String getPath(String... paths) {
        StringJoiner sb = new StringJoiner(File.separator);
        for (String path : paths) {
            sb.add(path);
        }
        return sb.toString();
    }
}
