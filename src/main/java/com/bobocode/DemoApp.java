package com.bobocode;

import com.bobocode.orm.MySession;
import com.bobocode.orm.SessionFactory;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DemoApp {
    public static void main(String[] args) {

    }

    private MySession initSession(){
        var dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://93.175.204.87:5432/postgres");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        SessionFactory sf = new SessionFactory(dataSource);
        return sf.createSession();
    }
}
