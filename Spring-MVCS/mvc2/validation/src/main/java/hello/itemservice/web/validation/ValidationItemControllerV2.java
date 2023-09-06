package hello.itemservice.web.validation;

import com.sun.jdi.Field;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @InitBinder // 모든 경우에 검증
    public void init(WebDataBinder dataBinder){
        dataBinder.addValidators(itemValidator);
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    //@PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item,
                          BindingResult bindingResult, // ModelAttribute 바로 뒤에 와야 함! (순서 중요)
                          RedirectAttributes redirectAttributes,
                          Model model) {

        // Validation Logic
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다"));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price", "가격은 ~ 입니다"));
        }

        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item", "quantity", "수량 에러 최대 9,999"));
        }

        // ModelAttribute는 값을 자동으로 넣어줌 -> th:object 그대로 남아있음.
        // 메모리상에 남아있는 객체 자료 재활용함. 위에 new Item()으로 선언한 이유임

        // Combinational Validation
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item", "가격과 수량의 합은 10000 이상이여야 합니다"));
            }

        }

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 에러 없는 경우(아래 내용)

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);


        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item,
                            BindingResult bindingResult, // ModelAttribute 바로 뒤에 와야 함! (순서 중요)
                            RedirectAttributes redirectAttributes,
                            Model model) {

        // Validation Logic
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다"));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 ~ 입니다"));
        }

        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량 에러 최대 9,999"));
        }

        // ModelAttribute는 값을 자동으로 넣어줌 -> th:object 그대로 남아있음.
        // 메모리상에 남아있는 객체 자료 재활용함. 위에 new Item()으로 선언한 이유임

        // Combinational Validation
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item", null,null,"가격과 수량의 합은 10000 이상이여야 합니다"));
                // 이건 값이 넘어오는게 아니기 때문에 따로 바인딩 실패할 일이 없음
            }

        }

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 에러 없는 경우(아래 내용)

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);


        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item,
                            BindingResult bindingResult, // ModelAttribute 바로 뒤에 와야 함! (순서 중요)
                            RedirectAttributes redirectAttributes,
                            Model model) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());
        // Binding Result는 Target을 가지고 있다.
        // rejectValue, reject를 사용하면 field, object error를 사용하지 않고도 검증 구현 가능.

        // Validation Logic
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError
                    (new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, "상품 이름은 필수입니다"));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.addError
                    (new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, "가격은 ~ 입니다"));
        }

        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError
                    (new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, "수량 에러 최대 9,999"));
        }

        // ModelAttribute는 값을 자동으로 넣어줌 -> th:object 그대로 남아있음.
        // 메모리상에 남아있는 객체 자료 재활용함. 위에 new Item()으로 선언한 이유임

        // Combinational Validation
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"},new Object[]{10000, resultPrice},"가격과 수량의 합은 10000 이상이여야 합니다"));
                // 이건 값이 넘어오는게 아니기 때문에 따로 바인딩 실패할 일이 없음
            }

        }

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 에러 없는 경우(아래 내용)

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);


        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item,
                            BindingResult bindingResult, // ModelAttribute 바로 뒤에 와야 함! (순서 중요)
                            RedirectAttributes redirectAttributes,
                            Model model) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());
        // Binding Result는 Target을 가지고 있다.
        // rejectValue, reject를 사용하면 field, object error를 사용하지 않고도 검증 구현 가능.
        // 더 디테일한 내용? errors.properties에 있는 코드 직접 사용 X -> HOW?
        // 결국 필드 에러 대신해서 생성해주게 됨.

        // Validation Logic
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.rejectValue("itemName", "required", "기본 : 상품 이름은 필수입니다");
            // 첫글자만 따서 넣어주면 됨 (규칙?)
            // required.item.itemName
        }

        ValidationUtils.rejectIfEmpty(bindingResult, "itemName", "required");

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.rejectValue("price", "range", new Object[]{1000, 10000000}, null);
        }

        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // ModelAttribute는 값을 자동으로 넣어줌 -> th:object 그대로 남아있음.
        // 메모리상에 남아있는 객체 자료 재활용함. 위에 new Item()으로 선언한 이유임

        // Combinational Validation
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }

        }

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        } // 오류 메세지 두개 모두 보여주고 싶으면 여기에, 타입 미스매치만 표시하고 싶으면 컨트롤러 메소드 맨 위에

        // 에러 없는 경우(아래 내용)

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);


        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item,
                            BindingResult bindingResult, // ModelAttribute 바로 뒤에 와야 함! (순서 중요)
                            RedirectAttributes redirectAttributes,
                            Model model) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());
        // Binding Result는 Target을 가지고 있다.
        // rejectValue, reject를 사용하면 field, object error를 사용하지 않고도 검증 구현 가능.
        // 더 디테일한 내용? errors.properties에 있는 코드 직접 사용 X -> HOW?
        // 결국 필드 에러 대신해서 생성해주게 됨.

//        // Validation Logic
//        if(!StringUtils.hasText(item.getItemName())){
//            bindingResult.rejectValue("itemName", "required", "기본 : 상품 이름은 필수입니다");
//            // 첫글자만 따서 넣어주면 됨 (규칙?)
//            // required.item.itemName
//        }
//
//        ValidationUtils.rejectIfEmpty(bindingResult, "itemName", "required");
//
//        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
//            bindingResult.rejectValue("price", "range", new Object[]{1000, 10000000}, null);
//        }
//
//        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
//            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
//        }
//
//        // ModelAttribute는 값을 자동으로 넣어줌 -> th:object 그대로 남아있음.
//        // 메모리상에 남아있는 객체 자료 재활용함. 위에 new Item()으로 선언한 이유임
//
//        // Combinational Validation
//        if(item.getPrice() != null && item.getQuantity() != null){
//            int resultPrice = item.getPrice() * item.getQuantity();
//            if(resultPrice < 10000){
//                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
//            }
//
//        }

        itemValidator.validate(item, bindingResult);

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        } // 오류 메세지 두개 모두 보여주고 싶으면 여기에, 타입 미스매치만 표시하고 싶으면 컨트롤러 메소드 맨 위에

        // 에러 없는 경우(아래 내용)

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);


        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item,
                            BindingResult bindingResult, // ModelAttribute 바로 뒤에 와야 함! (순서 중요)
                            RedirectAttributes redirectAttributes,
                            Model model) {

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        } // 오류 메세지 두개 모두 보여주고 싶으면 여기에, 타입 미스매치만 표시하고 싶으면 컨트롤러 메소드 맨 위에

        // 에러 없는 경우(아래 내용)

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);


        return "redirect:/validation/v2/items/{itemId}";

    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

