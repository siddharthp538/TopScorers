package com.intuit.craft.demo.TopScorers.strategy;

import com.intuit.craft.demo.TopScorers.entity.PlayerScores;
import com.intuit.craft.demo.TopScorers.entity.Score;
import com.intuit.craft.demo.TopScorers.repository.PlayerScoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.MessageConsumer;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TopFiveScorer implements TopScorer {
    private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);
    private int k = 5;
    private PriorityQueue<Score> priorityQueue;
    private final ConcurrentHashMap<String, Score> playerScoreDataConcurrentHashMap = new ConcurrentHashMap<>();
    PlayerScoreRepository playerScoreRepository;

    @Autowired
    public TopFiveScorer(final PlayerScoreRepository playerScoreRepository) {
        this.playerScoreRepository = playerScoreRepository;
        priorityQueue = new PriorityQueue<>((a, b) -> (int) (a.getPlayerScore() - b.getPlayerScore()));
        init();
    }


    @Override
    public List<Score> getTopK() {
        List<Score> sortedTopScores = new ArrayList<>(priorityQueue);
        sortedTopScores.sort((a, b) -> (int) (b.getPlayerScore() - a.getPlayerScore()));
        return sortedTopScores;
    }

    @Override
    public void produce(final List<Score> playerScoreDataList) {
        for (Score score : playerScoreDataList) {
            if (playerScoreDataConcurrentHashMap.containsKey(score.getPlayerName())) {
                Long currentVal = score.getPlayerScore();
                Long oldVal = playerScoreDataConcurrentHashMap.get(score.getPlayerName()).getPlayerScore();
                log.info("Key {} already exists in HashMap, so will update in instead of inserting.", score.getPlayerName());
                log.info("New Value for Key is: {}" , currentVal + oldVal);
                playerScoreDataConcurrentHashMap.get(score.getPlayerName()).setPlayerScore(oldVal+currentVal);
            } else {
                playerScoreDataConcurrentHashMap.put(score.getPlayerName(), score);
                priorityQueue.add(score);
                if (priorityQueue.size() > k) priorityQueue.poll();
            }
        }
    }

    @Override
    public void init() {
        List<PlayerScores> playerScores = playerScoreRepository.findTopK(k);
        for (PlayerScores playerScore : playerScores) {
            Score currentScore = new Score(playerScore.getName(), playerScore.getScore());
            priorityQueue.add(currentScore);
            playerScoreDataConcurrentHashMap.put(currentScore.getPlayerName(), currentScore);
            if (priorityQueue.size() > k) priorityQueue.poll();
        }
    }
}