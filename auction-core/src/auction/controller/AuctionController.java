package auction.controller;

import auction.commands.Command;

public interface AuctionController {
    void processCommand(Command command) throws Exception;

    void replayAllEvents() throws Exception;
}
