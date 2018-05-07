package auction.commands;

import java.util.Date;
import java.util.UUID;

public abstract class Command {
    private final UUID auctionId;
    private final Date timestamp;

    public Command(UUID auctionId, Date timestamp) {
        this.auctionId = auctionId;
        this.timestamp = timestamp;
    }

    public final UUID getAuctionId() {
        return auctionId;
    }

    public final Date getTimestamp() {
        return timestamp;
    }
}
