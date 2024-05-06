package com.boot.springbootstudy.domain;

import jakarta.persistence.*;
import lombok.*;

//@Entity 애너테이션은 이 클래스가 데이터베이스의 테이블과 매핑되는 엔티티 클래스임을 나타냅니다. 즉, 이 클래스의 객체는 데이터베이스 테이블의 레코드에 해당합니다.
//테이블의 레코드는 데이터베이스에서 하나의 행(row)에 해당하는 정보를 의미합니다. 관계형 데이터베이스에서 테이블은 행과 열의 형태로 데이터를 구조화한 것이며, 각 행은 특정한 데이터를 나타냅니다.
@Entity //테이블 이름 = 클래스명
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board extends BaseEntity{

    @Id //@Id 애너테이션은 해당 필드가 엔티티의 기본 키(primary key)임을 나타냅니다. 기본 키는 각 레코드를 고유하게 식별하는 데 사용됩니다.
//@GeneratedValue 애너테이션은 기본 키의 값을 자동으로 생성하는 방법을 지정합니다.
//strategy 속성은 값을 생성하는 전략을 지정하며, 여기서는 GenerationType.IDENTITY로 설정되어 있습니다.
//이는 데이터베이스의 자동 증가(auto-increment) 기능을 이용하여 기본 키 값을 생성함을 의미합니다. 즉, 데이터베이스가 자동으로 기본 키 값을 할당합니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
// 1. IDENTITY: 데이터베이스에 위임(MYSQL/MariaDB) - auto_increment      2. SEQUENCE: 데이터베이스 시퀀스 오브젝트 사용(ORACLE) - @SequenceGenerator 필요
// 3. TABLE: 키 생성용 테이블 사용, 모든 DB에서 사용 - @TableGenerator 필요      4. AUTO: 방언에 따라 자동 지정, 기본 값
//    *방언 : 표준 SQL 문법 외에 독자적인 기능을 가진 다양한 데이터베이스 제품이 존재. 각각의 데이터 베이스가 제공하는 문법과 함수에 차이가 있음.
//           이러한 차이를 방언이라고 함. JPA는 종속되지 않은 추상화된 방언 클래스를 제공. 변경된 DBMS라도 자동으로 처리 가능.

    @Column(length = 500, nullable = false) //length: 컬럼의 길이
    private String title;

    @Column(length = 2000, nullable = false) //nullable: null 허용 여부
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;


//    update기능
    public void change(String title, String content){
        this.title=title;
        this.content=content;
    }

}
