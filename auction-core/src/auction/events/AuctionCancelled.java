package auction.events;

import org.bson.Document;

import java.util.Date;
import java.util.UUID;

public class AuctionCancelled extends Event {

    public AuctionCancelled(UUID auctionId, Date timestamp) {
        super(auctionId, timestamp);
    }

    @Override
    public Document getEventDataDocument() {
        return null;
    }
}
