package jpabook.jpashop.domain.items;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("B")
//db 입장에서 구분이 되어야됨
public class Book extends Item{
    private String author;
    private String isbn;


}
