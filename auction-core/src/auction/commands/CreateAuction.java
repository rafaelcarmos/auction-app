package auction.commands;

import java.util.Date;
import java.util.UUID;

public class CreateAuction extends Command {
    private final UUID auctioneerId;
    private final UUID itemId;
    private final double startPrice;

    public CreateAuction(UUID auctionId, Date timestamp, UUID auctioneerId, UUID itemId, double startPrice) {
        super(auctionId, timestamp);
        this.auctioneerId = auctioneerId;
        this.itemId = itemId;
        this.startPrice = startPrice;
    }

    public final UUID getAuctioneerId() {
        return auctioneerId;
    }

    public final UUID getItemId() {
        return itemId;
    }

    public double getStartPrice() {
        return startPrice;
    }
}
