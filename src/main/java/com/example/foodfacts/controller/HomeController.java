package com.example.foodfacts.controller;

import com.example.foodfacts.service.FoodSearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 메인 페이지 Controller
 * - 이번 단계에서는 "카테고리 검색 결과를 화면에 뿌리기"만 합니다.
 */
@Controller
public class HomeController {

    private final FoodSearchService foodSearchService;

    public HomeController(FoodSearchService foodSearchService) {
        this.foodSearchService = foodSearchService;
    }

    /**
     * 예)
     *  - http://localhost:8080/?category=Orange Juice
     *
     * category 파라미터가 없으면 기본값을 사용합니다.
     */
    @GetMapping("/")
    public String home(
            @RequestParam(name = "category", required = false, defaultValue = "Orange Juice") String category,
            Model model
    ) {
        // 1) API에서 데이터 가져오기 (Service가 처리)
        var products = foodSearchService.getProductsByCategory(category);

        // 2) 화면에 뿌릴 데이터(Model)에 담기
        model.addAttribute("category", category);
        model.addAttribute("products", products);

        // 3) templates/home.html 렌더링
        return "home";
    }
}
