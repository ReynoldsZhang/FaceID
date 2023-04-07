package cn.zzhxccelerator.face;

import cn.zzhxccelerator.util.CompareResult;
import cn.zzhxccelerator.weight.Weight;

import java.lang.reflect.Method;
import java.util.Arrays;

public class FaceComparator {
    /**
     * @param base  the base face
     * @param other the other face
     * @return the compare result, which contains a differenceSum and a ratioSum
     * @see Face
     */
    public static CompareResult compare(Face base, Face other) {
        // if one of the face is null, return 0
        if (base == null || other == null) {
            return new CompareResult(0.0, 0.0);
        }
        // get all the methods in Face.class
        Method[] methods = Face.class.getDeclaredMethods();
        // iterate all the methods
        double differenceSum = Arrays.stream(methods).mapToDouble(method -> {
                    // if the method is annotated with @Weight
                    if (method.isAnnotationPresent(Weight.class)) {
                        try {
                            // get the difference of the two methods' result
                            double diff = (double) method.invoke(other) - (double) method.invoke(base);
                            // return the absolute value of the difference * the weight
                            return Math.abs(diff) * method.getAnnotation(Weight.class).value();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return 0;
                })
                // sum all the differences
                .sum();

        // iterate all the methods
        double ratioSum = Arrays.stream(methods).mapToDouble(method -> {
                    // if the method is annotated with @Weight
                    if (method.isAnnotationPresent(Weight.class)) {
                        try {
                            // get the ratio of the two methods' result
                            double percentage = (double) method.invoke(other) / (double) method.invoke(base);
                            // return the absolute value of the difference * the weight
                            return Math.abs(1 - percentage) * method.getAnnotation(Weight.class).value();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return 0;
                })
                // sum all the ratios
                .sum();
        return new CompareResult(differenceSum, ratioSum);
    }
}
