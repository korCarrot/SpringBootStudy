package com.boot.springbootstudy.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

//첨부파일
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage>{  //Comparable<> : 객체들이 서로 비교될 수 있도록 만들어줍니다. 이 인터페이스를 구현하면 객체를 정렬하거나 비교할 수 있습니다.

    @Id
    private String uuid;    //고유한 uuid 값

    private String fileName;    //파일 이름

    private int ord;    //순번

    @ManyToOne
    private Board board;


    //compareTo() : 현재 객체(this)와 다른 객체(other)를 비교합니다. 이 메서드는 정수를 반환하며, 반환 값에 따라 두 객체의 순서가 결정합니다.
    /* this.ord - other.ord 값이 양수이면 this 객체가 other 객체보다 크다는 의미입니다.
       this.ord - other.ord 값이 0이면 this 객체와 other 객체의 순번이 같다는 의미입니다.
       this.ord - other.ord 값이 음수이면 this 객체가 other 객체보다 작다는 의미입니다. */
    @Override
    public int compareTo(BoardImage other) {
        return this.ord - other.ord;
    }

    public void changeBoard(Board board){
        this.board = board;
    }
}
