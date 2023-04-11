package cn.zzhxccelerator.util;


/**
 * The result of the face comparison
 */
public class CompareResult {
    public final double diff;
    public final double ratio;

    public CompareResult(double diff, double ratio) {
        this.diff = diff;
        this.ratio = ratio;
    }

    public double getDiff() {
        return diff;
    }

    public double getRatio() {
        return ratio;
    }

    @Override
    public String toString() {
        return "(" + diff + ", " + ratio + ")";
    }

    /**
     * @return true if the face is passed, false otherwise
     */
    public boolean isPassed() {
        return diff < 3.3 && ratio < 10;
    }
}
