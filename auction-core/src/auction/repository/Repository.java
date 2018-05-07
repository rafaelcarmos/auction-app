package auction.repository;

import auction.aggregate.Auction;
import auction.events.Event;

import java.util.List;
import java.util.UUID;

public interface Repository {

    void save(Event e);

    void save(Iterable<Event> e);

    List<Event> getEvents();

    Auction getAuction(UUID id);

    void close();
}
