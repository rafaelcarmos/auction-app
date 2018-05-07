package auction.test;

import auction.dispatcher.CommandDispatcher;
import auction.dispatcher.DisruptorCommandDispatcher;
import auction.repository.MongoRepository;
import auction.repository.Repository;
import com.google.gson.JsonObject;

import java.util.UUID;

public class TestMain {
    public static void main(String[] args) {
        Repository rep = new MongoRepository("localhost:27017", "auction", "events");
        CommandDispatcher dispatcher = null;
        try {

            dispatcher = new DisruptorCommandDispatcher(rep, 262144);

            JsonObject createAuction = new JsonObject();
            createAuction.addProperty("commandType", "CreateAuction");
            createAuction.addProperty("auctioneerId", UUID.randomUUID().toString());
            createAuction.addProperty("itemId", UUID.randomUUID().toString());
            createAuction.addProperty("startPrice", 1200d);
            dispatcher.processCommand(createAuction.toString());

            JsonObject startAuction = new JsonObject();
            createAuction.addProperty("commandType", "StartAuction");
            createAuction.addProperty("auctionId", "21d5aeca-6bce-4d54-8f76-689e36fbfbce");
            dispatcher.processCommand(createAuction.toString());

            for (int i = 0; i < 1000000; i++) {
                JsonObject bid = new JsonObject();
                bid.addProperty("bidderId", UUID.randomUUID().toString());
                bid.addProperty("auctionId", "21d5aeca-6bce-4d54-8f76-689e36fbfbce");
                bid.addProperty("amount", 10000d + i);
                bid.addProperty("commandType", "PlaceBid");
                dispatcher.processCommand(bid.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            dispatcher.shutdown();
            rep.close();
        }
    }
}
