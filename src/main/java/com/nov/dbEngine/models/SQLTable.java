package com.nov.dbEngine.models;

import java.util.ArrayList;
import java.util.List;

public class SQLTable {

    private String TableName;

    private List<Row> rows;

    public SQLTable() {
        this.rows = new ArrayList<>();
    }

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public void addRow(Row r){
        this.rows.add(r);
    }

}
