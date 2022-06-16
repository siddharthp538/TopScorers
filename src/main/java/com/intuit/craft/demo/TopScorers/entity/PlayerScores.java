package com.intuit.craft.demo.TopScorers.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "player_scores")
public class PlayerScores {

    @Id
    @Column(name = "name", columnDefinition = "String(2) default 'yo'")
    String name; // name of the player

    @Column(name = "score", columnDefinition = "Long(1) default 0")
    Long score; // score of the player

    public PlayerScores() {
    }

    public PlayerScores(String player_name, long player_score) {
        this.name = player_name;
        this.score = player_score;
    }

    public String getName() {
        return name;
    }

    public Long getScore() {
        return score;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerScores that = (PlayerScores) o;
        return Objects.equals(name, that.name) && Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, score);
    }
}