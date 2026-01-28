package com.nexacorp.ai.texttosql.validation;

import com.nexacorp.ai.texttosql.schema.SchemaProvider;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SqlValidator {

    private final SchemaProvider schemaProvider;
    public SqlValidator(@Qualifier("cachedSchemaProvider") SchemaProvider schemaProvider) {
        this.schemaProvider = schemaProvider;
    }

    public ValidationResult validate(String sql) {

        try {
            Statement statement = CCJSqlParserUtil.parse(sql);

            // Rule1 1: only SELECT statements allowed
            if (!(statement instanceof Select)) {
                return ValidationResult.failure("Only SELECT statements are allowed. Unsafe SQL detected.");
            }

            // Rule 2: ensures tables and columns are valid
            Select select = (Select) statement;
            Set<String> tables = SqlAstUtils.extractTables(select);
            ValidationResult tableCheck = validateTables(tables);
            if (!tableCheck.isValid()) return tableCheck;

            Set<String> columns = SqlAstUtils.extractColumns(select);
            ValidationResult columnCheck = validateColumns(tables, columns);
            if (!columnCheck.isValid()) return columnCheck;

            return ValidationResult.success();
        } catch (JSQLParserException e) {
            return ValidationResult.failure("Invalid SQL syntax. Unable to parse query.");
        }
    }

    private ValidationResult validateColumns(Set<String> tables, Set<String> columns) {
        Map<String, Set<String>> tableColumns = schemaProvider.getTables()
                .stream()
                .collect(Collectors.toMap(
                        t -> t.getTableName().toLowerCase(),
                        t -> t.getColumns().stream()
                                .map(c -> c.getName().toLowerCase())
                                .collect(Collectors.toSet())
                ));

        for (String column : columns) {
            boolean found = false;

            for (String table : tables) {
                Set<String> allowedCols = tableColumns.get(table.toLowerCase());
                if (allowedCols != null && allowedCols.contains(column.toLowerCase())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return ValidationResult.failure("Query references unknown column: " + column);
            }
        }
        return ValidationResult.success();
    }

    private ValidationResult validateTables(Set<String> tables) {

        Set<String> allowedTables = schemaProvider.getTables()
                .stream()
                .map(t -> t.getTableName().toLowerCase())
                .collect(Collectors.toSet());

        for (String table : tables) {
            if (!allowedTables.contains(table.toLowerCase())) {
                return ValidationResult.failure("Query references unknown or unauthorized table: " + table);
            }
        }
        return ValidationResult.success();
    }

    public void validateOrThrow(String sql) {
        ValidationResult result = validate(sql);
        if (!result.isValid()) {
            throw new SqlValidationException(result.getMessage() + " SQL: " + sql);
        }
    }


}
