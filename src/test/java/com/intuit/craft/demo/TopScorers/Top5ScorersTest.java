package com.intuit.craft.demo.TopScorers;

import com.intuit.craft.demo.TopScorers.entity.PlayerScores;
import com.intuit.craft.demo.TopScorers.entity.Score;
import com.intuit.craft.demo.TopScorers.repository.PlayerScoreRepository;
import com.intuit.craft.demo.TopScorers.strategy.TopFiveScorer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.MessageConsumer;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class Top5ScorersTest {
    private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);

    @Mock
    private PlayerScoreRepository playerScoreRepository;


    private TopFiveScorer topFiveScorer;

    @Before
    public void setUp() throws Exception {
        List<PlayerScores> playerScores = getPlayerScores();
        Mockito.when(playerScoreRepository.findTopK(Mockito.eq(5))).thenReturn(playerScores);
        topFiveScorer = new TopFiveScorer(playerScoreRepository);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFetchTop5Scorers() {

        List<Score> topFivePlayers = topFiveScorer.getTopK();
        log.info("Top Five Players size: {}", topFivePlayers.size());
        List<PlayerScores> playerScores = getPlayerScores();
        for (int i = 0; i < topFivePlayers.size(); i++) {
            Assert.assertEquals(playerScores.get(i).getScore(), topFivePlayers.get(i).getPlayerScore());
            Assert.assertEquals(playerScores.get(i).getName(), topFivePlayers.get(i).getPlayerName());
        }
    }



    private List<PlayerScores> getPlayerScores() {
        List<PlayerScores> scores = new ArrayList<>();
        scores.add(new PlayerScores("Abhinav", 1000000000L));
        scores.add(new PlayerScores("George", 10040300L));
        scores.add(new PlayerScores("Sid", 138007L));
        scores.add(new PlayerScores("Pier", 50000L));
        scores.add(new PlayerScores("Tom", 31002L));
        return scores;
    }
}
