package cn.zzhxccelerator.face;

import cn.zzhxccelerator.util.CompareResult;

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

    /**
     * @param face the face to compare
     * @return the average compare result
     */
    public CompareResult getCompareResult(Face face) {
        // using three base faces to compare
        CompareResult face1Result = FaceComparator.compare(face1, face);
        CompareResult face2Result = FaceComparator.compare(face2, face);
        CompareResult face3Result = FaceComparator.compare(face3, face);
        // get the average difference and ratio
        double averageDiff = (face1Result.diff + face2Result.diff + face3Result.diff) / 3;
        double averagePercentage = (face1Result.ratio + face2Result.ratio + face3Result.ratio) / 3;
        CompareResult getDif = new CompareResult(averageDiff, averagePercentage);
        return getDif;
    }


    /**
     * @param face the face to compare
     * @return true if the face is passed, false otherwise
     */
    public boolean compare(Face face) {
        return getCompareResult(face).isPassed();
    }

    public boolean compare(String facePath) {
        return getCompareResult(Face.getFace(facePath)).isPassed();
    }
}
