package com.example.mas.shared;

import io.a2a.A2A;
import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.server.agentexecution.RequestContext;
import io.a2a.server.events.EventQueue;
import io.a2a.server.events.InMemoryQueueManager;
import io.a2a.server.events.QueueManager;
import io.a2a.server.requesthandlers.DefaultRequestHandler;
import io.a2a.server.requesthandlers.RequestHandler;
import io.a2a.server.tasks.*;
import io.a2a.spec.JSONRPCError;
import io.a2a.spec.UnsupportedOperationError;
import io.a2a.transport.grpc.handler.CallContextFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class GrpcServiceConfiguration {
    @Bean
    public A2AGrpcServerService serverService(RequestHandler requestHandler, AgentCardProvider agentCardProvider) {
    // public A2AGrpcServerService serverService(RequestHandler requestHandler, AgentCardProvider agentCardProvider, CallContextFactory callContextFactory) {
        // return new A2AGrpcServerService(requestHandler, agentCardProvider.get(), callContextFactory);
        return new A2AGrpcServerService(requestHandler, agentCardProvider.get());
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
    public AgentExecutor agentExecutor() {
        return new AgentExecutor() {
            @Override
            public void execute(RequestContext context, EventQueue eventQueue) throws JSONRPCError {
                eventQueue.enqueueEvent(A2A.toAgentMessage("Hello World"));
            }

            @Override
            public void cancel(RequestContext context, EventQueue eventQueue) throws JSONRPCError {
                throw new UnsupportedOperationError();
            }
        };
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
