package com.ladsoft.bilheteunicobalancechecker.model;

public class TransurcBilheteUnicoQueryParameter {

    public TransurcBilheteUnicoQueryParameter(String id, String birthDate) {
        this.id = id;
        this.birthDate = birthDate;
    }

    private String id;
    private String birthDate;

    public String getId() {
        return id;
    }

    public String getBirthDate() {
        return birthDate;
    }
}
