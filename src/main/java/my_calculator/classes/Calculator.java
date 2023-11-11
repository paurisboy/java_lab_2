/**
 * The Calculator class provides methods for evaluating mathematical expressions.
 * It supports basic arithmetic operations, unary and binary operators, and various mathematical functions.
 */
package my_calculator.classes;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Calculator {
    /**
     * Evaluates a mathematical expression and returns the result.
     *
     * @param exp The mathematical expression to be evaluated.
     * @return The result of the evaluation.
     */
    public static double calculate(String exp) {
        String postfix = infixToPostfix(exp);
        Deque<Double> stack = new ArrayDeque<>();
        String[] tokens = postfix.split(" ");

        for (String token : tokens) {
            if (Character.isDigit(token.charAt(0))) {
                stack.push(Double.parseDouble(token));
            }
            else if (isUnaryMinus(token)) {
                double operand = stack.pop();
                stack.push(-operand);
            }
            else if (isBinaryOperator(token)) {
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                double result = applyOperator(token, operand1, operand2);
                stack.push(result);
            }
            else if (isFunction(token)) {
                double operand = stack.pop();
                double result = applyFunction(token, operand);
                stack.push(result);
            }
        }
        return stack.pop();
    }
    /**
     * Converts an infix mathematical expression to its postfix form.
     *
     * @param exp The infix mathematical expression.
     * @return The postfix form of the expression.
     */
    private static String infixToPostfix(String exp) {
        StringBuilder result = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();
        Deque<String> func_stack = new ArrayDeque<>();

        int i = 0;
        while (i < exp.length()) {
            char c = exp.charAt(i);

            if (Character.isDigit(c)) {
                ReturnHelper res = getNumber(exp, i);
                i = res.getIndex();
                result.append(res.getVal());
                result.append(" ");
                if (!stack.isEmpty() && stack.peek() == '~') {
                    result.append(stack.peek()).append(" ");
                    stack.pop();
                }
                if(!func_stack.isEmpty())
                {
                    result.append(func_stack.peek()).append(" ");
                    func_stack.pop();
                }
            }
            else if (c == '(') {
                stack.push(c);
            }
            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result.append(stack.peek()).append(" ");
                    stack.pop();
                    if(!func_stack.isEmpty()) result.append(func_stack.pop()).append(" ");
                }
                stack.pop();
            }
            else if (c == '-' && (i == 0 || exp.charAt(i - 1) == '(')) {
                stack.push('~');
            }
            else if (isBinaryOperator(String.valueOf(c))) {
                while (!stack.isEmpty() && Prec(c) <= Prec(stack.peek())) {
                    result.append(stack.peek()).append(" ");
                    stack.pop();
                }
                stack.push(c);
            }
            else if (Character.isLetter(c)) {
                if (i+1 < exp.length() && !Character.isLetter(exp.charAt(i + 1))) {
                    String value = getVariable(c);
                    result.append(value);
                    if(!func_stack.isEmpty())
                    {
                        result.append(func_stack.peek()).append(" ");
                        func_stack.pop();
                    }

                } else {
                    StringBuilder token = new StringBuilder();
                    while(i < exp.length () && Character.isLetter(exp.charAt(i))) {
                        token.append(exp.charAt(i));
                        ++i;
                    }
                    if(isFunction(token.toString())) {
                        if(exp.charAt(i) == '(') {
                            stack.push('(');
                            func_stack.push(token.toString());
                        }
                    }
                    else {
                        throw new IllegalArgumentException("Unknown formula");
                    }

                }
            }
            else if(c == ' ') {}
            else {
                throw new IllegalArgumentException("Invalid Expression: Character is not allowed");
            }
            ++i;
        }


        while (!func_stack.isEmpty()) {
            result.append(func_stack.pop()).append(" ");
        }
        while (!stack.isEmpty()) {
            if (stack.peek() == '(') {
                throw new IllegalArgumentException("Invalid Expression: Unmatched opening parenthesis");
            }
            result.append(stack.pop()).append(" ");
        }

        return result.toString();
    }
    /**
     * Returns the precedence of a binary operator.
     *
     * @param ch The binary operator.
     * @return The precedence value.
     */
    private static int Prec(char ch) {
        return switch (ch) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> -1;
        };
    }

    /**
     * Checks if a token is a binary operator.
     *
     * @param token The token to be checked.
     * @return True if the token is a binary operator, false otherwise.
     */
    private static boolean isBinaryOperator(String token) {
        return "+-*/^".contains(token);
    }

    /**
     * Helper method to extract a number from the expression.
     *
     * @param exp The expression.
     * @param i   The current index in the expression.
     * @return A helper object containing the extracted number and the updated index.
     */
    private static ReturnHelper getNumber(String exp, int i) {
        StringBuilder num = new StringBuilder();
        while (i < exp.length() && (Character.isDigit(exp.charAt(i)) || exp.charAt(i) == '.')) {
            num.append(exp.charAt(i));
            ++i;
        }
        --i;
        return new ReturnHelper(num.toString(), i);
    }

    /**
     * Retrieves the value of a variable from the user.
     *
     * @param var The variable character.
     * @return The value of the variable.
     */
    private static String getVariable(Character var) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter value of " + var + ": ");
        double value;
        try {
            value = scanner.nextDouble();
        } catch (InputMismatchException e) {
            throw new IllegalArgumentException("Invalid input. Please enter a valid number.");
        }

        StringBuilder res = new StringBuilder();
        if(value < 0) {
            res.append(-value).append(" ~ ");
        }
        else res.append(value).append(" ");
        return res.toString();
    }

    /**
     * Checks if a token represents the unary minus operator.
     *
     * @param token The token to be checked.
     * @return True if the token is the unary minus operator, false otherwise.
     */
    private static boolean isUnaryMinus(String token) {
        return token.equals("~");
    }

    /**
     * Applies a binary operator to two operands.
     *
     * @param operator The binary operator.
     * @param a        The first operand.
     * @param b        The second operand.
     * @return The result of applying the operator to the operands.
     */
    private static double applyOperator(String operator, double a, double b) {
        return switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            case "^" -> Math.pow(a, b);
            default -> 0;
        };
    }

    /**
     * Checks if a token represents a mathematical function.
     *
     * @param token The token to be checked.
     * @return True if the token is a mathematical function, false otherwise.
     */
    private static boolean isFunction(String token) {
        return "cos sin tan cot log ln sqrt".contains(token);
    }

    /**
     * Applies a mathematical function to an operand.
     *
     * @param function The mathematical function.
     * @param operand  The operand.
     * @return The result of applying the function to the operand.
     */
    private static double applyFunction(String function, double operand) {
        return switch (function) {
            case "cos" -> Math.cos(operand);
            case "sin" -> Math.sin(operand);
            case "tan" -> Math.tan(operand);
            case "cot" -> 1.0 / Math.tan(operand);
            case "log" -> Math.log10(operand);
            case "ln" -> Math.log(operand);
            case "sqrt" -> Math.sqrt(operand);
            default -> 0;
        };
    }
}