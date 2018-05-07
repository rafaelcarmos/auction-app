package auction.events;

import org.bson.Document;

import java.util.Date;
import java.util.UUID;

public class BidPlaced extends Event {
    private final UUID bidderId;
    private final double amount;

    public BidPlaced(UUID auctionId, Date timestamp, UUID bidderId, double amount) {
        super(auctionId, timestamp);
        this.bidderId = bidderId;
        this.amount = amount;
    }

    public UUID getBidderId() {
        return bidderId;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public Document getEventDataDocument() {
        return new Document()
                .append("bidderId", bidderId)
                .append("amount", amount);
    }
}
