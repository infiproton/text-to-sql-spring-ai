package com.nexacorp.ai.texttosql.service;

import com.nexacorp.ai.texttosql.dto.TextToSqlRequest;
import com.nexacorp.ai.texttosql.dto.TextToSqlResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TextToSqlService {

    private final PromptBuilder promptBuilder;
    private final SqlExecutionService sqlExecutionService;
    private final ChatClient chatClient;

    public TextToSqlResponse handle(TextToSqlRequest request) {

        String prompt = promptBuilder.buildPrompt(request.getQuestion());
        log.info("Prompting text to sql request: {}", prompt);

        String generatedSql = chatClient.prompt(prompt)
                .call().content();
        generatedSql = normalizeSql(generatedSql);

        log.info("Generated SQL: {}", generatedSql);

        List<Map<String,Object>> rows = sqlExecutionService.execute(generatedSql);
        return new TextToSqlResponse(generatedSql, rows);
    }

    private String normalizeSql(String sql) {
        return sql
                .replaceAll("\\s+", " ")
                .trim();
    }
}
