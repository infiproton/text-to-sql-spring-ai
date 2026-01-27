package com.nexacorp.ai.texttosql.validation;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.stereotype.Service;

@Service
public class SqlValidator {

    public ValidationResult validate(String sql) {

        try {
            Statement statement = CCJSqlParserUtil.parse(sql);

            // Rule1 1: only SELECT statements allowed
            if (!(statement instanceof Select)) {
                return ValidationResult.failure("Only SELECT statements are allowed. Unsafe SQL detected.");
            }

            return ValidationResult.success();
        } catch (JSQLParserException e) {
            return ValidationResult.failure("Invalid SQL syntax. Unable to parse query.");
        }
    }

    public void validateOrThrow(String sql) {
        ValidationResult result = validate(sql);
        if (!result.isValid()) {
            throw new SqlValidationException(result.getMessage());
        }
    }


}
