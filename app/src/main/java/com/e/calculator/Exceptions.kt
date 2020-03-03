package com.e.calculator

class InconsistentBracketsException(message: String = "brackets in the expression are not consistent"): Exception(message)
class InvalidExpressionException(message: String = "invalid expression"): Exception(message)

class InvalidTernarOperArgumentsException(message: String = "invalid arguments for a ternary operation"): Exception(message)
