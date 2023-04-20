package cn.zzhxccelerator.weight;

import java.lang.annotation.*;

/**
 * The weight of the face
 * For methods with this annotation, the return value will be multiplied by the weight
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Weight {
    /**
     * The weight of the face
     * @return the weight
     */
    double value();
}
