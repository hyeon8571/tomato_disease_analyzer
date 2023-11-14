package tomato.classifier.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tomato.classifier.config.auth.LoginUser;
import tomato.classifier.domain.dto.ArticleDto;
import tomato.classifier.domain.dto.ArticleLikesDto;
import tomato.classifier.domain.entity.User;
import tomato.classifier.domain.type.SearchType;
import tomato.classifier.service.ArticleService;
import tomato.classifier.service.AuthService;
import tomato.classifier.service.PaginationService;

import java.util.List;

import static tomato.classifier.domain.dto.user.UserResDto.*;

@Controller
@Slf4j
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final AuthService authService;

    private final PaginationService paginationService;

    @GetMapping()
    public String articleMain(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "modifiedTime", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        Page<ArticleDto> articles = articleService.searchArticles(searchType, searchValue, pageable);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());

        model.addAttribute("articles", articles);
        model.addAttribute("paginationBarNumbers", barNumbers);
        model.addAttribute("searchTypes", searchType.values());

        return "board/articleMain";
    }

    @GetMapping("/writeForm")
    public String articleAdd(@AuthenticationPrincipal LoginUser loginUser, Model model) {

        LoginResDto loginResDto = authService.findLoginUser(loginUser);

        model.addAttribute("loginUser", loginResDto);

        return "board/articleAdd";
    }

    @GetMapping("/{articleId}")
    public String articleDetail(@AuthenticationPrincipal LoginUser loginUser, @PathVariable Long articleId, Model model) {

        ArticleDto articleDto = articleService.show(articleId);


        if (loginUser != null) {

            LoginResDto loginResDto = authService.findLoginUser(loginUser);

            model.addAttribute("loginUser", loginResDto);

            ArticleLikesDto articleLikesDto = articleService.getLikeOrHateInfo(loginResDto, articleId);

            model.addAttribute("articleLikes", articleLikesDto);

        } else {
            model.addAttribute("loginUser", new LoginResDto(new User()));
        }

        model.addAttribute("article", articleDto);

        model.addAttribute("comments", articleDto.getComments());

        return "board/articleDetail";
    }

    @GetMapping("/{articleId}/editForm")
    public String edit(@PathVariable Long articleId, Model model) {

        ArticleDto articleDto = articleService.show(articleId);

        model.addAttribute("article", articleDto);

        return "board/articleUpdate";
    }


}
