package cn.zzhxccelerator.face;

import cn.zzhxccelerator.weight.Weight;

import java.lang.reflect.Method;
import java.util.Arrays;

public class FaceComparator {
    /**
     * @param base the base face
     * @param other the other face
     * @return the percentage of difference
     * @see Face
     */
    public static double compare(Face base, Face other) {
        if (base == null || other == null) {
            return 0;
        }
        Method[] methods = Face.class.getDeclaredMethods();
        double d = Arrays.stream(methods).mapToDouble(method -> {
            if (method.isAnnotationPresent(Weight.class)) {
                try {
                    double percentage = (double) method.invoke(other) / (double) method.invoke(base);
                    return Math.abs(1 - percentage) * method.getAnnotation(Weight.class).value();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return 0;
        }).sum();
//        System.out.println("==================================");
//        System.out.println("Face " + base.file.getName() + " to Face " + other.file.getName() + ": " + d);
//        System.out.println("==================================");
//        double percentageOfTwoEyesWidth = other.getTwoEyesWidthPercentage() / base.getTwoEyesWidthPercentage();
//        double percentageOfTwoEyesGap = other.getGapInEyesPercentage() / base.getGapInEyesPercentage();
//        double percentageOfLeftEyeWidth = other.getLeftEyeWidthPercentage() / base.getLeftEyeWidthPercentage();
//        double percentageOfRightEyeWidth = other.getRightEyeWidthPercentage() / base.getRightEyeWidthPercentage();
//        double percentageOfLeftEyeHeight = other.getLeftEyeHeightPercentage() / base.getLeftEyeHeightPercentage();
//        double percentageOfRightEyeHeight = other.getRightEyeHeightPercentage() / base.getRightEyeHeightPercentage();
//        double percentageOfNoseWidth = other.getNoseWidthPercentage() / base.getNoseWidthPercentage();
//        double percentageOfNoseHeight = other.getNoseHeightPercentageOfEyesWidth() / base.getNoseHeightPercentageOfEyesWidth();
//        double percentageOfMouthWidth = other.getMouthWidthPercentage() / base.getMouthWidthPercentage();
//        double percentageOfMouthHeight = other.getMouthHeightPercentage() / base.getMouthHeightPercentage();
//        double percentageOfLeftQuadrantA = other.getLeftEyebrowQuadraticA() / base.getLeftEyebrowQuadraticA();
//        double percentageOfRightQuadrantA = other.getRightEyebrowQuadraticA() / base.getRightEyebrowQuadraticA();
//        double percentageOfWidthHeightRatio = other.getFaceWidthHeightRatio() / base.getFaceWidthHeightRatio();
//        double percentageOfLeftEyebrowHeight = other.getLeftEyeBrowPercentage() / base.getLeftEyeBrowPercentage();
//        double percentageOfRightEyebrowHeight = other.getRightEyeBrowPercentage() / base.getRightEyeBrowPercentage();
//        double percentageOfNoseWidthHeightRatio = other.getNoseWidthHeightRatio() / base.getNoseWidthHeightRatio();
//        double percentageOfMouseNoseWidthRatio = other.getMouthNoseWidthRatio() / base.getMouthNoseWidthRatio();
//        System.out.println(percentageOfTwoEyesWidth);
//        System.out.println(percentageOfTwoEyesGap);
//        System.out.println(percentageOfLeftEyeWidth);
//        System.out.println(percentageOfRightEyeWidth);
//        System.out.println(percentageOfLeftEyeHeight);
//        System.out.println(percentageOfRightEyeHeight);
//        System.out.println(percentageOfNoseWidth);
//        System.out.println(percentageOfNoseHeight);
//        System.out.println(percentageOfMouthWidth);
//        System.out.println(percentageOfMouthHeight);
//        System.out.println(percentageOfLeftQuadrantA);
//        System.out.println(percentageOfRightQuadrantA);
//        System.out.println(percentageOfWidthHeightRatio);
//        System.out.println(percentageOfLeftEyebrowHeight);
//        System.out.println(percentageOfRightEyebrowHeight);
//        System.out.println(percentageOfNoseWidthHeightRatio);
//        System.out.println(percentageOfMouseNoseWidthRatio);

        return d;
    }
}
