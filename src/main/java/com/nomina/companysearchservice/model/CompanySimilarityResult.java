package com.nomina.companysearchservice.model;

public class CompanySimilarityResult {
    private Long id;
    private String markName;
    private String imagePath;
    private String link;
    private double perceptiveSimilarity;
    private double differenceSimilarity;

    public CompanySimilarityResult(Long id, String markName, String imagePath, String link,
                                   double perceptiveSimilarity, double differenceSimilarity) {
        this.id = id;
        this.markName = markName;
        this.imagePath = imagePath;
        this.link = link;
        this.perceptiveSimilarity = perceptiveSimilarity;
        this.differenceSimilarity = differenceSimilarity;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getMarkName() {
        return markName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getLink() {
        return link;
    }

    public double getPerceptiveSimilarity() {
        return perceptiveSimilarity;
    }

    public double getDifferenceSimilarity() {
        return differenceSimilarity;
    }
}