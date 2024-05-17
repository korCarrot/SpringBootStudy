[설치순서]

[설정파일]

[환경설정]

[폴더구조]


[SpringBoot Thymeleaf]
스프링부트_타임리프

* 타임리프 네임스페이스 선언 / 타임리프 문법은 태그 안에서 사용 가능 (예시: `<h1 th:text="${model에 담은 내용}"></h1>`)
* 타임리프 문법을 사용한 태그 안에 다른 내용을 적어도 페이지에 적용되지 않음. 타임리프 문법 내용만 적용됨.

1. 컨트롤러 model -> th:text로 받아보기. th:each.
2. HTML에서 `<a th:href="@{/매핑}"></a>`을 통해 컨트롤러로 이동 / 쿼리문 사용하는 법 `<a th:href="@{/매핑(키='값')}"></a>`
3. 타임리프 레이아웃 사용. `layout:decorate="~{불러오고자 하는 layout 파일 위치}"` / `layout:fragment`




[SpringBoot JPA]
스트링부트_JPA

* '자바로 영속 영역을 처리하는 API', '객체 지향으로 구성한 시스템을 관계형 데이터베이스에 매핑'
@Entity, @ID, @GeneratedValue(strategy = GenerationType.IDENTITY), @Column(length,nullable)
/ @EnableJpaAuditing
/ @MappedSuperclass,  @EntityListeners(value = {AuditingEntityListener.class}), @CreatedDate, @LastModifiedDate

* JpaRepository<T, ID>
save(), findById(), deleteById()
orElseThrow()

* <페이징 처리>
리턴 타입 Page<T> (자동으로 count 처리) - Pageable 인터페이스 - PageRequest.of(페이지 번호, 사이즈, 정렬 조건, 속성...)
findAll(), findAll(Pageable)
