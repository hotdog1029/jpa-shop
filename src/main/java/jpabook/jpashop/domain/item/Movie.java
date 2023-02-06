package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("M") // 하위 클래스에 선언한다. 엔티티를 저장할 때 슈퍼타입의 구분 컬럼에 저장할 값을 지정한다.
@Getter @Setter
public class Movie extends Item {

    private String director;
    private String actor;
}
