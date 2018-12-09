package quantasma.integrations.event;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

public class AccountEvent implements Event<AccountInfo> {
    private static final String NAME = "account-event";

    private final String id;
    private final AccountInfo data;

    AccountEvent(AccountInfo data) {
        this.data = data;
        this.id = ZonedDateTime.now(ZoneOffset.UTC).toString() + "_" + UUID.randomUUID().toString();
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public AccountInfo data() {
        return data;
    }
}

