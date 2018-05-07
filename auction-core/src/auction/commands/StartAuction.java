package auction.commands;

import java.util.Date;
import java.util.UUID;

public class StartAuction extends Command {

    public StartAuction(UUID auctionId, Date timestamp) {
        super(auctionId, timestamp);
    }
}
