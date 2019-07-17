package com.nov.service;

import com.nov.dbEngine.SQLConnectionBuilder;
import com.nov.dbEngine.models.Column;
import com.nov.dbEngine.models.Row;
import com.nov.dbEngine.models.SQLTable;
import com.nov.domain.Connexion;
import com.nov.domain.Execution;
import com.nov.domain.Query;
import com.nov.repository.ConnexionRepository;
import com.nov.repository.QueryRepository;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AppEngineService {

    @Autowired
    QueryRepository queryRepository;

    private final Logger log = LoggerFactory.getLogger(AppEngineService.class);

    /*
        this method  executes any query to any connection given as parameter
     */
    public SQLTable executeQuery(Connexion conn, String query){
        SQLTable table = new SQLTable();
        SQLConnectionBuilder builder = new SQLConnectionBuilder(conn);
        System.out.println(builder.getConnetionParams());
        JdbcTemplate template = builder.build();
        try {
            table.setRows(template.query(query, new RowMapper<Row>() {
                @Override
                public Row mapRow(ResultSet resultSet, int ix) throws SQLException {

                    return  mapRowMethod(resultSet, ix, table);
                }
            }));
        }catch (BadSqlGrammarException e){
            e.printStackTrace();
        }


        return table;
    }

    // this method is used for both  executeQuery methods*
    public Row mapRowMethod(ResultSet resultSet, int ix,SQLTable table) throws SQLException {
        if(table.getTableName()== null){
            table.setTableName(resultSet.getMetaData().getTableName(1));
        }
        Row row = new Row();
        int columsCount = resultSet.getMetaData().getColumnCount();
        for(int i=1;i<columsCount;i++){
            row.addCollumn(new Column<String>(resultSet.getMetaData().getColumnName(i),resultSet.getString(i)));
        }
        return row;
    }

    public SQLTable executeQuery(Connexion conn, Query query){
        SQLTable table = new SQLTable();
        SQLConnectionBuilder builder = new SQLConnectionBuilder(conn);

        JdbcTemplate template = builder.build();
        Execution ex = new Execution();
        ex.setExDate(LocalDate.now());
        try {

            table.setRows(template.query(query.getStatment(), new RowMapper<Row>() {
                @Override
                public Row mapRow(ResultSet resultSet, int ix) throws SQLException {
                    return mapRowMethod(resultSet, ix, table);
                }


            }));
            ex.setStatus(true);
        }catch (BadSqlGrammarException e){
            ex.setStatus(false);
        }
        query.addExecutions(ex);
        queryRepository.save(query);

        return table;
    }

    public SQLTable getAllDatabases(Connexion conn){
        SQLTable table = new SQLTable();
        table.setTableName("DATABASES ["+conn.getId()+"]");
        try {
            ResultSet rs =(ResultSet)JdbcUtils.extractDatabaseMetaData(new SQLConnectionBuilder(conn).dataSource(conn.getConnector().getType()),
                new DatabaseMetaDataCallback(){
                    @Override
                    public Object processMetaData(DatabaseMetaData databaseMetaData) throws SQLException,
                        MetaDataAccessException {
                        return databaseMetaData.getCatalogs();
                    }
                });
            while (rs.next()){
                table.addRow(new Row( new Column<String>("Database Name",
                    rs.getString("TABLE_CAT"))));
            }

        } catch (MetaDataAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return table;
    }


}
