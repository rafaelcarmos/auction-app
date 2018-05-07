package auction.dispatcher;

import auction.controller.AuctionController;
import auction.controller.AuctionControllerImpl;
import auction.dispatcher.handlers.CommandParser;
import auction.dispatcher.handlers.CommandProcessor;
import auction.repository.Repository;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.Executors;

public class DisruptorCommandDispatcher implements CommandDispatcher {

    private final Disruptor<CommandBase> disruptor;
    private final Repository repository;
    private final AuctionController controller;

    public DisruptorCommandDispatcher(Repository repository, int bufferSize) throws Exception {
        this.repository = repository;
        this.controller = new AuctionControllerImpl(repository);
        System.out.println("Replaying events...");
        controller.replayAllEvents();
        System.out.println("Events replayed");
        this.disruptor = new Disruptor<>(CommandBase::new, bufferSize, Executors.defaultThreadFactory());
        this.disruptor.handleEventsWith(new CommandParser()).then(new CommandProcessor(repository, controller));
        disruptor.start();
    }

    @Override
    public void processCommand(String rawMessage) {
        long seq = disruptor.getRingBuffer().next();
        CommandBase cmd = disruptor.getRingBuffer().claimAndGetPreallocated(seq);
        cmd.setRawMessage(rawMessage);
        disruptor.getRingBuffer().publish(seq);
    }

    @Override
    public void shutdown() {
        disruptor.shutdown();
    }
}
