package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range; // Hibernate Validator에서만 동작
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank; // 표준 제공 (어떤 구현체에서도 동작)
import javax.validation.constraints.NotNull;

@Data
// @ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "10000원 넘게 입력해주세요") // 이거까지 가는건 좀 오버
// 검증 기능이 해당 객체를 넘어서는 경우가 많음 억지로 사용하는 것보다 오브젝트 오류 관련 부분만 직접 자바 코드로 작성하는 것을 권장

// 그냥 복합 룰 검증으로 사용하자!
public class Item {

//    @NotNull(groups = UpdateCheck.class)
    private Long id;

//    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;

 //   @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
 //   @Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

 //   @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
  //  @Max(value = 9999, groups = {SaveCheck.class})
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
