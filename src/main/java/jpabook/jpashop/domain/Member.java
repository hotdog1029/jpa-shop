package jpabook.jpashop.domain;



import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id") // pk 이름설정
    private Long id;

    private String name;

    @Embedded // 내장타입을 포함했다는 어노테이션
    private Address address;

    @OneToMany(mappedBy = "member") // member기준으로 orders와는 일대다의 관계이기 때문, 나는 연관관계의 주인이 아니다라는 구문 추가
    private List<Order> orders = new ArrayList<>();
}
