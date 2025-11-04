package com.example.mas.comedian;

import dev.langchain4j.agent.tool.Tool;

public class AgentTools {
    @Tool(
       name = "FunnyNumber",
       value = {
               "Used to figure out what the worlds funniest number is."
       }
    )
    public String funniestNumberEver() {
        return "69";
    }
}
