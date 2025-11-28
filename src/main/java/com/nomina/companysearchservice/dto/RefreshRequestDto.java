package com.nomina.companysearchservice.dto;

public class RefreshRequestDto {
    private int id;
    private String link;

    public RefreshRequestDto() {
    }

    public RefreshRequestDto(int id, String link) {
        this.id = id;
        this.link = link;
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
