package com.example.mas.shared;

import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.server.events.InMemoryQueueManager;
import io.a2a.server.events.QueueManager;
import io.a2a.server.requesthandlers.DefaultRequestHandler;
import io.a2a.server.requesthandlers.RequestHandler;
import io.a2a.server.tasks.*;
import io.a2a.spec.AgentCard;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class GrpcServiceConfiguration {
    @Bean
    public A2AGrpcServerService serverService(RequestHandler requestHandler, AgentCard agentCard) {
        return new A2AGrpcServerService(requestHandler, agentCard);
    }

    @Bean
    public TaskStore taskStore() {
        return new InMemoryTaskStore();
    }

    @Bean
    public QueueManager queueManager(InMemoryTaskStore taskStore) {
        return new InMemoryQueueManager(taskStore);
    }

    @Bean
    public PushNotificationConfigStore pushNotificationConfigStore() {
        return new InMemoryPushNotificationConfigStore();
    }

    @Bean
    public PushNotificationSender pushNotificationSender(PushNotificationConfigStore configStore) {
        return new BasePushNotificationSender(configStore);
    }

    @Bean(name = "RequestHandlerThreadExecutor")
    public Executor requestHandlerThreadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public RequestHandler a2aRequestHandler(
            AgentExecutor agentExecutor,
            TaskStore taskStore,
            QueueManager queueManager,
            PushNotificationConfigStore pushNotificationConfigStore,
            PushNotificationSender pushNotificationSender,
            @Qualifier("RequestHandlerThreadExecutor") Executor requestHandlerThreadExecutor
    ) {
        return new DefaultRequestHandler(
                agentExecutor,
                taskStore,
                queueManager,
                pushNotificationConfigStore,
                pushNotificationSender,
                requestHandlerThreadExecutor
        );
    }
}
