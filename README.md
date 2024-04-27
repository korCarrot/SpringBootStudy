[SpringBoot Thymeleaf]
스프링부트_타임리프
*타임리프 네임스페이스 선언 / 타임리프문은 태그 안에서 사용가능 ( 예시 : <h1 th:text="${model에 담은 내용}"></h1> ) / 타임리프문을 사용한 태그 안에 다른 내용을 적어도 페이지에 적용 안됨. 타임리프문 내용만 적용.

1. 컨트롤러 model -> th:text로 받아보기. th:each.
2. html에서 <a th:href="@{/매핑}"></a>을 통해 컨트롤러로 이동 / 쿼리문 사용하는법 <a th:href="@{/매핑(키='값'}"></a>
3. 타임리프 레이아웃 사용. layout:decorate="~{불러오고자 하는 layout 파일 위치}" / layout:fragment
