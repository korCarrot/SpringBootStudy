package com.boot.springbootstudy.domain;

import lombok.*;

import javax.persistence.*;

@Entity
//엔티티 클래스에 대한 테이블 매핑 정보를 제공하는데 사용됩니다. 이 경우에는 테이블의 이름과 인덱스 정보를 정의
//name : 해당 엔티티 클래스가 매핑될 데이터베이스 테이블의 이름을 지정  /  index : 테이블에 적용할 인덱스를 정의합니다. @Index 애노테이션을 사용하여 인덱스를 지정(인덱스 이름, 인덱스 지정할 테이블의 열)
@Table(name = "Reply", indexes = {@Index(name = "idx_reply_board_bno", columnList = "board_bno")})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@ToString(exclude = "board") //참조하는 객체를 사용하지 않도록 반드시 exclude 속성값 지정 / exclude를 사용하지 않을 시 @Transactional 추가
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

// @ManyToOne : 해당 필드가 다른 엔티티의 한쪽인 것을 나타냅니다.
// 지연 로딩(Lazy Loading)을 사용하여 관계를 로드하도록 설정합니다. 이는 연관된 엔티티를 즉시 로드하는 대신, 해당 엔티티에 실제로 접근할 때까지 연관된 엔티티를 로드하지 않습니다. 이는 성능을 향상시킬 수 있으며, 특히 관련된 엔티티가 많은 경우에 유용합니다.
    @ManyToOne(fetch = FetchType.LAZY) //연관 관계를 나타낼 때는 반드시 fetch 속성을 LAZY로 지정
    private Board board;    //다른 Entity와의 관계

    private String replyText;

    private String replyer;

}
