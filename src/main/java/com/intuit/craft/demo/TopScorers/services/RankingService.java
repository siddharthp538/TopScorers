package com.intuit.craft.demo.TopScorers.services;

import com.intuit.craft.demo.TopScorers.strategy.TopScorer;
import com.intuit.craft.demo.TopScorers.entity.Score;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class RankingService {
    private final BlockingQueue<Score> scoreDataBlockingQueue = new LinkedBlockingDeque<>();
    private final List<TopScorer> topScorers = new ArrayList<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);

    public RankingService() {

    }

    public void produce(final Score score) {
        scoreDataBlockingQueue.add(score);
    }

    public void registerTopScorer(final TopScorer topScorer) {
        topScorers.add(topScorer);
    }

    public void scheduleScoreConsumer() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            List<Score> playerScoreDataList = new ArrayList<>();
            scoreDataBlockingQueue.drainTo(playerScoreDataList);
            topScorers.forEach(topScorer -> topScorer.produce(playerScoreDataList));
        }, 1, 1, TimeUnit.SECONDS);
    }
}