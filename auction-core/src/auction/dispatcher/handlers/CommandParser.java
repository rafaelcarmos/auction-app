package auction.dispatcher.handlers;

import auction.commands.*;
import auction.dispatcher.CommandBase;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lmax.disruptor.EventHandler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

public class CommandParser implements EventHandler<CommandBase> {

    private final JsonParser jsonParser = new JsonParser();

    @Override
    public void onEvent(CommandBase commandBase, long l, boolean b) {
        JsonObject json = jsonParser.parse(commandBase.getRawMessage()).getAsJsonObject();
        String commandType = json.get("commandType").getAsString();
        Date timestamp = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        commandBase.setTimestamp(timestamp);
        Command command = null;

        if (commandType.equals("CancelAuction")) {
            UUID auctionId = UUID.fromString(json.get("auctionId").getAsString());
            command = new CancelAuction(auctionId, timestamp);
        } else if (commandType.equals("CreateAuction")) {
            UUID auctionId = UUID.randomUUID();
            UUID auctioneerId = UUID.fromString(json.get("auctioneerId").getAsString());
            UUID itemId = UUID.fromString(json.get("itemId").getAsString());
            double startPrice = json.get("startPrice").getAsDouble();
            command = new CreateAuction(auctionId, timestamp, auctioneerId, itemId, startPrice);
        } else if (commandType.equals("EndAuction")) {
            UUID auctionId = UUID.fromString(json.get("auctionId").getAsString());
            command = new EndAuction(auctionId, timestamp);
        } else if (commandType.equals("StartAuction")) {
            UUID auctionId = UUID.fromString(json.get("auctionId").getAsString());
            command = new StartAuction(auctionId, timestamp);
        } else if (commandType.equals("PlaceBid")) {
            UUID auctionId = UUID.fromString(json.get("auctionId").getAsString());
            UUID bidderId = UUID.fromString(json.get("bidderId").getAsString());
            double amount = json.get("amount").getAsDouble();
            command = new PlaceBid(auctionId, timestamp, bidderId, amount);
        } else {
            throw new RuntimeException("Invalid command type " + commandType);
        }

        commandBase.setCommand(command);
    }
}
