package com.acp.receitas.Model;

import io.realm.RealmObject;

public class IncLisIngItem extends RealmObject {
    private String mtxtListQtd;
    private String mtxtLisMed;
    private String mtxtListIng;

    public IncLisIngItem() {
    }

    public IncLisIngItem(String mtxtListQtd, String mtxtLisMed, String mtxtListIng ) {
        this.mtxtListQtd = mtxtListQtd;
        this.mtxtLisMed = mtxtLisMed;
        this.mtxtListIng = mtxtListIng;
    }

    public String getMtxtListQtd() {
        return mtxtListQtd;
    }

    public void setMtxtListQtd(String mtxtListQtd) {
        this.mtxtListQtd = mtxtListQtd;
    }

    public String getMtxtLisMed() {
        return mtxtLisMed;
    }

    public void setMtxtLisMed(String mtxtLisMed) {
        this.mtxtLisMed = mtxtLisMed;
    }

    public String getMtxtListIng() {
        return mtxtListIng;
    }

    public void setMtxtListIng(String mtxtListIng) {
        this.mtxtListIng = mtxtListIng;
    }

}
