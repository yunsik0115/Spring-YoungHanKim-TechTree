package hello.itemservice.web.validation;

import com.sun.jdi.Field;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
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
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {
    // LocalValidatorFactoryBean - 애노테이션들을 보고 검증 로직을 구성해줌
    // Global Validator로 등록되어있어 @Validated만 사용해주면 검증이 됨!

    private final ItemRepository itemRepository;


    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }


    //@PostMapping("/add")
    public String addItem(@Validated @ModelAttribute Item item, // Bean Validation이 그냥 적용됨.
                          BindingResult bindingResult, // ModelAttribute 바로 뒤에 와야 함! (순서 중요)
                          RedirectAttributes redirectAttributes,
                          Model model) {

        if(item.getPrice() != null && item.getQuantity() != null){ // 오브젝트 관련된 건 따로 처리하고 Method Extraction으로 가독성 향상
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }

        }

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v4/addForm";
        } // 오류 메세지 두개 모두 보여주고 싶으면 여기에, 타입 미스매치만 표시하고 싶으면 컨트롤러 메소드 맨 위에

        // 에러 없는 경우(아래 내용)

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);


        return "redirect:/validation/v4/items/{itemId}";

    }

    @PostMapping("/add")
    public String addItemV2(@Validated @ModelAttribute("item") ItemSaveForm form, // Bean Validation이 그냥 적용됨.
                            BindingResult bindingResult, // ModelAttribute 바로 뒤에 와야 함! (순서 중요)
                            RedirectAttributes redirectAttributes,
                            Model model) {

        if(form.getPrice() != null && form.getQuantity() != null){ // 오브젝트 관련된 건 따로 처리하고 Method Extraction으로 가독성 향상
            int resultPrice = form.getPrice() * form.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }

        }

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v4/addForm";
        } // 오류 메세지 두개 모두 보여주고 싶으면 여기에, 타입 미스매치만 표시하고 싶으면 컨트롤러 메소드 맨 위에

        // 에러 없는 경우(아래 내용)

        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setQuantity(form.getPrice());
        item.setQuantity(form.getQuantity());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);


        return "redirect:/validation/v4/items/{itemId}";

    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

    //@PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {

        if(item.getPrice() != null && item.getQuantity() != null){ // 오브젝트 관련된 건 따로 처리하고 Method Extraction으로 가독성 향상
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }

        }

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v4/editForm";
        } // 오류 메세지 두개 모두 보여주고 싶으면 여기에, 타입 미스매치만 표시하고 싶으면 컨트롤러 메소드 맨 위에


        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {

        if(form.getPrice() != null && form.getQuantity() != null){ // 오브젝트 관련된 건 따로 처리하고 Method Extraction으로 가독성 향상
            int resultPrice = form.getPrice() * form.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }

        }

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v4/editForm";
        } // 오류 메세지 두개 모두 보여주고 싶으면 여기에, 타입 미스매치만 표시하고 싶으면 컨트롤러 메소드 맨 위에

        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }

}

