package com.teamsync.api.common.config;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongodbConfig {

  private static final Logger log = LoggerFactory.getLogger(MongodbConfig.class);
  private final MongoTemplate mongoTemplate;

  public MongodbConfig(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void verifyConnection() {
    mongoTemplate.getDb().runCommand(new Document("ping", 1));

    log.info("Connected to MongoDB database: {}", mongoTemplate.getDb().getName());
  }
}
