package com.nexacorp.ai.texttosql.service;

import com.nexacorp.ai.texttosql.schema.Relationship;
import com.nexacorp.ai.texttosql.schema.SchemaProvider;
import com.nexacorp.ai.texttosql.schema.TableSchema;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class PromptBuilder {

    private final SchemaProvider schemaProvider;

    public PromptBuilder(SchemaProvider schemaProvider) {
        this.schemaProvider = schemaProvider;
    }

    public String buildPrompt(String question) {

        String tables = schemaProvider.getTables().stream()
                .map(this::renderTable)
                .collect(Collectors.joining("\n"));

        String relationships = schemaProvider.getRelationships().stream()
                .map(this::renderRelationship)
                .collect(Collectors.joining("\n"));

        return """
               You are an assistant that generates SQL queries for a PostgreSQL database. 

               Database schema:
               %s

               Relationship:
               %s
               
                Instructions:
                   - Return ONLY the SQL query.
                   - Do NOT include explanations.
                   - Do NOT include markdown.
                   - Do NOT include code fences.
                   - The output must be a single SQL statement.

               Generate a SQL query for the following question:
               %s
               """.formatted(tables, relationships, question);

    }

    private String renderTable(TableSchema table) {
        String columns = String.join(", ", table.getColumns());
        return "- %s(%s)".formatted(table.getTableName(), columns);
    }

    private String renderRelationship(Relationship relationship) {
        return "- %s.%s references %s.%s".formatted(
                relationship.getFromTable(),
                relationship.getFromColumn(),
                relationship.getToTable(),
                relationship.getToColumn()
        );
    }

}
