package com.intuit.craft.demo.TopScorers.controllers;

import com.intuit.craft.demo.TopScorers.strategy.TopFiveScorer;
import com.intuit.craft.demo.TopScorers.entity.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ScoreController {

    private TopFiveScorer topFiveScorer;

    @Autowired
    public ScoreController(TopFiveScorer topFiveScorer) {
        this.topFiveScorer = topFiveScorer;
    }

    @GetMapping
    @RequestMapping(value = "/getTop5Score")
    public List<Score> getTop5Score() {
        return topFiveScorer.getTopK();
    }
}
