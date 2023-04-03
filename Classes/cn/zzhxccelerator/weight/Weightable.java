package cn.zzhxccelerator.weight;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public abstract class Weightable {
    public double getWeightedValue() {
        double sum = 0;
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Weight.class)) {
                try {
                    sum += (double) method.invoke(this) * method.getAnnotation(Weight.class).value();
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return sum;
    }

    public static void checkWeight(Class<? extends Weightable> cls) {
        if (cls == null) {
            return;
        }
        Method[] methods = cls.getDeclaredMethods();
        double sum = Arrays.stream(methods).filter(method ->
                method.isAnnotationPresent(Weight.class)
        ).mapToDouble(method -> {
            if (method.getParameterCount() != 0) {
                throw new RuntimeException("The method " + method.getName() + " should have no parameter");
            }
            if (!method.getReturnType().equals(double.class)) {
                throw new RuntimeException("The method " + method.getName() + " should return double");
            }
            return method.getAnnotation(Weight.class).value();
        }).sum();

        if (sum != 1) {
            throw new RuntimeException("The sum of weights should be 1, but it is " + sum + " now.");
        }
    }
}
