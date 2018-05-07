package auction.dispatcher;

public interface CommandDispatcher {

    void processCommand(String rawMessage);

    void shutdown();
}
