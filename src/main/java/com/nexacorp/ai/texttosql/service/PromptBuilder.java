package com.nexacorp.ai.texttosql.service;

import org.springframework.stereotype.Service;

@Service
public class PromptBuilder {

    public String buildPrompt(String question) {

        return """
               You are an assistant that generates SQL queries for a PostgreSQL database. 

               Database schema:
               - departments(id, name, location)
               - employees(id, department_id, first_name, last_name, role, salary, status)

               Relationship:
               - employees.department_id references departments.id
               
                Instructions:
                   - Return ONLY the SQL query.
                   - Do NOT include explanations.
                   - Do NOT include markdown.
                   - Do NOT include code fences.
                   - The output must be a single SQL statement.

               Generate a SQL query for the following question:
               %s
               """.formatted(question);

    }
}
