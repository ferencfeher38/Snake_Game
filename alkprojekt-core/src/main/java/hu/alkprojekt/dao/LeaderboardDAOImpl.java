package hu.alkprojekt.dao;

import hu.alkprojekt.config.LeaderboardConfiguration;
import hu.alkprojekt.model.Leaderboard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LeaderboardDAOImpl implements LeaderboardDAO {
    private static final String SELECT_ALL_LEADERBOARD = "SELECT * FROM leaderboard ORDER BY score DESC LIMIT 10";
    private static final String INSERT_LEADERBOARD = "INSERT INTO leaderboard (name, score) VALUES (?,?)";
    private static final String UPDATE_LEADERBOARD = "UPDATE leaderboard SET name = ?, score = ? WHERE id=?";
    private static final String DELETE_LEADERBOARD = "DELETE FROM leaderboard WHERE id = ?";
    private String connectionURL;

    public LeaderboardDAOImpl(){
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connectionURL = LeaderboardConfiguration.getValue("db.url");
    }

    @Override
    public List<Leaderboard> findAll() {

        List<Leaderboard> result = new ArrayList<>();

        try(Connection c = DriverManager.getConnection(connectionURL);
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_ALL_LEADERBOARD)
        ){

            while(rs.next()){
                Leaderboard leaderboard = new Leaderboard();
                leaderboard.setId(rs.getInt("id"));
                leaderboard.setName(rs.getString("name"));
                leaderboard.setScore(rs.getInt("score"));
                result.add(leaderboard);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;

    }

    @Override
    public Leaderboard save(Leaderboard leaderboard) {
        try(Connection c = DriverManager.getConnection(connectionURL);
            PreparedStatement stmt = leaderboard.getId() <= 0 ? c.prepareStatement(INSERT_LEADERBOARD, Statement.RETURN_GENERATED_KEYS) : c.prepareStatement(UPDATE_LEADERBOARD)
        ){
            if(leaderboard.getId() > 0){ // UPDATE
                stmt.setInt(3, leaderboard.getId());
            }

            stmt.setString(1, leaderboard.getName());
            stmt.setInt(2, leaderboard.getScore());

            TimeUnit.SECONDS.sleep(5);
            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0){
                return null;
            }

            if(leaderboard.getId() <= 0){ // INSERT
                ResultSet genKeys = stmt.getGeneratedKeys();
                if(genKeys.next()){
                    leaderboard.setId(genKeys.getInt(1));
                }
            }

        } catch (SQLException | InterruptedException throwables) {
            throwables.printStackTrace();
            return null;
        }

        return leaderboard;
    }


    @Override
    public void delete(Leaderboard contact) {

        try(Connection c = DriverManager.getConnection(connectionURL);
            PreparedStatement stmt = c.prepareStatement(DELETE_LEADERBOARD);
        ) {
            stmt.setInt(1, contact.getId());
            stmt.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
