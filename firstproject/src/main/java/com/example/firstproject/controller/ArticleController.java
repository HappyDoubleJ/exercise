package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class ArticleController {
    @Autowired

    private ArticleRepository articleRepository;
    @GetMapping("articles/new")
    public String randomQuote(Model model) {
        String[] quotes = {
                "내가 헛되이 보낸 오늘 하루는 어제 죽어간 이들이 그토록 바라던 하루이다." +
                        "-소포클레스-",
                "변명 중에서도 가장 어리석고 못난 변명은 \"시간이 없어서\"라는 변명이다." +
                        "-에디슨-",
                "시간을 지배할 줄 아는 사람은 인생을 지배할 줄 아는 사람이다." +
                        "-에센 바흐-",
                "시간의 걸음걸이에는 세가지가 있다.\n 미래는 주저하면서 다가오고, 현재는 화살처럼 날라가고, 과거는 영원히 정지하고 있다" +
                        "-F.실러-",
                "시간을 선택하는 것은 시간을 절약하는 것이다" +
                        "-베이컨-",
                "오늘의 식사는 내일로 미루지 않으면서 오늘 할 일은 내일로 미루는 사람이 많다." +
                        "-C.힐티-",
                "전력을 다해서 시간에 대항하라" +
                        "-톨스토이-",
                "세월은 모두에게 공평하게 주어진 자본금이다. 이 자본을 잘 이용한 사람에겐 승리가 있다." +
                        "-아뷰난드-",
                "내일을 위한 최선의 준비는 오늘의 일을 모두 마치는 것이다." +
                        "-W.오슬러-",
                "인생은 흘러가는 것이 아니라 채워지는 것이다. 우리는 하루 하루를 보내는 것이 아니라 내가 가진 무엇으로 채워가는 것이다." +
                        "-러스킨-"

        };
        int randInt = (int) (Math.random() * quotes.length);
        model.addAttribute("randomQuote",quotes[randInt]);
        return "articles/new";
    }

    @PostMapping("articles/create")
    public String createArticle(ArticleForm form) {
        log.info(form.toString());
        //System.out.println(form.toString());
        //1.DTO를 엔티티로 변환
        Article article = form.toEntity();
        //2.리파지터리로 엔티티를 DB에 저장
        log.info(article.toString());
        //System.out.println(article.toString()); //DTO가 엔티티로 잘 변환되는지 확인 출력
        //2.리파지터리로 엔티티를 DB에 저장
        Article saved =articleRepository.save(article);
        log.info(saved.toString());
        // System.out.println(saved.toString()); //article이 DB에 잘 저장되는지 확인
        // to string()은 출력객체가 가지고 있는 정보나 값들을 문자열로 만들어 리턴하는 메소드이다.

        return "redirect:/articles/" + saved.getId(); //리다이렉트

    }
    @GetMapping("articles/{id}")
    public String show(@PathVariable Long id,Model model) {
        log.info("id = " + id);
        //id를 조회해 데이터를 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);

        //모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);
        //뷰 페이지 반환하기
        return "articles/show";
    }
    @GetMapping("articles")
    public String index(Model model) {
        List<Article> articleEntityList = articleRepository.findAll();
        model.addAttribute("articleList", articleEntityList);
        return "articles/index";

    }
    @GetMapping("/articles/{id}/edit") //뷰페이지에서 변수 사용할 때는 중괄호 두개, 컨트롤러에서 url 변수 쓸때는 중괄호 하나.
    public String edit(@PathVariable Long id, Model model) {
        Article articleEntity =articleRepository.findById(id).orElse(null);
        model.addAttribute("article", articleEntity);
        return "articles/edit";

    }
    @PostMapping("articles/update")
    public String update(ArticleForm form) {
        log.info(form.toString());
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        if (target != null) {
            articleRepository.save(articleEntity);
        }

        return "redirect:/articles/" + articleEntity.getId();

    }
    @GetMapping("articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        log.info("삭제 요청이 들어왔습니다.");
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());
        if (target != null){
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제완료");
        }
        return "redirect:/articles";

    }
}
