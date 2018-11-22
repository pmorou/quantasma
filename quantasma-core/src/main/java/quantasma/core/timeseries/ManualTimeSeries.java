package quantasma.core.timeseries;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;

import java.lang.reflect.Field;

public class ManualTimeSeries extends BaseTimeSeries {

    private boolean isIndexManipulated;

    @Override
    public void addBar(Bar bar, boolean replace) {
        if (isIndexManipulated) {
            throw new RuntimeException("Cannot add bars as indexes are already manipulated");
        }
        super.addBar(bar, replace);
    }

    public void resetIndexes() {
        if (getSuperBeginIndex() < 0) {
            return;
        }

        ensureIndexesManipulated();

        setSuperBeginIndex(0);
        setSuperEndIndex(-1);
    }

    private void ensureIndexesManipulated() {
        if (!isIndexManipulated) {
            isIndexManipulated = true;
        }
    }

    public void nextIndex() {
        if (getSuperEndIndex() == addedBarsCount() - 1) {
            throw new RuntimeException(String.format("No next bar available at index [%s] - bars count [%s]", getSuperBeginIndex() + 1, addedBarsCount()));
        }
        setSuperEndIndex(getSuperEndIndex() + 1);
    }

    private int addedBarsCount() {
        return getBarData().size();
    }

    private int getSuperBeginIndex() {
        return (int) getSuperclassField("seriesBeginIndex");
    }

    private void setSuperBeginIndex(int value) {
        setSuperclassField("seriesBeginIndex", value);
    }

    private int getSuperEndIndex() {
        return (int) getSuperclassField("seriesEndIndex");
    }

    private void setSuperEndIndex(int value) {
        setSuperclassField("seriesEndIndex", value);
    }

    private Object getSuperclassField(String fieldName) {
        try {
            final Field seriesEndIndex = getClass().getSuperclass().getDeclaredField(fieldName);
            seriesEndIndex.setAccessible(true);
            return seriesEndIndex.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Caught checked exception", e);
        }
    }

    private void setSuperclassField(String fieldName, Object value) {
        try {
            final Field seriesEndIndex = getClass().getSuperclass().getDeclaredField(fieldName);
            seriesEndIndex.setAccessible(true);
            seriesEndIndex.set(this, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Caught checked exception", e);
        }
    }

}
