package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        // 에러 코드 넣으면 메세지 코드가 여러 개 나옴

        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }
        /*
        messageCode = required.item
        messageCode = required
        메세지 코드를 new ObjectError해서 만들 때 codes를 new String[]{}에 2개 넣어주고 순서대로 찾는다 (디테일한게 먼저, 범용적인게 순서대로)
         */

        Assertions.assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void messageCodesResolverField(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }
        /*
        실행 결과
    messageCode = required.item.itemName
    messageCode = required.itemName
    messageCode = required.java.lang.String  type (문자냐 숫자냐)
    messageCode = required

        BindingResult.rejectValue가 이걸 자동으로 쓴다. 에러코드를 required라고 넣어줬던 것
        rejectedValue에서 codesResolver를 호출
        new FieldError를 만들 때 codes[]를 넘김
         */

        Assertions.assertThat(messageCodes).containsExactly("required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required");
    }
}
