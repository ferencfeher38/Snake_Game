package hu.alkprojekt.dao;

import hu.alkprojekt.model.Leaderboard;
import java.util.List;

public interface LeaderboardDAO {
    List<Leaderboard> findAll();
    Leaderboard save(Leaderboard leaderboard);
    void delete(Leaderboard contact);
}
