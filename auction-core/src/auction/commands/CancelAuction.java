package auction.commands;

import java.util.Date;
import java.util.UUID;

public class CancelAuction extends Command {

    public CancelAuction(UUID auctionId, Date timestamp) {
        super(auctionId, timestamp);
    }
}
