package hello.itemservice.web.basic;

import hello.itemservice.domain.Item;
import hello.itemservice.domain.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @RequiredArgsConsturctor : 아래 1,2 와 같음(itemRepository Bean 등록, 생성자 주입
 *
 * private final ItemRepository itemRepository;
 *
 * ```java
 *
 * 1.
 * private final ItemRepository itemRepository;
 *
 * @Autowired
 * public BasicItemRepository(ItemRespository itemRepository) {
 *      this.itemRepository = itemRepository;
 * }
 *
 * 2.
 * private final ItemRepository itemRepository;
 *
 * public BasicItemRepository(ItemRepository itemRepository) {
 *     this.itemRepository = itemRepository;
 * }
 *
 * BasicItemController(Item)
 * ```
 * */

@Slf4j
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    /**
     * @ModelAttribute
     * 일반적으로 사용되는 ModelAttribute 와 성격이 다르고,
     * BasicItemController 에서 request 가 수행될 때마다 속성인 regions 를 key 로 return 된 값이 value 로 담기게 된다.
     *
     * 이 경우 최적화를 위해 regions 의 내용이 변하지 않으면 static 한 공간에 넣고 쓰는 것이 좋음(큰 성능 차이는 딱히 없음)
     * */
    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>(); // 순서 보장을 위한 linkedHashMap 사용
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {

        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "basic/addForm";
    }

    /**
     * redirectAttribute
     * - {itemId} : addAttribute 한 이름으로 savedItem.getId() 치환
     * - status : parameter 형식으로 붙고, true 면 save 처리 후에 전달하는 개념
     * ex) /basic/items/1?status=true
     * -> 리다이렉트 된 페이지에 ${param.status} 로 반환 가능
     */
    @PostMapping("/add")
    public String save(Item item, RedirectAttributes redirectAttributes) {
        log.info("item.open={}", item.getOpen());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }


    /**
     * 테스트용 데이터
     * @PostContruct : Bean 이 초기화되고 의존성 주입이 이루어진 후 딱 한 번 초기화를 수행함, Service 로직을 타기전에 수행함
     * */
    @PostConstruct
    public void init() {

        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
