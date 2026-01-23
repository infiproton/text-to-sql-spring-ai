package com.nexacorp.ai.texttosql.controller;

import com.nexacorp.ai.texttosql.dto.TextToSqlRequest;
import com.nexacorp.ai.texttosql.dto.TextToSqlResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@RestController
@RequestMapping("/api")
class TextToSqlController {

    @PostMapping("/text-to-sql")
    public TextToSqlResponse textToSql(@RequestBody TextToSqlRequest request) {

        return new TextToSqlResponse("NOT_IMPLEMENTED_YET", Collections.emptyList());
    }
}
