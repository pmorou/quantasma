package quantasma.core.analysis.parametrize;

import java.util.ArrayList;
import java.util.List;

public class Ints {

    public static List<Integer> range(int fromInclusive, int toExclusive) {
        return range(fromInclusive, toExclusive, 1);
    }

    public static List<Integer> range(int fromInclusive, int toExclusive, int step) {
        final List<Integer> list = new ArrayList<>();

        // ascending case
        for (int i = fromInclusive; i < toExclusive; i += step) {
            list.add(i);
        }

        // descending case
        for (int i = fromInclusive; i > toExclusive; i -= step) {
            list.add(i);
        }

        return list;
    }

}
