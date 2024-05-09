package com.boot.springbootstudy.domain;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

//LocalDate: 이 클래스는 연도, 월, 일을 나타내는데 사용됩니다. 시간 정보는 포함되지 않습니다. 즉, 특정 날짜를 나타내는 데 사용됩니다.
//LocalDateTime: 이 클래스는 연도, 월, 일, 시, 분, 초 등을 나타내는 데 사용됩니다. 즉, 특정 날짜와 시간을 모두 포함합니다.
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//@MappedSuperclass:이 어노테이션은 부모 클래스가 하위 엔티티 클래스들에게 매핑 정보를 제공하기 위해 사용됩니다.
// 즉, 이 클래스에 선언된 필드들은 상속된 하위 엔티티 클래스에서도 매핑되어야 합니다.
@MappedSuperclass
//@EntityListeners : 엔티티의 이벤트(예: 생성, 수정)를 수신하는 리스너를 지정하는 데 사용
//AuditingEntityListener : Spring Data JPA에서 제공되는 엔티티 수명 주기 이벤트를 처리하는 리스너입니다. 보통 엔티티의 생성일과 수정일을 추적하는 데 사용
//엔티티의 생성일 및 수정일 설정: @CreatedDate 및 @LastModifiedDate 어노테이션을 사용하여 엔티티가 생성되거나 수정될 때 해당 필드에 자동으로 날짜 및 시간을 설정합니다.
//Spring의 리소스 활용: AuditingEntityListener는 Spring의 ApplicationContext를 사용하여 현재 사용자, 시간대 및 다른 관련 정보를 검색할 수 있습니다.
@EntityListeners(value = {AuditingEntityListener.class})    // *AuditingEntityListener 를 활성화 시키기 위해서는 프로젝트의 설정(SpringBootStudyApplication)에 @EnableJpaAuditing을 추가해야 함
@Getter
abstract class BaseEntity { //추상 클래스 - 공통적인 속성을 가지는 모든 엔티티 클래스의 부모 역할   (@MappedSuperclass를 사용하면서 추상클래스로도 선언하는 듯?)

    @CreatedDate    // 엔티티가 생성될 때 자동으로 해당 필드에 현재 시간을 설정
    //@Column : JPA 엔티티 클래스의 필드를 데이터베이스 테이블의 열에 매핑하는 데 사용
    //updatable = false: 이 속성은 해당 필드가 수정되어도 데이터베이스에 반영되지 않도록 합니다. 즉, regDate 필드의 값은 한 번 설정되면 변경할 수 없습니다. 주로 생성일과 같은 필드에 사용되어, 데이터가 한 번 설정되고 나면 변경되지 않도록 합니다.
    @Column(name = "regDate", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate   //엔티티가 수정될 때 자동으로 해당 필드에 현재 시간을 설정
    @Column(name = "modDate")
    private LocalDateTime modDate;


}
