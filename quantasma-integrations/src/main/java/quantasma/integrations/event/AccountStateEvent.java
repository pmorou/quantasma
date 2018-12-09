package quantasma.integrations.event;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

public class AccountStateEvent implements Event<AccountState> {
    private static final String NAME = "account_state-event";

    private final String id;
    private final AccountState data;

    AccountStateEvent(AccountState data) {
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
    public AccountState data() {
        return data;
    }
}

