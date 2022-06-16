package com.intuit.craft.demo.TopScorers.strategy;

import com.intuit.craft.demo.TopScorers.entity.Score;

import java.util.List;


public interface TopScorer {
    List<Score> getTopK();

    void produce(List<Score> playerScoreDataList);

    void init();
}
