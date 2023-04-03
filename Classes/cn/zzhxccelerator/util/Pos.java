package cn.zzhxccelerator.util;

import java.io.Serializable;
import java.util.Objects;

public class Pos implements Serializable {
    public final double x;
    public final double y;

    public Pos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Pos(double[] xy) {
        if (xy.length != 2) {
            throw new IllegalArgumentException("xy.length != 2");
        }
        this.x = xy[0];
        this.y = xy[1];
    }

    public Pos(int[] xy) {
        if (xy.length != 2) {
            throw new IllegalArgumentException("xy.length != 2");
        }
        this.x = xy[0];
        this.y = xy[1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pos pos = (Pos) o;
        return x == pos.x && y == pos.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
