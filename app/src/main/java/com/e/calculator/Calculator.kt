package com.e.calculator

import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

internal object Calculator {
    fun calc(postfix: List<String>): Double {
        val stack: Deque<Double> = ArrayDeque<Double>()
        for (x in postfix) {
            when(x){
                "√" -> stack.push(sqrt(stack.pop()))
                "cube" -> {
                    val tmp = stack.pop()
                    stack.push(tmp * tmp * tmp)
                }
                "10ⁿ" -> stack.push((10.0).pow(stack.pop()))
                "+" -> stack.push(stack.pop() + stack.pop())
                "-" -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(a - b)
                }
                "*" -> stack.push(stack.pop() * stack.pop())
                "/" -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(a / b)
                }
                "u-" -> stack.push(-stack.pop())
                ">" -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(a - b) // true - положительное число
                }
                "<" -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(b - a) // true - положительное число
                }
                "≡" -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    if(a == b)
                        stack.push(1.0) // true - положительное число
                    else
                        stack.push(-1.0)
                }
                "≠" -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    if(a == b)
                        stack.push(-1.0) // true - отрецательное число
                    else
                        stack.push(1.0)
                }
                ":" -> {
                    val f = stack.pop()
                    val t = stack.pop()
                    val comperison = stack.pop()
                    if(comperison > 0)
                        stack.push(t) // true - отрецательное число
                    else
                        stack.push(f)
                }
                else -> stack.push(x.toDouble())
            }
        }
        return stack.pop()
    }
}