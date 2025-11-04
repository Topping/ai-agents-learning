package com.example.mas.comedian;

import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.server.agentexecution.RequestContext;
import io.a2a.server.events.EventQueue;
import io.a2a.server.tasks.TaskUpdater;
import io.a2a.spec.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComedianExecutor implements AgentExecutor {
    private final ComedianAgent agent;

    public ComedianExecutor(ComedianAgent agent) {
        this.agent = agent;
    }

    @Override
    public void execute(RequestContext context, EventQueue eventQueue) throws JSONRPCError {
        final TaskUpdater taskUpdater = new TaskUpdater(context, eventQueue);

        if(context.getTask() == null) {
            taskUpdater.submit();
        }
        taskUpdater.startWork();
        String prompt = extractTextMessage(context.getMessage());
        String result = agent.doJoke(prompt);
        final TextPart responsePart = new TextPart(result, null);
        final List<Part<?>> parts = List.of(responsePart);
        taskUpdater.addArtifact(parts);
        taskUpdater.complete();
    }

    private String extractTextMessage(final Message message) {
        final StringBuilder textBuilder = new StringBuilder();
        if (message.getParts() != null) {
            for (final Part<?> part : message.getParts()) {
                if (part instanceof TextPart text) {
                    textBuilder.append(text.getText());
                }
            }
        }
        return textBuilder.toString();
    }

    @Override
    public void cancel(RequestContext context, EventQueue eventQueue) throws JSONRPCError {
        final Task task = context.getTask();

        if (task.getStatus().state() == TaskState.CANCELED) {
            // task already cancelled
            throw new TaskNotCancelableError();
        }

        if (task.getStatus().state() == TaskState.COMPLETED) {
            // task already completed
            throw new TaskNotCancelableError();
        }

        // cancel the task
        final TaskUpdater updater = new TaskUpdater(context, eventQueue);
        updater.cancel();
    }
}
