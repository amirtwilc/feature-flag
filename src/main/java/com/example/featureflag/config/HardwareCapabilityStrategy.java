package com.example.featureflag.config;

import org.ff4j.core.FeatureStore;
import org.ff4j.core.FlippingExecutionContext;
import org.ff4j.strategy.AbstractFlipStrategy;

import java.util.HashMap;
import java.util.Map;

public class HardwareCapabilityStrategy extends AbstractFlipStrategy {

    @Override
    public void init(String featureName, Map<String, String> initParam) {
        this.initParams = initParam;
    }

    @Override
    public boolean evaluate(String featureName, FeatureStore featureStore, FlippingExecutionContext context) {
        double hardwareVersion = context.getDouble("hardwareVersion", true);
        int availableRAM = context.getInt("availableRAM", true);

        // Check if hardware meets minimum requirements
        return hardwareVersion >= Double.parseDouble(initParams.get("minHardwareVersion")) &&
                availableRAM >= Integer.parseInt(initParams.get("requiredRAM").replace("GB", ""));
    }
}
