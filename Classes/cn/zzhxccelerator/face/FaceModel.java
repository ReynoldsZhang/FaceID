package cn.zzhxccelerator.face;

public class FaceModel {
    public final Face face1;
    public final Face face2;
    public final Face face3;

    public FaceModel(Face face1, Face face2, Face face3) {
        this.face1 = face1;
        this.face2 = face2;
        this.face3 = face3;
    }

    public FaceModel(String face1, String face2, String face3) {
        this.face1 = Face.getFace(face1);
        this.face2 = Face.getFace(face2);
        this.face3 = Face.getFace(face3);
    }

    public double compare(Face face) {
        double d1 = FaceComparator.compare(face1, face);
        double d2 = FaceComparator.compare(face2, face);
        double d3 = FaceComparator.compare(face3, face);
        double d = (d1 + d2 + d3) / 3;
        return d;
    }
}
