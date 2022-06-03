package ru.renett.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Company {
    private Long id;
    private String title;
    private String logoUrl;
    private  String domain;

    public Company(String title, String logoUrl, String domain) {
        this.title = title;
        this.logoUrl = logoUrl;
        this.domain = domain;
    }
}
