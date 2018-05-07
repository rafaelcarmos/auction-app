package auction.controller;

import auction.aggregate.Auction;
import auction.aggregate.exceptions.InvalidCommandException;
import auction.aggregate.exceptions.InvalidEventException;
import auction.commands.*;
import auction.events.*;
import auction.repository.Repository;

public class AuctionControllerImpl implements AuctionController {

    private final Repository repository;

    public AuctionControllerImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void processCommand(Command command) throws Exception {

        Auction auction = repository.getAuction(command.getAuctionId());

        if (command instanceof CancelAuction) {
            AuctionCancelled event = auction.handle((CancelAuction) command);
            auction.apply(event);
            repository.save(event);
        } else if (command instanceof CreateAuction) {
            AuctionCreated event = auction.handle((CreateAuction) command);
            auction.apply(event);
            repository.save(event);
        } else if (command instanceof EndAuction) {
            AuctionEnded event = auction.handle((EndAuction) command);
            auction.apply(event);
            repository.save(event);
        } else if (command instanceof PlaceBid) {
            BidPlaced event = auction.handle((PlaceBid) command);
            auction.apply(event);
            repository.save(event);
        } else if (command instanceof StartAuction) {
            AuctionStarted event = auction.handle((StartAuction) command);
            auction.apply(event);
            repository.save(event);
        } else {
            throw new InvalidCommandException("Command not supported by Auction: " + command.getClass().getName());
        }
    }

    @Override
    public void replayAllEvents() throws Exception {

        Iterable<Event> events = repository.getEvents();

        for (Event e : events) {

            Auction auction = repository.getAuction(e.getAuctionId());

            if (e instanceof AuctionCancelled) {
                auction.apply((AuctionCancelled) e);
            } else if (e instanceof AuctionCreated) {
                auction.apply((AuctionCreated) e);
            } else if (e instanceof AuctionEnded) {
                auction.apply((AuctionEnded) e);
            } else if (e instanceof BidPlaced) {
                auction.apply((BidPlaced) e);
            } else if (e instanceof AuctionStarted) {
                auction.apply((AuctionStarted) e);
            } else {
                throw new InvalidEventException("Invalid Event: " + e.getClass().getName());
            }
        }
    }
}
