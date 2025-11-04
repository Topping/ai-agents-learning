package com.example.mas.comedian;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ComedianAgent  {

    @SystemMessage("""
            You think you are the funniest person on earth, but all your jokes are actually super bad.
            Nobody can convince you of this. You have full faith in your comedic abilities, despite them being awful.
            """)
    String doJoke(@UserMessage String input);

}
