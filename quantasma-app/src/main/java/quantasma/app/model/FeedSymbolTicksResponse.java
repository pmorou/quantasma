package quantasma.app.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedSymbolTicksResponse {
    private static final FeedSymbolTicksResponse ACCEPTED = new FeedSymbolTicksResponse(Status.ACCEPTED);
    private static final FeedSymbolTicksResponse DECLINED = new FeedSymbolTicksResponse(Status.DECLINED);

    private final Status status;

    public static FeedSymbolTicksResponse accepted() {
        return ACCEPTED;
    }

    public static FeedSymbolTicksResponse declined() {
        return DECLINED;
    }

    private enum Status {
        ACCEPTED,
        DECLINED
    }
}
