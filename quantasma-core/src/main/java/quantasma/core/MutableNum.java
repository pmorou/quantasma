package quantasma.core;

import org.ta4j.core.num.Num;

import java.util.function.Function;

import static org.ta4j.core.num.NaN.NaN;

/**
 * WARNING! Might be removed in future releases.<p><p>
 *
 * The class is equivalent to {@code DoubleNum} with additional method {@code mutate()} to change the inner value.<p><p>
 *
 * Avoid using this implementation, prefer immutable ones. Use in special cases only.<p>
 * Use case: passing strategy-controlled order amount to {@code TimeSeriesManager} while keeping API untouched.
 */
public class MutableNum implements Num {

    private static final long serialVersionUID = -2611177221813615071L;

    private double delegate;

    private final static double EPS = 0.00001;

    @Override
    public Function<Number, Num> function() {
        return MutableNum::valueOf;
    }

    private MutableNum(double val) {
        delegate = val;
    }

    /**
     * Mutate inner value.<p><p>
     *
     * Beware, it will complicate the data flow.
     *
     * @param newValue
     */
    public void mutate(double newValue) {
        this.delegate = newValue;
    }

    @Override
    public Double getDelegate() {
        return delegate;
    }

    @Override
    public String getName() {
        return "MutableNum";
    }

    @Override
    public Num plus(Num augend) {
        return augend.isNaN() ? NaN : new MutableNum(delegate + ((MutableNum) augend).delegate);
    }

    @Override
    public Num minus(Num subtrahend) {
        return subtrahend.isNaN() ? NaN : new MutableNum(delegate - ((MutableNum) subtrahend).delegate);
    }

    @Override
    public Num multipliedBy(Num multiplicand) {
        return multiplicand.isNaN() ? NaN : new MutableNum(delegate * ((MutableNum) multiplicand).delegate);
    }

    @Override
    public Num dividedBy(Num divisor) {
        if (divisor.isNaN() || divisor.isZero()) {
            return NaN;
        }
        MutableNum divisorD = (MutableNum) divisor;
        return new MutableNum(delegate / divisorD.delegate);
    }

    @Override
    public Num remainder(Num divisor) {
        return divisor.isNaN() ? NaN : new MutableNum(delegate % ((MutableNum) divisor).delegate);
    }

    @Override
    public Num pow(int n) {
        return new MutableNum(Math.pow(delegate, n));
    }

    @Override
    public Num pow(Num n) {
        return new MutableNum(Math.pow(delegate, n.doubleValue()));
    }

    @Override
    public Num sqrt() {
        if (delegate < 0) {
            return NaN;
        }
        return new MutableNum(Math.sqrt(delegate));
    }

    @Override
    public Num sqrt(int precision) {
        return sqrt();
    }

    @Override
    public Num abs() {
        return new MutableNum(Math.abs(delegate));
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
        return !other.isNaN() && delegate == ((MutableNum) other).delegate;
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
        return other.isNaN() ? NaN : new MutableNum(Math.min(delegate, ((MutableNum) other).delegate));
    }

    @Override
    public Num max(Num other) {
        return other.isNaN() ? NaN : new MutableNum(Math.max(delegate, ((MutableNum) other).delegate));
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
        MutableNum VariableAmountObj = (MutableNum) obj;
        return Math.abs(delegate - VariableAmountObj.delegate) < EPS;
    }

    @Override
    public int compareTo(Num o) {
        if (this == NaN || o == NaN) {
            return 0;
        }
        MutableNum VariableAmountO = (MutableNum) o;
        return Double.compare(delegate, VariableAmountO.delegate);
    }

    public static MutableNum valueOf(int i) {
        return new MutableNum((double) i);
    }

    public static MutableNum valueOf(float i) {
        return new MutableNum((double) i);
    }

    public static MutableNum valueOf(Number i) {
        return new MutableNum(Double.parseDouble(i.toString()));
    }
}