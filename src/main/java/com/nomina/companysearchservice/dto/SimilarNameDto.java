package com.nomina.companysearchservice.dto;

public class SimilarNameDto {
    private int id;
    private String markName;
    private double similarityScore;
    private String link;

    public SimilarNameDto(int id ,String markName, double similarityScore, String link) {
        this.id = id;
        this.markName = markName;
        this.similarityScore = similarityScore;
        this.link = link;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
