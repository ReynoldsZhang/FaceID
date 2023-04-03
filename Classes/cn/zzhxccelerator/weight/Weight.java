package cn.zzhxccelerator.weight;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Weight {
    double value();
}
