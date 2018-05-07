package auction.events;

import org.bson.Document;

import java.util.Date;
import java.util.UUID;

public abstract class Event {
    private final UUID auctionId;
    private final Date timestamp;

    public Event(UUID auctionId, Date timestamp) {
        this.auctionId = auctionId;
        this.timestamp = timestamp;
    }

    public final UUID getAuctionId() {
        return auctionId;
    }

    public final Date getTimestamp() {
        return timestamp;
    }

    public abstract Document getEventDataDocument();

    public final Document getDocument() {
        return new Document()
                .append("auctionId", auctionId)
                .append("timestamp", timestamp)
                .append("eventType", this.getClass().getSimpleName())
                .append("eventData", getEventDataDocument());
    }
}
