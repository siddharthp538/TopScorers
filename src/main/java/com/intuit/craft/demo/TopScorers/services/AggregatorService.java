package com.intuit.craft.demo.TopScorers.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.craft.demo.TopScorers.entity.PlayerScores;
import com.intuit.craft.demo.TopScorers.entity.Score;
import com.intuit.craft.demo.TopScorers.repository.PlayerScoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.MessageConsumer;

@Service
public class AggregatorService {

    private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private PlayerScoreRepository playerScoreRepository;

    @Autowired
    private RankingService rankingService;

    private ObjectMapper objectMapper;

    public AggregatorService() {
        objectMapper = new ObjectMapper();
    }

    @JmsListener(destination = "gaming-scores")
    public void fetchAndAggregateFromStore(String scoreJson) {
        Score score = null;
        try {
            score = objectMapper.readValue(scoreJson, Score.class);
        } catch (JsonProcessingException e) {
            log.error(String.format("Failed while parsing score object for json [%1s]", score), e);
        }
        log.info("Message received: {}", scoreJson);
        playerScoreRepository.insertInScores(score.getPlayerName(), score.getPlayerScore());
        PlayerScores playerScores = playerScoreRepository.findById(score.getPlayerName()).orElseThrow(RuntimeException::new);
        final Score playerScore = new Score();
        playerScore.setPlayerScore(playerScores.getScore());
        playerScore.setPlayerName(playerScores.getName());
        rankingService.produce(playerScore);
    }
}
