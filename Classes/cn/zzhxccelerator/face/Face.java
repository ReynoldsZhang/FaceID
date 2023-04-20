package cn.zzhxccelerator.face;

import cn.zzhxccelerator.Facemark;
import cn.zzhxccelerator.util.Pos;
import cn.zzhxccelerator.weight.Weight;
import cn.zzhxccelerator.weight.Weightable;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

import static cn.zzhxccelerator.util.Utils.*;

public class Face extends Weightable implements Serializable {
    public final File file;
    /**
     * <b>The face border</b>
     * <p>
     * 0 - 16: face border
     * <p>
     * length: 17
     * <p>
     * <b>The eyebrows</b>
     * <p>
     * 17 - 21: left eyebrow
     * <p>
     * 22 - 26: right eyebrow
     * <p>
     * length: 10
     * <p>
     * <b>The nose</b>
     * <p>
     * 27 - 30: nose horizontal
     * <p>
     * 31 - 35: nose vertical
     * <p>
     * length: 9
     * <p>
     * <b>The eyes</b>
     * <p>
     * 36 - 41: left eye
     * <p>
     * 42 - 47: right eye
     * <p>
     * length: 12
     * <p>
     * <b>The mouth</b>
     * <p>
     * 48 - 67: mouth
     * <p>
     * length: 20
     */

    public final Pos[] faceMarks;

    public Face(int[][] facemarks, String path) {
        this.faceMarks = new Pos[68];
        this.file = new File(path);
        for (int i = 0; i < 68; i++) {
            this.faceMarks[i] = new Pos(facemarks[i]);
        }
    }

    public static Face getFace(String path) {
        try {
            int[][] xys = Facemark.getFacemarks(path, "NumberedPoints", "");
            if (xys == null) {
                throw new RuntimeException("No face detected or more than one face detected.");
            }
            return new Face(xys, path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the width of the face
     *
     * @return the distance between point 0 and point 16
     */
    public double getFaceWidth() {
        return distance(faceMarks[0], faceMarks[16]);
    }

    /**
     * Get the height of the face
     *
     * @return the distance between point 8 and point 27
     */
    public double getHalfFaceHeight() {
        return distance(faceMarks[8], faceMarks[27]);
    }

    public double getTwoEyesWidth() {
        return distance(faceMarks[36], faceMarks[45]);
    }

    public double getLeftEyeWidth() {
        return distance(faceMarks[36], faceMarks[39]);
    }

    public double getRightEyeWidth() {
        return distance(faceMarks[42], faceMarks[45]);
    }

    public double getTwoEyesGap() {
        return distance(faceMarks[39], faceMarks[42]);
    }

    public double getLeftEyeHeight() {
        return average(distance(faceMarks[37], faceMarks[41]), distance(faceMarks[38], faceMarks[40]));
    }

    public double getRightEyeHeight() {
        return average(distance(faceMarks[43], faceMarks[47]), distance(faceMarks[44], faceMarks[46]));
    }

    public double getLeftEyebrowWidth() {
        return distance(faceMarks[17], faceMarks[21]);
    }

    public double getRightEyebrowWidth() {
        return distance(faceMarks[22], faceMarks[26]);
    }

    public double getNoseHeight() {
        return distance(faceMarks[27], faceMarks[30]);
    }

    public double getNoseWidth() {
        return distance(faceMarks[31], faceMarks[35]);
    }

    public double getMouthWidth() {
        return distance(faceMarks[48], faceMarks[54]);
    }

    public double getMouthHeight() {
        return distance(faceMarks[51], faceMarks[57]);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(faceMarks);
    }

    @Override
    public String toString() {
        return "Face{" +
                "faceMarks=" + Arrays.toString(faceMarks) +
                '}';
    }

    @Weight(12)
    public double getTwoEyesWidthPercentage() {
        return getTwoEyesWidth() / getFaceWidth();
    }

    @Weight(8)
    public double getGapInEyesPercentage() {
        return getTwoEyesGap() / getFaceWidth();
    }

    @Weight(2)
    public double getLeftEyeWidthPercentage() {
        return getLeftEyeWidth() / getFaceWidth();
    }

    @Weight(2)
    public double getRightEyeWidthPercentage() {
        return getRightEyeWidth() / getFaceWidth();
    }

    @Weight(2)
    public double getLeftEyeHeightPercentage() {
        return getLeftEyeHeight() / getNoseHeight();
    }

    @Weight(2)
    public double getRightEyeHeightPercentage() {
        return getRightEyeHeight() / getNoseHeight();
    }

    @Weight(5)
    public double getNoseWidthPercentage() {
        return getNoseWidth() / getFaceWidth();
    }

    @Weight(12)
    public double getNoseHeightPercentageOfEyesWidth() {
        return getNoseHeight() / getTwoEyesWidth();
    }

    @Weight(5)
    public double getMouthWidthPercentage() {
        return getMouthWidth() / getFaceWidth();
    }

    @Weight(3)
    public double getMouthHeightPercentage() {
        return getMouthHeight() / getHalfFaceHeight();
    }

    @Weight(2)
    public double getLeftEyebrowQuadraticA() {
        return getQuadraticA(this.faceMarks[17], this.faceMarks[19], this.faceMarks[21]);
    }

    @Weight(2)
    public double getRightEyebrowQuadraticA() {
        return getQuadraticA(this.faceMarks[22], this.faceMarks[24], this.faceMarks[26]);
    }

    @Weight(2)
    public double getFaceWidthHeightRatio() {
        return getFaceWidth() / getHalfFaceHeight();
    }

    @Weight(3)
    public double getLeftEyeBrowPercentage() {
        return getLeftEyebrowWidth() / getFaceWidth();
    }

    @Weight(3)
    public double getRightEyeBrowPercentage() {
        return getRightEyebrowWidth() / getFaceWidth();
    }

    @Weight(8)
    public double getNoseWidthHeightRatio() {
        return getNoseWidth() / getNoseHeight();
    }

    @Weight(5)
    public double getMouthNoseWidthRatio() {
        return getMouthWidth() / getNoseWidth();
    }

//    public double testWeightedValue() {
//        double sum = 0;
//        double a = getTwoEyesWidthPercentage();
//        double b = getGepInEyesPercentage();
//        double c = getLeftEyeWidthPercentage();
//        double d = getRightEyeWidthPercentage();
//        double e = getLeftEyeHeightPercentage();
//        double f = getRightEyeHeightPercentage();
//        double g = getNoseWidthPercentage();
//        double h = getNoseHeightPercentageOfEyesWidth();
//        double i = getMouthWidthPercentage();
//        double j = getMouthHeightPercentage();
//        double k = getLeftEyebrowQuadraticA();
//        double l = getFaceWidthHeightRatio();
//        double m = getLeftEyeBrowPercentage();
//        double n = getRightEyeBrowPercentage();
//        double o = getNoseWidthHeightRatio();
//        double p = getMouthNoseWidthRatio();
//        sum += a * 0.1;
//        sum += b * 0.1;
//        sum += c * 0.05;
//        sum += d * 0.05;
//        sum += e * 0.05;
//        sum += f * 0.05;
//        sum += g * 0.1;
//        sum += h * 0.1;
//        sum += i * 0.05;
//        sum += j * 0.03;
//        sum += k * 0.1;
//        sum += l * 0.03;
//        sum += m * 0.05;
//        sum += n * 0.05;
//        sum += o * 0.05;
//        sum += p * 0.04;
//        return sum;
//    }
}
