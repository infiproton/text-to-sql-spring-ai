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
                        "Organizational departments within NexaCorp",
                        List.of(
                                new ColumnSchema("id", "Unique identifier for the department"),
                                new ColumnSchema("name", "Department name"),
                                new ColumnSchema("location", "Primary physical location of the department")
                        )
                ),
                new TableSchema(
                        "employees",
                        "Employees working at NexaCorp",
                        List.of(
                                new ColumnSchema("id", "Unique identifier for the employee"),
                                new ColumnSchema("department_id", "Department the employee belongs to"),
                                new ColumnSchema("first_name", "Employee first name"),
                                new ColumnSchema("last_name", "Employee last name"),
                                new ColumnSchema("role", "Job title or role of the employee"),
                                new ColumnSchema("salary", "Annual salary of the employee"),
                                new ColumnSchema(
                                        "status",
                                        "Employment status. Valid values include ACTIVE and EXITED"
                                )
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
