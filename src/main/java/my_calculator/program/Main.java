package my_calculator.program;
import my_calculator.classes.Calculator;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        System.out.print("Enter your formula:");
        Scanner scanner = new Scanner(System.in);
        String exp = scanner.nextLine();
        double result = Calculator.calculate(exp);
        System.out.println("Result: " + result);
    }
}
