package com.nomina.companysearchservice.dto;

import java.math.BigInteger;

public class CompanyData {
    private Long id;
    private String markName;
    private String imagePath;
    private BigInteger perceptiveHash;
    private BigInteger differenceHash;
    private String link;

    public CompanyData(Long id, String markName, String imagePath,
                       BigInteger perceptiveHash, BigInteger differenceHash, String link) {
        this.id = id;
        this.markName = markName;
        this.imagePath = imagePath;
        this.perceptiveHash = perceptiveHash;
        this.differenceHash = differenceHash;
        this.link = link;
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

    public BigInteger getPerceptiveHash() {
        return perceptiveHash;
    }

    public BigInteger getDifferenceHash() {
        return differenceHash;
    }

    public String getLink() {
        return link;
    }
}
