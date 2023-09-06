package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component
@Slf4j
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
        // 파라미터로 넘어오는 클래스가 Item에 지원이 되냐
        // item == clazz
        // item == subItem(자식 클래스도 통과하게 끔 할 수 있다)
    }

    @Override
    public void validate(Object target, Errors errors) {

        // Object, Errors 가 넘어온다
        // Object는 target (item을 넘긴다 캐스팅 필요)

        Item item = (Item) target;
        // Errors 는 errors의 부모

        // Binding Result는 Target을 가지고 있다.
        // rejectValue, reject를 사용하면 field, object error를 사용하지 않고도 검증 구현 가능.
        // 더 디테일한 내용? errors.properties에 있는 코드 직접 사용 X -> HOW?
        // 결국 필드 에러 대신해서 생성해주게 됨.

        // Validation Logic
        if(!StringUtils.hasText(item.getItemName())){
            errors.rejectValue("itemName", "required", "기본 : 상품 이름은 필수입니다");
            // 첫글자만 따서 넣어주면 됨 (규칙?)
            // required.item.itemName
        }

        ValidationUtils.rejectIfEmpty(errors, "itemName", "required");

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            errors.rejectValue("price", "range", new Object[]{1000, 10000000}, null);
        }

        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // ModelAttribute는 값을 자동으로 넣어줌 -> th:object 그대로 남아있음.
        // 메모리상에 남아있는 객체 자료 재활용함. 위에 new Item()으로 선언한 이유임

        // Combinational Validation
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }

        }

    }
}
