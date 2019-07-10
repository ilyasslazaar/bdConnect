package com.nov.dbEngine.models;

public class Column<T> {

    private String ColumnName;

    private T ColumnValue;


    public Column(String ColumnName, T ColumnValue){

        this.ColumnName = ColumnName;
        this.ColumnValue = ColumnValue;

    }

    public String getColumnName() {
        return ColumnName;
    }

    public void setColumnName(String columnName) {
        ColumnName = columnName;
    }

    public T getColumnValue() {
        return ColumnValue;
    }

    public void setColumnValue(T columnValue) {
        ColumnValue = columnValue;
    }
}
