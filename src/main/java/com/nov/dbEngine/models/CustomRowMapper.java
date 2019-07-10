package com.nov.dbEngine.models;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int ix) throws SQLException {

        /*
        Row row = new Row();
        int columsCount = resultSet.getMetaData().getColumnCount();

        for(int i=0;i<columsCount;i++){
            row.addCollumn(new Column<String>(resultSet.getMetaData().getColumnName(i),resultSet.getString(i)));
        }*/

        return null;
    }
}
