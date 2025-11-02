package com.example.mas.shared;

import io.a2a.spec.AgentCard;

@FunctionalInterface
public interface AgentCardProvider {
    AgentCard get();
}
