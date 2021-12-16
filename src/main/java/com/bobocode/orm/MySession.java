package com.bobocode.orm;

import com.bobocode.annotation.Column;
import com.bobocode.annotation.Table;
import com.bobocode.entity.EntityKey;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class MySession {
    private static final String SELECT_QUERY = "select * from %s where id=?";
    private static final String UPDATE_QUERY = "update %s set %s where id=?";
    private final DataSource dataSource;
    private Map<EntityKey<?>,Object> entitiesMap = new HashMap<>();

    public <T> T find(Class<T> type, Object id){
        var key = new EntityKey<>(type,id);
        var entity = entitiesMap.computeIfAbsent(key,k -> findIfAbsentInCache(type, id));
        return (T) entity;
    }

    @SneakyThrows
    private <T> T findIfAbsentInCache(Class<T> type, Object id){
        try(var connection = dataSource.getConnection()){
            var selectString = formSelectQuery(type);
            try(var statement = connection.prepareStatement(selectString)){
                statement.setObject(1,id);
                var resultSet = statement.getResultSet();
                return newEntityFromResultSet(type,resultSet);
            }
        }
    }

    @SneakyThrows
    private <T> T newEntityFromResultSet(Class<T> type, ResultSet resultSet) {
        resultSet.next();
        var entity = type.getConstructor().newInstance();
        Field[] fields = type.getDeclaredFields();
        for(Field field:fields){
            var columnAnnotation = field.getAnnotation(Column.class);
            var columnName = columnAnnotation.value();
            field.setAccessible(true);
            var value = resultSet.getObject(columnName);
            field.set(entity, value);
        }
        return entity;
    }

    private String formSelectQuery(Class<?> type) {
        var tableAnnotation = type.getAnnotation(Table.class);
        var tableName = tableAnnotation.value();
        return String.format(SELECT_QUERY,tableName);
    }

}
