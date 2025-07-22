package com.example.featureflag.config;

import com.mongodb.client.MongoClient;
import org.ff4j.FF4j;
import org.ff4j.core.Feature;
import org.ff4j.core.FlippingStrategy;
import org.ff4j.mongo.store.FeatureStoreMongo;
import org.ff4j.mongo.store.PropertyStoreMongo;
import org.ff4j.strategy.PonderationStrategy;
import org.ff4j.strategy.WhiteListStrategy;
import org.ff4j.strategy.time.OfficeHourStrategy;
import org.ff4j.strategy.time.ReleaseDateFlipStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class FF4JConfig {

    @Bean
    public FF4j ff4j(MongoClient mongoClient) {
        FF4j ff4j = new FF4j();

        // Setup MongoDB stores
        ff4j.setFeatureStore(new FeatureStoreMongo(mongoClient, "ff4j_demo", "features"));
        ff4j.setPropertiesStore(new PropertyStoreMongo(mongoClient, "ff4j_demo", "properties"));

        // Initialize features with different strategies
        setupPonderationStrategy(ff4j);
        setupReleaseDateStrategy(ff4j);
        setupWhiteListStrategy(ff4j);
        setupTimeBasedStrategy(ff4j);
        setupHardwareStrategy(ff4j);

        return ff4j;
    }

    private void setupPonderationStrategy(FF4j ff4j) {
        String featureName = "PS5_PONDERATION";
        if (!ff4j.exist(featureName)) {
            Feature feature = new Feature(featureName);

            // Setup percentage rollout strategy (50% of users)
            feature.setFlippingStrategy(new PonderationStrategy(0.5));

            feature.enable();
            ff4j.createFeature(feature);
        }
    }

    private void setupReleaseDateStrategy(FF4j ff4j) {
        String featureName = "PS5_RELEASE_DATE";
        if (!ff4j.exist(featureName)) {
            Feature feature = new Feature(featureName);

            feature.setFlippingStrategy(new ReleaseDateFlipStrategy("2026-01-01-03:00"));

            feature.enable();
            ff4j.createFeature(feature);
        }
    }

    private void setupWhiteListStrategy(FF4j ff4j) {
        String featureName = "PS5_WHITE_LIST";
        if (!ff4j.exist(featureName)) {
            Feature feature = new Feature(featureName);

            feature.setFlippingStrategy(new WhiteListStrategy("user1,user2"));

            feature.enable();
            ff4j.createFeature(feature);
        }
    }

    private void setupTimeBasedStrategy(FF4j ff4j) {
        String featureName = "PS5_BUSINESS_HOURS";
        if (!ff4j.exist(featureName)) {
            Feature feature = new Feature(featureName);

            // Setup office hours strategy (9 AM to 5 PM)
            Map<String, String> initParams = new HashMap<>();
            initParams.put("sunday", "05:00-06:00");
            initParams.put("monday", "09:00-10:00");
            initParams.put("tuesday", "09:00-17:00");
            initParams.put("wednesday", "09:00-17:00");
            initParams.put("thursday", "20:00-21:00");

            FlippingStrategy strategy = new OfficeHourStrategy();
            strategy.init(featureName, initParams);
            feature.setFlippingStrategy(strategy);

            feature.enable();
            ff4j.createFeature(feature);
        }
    }

    private void setupHardwareStrategy(FF4j ff4j) {
        String featureName = "PS5_HARDWARE";
        if (!ff4j.exist(featureName)) {
            Feature feature = new Feature(featureName);

            Map<String, String> params = new HashMap<>();
            params.put("minHardwareVersion", "3.0");
            params.put("requiredRAM", "8GB");
            FlippingStrategy strategy = new HardwareCapabilityStrategy();
            strategy.init(featureName, params);
            feature.setFlippingStrategy(strategy);

            feature.enable();
            ff4j.createFeature(feature);
        }
    }
}
