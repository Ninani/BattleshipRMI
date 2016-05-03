package Producer;

import java.util.Random;

public class ExpressionGenerator {

    private char[] operations = {'+', '-', '*', '/' };

    public String getExpression(){

        int number1 = random(1, 9);
        int number2 = random(1, 9);
        int operationIndex = random(0, 3);

        String expression = String.format("%d%c%d", number1, operations[operationIndex], number2);


        return expression;
    }

    private int random(int minValue, int maxValue){

        Random generator = new Random();
        return generator.nextInt(maxValue) + minValue;

    }

}
