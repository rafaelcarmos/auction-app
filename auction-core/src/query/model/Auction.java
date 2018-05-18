package query.model;

import command.aggregate.exceptions.AuctionCancelledException;
import command.aggregate.exceptions.AuctionEndedException;
import command.aggregate.exceptions.AuctionNotStartedException;
import command.aggregate.exceptions.InvalidBidException;
import command.commands.*;
import command.events.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Auction {

    private final UUID id;
    private UUID auctioneerId;
    private UUID itemId;
    private double startPrice;
    private UUID currentWinnerId;
    private double currentWinningBid = 0;
    private AuctionState state;

    public Auction(UUID id) {
        this.id = id;
    }

    public AuctionCancelled handle(CancelAuction cmd) throws Exception {

        if (state == AuctionState.CREATED) {
            throw new AuctionNotStartedException("Auction hasn't started");
        }

        if (state == AuctionState.CANCELLED) {
            throw new AuctionCancelledException("Auction has already been cancelled");
        }

        if (state == AuctionState.ENDED) {
            throw new AuctionEndedException("Auction has already ended");
        }

        return new AuctionCancelled(cmd.getAuctionId(), cmd.getTimestamp());
    }

    public AuctionCreated handle(CreateAuction cmd) {

        List<Event> result = new ArrayList<Event>();

        return new AuctionCreated(cmd.getAuctionId(), cmd.getTimestamp(), cmd.getAuctioneerId(), cmd.getItemId(), cmd.getStartPrice());
    }

    public AuctionEnded handle(EndAuction cmd) throws Exception {

        List<Event> result = new ArrayList<Event>();

        if (state == AuctionState.CREATED) {
            throw new AuctionNotStartedException("Auction hasn't started");
        }

        if (state == AuctionState.CANCELLED) {
            throw new AuctionCancelledException("Auction has already been cancelled");
        }

        if (state == AuctionState.ENDED) {
            throw new AuctionEndedException("Auction has already ended");
        }

        return new AuctionEnded(cmd.getAuctionId(), cmd.getTimestamp());
    }

    public BidPlaced handle(PlaceBid cmd) throws Exception {

        List<Event> result = new ArrayList<Event>();

        if (state == AuctionState.CREATED) {
            throw new AuctionNotStartedException("Auction hasn't started");
        }

        if (state == AuctionState.CANCELLED) {
            throw new AuctionCancelledException("Auction has already been cancelled");
        }

        if (state == AuctionState.ENDED) {
            throw new AuctionEndedException("Auction has already ended");
        }

        if (currentWinningBid == 0) {
            if (cmd.getAmount() < startPrice) {
                throw new InvalidBidException("Bid amount is less than start price");
            }
        } else {
            if (cmd.getAmount() < currentWinningBid) {
                throw new InvalidBidException("Bid amount is less than current winning bid");
            }
        }

        return new BidPlaced(cmd.getAuctionId(), cmd.getTimestamp(), cmd.getBidderId(), cmd.getAmount());
    }

    public AuctionStarted handle(StartAuction cmd) throws Exception {

        List<Event> result = new ArrayList<>();

        if (state == AuctionState.STARTED) {
            throw new AuctionNotStartedException("Auction has already started");
        }

        if (state == AuctionState.CANCELLED) {
            throw new AuctionCancelledException("Auction has already been cancelled");
        }

        if (state == AuctionState.ENDED) {
            throw new AuctionEndedException("Auction has already ended");
        }

        return new AuctionStarted(cmd.getAuctionId(), cmd.getTimestamp());
    }

    public void apply(AuctionCancelled evt) {
        state = AuctionState.CANCELLED;
    }

    public void apply(AuctionCreated evt) {
        startPrice = evt.getStartPrice();
        state = AuctionState.CREATED;
    }

    public void apply(AuctionEnded evt) {
        state = AuctionState.ENDED;
    }

    public void apply(BidPlaced evt) {
        currentWinnerId = evt.getBidderId();
        currentWinningBid = evt.getAmount();
    }

    public void apply(AuctionStarted evt) {
        state = AuctionState.STARTED;
    }

    public enum AuctionState {
        CREATED,
        STARTED,
        CANCELLED,
        ENDED
    }
}
