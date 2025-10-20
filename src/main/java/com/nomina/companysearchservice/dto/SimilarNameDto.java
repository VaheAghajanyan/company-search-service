package com.nomina.companysearchservice.dto;

public class SimilarNameDto {
    private String markName;
    private double similarityScore;

    public SimilarNameDto(String markName, double similarityScore) {
        this.markName = markName;
        this.similarityScore = similarityScore;
    }

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }
}
