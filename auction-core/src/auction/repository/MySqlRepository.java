package auction.repository;

import auction.aggregate.Auction;
import auction.events.Event;

import java.sql.DriverManager;
import java.util.List;
import java.util.UUID;

public class MySqlRepository implements Repository {

    public MySqlRepository() throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        DriverManager.getConnection("");
    }

    @Override
    public void save(Event e) {

    }

    @Override
    public void save(Iterable<Event> e) {

    }

    @Override
    public List<Event> getEvents() {
        return null;
    }

    @Override
    public Auction getAuction(UUID id) {
        return null;
    }

    @Override
    public void close() {

    }
}
