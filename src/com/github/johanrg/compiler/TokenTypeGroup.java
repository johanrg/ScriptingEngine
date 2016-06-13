package com.github.johanrg.compiler;

/**
 * @author Johan Gustafsson
 * @since 2016-06-03
 */
enum TokenTypeGroup {
    NONE,
    KEYWORD,
    IDENTIFIER,
    BINARY_OPERATOR,
    UNARY_OPERATOR,
    DELIMITER,
    ASSIGNMENT_OPERATOR,
    EQUALITY_OPERATOR,
    TYPEDEF;
}
