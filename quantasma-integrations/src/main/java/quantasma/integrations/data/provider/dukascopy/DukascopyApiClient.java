package quantasma.integrations.data.provider.dukascopy;

import com.dukascopy.api.IStrategy;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.system.ClientFactory;
import com.dukascopy.api.system.IClient;
import com.dukascopy.api.system.ISystemListener;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class DukascopyApiClient {

    private final String jnlpUrl;
    private final String userName;
    private final String password;

    private IClient client;
    private int lightReconnects = 3;

    public DukascopyApiClient(String url, String userName, String password) {
        this.jnlpUrl = url;
        this.userName = userName;
        this.password = password;
        try {
            initializeClient();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void setSystemListener() {
        client.setSystemListener(new ISystemListener() {

            @Override
            public void onStart(long processId) {
                log.info("Strategy started [processingId: {}]", processId);
            }

            @Override
            public void onStop(long processId) {
                log.info("Strategy stopped [processingId: {}]", processId);
            }

            @Override
            public void onConnect() {
                log.info("Connected");
                lightReconnects = 3;
            }

            @Override
            public void onDisconnect() {
                log.info("Disconnected");
                tryToReconnect();
            }
        });
    }

    private void tryToConnect() throws Exception {
        for (int i = 1;
             !(i > 10 || client.isConnected());
             i++) {
            log.info("Connecting... attempt {}/10", i);
            client.connect(jnlpUrl, userName, password);
            Thread.sleep(1000);
        }
    }

    private void tryToReconnect() {
        new Thread(() -> {
            log.info("Reconnecting...");
            while (!client.isConnected()) {
                try {
                    if (lightReconnects > 0 && client.isReconnectAllowed()) {
                        client.reconnect();
                        --lightReconnects;
                    } else {
                        tryToConnect();
                    }

                    if (!client.isConnected()) {
                        Thread.sleep((long) (60 * 1000));
                    }
                } catch (InterruptedException e) {
                    log.error("Thread interrupted", e);
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("Reconnecting failed", e);
                }
            }
        }).start();
    }

    private void subscribeToInstruments() {
        Set<Instrument> instruments = new HashSet<>();
        instruments.add(Instrument.EURUSD);
        log.info("Subscribing to instruments: {}", instruments);
        client.setSubscribedInstruments(instruments);
    }

    private void initializeClient() throws Exception {
        client = ClientFactory.getDefaultInstance();
        setSystemListener();
        log.info("Initializing client...");
        tryToConnect();
        if (!client.isConnected()) {
            throw new RuntimeException("Failed to connect Dukascopy servers");
        }
        subscribeToInstruments();
    }

    public long runStrategy(IStrategy strategy) {
        return client.startStrategy(strategy);
    }

    public void stopStrategy(long processId) {
        client.stopStrategy(processId);
    }

    public IClient getClient() {
        return client;
    }

    public void stop() {
        client.disconnect();
    }

    public boolean isRunning() {
        return client.isConnected();
    }
}
