package dao;

import database.DBConnection;
import models.Publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PublisherDAO {
    private final Connection connection;

    public PublisherDAO() {
        connection = DBConnection.getConnection();
    }

    public Publisher getPublisherById(int id) {
        Publisher publisher = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM publishers WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                publisher = new Publisher();
                publisher.setId(rs.getInt("id"));
                publisher.setName(rs.getString("name"));
                publisher.setAddress(rs.getString("address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publisher;
    }


    public List<Publisher> getAllPublishers() {
        List<Publisher> publishers = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM publishers");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Publisher publisher = new Publisher();
                publisher.setId(rs.getInt("id"));
                publisher.setName(rs.getString("name"));
                publisher.setAddress(rs.getString("address"));
                publishers.add(publisher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publishers;
    }

    public void insertPublisher(Publisher publisher) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO publishers (name, address) VALUES (?, ?)");
            stmt.setString(1, publisher.getName());
            stmt.setString(2, publisher.getAddress());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePublisher(Publisher publisher) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE publishers SET name = ?, address = ? WHERE id = ?");
            stmt.setString(1, publisher.getName());
            stmt.setString(2, publisher.getAddress());
            stmt.setInt(3, publisher.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePublisher(int id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM publishers WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasRelatedBooks(int publisherId) {
        String query = "SELECT COUNT(*) FROM books WHERE publisher_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, publisherId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<Publisher> searchPublishers(String searchTerm) {
        List<Publisher> publishers = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM publishers WHERE name LIKE ?");
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Publisher publisher = new Publisher();
                publisher.setId(rs.getInt("id"));
                publisher.setName(rs.getString("name"));
                publisher.setAddress(rs.getString("address"));
                publishers.add(publisher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publishers;
    }
}