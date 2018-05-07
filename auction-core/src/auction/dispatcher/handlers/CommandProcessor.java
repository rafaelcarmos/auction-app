package auction.dispatcher.handlers;

import auction.controller.AuctionController;
import auction.dispatcher.CommandBase;
import auction.repository.Repository;
import com.lmax.disruptor.EventHandler;

public class CommandProcessor implements EventHandler<CommandBase> {

    private final Repository repository;
    private final AuctionController controller;

    public CommandProcessor(Repository repository, AuctionController controller) {
        this.repository = repository;
        this.controller = controller;
    }

    @Override
    public void onEvent(CommandBase commandBase, long l, boolean b) throws Exception {
        controller.processCommand(commandBase.getCommand());
        if (b) System.out.println("true");
    }
}
