package eu.mattflix.utils;

public class SubComparerResult {


    private double matchRatio;
    private String fileName;
    private String path;



    public SubComparerResult(String name,  double ratio) {

        this.fileName = name;
        this.matchRatio = ratio;
    }



    @Override
    public String toString() {
        return "fileName=" + fileName + "Ratio=" + this.matchRatio+"%";
    }



    public double getMatchRatio() {
        return matchRatio;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
