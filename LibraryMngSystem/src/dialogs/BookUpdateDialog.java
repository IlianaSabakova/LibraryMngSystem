package dialogs;

import dao.AuthorDAO;
import dao.BookDAO;
import dao.PublisherDAO;
import models.Author;
import models.Book;
import models.Publisher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BookUpdateDialog extends JDialog {

    private JTextField titleField;
    private JComboBox<Author> authorComboBox;
    private JComboBox<Publisher> publisherComboBox;
    private BookDAO bookDAO;
    private AuthorDAO authorDAO;
    private PublisherDAO publisherDAO;

    public BookUpdateDialog(JFrame parentFrame, Book bookToUpdate) {
        super(parentFrame, "Update Book", true);

        bookDAO = new BookDAO();
        authorDAO = new AuthorDAO();
        publisherDAO = new PublisherDAO();

        setLayout(new GridLayout(4, 2));

        add(new JLabel("Title:"));
        titleField = new JTextField(bookToUpdate.getTitle());
        add(titleField);

        add(new JLabel("Author:"));
        List<Author> authors = authorDAO.getAllAuthors();
        authorComboBox = new JComboBox<>(authors.toArray(new Author[0]));
        authorComboBox.setSelectedItem(bookToUpdate.getAuthor());
        add(authorComboBox);

        add(new JLabel("Publisher:"));
        List<Publisher> publishers = publisherDAO.getAllPublishers();
        publisherComboBox = new JComboBox<>(publishers.toArray(new Publisher[0]));
        publisherComboBox.setSelectedItem(bookToUpdate.getPublisher());
        add(publisherComboBox);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book updatedBook = new Book();
                updatedBook.setId(bookToUpdate.getId());
                updatedBook.setTitle(titleField.getText().trim());
                updatedBook.setAuthor((Author) authorComboBox.getSelectedItem());
                updatedBook.setPublisher((Publisher) publisherComboBox.getSelectedItem());
                bookDAO.updateBook(updatedBook);
                setVisible(false);
            }
        });
        add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        add(cancelButton);

        pack();
        setLocationRelativeTo(parentFrame);
    }
}
