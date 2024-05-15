package com.boot.springbootstudy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    //@Builder.Default : 빌더 패턴을 사용하여 객체를 생성할 때 기본값을 지정
    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String type; // 검색의 종류 t, c, w, tc, tw, twc

    private String keyword;

    private String link;


    public String[] getTypes(){

        if (type == null || type.isEmpty()) {
            return null;
        }

        return type.split("");
    }
//가변 인자를 나타내는 String... 뒤에는 변수명을 작성할 수 있습니다. 변수명은 개발자가 원하는 대로 지정할 수 있으며, 일반적으로 가변 인자의 목적이나 사용되는 데이터의 의미를 잘 표현하는 이름을 선택하는 것이 좋습니다
//String...props : 가변 인자(Varargs), 여러 개의 문자열 인수를 전달, 인수로 전달된 문자열은 배열로 취급
    public Pageable getPageable(String...props){
        return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
    }


//  검색 조건과 페이징 조건 등을 문자열로 구성
    public String getLink(){

        if (link == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("page="+this.page);

            builder.append("&size=" + this.size);

            if(type != null && type.length() > 0){
                builder.append("&type=" + type);
            }

            if(keyword != null){
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword,"UTF-8"));
                } catch (UnsupportedEncodingException e) { }
            }

            link = builder.toString();
        }

        return link;
        }

}
