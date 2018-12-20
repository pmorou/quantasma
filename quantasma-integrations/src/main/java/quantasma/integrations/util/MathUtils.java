package quantasma.integrations.util;

import java.text.DecimalFormat;

public class MathUtils {

    private static final DecimalFormat DROP_DECIMAL_POINT_FORMAT = new DecimalFormat("#.");
    private static final DecimalFormat FIRST_DECIMAL_POINT_FORMAT = new DecimalFormat("#.#");
    private static final DecimalFormat SECOND_DECIMAL_POINT_FORMAT = new DecimalFormat("#.##");
    private static final DecimalFormat THIRD_DECIMAL_POINT_FORMAT = new DecimalFormat("#.###");

    public static double round(double value, int decimalPoint) {
        if (decimalPoint < 0) {
            throw new IllegalArgumentException("Decimal point: " + decimalPoint);
        }

        switch (decimalPoint) {
            // Most common cases
            case 0: {
                return Double.valueOf(DROP_DECIMAL_POINT_FORMAT.format(value));
            }
            case 1: {
                return Double.valueOf(FIRST_DECIMAL_POINT_FORMAT.format(value));
            }
            case 2: {
                return Double.valueOf(SECOND_DECIMAL_POINT_FORMAT.format(value));
            }
            case 3: {
                return Double.valueOf(THIRD_DECIMAL_POINT_FORMAT.format(value));
            }
        }

        // Other cases
        final StringBuilder formatBuilder = new StringBuilder("#.###");
        for (int i = 3; i < decimalPoint; i++) {
            formatBuilder.append("#");
        }
        return Double.valueOf(new DecimalFormat(formatBuilder.toString()).format(value));
    }

}
