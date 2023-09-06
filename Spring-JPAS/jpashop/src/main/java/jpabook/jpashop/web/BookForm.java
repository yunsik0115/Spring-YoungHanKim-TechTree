package jpabook.jpashop.web;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BookForm {

    private Long id;

    @NotBlank(message = "제품명 입력은 필수입니다")
    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;
}
