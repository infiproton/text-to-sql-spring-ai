package com.nexacorp.ai.texttosql.service;

import com.nexacorp.ai.texttosql.dto.TextToSqlRequest;
import com.nexacorp.ai.texttosql.dto.TextToSqlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TextToSqlService {

    private final PromptBuilder promptBuilder;
    private final SqlExecutionService sqlExecutionService;

    public TextToSqlResponse handle(TextToSqlRequest request) {

        String prompt = promptBuilder.buildPrompt(request.getQuestion());
        String generatedSql = "NOT_IMPLEMENTED_YET";

        List<Map<String,Object>> rows = sqlExecutionService.execute(generatedSql);
        return new TextToSqlResponse(generatedSql, rows);
    }
}
