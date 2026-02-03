package com.example.foodfacts.controller;

import com.example.foodfacts.service.FavoriteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 즐겨찾기 컨트롤러
 * - 추가/삭제는 POST
 * - 목록은 GET
 */
@Controller
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    // 즐겨찾기 추가
    @PostMapping("/products/{code}/favorite")
    public String add(
            @PathVariable String code,
            @RequestParam(name = "lang", required = false, defaultValue = "ko") String lang,
            Authentication authentication
    ) {
        favoriteService.add(authentication.getName(), code);
        return "redirect:/products/" + code + "?lang=" + lang;
    }

    // 즐겨찾기 해제
    @PostMapping("/products/{code}/unfavorite")
    public String remove(
            @PathVariable String code,
            @RequestParam(name = "lang", required = false, defaultValue = "ko") String lang,
            Authentication authentication
    ) {
        favoriteService.remove(authentication.getName(), code);
        return "redirect:/products/" + code + "?lang=" + lang;
    }

    // 내 즐겨찾기 목록
    @GetMapping("/me/favorites")
    public String myFavorites(
            Authentication authentication,
            @RequestParam(name = "lang", required = false, defaultValue = "ko") String lang,
            Model model
    ) {
        var listView = favoriteService.listView(authentication.getName(), lang);
        model.addAttribute("favorites", listView);
        model.addAttribute("lang", lang);
        return "my-favorites";
    }
}
