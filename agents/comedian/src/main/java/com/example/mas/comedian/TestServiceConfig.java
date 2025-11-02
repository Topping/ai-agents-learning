package com.example.mas.comedian;

import com.example.mas.shared.AgentCardProvider;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import io.a2a.spec.AgentCapabilities;
import io.a2a.spec.AgentCard;
import io.a2a.spec.AgentSkill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TestServiceConfig {
    private static final String NAME = "jokester";
    private static final String DESCRIPTION = "Creates funny jokes, puns, limerics and other things to make people laugh.";

    @Bean
    public AgentCardProvider agentCardProvider() {
        return this::getComedianAgentCard;
    }

    @Bean
    public BaseAgent comedianAgent() {
        return LlmAgent.builder()
                .name(NAME)
                .description(DESCRIPTION)
                .instruction(
                        """
                        You are a funny comedian, who knows how to make any subject funny. You understand what kind of jokes would make most sense based on the subject matter.
                        If available, you can delegate to other agents who are specialized in different types of jokes.
                        
                        You should find the subject matter in the user request, decide what type of joke would be most appropriate, and if a specialized agent exists for that kind of joke, delegate your work to that specific agent.
                        """
                )
                .tools()
                .build();
    }

    private AgentCard getComedianAgentCard() {
        var builder = new AgentCard.Builder();
        // Identity
        builder.name("Comedian")
                .description("Knows how to make funny jokes")
                .capabilities(capabilities())
                .skills(skills())
                .url("http://localhost:9090")
                .version("1.0.0")
                .preferredTransport("GRPC")
                .defaultInputModes(List.of("text"))
                .defaultOutputModes(List.of("text"));


        return builder.build();
    }

    private AgentCapabilities capabilities() {
        return new AgentCapabilities.Builder()
                .pushNotifications(true)
                .build();
    }

    private List<AgentSkill> skills() {
        return List.of(
                new AgentSkill.Builder()
                        .id(NAME)
                        .name(NAME)
                        .description(DESCRIPTION)
                        .tags(List.of("comedy", "jokes", "fun"))
                        .examples(List.of("Write a funny joke", "Make me laugh"))
                        .build()
        );
    }
}
