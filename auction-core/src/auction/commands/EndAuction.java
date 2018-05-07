package auction.commands;

import java.util.Date;
import java.util.UUID;

public class EndAuction extends Command {

    public EndAuction(UUID auctionId, Date timestamp) {
        super(auctionId, timestamp);
    }
}
