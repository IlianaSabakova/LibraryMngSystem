package dao;

import database.DBConnection;
import models.Author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {
    private final Connection connection;

    public AuthorDAO() {
        this.connection = DBConnection.getConnection();
    }

    public Author getAuthorById(int id) {
        Author author = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM authors WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                author = new Author();
                author.setId(rs.getInt("id"));
                author.setFirstName(rs.getString("first_name"));
                author.setLastName(rs.getString("last_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return author;
    }


    public List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM authors");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Author author = new Author();
                author.setId(rs.getInt("id"));
                author.setFirstName(rs.getString("first_name"));
                author.setLastName(rs.getString("last_name"));
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    public void insertAuthor(Author author) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO authors (first_name, last_name) VALUES (?, ?)");
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAuthor(Author author) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE authors SET first_name = ?, last_name = ? WHERE id = ?");
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.setInt(3, author.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAuthor(int id) {
//        try {
//            PreparedStatement stmt = connection.prepareStatement("DELETE FROM authors WHERE id = ?");
//            stmt.setInt(1, id);
//            int deletedRows = stmt.executeUpdate();
//
//            if (deletedRows > 0) {
//                System.out.println("Author deleted successfully.");
//            } else {
//                System.out.println("No author found with the given id.");
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM authors WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean hasRelatedBooks(int authorId) {
        String query = "SELECT COUNT(*) FROM books WHERE author_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<Author> searchAuthorsByLastName(String lastName) {
        List<Author> authors = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM authors WHERE last_name LIKE ?");
            stmt.setString(1, "%" + lastName + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Author author = new Author();
                author.setId(rs.getInt("id"));
                author.setFirstName(rs.getString("first_name"));
                author.setLastName(rs.getString("last_name"));
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }
}