package Solver;

import Expression.ExpressionType;

public class SolvedExpression {

    private String expression;
    private int result;
    private ExpressionType expressionType;

    public SolvedExpression(String expression, int result, ExpressionType expressionType){
        this.expression = expression;
        this.result = result;
        this.expressionType = expressionType;
    }

    public String getExpression() {
        return expression;
    }

    public int getResult() {
        return result;
    }

    public ExpressionType getExpressionType() {
        return expressionType;
    }
}
