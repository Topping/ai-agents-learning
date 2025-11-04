package com.example.mas.comedian;

import com.example.mas.shared.CustomGrpcThing;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.models.langchain4j.LangChain4j;
import com.google.adk.web.AgentLoader;
import com.google.common.collect.ImmutableList;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import io.a2a.server.PublicAgentCard;
import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.spec.AgentCapabilities;
import io.a2a.spec.AgentCard;
import io.a2a.spec.AgentSkill;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@CustomGrpcThing
public class AgentConfiguration {
    @Value("${OPENAI_KEY}")
    private String openAiAPIKey;

    @Bean
    @PublicAgentCard
    public AgentCard agentCard() {
        return new AgentCard.Builder()
                .name("Comedian")
                .description("Funny guy that makes great jokes")
                .url("http://localhost:9090")
                .version("1.0.0")
                .preferredTransport("GRPC")
                .defaultInputModes(List.of("text"))
                .defaultOutputModes(List.of("text"))
                .capabilities(
                        new AgentCapabilities.Builder()
                                .pushNotifications(true)
                                .build()
                )
                .skills(
                        List.of(
                                new AgentSkill.Builder()
                                        .id("jokester")
                                        .name("Writes Jokes")
                                        .description("Tells funny jokes about any subject area")
                                        .tags(List.of("comedy", "jokes", "fun"))
                                        .examples(
                                                List.of("Write a funny joke", "Make me laugh", "I like turtles")
                                        )
                                        .build()
                        )
                )
                .build();
    }

    @Bean
    public StreamingChatModel openAiStreamingModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(openAiAPIKey)
                .modelName(OpenAiChatModelName.GPT_5_NANO)
                .build();
    }

    @Bean
    public ChatModel chatModel() {
        return OpenAiChatModel.builder()
                .apiKey(openAiAPIKey)
                .modelName(OpenAiChatModelName.GPT_5_NANO)
                .build();
    }

    @Bean
    public BaseAgent comedianAgent(ChatModel chatModel, StreamingChatModel streamingChatModel) {
        LangChain4j wrapper = new LangChain4j(chatModel, streamingChatModel, OpenAiChatModelName.GPT_5_NANO.name());

        return LlmAgent.builder()
                .name("Funny Guy")
                .description("Funny guy that makes great jokes.")
                .instruction(
                        "You make funny jokes"
                )
                .model(wrapper)
                .build();
    }

    @Bean
    public ComedianAgent agent(ChatModel chatModel, StreamingChatModel streamingChatModel) {
        return AiServices.builder(ComedianAgent.class)
                .chatModel(chatModel)
                .streamingChatModel(streamingChatModel)
                .tools(new AgentTools())
                .build();
    }

    @Bean
    public AgentLoader agentLoader(Map<String, BaseAgent> agents) {
        return new CustomAgentLoader(agents);
    }

    @Bean
    public AgentExecutor agentExecutor(ComedianAgent agent) {
        return new ComedianExecutor(agent);
    }

    public static class CustomAgentLoader implements AgentLoader {
        private final Map<String, BaseAgent> agents;

        public CustomAgentLoader(Map<String, BaseAgent> agents) {
            this.agents = agents;
        }

        @NotNull
        @Override
        public ImmutableList<String> listAgents() {
            return ImmutableList.copyOf(agents.keySet());
        }

        @Override
        public BaseAgent loadAgent(String name) {
            return agents.get(name);
        }
    }
}
