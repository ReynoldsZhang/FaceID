package cn.zzhxccelerator.util;

import java.io.File;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Function;

public class Utils {
    public static double distance(Pos p1, Pos p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    public static double average(double... values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    public static double slope(Pos p1, Pos p2) {
        return (p2.y - p1.y) / (p2.x - p1.x);
    }

    public static double getQuadraticA(Pos p1, Pos p2, Pos p3) {
        double temp1 = (p2.y - p1.y) / (p2.x - p1.x);
        double temp2 = (p3.y - p1.y) / (p3.x - p1.x);
        return (temp1 - temp2) / (p2.x - p3.x);
    }

    public static String getPath(String... paths) {
        StringJoiner sb = new StringJoiner(File.separator);
        for (String path : paths) {
            sb.add(path);
        }
        return sb.toString();
    }
    @SafeVarargs
    public static <T, R> R[] doAll(Function<T, R> consumer, T... objects) {
        R[] result = (R[]) new Object[objects.length];
        for (int i = 0; i < objects.length; i++) {
            result[i] = consumer.apply(objects[i]);
        }
        return result;
    }

    @SafeVarargs
    public static <T, R> R[] doAllNullSafe(Function<T, R> consumer, T... objects) {
        R[] result = (R[]) new Object[objects.length];
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null)
                continue;
            result[i] = consumer.apply(objects[i]);
        }
        return result;
    }

    @SafeVarargs
    public static <T> void doAllNullSafe(Consumer<T> consumer, T... objects) {
        for (T object : objects) {
            if (object == null)
                continue;
            consumer.accept(object);
        }
    }
}
