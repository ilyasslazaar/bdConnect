package com.nov.dbEngine.models;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private List<Column> Columns;

    public Row() {
        this.Columns = new ArrayList<>();
    }
    public Row(Column c){
        this();
        this.addCollumn(c);
    }

    public List<Column> getColumns() {
        return Columns;
    }

    public void setColumns(List<Column> columns) {
        Columns = columns;
    }

    public void addCollumn(Column c ){
        this.Columns.add(c);
    }
    public Column getColumn(int index){
       return  this.Columns.get(index);
    }
}
