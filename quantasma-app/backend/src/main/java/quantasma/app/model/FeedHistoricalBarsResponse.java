package quantasma.app.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedHistoricalBarsResponse {
    private static final FeedHistoricalBarsResponse ACCEPTED = new FeedHistoricalBarsResponse(Status.ACCEPTED);
    private static final FeedHistoricalBarsResponse DECLINED = new FeedHistoricalBarsResponse(Status.DECLINED);

    private final Status status;

    public static FeedHistoricalBarsResponse accepted() {
        return ACCEPTED;
    }

    public static FeedHistoricalBarsResponse declined() {
        return DECLINED;
    }

    private enum Status {
        ACCEPTED,
        DECLINED
    }
}
