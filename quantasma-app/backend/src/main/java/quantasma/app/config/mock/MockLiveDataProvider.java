package quantasma.app.config.mock;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import quantasma.integrations.data.provider.LiveDataProvider;

@Component
@Profile("mock")
public class MockLiveDataProvider implements LiveDataProvider {
    @Override
    public void run() {
        // mock
    }

    @Override
    public void stop() {
        // mock
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
