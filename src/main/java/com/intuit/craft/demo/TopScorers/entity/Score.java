package com.intuit.craft.demo.TopScorers.entity;

import java.io.Serializable;


public class Score implements Serializable {
    private static final long serialVersionUID = -5140723103779941071L;
    private String playerName;
    private Long playerScore;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Score() {
    }

    @Override
    public String toString() {
        return "Score{" +
                "playerName='" + playerName + '\'' +
                ", playerScore=" + playerScore +
                '}';
    }

    public void setPlayerScore(Long playerScore) {
        this.playerScore = playerScore;
    }

    public Score(String playerName, Long playerScore) {
        this.playerName = playerName;
        this.playerScore = playerScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Long getPlayerScore() {
        return playerScore;
    }
}
