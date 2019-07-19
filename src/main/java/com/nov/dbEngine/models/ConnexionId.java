package com.nov.dbEngine.models;

import java.util.ArrayList;
import java.util.List;

public class ConnexionId {

    public ConnexionId(){
        this.Id = new ArrayList<>();
    }
    public List<Long> getId() {
        return Id;
    }

    public void setId(List<Long> id) {
        Id = id;
    }

    private List<Long> Id ;

}
