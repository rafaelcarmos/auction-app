package auction.repository;

import auction.aggregate.Auction;
import auction.events.*;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class MongoRepository implements Repository {

    private final MongoCollection<Document> eventCollection;
    private final MongoDatabase db;
    private final MongoClient mongoClient;
    private Map<UUID, Auction> auctionMap = new HashMap<>();

    public MongoRepository(String mongoAddress, String databaseName, String collectionName) {
        mongoClient = new MongoClient(mongoAddress);
        db = mongoClient.getDatabase(databaseName);
        eventCollection = db.getCollection(collectionName);
    }

    @Override
    public void save(Event e) {
        eventCollection.insertOne(e.getDocument());
    }

    @Override
    public void save(Iterable<Event> events) {
        List<Document> lst = new ArrayList<>();
        for (Event e : events) {
            lst.add(e.getDocument());
        }
        eventCollection.insertMany(lst);
    }

    @Override
    public List<Event> getEvents() {
        FindIterable<Document> find = eventCollection.find();
        List<Event> result = new ArrayList<>();
        for (Document d : find) {
            result.add(parseEvent(d));
        }
        return result;
    }

    private Event parseEvent(Document d) {
        String eventType = (String) d.get("eventType");
        LocalDateTime timestamp = LocalDateTime.ofInstant(((Date) d.get("timestamp")).toInstant(), ZoneId.systemDefault());
        UUID auctionId = (UUID) d.get("auctionId");
        Event event;

        switch (eventType) {
            case "AuctionCancelled":
                event = new AuctionCancelled(auctionId, timestamp);
                break;
            case "AuctionCreated": {
                Document data = (Document) d.get("eventData");
                UUID auctioneerId = (UUID) data.get("auctioneerId");
                UUID itemId = (UUID) data.get("itemId");
                double startPrice = (double) data.get("startPrice");
                event = new AuctionCreated(auctionId, timestamp, auctioneerId, itemId, startPrice);
                break;
            }
            case "AuctionEnded":
                event = new AuctionEnded(auctionId, timestamp);
                break;
            case "AuctionStarted":
                event = new AuctionStarted(auctionId, timestamp);
                break;
            case "BidPlaced": {
                Document data = (Document) d.get("eventData");
                UUID bidderId = (UUID) data.get("bidderId");
                double amount = (double) data.get("amount");
                event = new BidPlaced(auctionId, timestamp, bidderId, amount);
                break;
            }
            default:
                throw new RuntimeException("Invalid event type " + eventType);
        }

        return event;
    }

    @Override
    public Auction getAuction(UUID id) {
        if (auctionMap.containsKey(id)) {
            return auctionMap.get(id);
        } else {
            Auction auction = new Auction(id);
            auctionMap.put(id, auction);
            return auction;
        }
    }

    @Override
    public void close() {
        mongoClient.close();
    }
}
