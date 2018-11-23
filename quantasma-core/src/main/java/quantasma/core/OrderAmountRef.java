package quantasma.core;

import org.ta4j.core.num.Num;

import java.util.function.Function;

import static org.ta4j.core.num.NaN.NaN;

public class OrderAmountRef implements Num {

    private static final long serialVersionUID = -2611177221813615071L;

    private double delegate;

    private final static double EPS = 0.00001;

    @Override
    public Function<Number, Num> function() {
        return OrderAmountRef::valueOf;
    }

    private OrderAmountRef(double val) {
        delegate = val;
    }

    public void setValue(double newValue) {
        this.delegate = newValue;
    }

    public void resetValue() {
        this.delegate = 0;
    }

    @Override
    public Double getDelegate() {
        return delegate;
    }

    @Override
    public String getName() {
        return "OrderAmountRef";
    }

    @Override
    public Num plus(Num augend) {
        return augend.isNaN() ? NaN : new OrderAmountRef(delegate + ((OrderAmountRef) augend).delegate);
    }

    @Override
    public Num minus(Num subtrahend) {
        return subtrahend.isNaN() ? NaN : new OrderAmountRef(delegate - ((OrderAmountRef) subtrahend).delegate);
    }

    @Override
    public Num multipliedBy(Num multiplicand) {
        return multiplicand.isNaN() ? NaN : new OrderAmountRef(delegate * ((OrderAmountRef) multiplicand).delegate);
    }

    @Override
    public Num dividedBy(Num divisor) {
        if (divisor.isNaN() || divisor.isZero()) {
            return NaN;
        }
        OrderAmountRef divisorD = (OrderAmountRef) divisor;
        return new OrderAmountRef(delegate / divisorD.delegate);
    }

    @Override
    public Num remainder(Num divisor) {
        return divisor.isNaN() ? NaN : new OrderAmountRef(delegate % ((OrderAmountRef) divisor).delegate);
    }

    @Override
    public Num pow(int n) {
        return new OrderAmountRef(Math.pow(delegate, n));
    }

    @Override
    public Num pow(Num n) {
        return new OrderAmountRef(Math.pow(delegate, n.doubleValue()));
    }

    @Override
    public Num sqrt() {
        if (delegate < 0) {
            return NaN;
        }
        return new OrderAmountRef(Math.sqrt(delegate));
    }

    @Override
    public Num sqrt(int precision) {
        return sqrt();
    }

    @Override
    public Num abs() {
        return new OrderAmountRef(Math.abs(delegate));
    }

    @Override
    public boolean isZero() {
        return delegate == 0;
    }

    @Override
    public boolean isPositive() {
        return delegate > 0;
    }

    @Override
    public boolean isPositiveOrZero() {
        return delegate >= 0;
    }

    @Override
    public boolean isNegative() {
        return delegate < 0;
    }

    @Override
    public boolean isNegativeOrZero() {
        return delegate <= 0;
    }

    @Override
    public boolean isEqual(Num other) {
        return !other.isNaN() && delegate == ((OrderAmountRef) other).delegate;
    }

    public boolean isGreaterThan(Num other) {
        return !other.isNaN() && compareTo(other) > 0;
    }

    public boolean isGreaterThanOrEqual(Num other) {
        return !other.isNaN() && compareTo(other) > -1;
    }

    public boolean isLessThan(Num other) {
        return !other.isNaN() && compareTo(other) < 0;
    }

    @Override
    public boolean isLessThanOrEqual(Num other) {
        return !other.isNaN() && compareTo(other) < 1;
    }

    @Override
    public Num min(Num other) {
        return other.isNaN() ? NaN : new OrderAmountRef(Math.min(delegate, ((OrderAmountRef) other).delegate));
    }

    @Override
    public Num max(Num other) {
        return other.isNaN() ? NaN : new OrderAmountRef(Math.max(delegate, ((OrderAmountRef) other).delegate));
    }

    @Override
    public int hashCode() {
        return ((Double) (delegate)).hashCode();
    }

    @Override
    public String toString() {
        return Double.toString(delegate);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Num)) {
            return false;
        }
        if (obj == NaN) {
            return false;
        }
        OrderAmountRef VariableAmountObj = (OrderAmountRef) obj;
        return Math.abs(delegate - VariableAmountObj.delegate) < EPS;
    }

    @Override
    public int compareTo(Num o) {
        if (this == NaN || o == NaN) {
            return 0;
        }
        OrderAmountRef VariableAmountO = (OrderAmountRef) o;
        return Double.compare(delegate, VariableAmountO.delegate);
    }

    public static OrderAmountRef instance() {
        return new OrderAmountRef(0);
    }

    private static OrderAmountRef valueOf(Number number) {
        return new OrderAmountRef(Double.parseDouble(number.toString()));
    }
}