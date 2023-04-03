package cn.zzhxccelerator;

import cn.zzhxccelerator.util.Utils;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
//        System.out.println("Smith".compareTo("Anderson"));
        System.out.println(File.separator);
        System.out.println(File.pathSeparator);
        System.out.println("Photos\\Base Photos\\" + "Base_Photo_1" + ".png");
        System.out.println(Path.of("Photos", "Base Photos", "Base_Photo_1.png"));
        System.out.println(Utils.getPath("Photos", "Base Photos", "Base_Photo_1.png"));
    }
}
