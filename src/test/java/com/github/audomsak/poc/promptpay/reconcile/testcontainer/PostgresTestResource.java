package com.github.audomsak.poc.promptpay.reconcile.testcontainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import lombok.extern.jbosslog.JBossLog;

import java.util.Map;

@JBossLog
public class PostgresTestResource implements QuarkusTestResourceLifecycleManager {
    @Override
    public Map<String, String> start() {
        //TODO: code implementation
        return null;
    }

    @Override
    public void stop() {
        //TODO: code implementation
    }
}
