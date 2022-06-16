package com.intuit.craft.demo.TopScorers.config;

import com.intuit.craft.demo.TopScorers.strategy.TopFiveScorer;
import com.intuit.craft.demo.TopScorers.services.RankingService;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

@EnableJms
@Configuration
public class AppConfig {

    @Autowired
    private RankingService rankingService;
    @Autowired
    private TopFiveScorer topFiveScorer;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
        return jmsListenerContainerFactory;
    }
    @Bean
    public SmartInitializingSingleton initialize() {
        return () -> {
            rankingService.registerTopScorer(topFiveScorer);
            rankingService.scheduleScoreConsumer();
        };
    }
}
