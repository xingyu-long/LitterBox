package scratch.newast.model.expression.num;

import java.math.BigInteger;

public class Number implements NumExpr {

    private BigInteger value;

    public Number(BigInteger value) {
        this.value = value;
    }

}