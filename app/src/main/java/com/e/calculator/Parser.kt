package com.e.calculator
import java.util.*

internal object Parser {
    private const val operators = "+-*/"
    private const val delimiters = "()><≠≡:? $operators"

    private fun isDelimiter(token: String): Boolean {
        if (token.length != 1) return false
        for (i in 0 until delimiters.length ) {
            if (token[0] == delimiters[i]) return true
        }
        return false
    }

    private fun isOperator(token: String): Boolean {
        if (token == "u-" || token == ":") return true
        for (i in 0 until  operators.length) {
            if (token[0] == operators[i]) return true
        }
        return false
    }

    private fun isFunction(token: String): Boolean {
        return if (token == "√" || token == "cube" || token == "10ⁿ") true else false
    }

    private fun isComperisonOper(token: String): Boolean {
        return if (token == "≡" || token == "≠" || token == "<" || token == "<") true else false
    }

    private fun priority(token: String): Int {
        if (token == "(") return 1
        if (token == "+" || token == "-" || token == ":") return 2
        return if (token == "*" || token == "/") 3 else 4
    }

    fun parse(infix: String?): List<String> {
        val postfix: MutableList<String> = ArrayList<String>()
        val stack: Deque<String> = ArrayDeque<String>()
        val tokenizer = StringTokenizer(infix, delimiters, true)
        var prev = ""
        var curr = ""
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken()
            if (curr == " ") continue
            if (isFunction(curr))
                stack.push(curr)
            else if (isDelimiter( curr )) {
                if (curr == "(") stack.push(curr)
                else if (curr == ")") {
                    while (stack.peek() != "(") {
                        if (stack.isEmpty()) {
                            throw InconsistentBracketsException("the number of opening brackets in " +
                                    "an expression is less than the number of closing brackets")
                            return postfix
                        }
                        postfix.add(stack.pop())
                    }
                    stack.pop()
                    if (!stack.isEmpty() && isFunction(stack.peek())) {
                        postfix.add(stack.pop())
                    }
                }
                else if(isComperisonOper(curr)){
                    stack.push(curr)
                }
                else if(curr == "?"){
                    if(!stack.isEmpty())
                        postfix.add(stack.pop())
                    else
                        throw InvalidTernarOperArgumentsException("comparison oper not found")
                }
                else {
                    if (!tokenizer.hasMoreTokens() && isOperator(curr)) {
                        throw InvalidExpressionException("the last character of an expression " +
                                "cannot be an operator")
                        return postfix
                    }
                    else if (curr == "-" && (prev == "" || prev == "u-" || isDelimiter(prev) && prev != ")")) { // унарный минус
                        curr = "u-"
                    }
                    else {
                        while (!stack.isEmpty() && priority(curr) <= priority(stack.peek())) {
                            postfix.add(stack.pop())
                        }
                    }
                    stack.push(curr)
                }
            }
            else {
                postfix.add(curr)
            }
            prev = curr
        }
        while (!stack.isEmpty()) {
            if (isOperator(stack.peek()) || isFunction(stack.peek()))
                postfix.add(stack.pop())
            else {
                throw InconsistentBracketsException()
                return postfix
            }
        }
        return postfix
    }
}