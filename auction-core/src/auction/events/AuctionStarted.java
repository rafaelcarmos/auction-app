package auction.events;

import org.bson.Document;

import java.util.Date;
import java.util.UUID;

public class AuctionStarted extends Event {

    public AuctionStarted(UUID auctionId, Date timestamp) {
        super(auctionId, timestamp);
    }

    @Override
    public Document getEventDataDocument() {
        return null;
    }
}
