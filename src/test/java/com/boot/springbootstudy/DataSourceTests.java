package com.boot.springbootstudy;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
@Log4j2
public class DataSourceTests {

    //객체 주입 방식1(필드 주입) @Autowired
    //객체 주입 방식2(생성자 주입) @Autowired 생략, 필드에 final 추가, 클래스에  @RequiredArgsConstructor 추가

    //* JUnit은 기본적으로 매개변수가 없는 기본 생성자를 사용한다.
    //하지만 Lombok의 @RequiredArgsConstructor는 final 필드를 초기화하는 생성자를 추가하므로 Junit 테스트 환경에서는 @Autowired를 사용해야만 한다.

//    DataSource Sql
    @Autowired
    private DataSource dataSource;

    @Test
    public void testConnection() throws SQLException {

    //  @Cleanup : 자동으로 close() 메서드를 호출하여 리소스를 정리하는 데 사용
        @Cleanup
        Connection con =dataSource.getConnection();
        log.info(con);

        //Assertions.assertNotNull()
        // : 주어진 객체가 null이 아님을 검증하기 위해 사용. null이면 AssertionError를 발생.
        Assertions.assertNotNull(con);
    }

//    @Test
//    public void testNotNull() {
//        String str = "Hello";
//        Assertions.assertNotNull(str);
//    }
//
//    @Test
//    public void testNotNull() {
//        String str = null;
//        Assertions.assertNotNull(str);
//    }
}
