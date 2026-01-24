package com.nexacorp.ai.texttosql.schema;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StaticSchemaProvider implements SchemaProvider {
    @Override
    public List<TableSchema> getTables() {
        return List.of(
                new TableSchema(
                        "departments",
                        List.of("id", "name", "location")
                ),
                new TableSchema(
                        "employees",
                        List.of(
                                "id",
                                "department_id",
                                "first_name",
                                "last_name",
                                "role",
                                "salary",
                                "status"
                        )
                )

        );
    }

    @Override
    public List<Relationship> getRelationships() {
        return List.of(
                new Relationship(
                        "employees",
                        "department_id",
                        "departments",
                        "id"
                )
        );
    }
}
