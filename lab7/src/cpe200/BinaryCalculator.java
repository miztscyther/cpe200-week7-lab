package cpe200;

import java.math.BigDecimal;

/**
 * Created by Miztscyther on 25/10/2559.
 */
public class BinaryCalculator {
    private IOperand first;
    private IOperand second;
    private BigDecimal firstOperand;
    private BigDecimal secondOperand;

    public BinaryCalculator()
    {
        first = new IntegerOperand(0);
        second = new IntegerOperand(0);
    }

    public void setFirstOperand(IOperand operand)
    {
        first = new StringOperand(toBinary(operand));
        String a = first.getOperand();
        firstOperand = toBigDecimal(first.getOperand());
    }

    public void setSecondOperand(IOperand operand)
    {
        second = new StringOperand(toBinary(operand));
        secondOperand = toBigDecimal(second.getOperand());
    }

    private BigDecimal toBigDecimal(String str)
    {
        double sum = 0;
        boolean negative = false;
        if(str.matches("^-.+$"))
        {
            negative = true;
            str = str.split("-")[1];
        }
        String split[] = str.split("\\.");
        //2cases 1.integer, 2.floting point
        switch (split.length)
        {
            case 1:
                split = split[0].split("");
                for(int i = 0; i < split.length; i++)
                    sum += Integer.parseInt(split[i])*pow(2,split.length-(i+1));
                break;
            case 2://floating point number
                String iSplit[] = split[0].split("");
                for(int i = 0; i < iSplit.length; i++)
                    sum += Integer.parseInt(iSplit[i])*pow(2,iSplit.length-(i+1));
                iSplit = split[1].split("");
                for(int i =0; i < iSplit.length; i++)
                    sum += Integer.parseInt(iSplit[i])*pow(0.5,(i+1));
                break;
        }
        return new BigDecimal(sum*((negative)?-1:1));
    }

    private double pow(double a, int b)
    {
        BigDecimal A = new BigDecimal(a);
        return A.pow(b).doubleValue();
    }

    private boolean isNegative()
    {
        //return firstOperand.getOperand().matches("^-.+$")||secondOperand.getOperand().matches("^-.+$");
        return firstOperand.doubleValue()<0||secondOperand.doubleValue()<0;
    }

    private String toBinary(IOperand operand)
    {
        String sBinary= "";
        String[] split = operand.getOperand().split("\\.");
        //check if negative number
        //toBinaryString method cannot convert negative number properly
        Integer iDecimal;
        if(split[0].matches("^-.+"))
        {
            sBinary = "-";
            iDecimal = Integer.parseInt(split[0].split("-")[1]);
        }
        else
            iDecimal = Integer.parseInt(split[0]);
        sBinary += Integer.toBinaryString(iDecimal);
        if(split.length > 1)//length can only be just 1 or 2
        {
            if(split[1].matches("^0+$"))
            {
                return sBinary;
            }
            else//floating point number
            {
                int i = 0;
                sBinary += ".";
                //loop
                do {
                    split[1] = "0."+split[1];
                    Double dDecimal = Double.parseDouble(split[1]);
                    dDecimal *= 2;
                    split[1] = dDecimal.toString();
                    split = split[1].split("\\.");
                    sBinary += split[0];
                    i++;
                }while(!split[1].matches("^0+$")&&i<5);
            }
        }
        return sBinary;
    }

    public String add() throws RuntimeException {
        if(isNegative())
            throw new RuntimeException();
        return firstOperand.add(secondOperand).stripTrailingZeros().toString();
    }

    public String subtract() throws RuntimeException {
        if(isNegative())
            throw new RuntimeException();
        return firstOperand.subtract(secondOperand).stripTrailingZeros().toString();
    }

    public String multiply() throws RuntimeException {
        if(isNegative())
            throw new RuntimeException();
        return firstOperand.multiply(secondOperand).stripTrailingZeros().toString();
    }

    /* This method should throw an exception when divide by zero */
    public String division() throws RuntimeException {
        if(isNegative()|| secondOperand.doubleValue() == 0)
            throw new ArithmeticException();
        return firstOperand.divide(secondOperand, 5, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toString();
    }

    public String power() throws RuntimeException {
        if(isNegative())
            throw new RuntimeException();
        return firstOperand.pow(secondOperand.intValue()).stripTrailingZeros().toString();
    }
}

