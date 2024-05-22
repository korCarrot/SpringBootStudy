package com.boot.springbootstudy.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@Entity 애너테이션은 이 클래스가 데이터베이스의 테이블과 매핑되는 엔티티 클래스임을 나타냅니다. 즉, 이 클래스의 객체는 데이터베이스 테이블의 레코드에 해당합니다.
//테이블의 레코드는 데이터베이스에서 하나의 행(row)에 해당하는 정보를 의미합니다. 관계형 데이터베이스에서 테이블은 행과 열의 형태로 데이터를 구조화한 것이며, 각 행은 특정한 데이터를 나타냅니다.
@Entity //테이블 이름 = 클래스명
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
public class Board extends BaseEntity{

    //@Id 애너테이션은 해당 필드가 엔티티의 기본 키(primary key)임을 나타냅니다. 기본 키는 각 레코드를 고유하게 식별하는 데 사용됩니다.
//@GeneratedValue 애너테이션은 기본 키의 값을 자동으로 생성하는 방법을 지정합니다.
//strategy 속성은 값을 생성하는 전략을 지정하며, 여기서는 GenerationType.IDENTITY로 설정되어 있습니다.
//이는 데이터베이스의 자동 증가(auto-increment) 기능을 이용하여 기본 키 값을 생성함을 의미합니다. 즉, 데이터베이스가 자동으로 기본 키 값을 할당합니다.
    @Id
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

    //mappedBy 속성은 양방향 관계에서 주인이 아닌 쪽에서 사용. 연관 관계의 주인을 설정하는 데 사용되며, 연관된 엔티티의 필드 이름을 지정. 외래 키가 정의된 곳을 가리킴.
    //mappedBy 속성을 사용하여 BoardImage 엔티티의 board 필드에 의해 매핑된다는 것을 나타냅니다. //BoardImage의 board변수. BoardImage를 연관관계의 핵심으로 지정

    //cascade 속성은 엔티티의 상태 변화를 관련 엔티티에도 전파하는 방법을 정의합니다. 여기서는 CascadeType.ALL을 사용하여 모든 상태 변화를 전파하도록 지정. Board 엔티티의 모든 상태 변화가 BoardImage 엔티티에도 전파되도록 설정.
    //fetch 속성은 연관된 엔티티를 어떻게 가져올지를 정의. FetchType.LAZY: 연관된 엔티티를 실제로 사용할 때 로드 (기본값).

    //orphanRemoval 속성은 부모 엔티티와의 관계가 끊어진 자식 엔티티를 자동으로 제거할지를 정의. orphanRemoval = true로 설정하면 부모 엔티티와의 연관이 끊어진 자식 엔티티가 자동으로 삭제.
    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)   //@BatchSize 어노테이션은 컬렉션 필드에만 적용. Board 엔티티의 imageSet 컬렉션을 가져올 때 한 번에 최대 20개의 BoardImage 엔티티를 가져오도록 설정.
    private Set<BoardImage> imageSet = new HashSet<>();

    public void addImage(String uuid, String fileName){

        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())   //imageSet은 Board 클래스에 정의된 이미지의 집합. imageSet.size()는 현재 imageSet의 크기를 반환. 즉, 현재 Board 객체에 속한 이미지의 개수를 의미. 새로 추가되는 이미지의 순서를 결정.
                .build();
        imageSet.add(boardImage);
    }

    //이미지 삭제 - changeBoard(null)를 사용하더라도 @OneToMany에서 orphanRemoval=true로 설정해야 삭제가 됨.
    public void clearImages(){

        imageSet.forEach(boardImage -> boardImage.changeBoard(null));   //null을 매개변수로 전달하여 BoardImage 객체가 더 이상 특정 Board 객체와 연관되지 않도록 합니다.

        this.imageSet.clear();  //clear 메서드는 java.util.Set 인터페이스에서 제공하는 메서드로, 호출 시 해당 Set의 모든 요소를 제거합니다.

    }

//    update기능
    public void change(String title, String content){
        this.title=title;
        this.content=content;
    }

}
