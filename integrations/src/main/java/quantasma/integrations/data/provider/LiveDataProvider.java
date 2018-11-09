package quantasma.integrations.data.provider;

public interface LiveDataProvider {

    void run();

    void stop();

    boolean isRunning();
}
