package app.persistence.teamteachers;

import app.entities.teamteachers.Quote;
import app.persistence.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QuoteMapper {

    private ConnectionPool connectionPool;

    private static final Logger logger =
            LoggerFactory.getLogger(QuoteMapper.class);

    public QuoteMapper(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    //TODO hent fra database
    public Quote getPhilosophicalAnswer(String input) {
        // hardcoded demo data. Should hit the database for real data
        return new Quote("Kierkegaard", "Af alle latterlige ting forekommer det mig at være det allerlatterligste at have travlt", "kierkegaard.png");
    }

    public static Quote getPhilosophicalAnswer(String input, ConnectionPool connectionPool) {
        List<Integer> keywordIDs = getKeyWordIDs(input, connectionPool);
        if (keywordIDs.isEmpty()) {
            return new Quote("Kierkegaard", "Af alle latterlige Ting forekommer det mig at være det allerlatterligste at have travlt", "kierkegaard.png");
        }

        String query = "SELECT p.name, p.picture, a.quote FROM keywords_answers ka " +
                "JOIN answers a ON ka.answer_id = a.id " +
                "JOIN philosophers p ON a.philosopher_id = p.id " +
                "WHERE ka.keyword_id IN (" + keywordIDs.stream().map(String::valueOf).collect(Collectors.joining(",")) + ") " +
                "ORDER BY RANDOM() LIMIT 1";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()){

            if (rs.next()) {
                return new Quote(rs.getString("name"), rs.getString("quote"), rs.getString("picture"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Quote("Kierkegaard", "Af alle latterlige Ting forekommer det mig at være det allerlatterligste at have travlt", "kierkegaard.png");
    }


    public static List<Integer> getKeyWordIDs(String input, ConnectionPool connectionPool) {
        Set<Integer> keywordIDs = new HashSet<>(); // Brug Set for at undgå duplikater
        Set<String> inputWords = new HashSet<>(Arrays.asList(input.toLowerCase().split("\\s+")));

        String query = "SELECT keyword, id FROM keywords";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String keyword = rs.getString("keyword").toLowerCase();
                int keywordID = rs.getInt("id");

                // Tjekker hvert ord i input
                for (String word : inputWords) {
                    Pattern pattern = Pattern.compile("\\b" + keyword + "\\w*\\b", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(word);
                    if (matcher.find()) {
                        keywordIDs.add(keywordID);
                        break; // Vi har fundet keywordet én gang, så vi går videre til næste keyword
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(keywordIDs);
    }
}