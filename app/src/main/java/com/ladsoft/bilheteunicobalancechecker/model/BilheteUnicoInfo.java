package com.ladsoft.bilheteunicobalancechecker.model;

public class BilheteUnicoInfo {

    public BilheteUnicoInfo(String number, String userName, String status, float vtBalance, float commonPassBalance) {
        this.number = number;
        this.userName = userName;
        this.status = status;
        this.vtBalance = vtBalance;
        this.commonPassBalance = commonPassBalance;
    }

    private String number;
    private String userName;
    private String status;
    private float vtBalance;
    private float commonPassBalance;

    public String getNumber() {
        return number;
    }

    public String getUserName() {
        return userName;
    }

    public String getStatus() {
        return status;
    }

    public float getVtBalance() {
        return vtBalance;
    }

    public float getCommonPassBalance() {
        return commonPassBalance;
    }
}
