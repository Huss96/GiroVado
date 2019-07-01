package com.example.hussnain.girovado;

public class Transport {
    private String type;
    private String company;
    private String link;


    Transport(String type, String company,String link){
        this.type = type;
        this.company = company;
        this.link = link;
    }

    public String getCompany() {
        return company;
    }

    public String getLink() {
        return link;
    }

    public String getType() {
        return type;
    }
}
