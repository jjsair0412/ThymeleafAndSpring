package hello.itemservice.web.form;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    @ModelAttribute("regions")
    public Map<String, String> regions(){
        // 순서를 보장하기 위해 LinkedHashMap 사용
        Map<String,String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes(){
        // Enum의 values 메서드는 Enum 클래스의 모든 정보를 배열로 반환한다.

        return ItemType.values();
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);

        // 순서를 보장하기 위해 LinkedHashMap 사용
//        Map<String,String> regions = new LinkedHashMap<>();
//        regions.put("SEOUL", "서울");
//        regions.put("BUSAN", "부산");
//        regions.put("JEJU", "제주");
//        model.addAttribute("regions",regions);

        model.addAttribute("item", item);
        return "form/item";
    }

    /**
     * 타임리프를 사용하고 싶다면, 아무런 값이 없는 Item이라도 model로 넘겨줘야한다.
     * model.addAttribute("item",new Item());
     * 여기서 Item이란 dto나 entity같이 데이터를 전달하는 역할을 한다.
     * 상품명과 가격, 상품번호등이 저장되는 프로퍼티이다.
     *
     * 빈객체 하나를 넘겨준다고해서 큰 변화가 일어나거나 비용이 많이 일어나지 않는다.
     * 그니까 괜찮당
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item",new Item());

        // 순서를 보장하기 위해 LinkedHashMap 사용
//        Map<String,String> regions = new LinkedHashMap<>();
//        regions.put("SEOUL", "서울");
//        regions.put("BUSAN", "부산");
//        regions.put("JEJU", "제주");
//        model.addAttribute("regions",regions);

        return "form/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        log.info("item.open={}",item.getOpen());
        log.info("item.regions={}",item.getRegions());
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    /**
     * edit에서는 이미 item을 넘겨주고 있다.
     * 타임리프를 사용하기 위해 빈 item을 넘길 필요가 없다.
     */
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);

        // 순서를 보장하기 위해 LinkedHashMap 사용
//        Map<String,String> regions = new LinkedHashMap<>();
//        regions.put("SEOUL", "서울");
//        regions.put("BUSAN", "부산");
//        regions.put("JEJU", "제주");
//        model.addAttribute("regions",regions);

        model.addAttribute("item", item);
        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

}

