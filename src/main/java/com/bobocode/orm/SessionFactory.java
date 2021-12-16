package com.bobocode.orm;

import com.bobocode.orm.MySession;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;

@RequiredArgsConstructor
public class SessionFactory {
    private final DataSource dataSource;

    public MySession createSession(){
        return new MySession(dataSource);
    }
}
