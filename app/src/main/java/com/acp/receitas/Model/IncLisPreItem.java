package com.acp.receitas.Model;

import io.realm.RealmObject;

public class IncLisPreItem extends RealmObject {
    private String mtxtListPrep;

    public IncLisPreItem() {
    }

    public IncLisPreItem(String txtListPrep){
        this.mtxtListPrep = txtListPrep;

    }

    public String getMtxtListPrep() {
        return mtxtListPrep;
    }

    public void setMtxtListPrep(String mtxtListPrep) {
        this.mtxtListPrep = mtxtListPrep;
    }

}
