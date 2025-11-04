package com.example.mas.shared;

import io.a2a.server.requesthandlers.RequestHandler;
import io.a2a.spec.AgentCard;
import io.a2a.transport.grpc.handler.CallContextFactory;
import io.a2a.transport.grpc.handler.GrpcHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@GrpcService
public class A2AGrpcServerService extends GrpcHandler {
    private final RequestHandler requestHandler;
    private final AgentCard agentCard;
    private final Executor executor;

    @Autowired
    public A2AGrpcServerService(RequestHandler requestHandler, AgentCard agentCard) {
        this.requestHandler = requestHandler;
        this.agentCard = agentCard;
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }


    @Override
    protected RequestHandler getRequestHandler() {
        return requestHandler;
    }

    @Override
    protected AgentCard getAgentCard() {
        return agentCard;
    }

    @Override
    protected CallContextFactory getCallContextFactory() {
        return null;
    }

    @Override
    protected Executor getExecutor() {
        return executor;
    }
}
