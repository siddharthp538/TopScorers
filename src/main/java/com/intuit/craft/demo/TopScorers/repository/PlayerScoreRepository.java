package com.intuit.craft.demo.TopScorers.repository;

import com.intuit.craft.demo.TopScorers.entity.PlayerScores;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface PlayerScoreRepository extends CrudRepository<PlayerScores, String> {
    @Modifying
    @Transactional
    @Query(value = "INSERT into player_scores ( name , score ) values ( :playerName , :playerScore ) on DUPLICATE KEY UPDATE score = score + :playerScore", nativeQuery = true)
    void insertInScores(@Param("playerName") String playerName, @Param("playerScore") Long playerScore);

    @Modifying
    @Transactional
    @Query(value = "SELECT *  FROM player_scores ORDER BY score DESC LIMIT :k", nativeQuery = true)
    List<PlayerScores> findTopK(@Param("k") Integer k);
}