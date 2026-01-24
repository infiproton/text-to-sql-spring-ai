package com.nexacorp.ai.texttosql.schema;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TableSchema {

    private final String tableName;
    private final List<String> columns;

}
