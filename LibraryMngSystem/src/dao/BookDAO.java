package dao;

import database.DBConnection;
import models.Author;
import models.Book;
import models.Publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private final Connection connection;

    public BookDAO() {
        connection = DBConnection.getConnection();
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM books JOIN authors ON books.author_id = authors.id JOIN publishers ON books.publisher_id = publishers.id");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("books.id"));
                book.setTitle(rs.getString("books.title"));

                Author author = new Author();
                author.setId(rs.getInt("authors.id"));
                author.setFirstName(rs.getString("authors.first_name"));
                author.setLastName(rs.getString("authors.last_name"));
                book.setAuthor(author);

                Publisher publisher = new Publisher();
                publisher.setId(rs.getInt("publishers.id"));
                publisher.setName(rs.getString("publishers.name"));
                publisher.setAddress(rs.getString("publishers.address"));
                book.setPublisher(publisher);

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void insertBook(Book book) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO books (title, author_id, publisher_id) VALUES (?, ?, ?)");
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthor().getId());
            stmt.setInt(3, book.getPublisher().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(Book book) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE books SET title = ?, author_id = ?, publisher_id = ? WHERE id = ?");
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthor().getId());
            stmt.setInt(3, book.getPublisher().getId());
            stmt.setInt(4, book.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(int id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM books WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> searchBooksByName(String searchTerm) {
        List<Book> books = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM books JOIN authors ON books.author_id = authors.id JOIN publishers ON books.publisher_id = publishers.id WHERE books.title LIKE ?");
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("books.id"));
                book.setTitle(rs.getString("books.title"));

                Author author = new Author();
                author.setId(rs.getInt("authors.id"));
                author.setFirstName(rs.getString("authors.first_name"));
                author.setLastName(rs.getString("authors.last_name"));
                book.setAuthor(author);

                Publisher publisher = new Publisher();
                publisher.setId(rs.getInt("publishers.id"));
                publisher.setName(rs.getString("publishers.name"));
                publisher.setAddress(rs.getString("publishers.address"));
                book.setPublisher(publisher);

                books.add(book);

            }
    }catch(SQLException e){
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> getBooksByAuthorAndPublisher(int authorId, int publisherId) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE author_id = ? AND publisher_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, authorId);
            preparedStatement.setInt(2, publisherId);
            ResultSet resultSet = preparedStatement.executeQuery();

            AuthorDAO authorDAO = new AuthorDAO();
            PublisherDAO publisherDAO = new PublisherDAO();

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));

                Author author = authorDAO.getAuthorById(authorId);
                book.setAuthor(author);

                Publisher publisher = publisherDAO.getPublisherById(publisherId);
                book.setPublisher(publisher);

                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }




}