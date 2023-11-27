package dialogs;

import dao.AuthorDAO;
import dao.PublisherDAO;
import models.Author;
import models.Book;
import models.Publisher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookAddDialog extends JDialog {
    private JTextField titleField;
    private JComboBox<Author> authorComboBox;
    private JComboBox<Publisher> publisherComboBox;

    private JButton addButton;
    private JButton cancelButton;

    private Book newBook;
    private AuthorDAO authorDAO;
    private PublisherDAO publisherDAO;

    public BookAddDialog(JFrame parent) {
        super(parent, "Add Book", true);

        setLayout(new GridLayout(4, 2));

        authorDAO = new AuthorDAO();
        publisherDAO = new PublisherDAO();

        titleField = new JTextField(20);

        java.util.List<Author> authors = authorDAO.getAllAuthors();
        authorComboBox = new JComboBox<>(authors.toArray(new Author[0]));

        java.util.List<Publisher> publishers = publisherDAO.getAllPublishers();
        publisherComboBox = new JComboBox<>(publishers.toArray(new Publisher[0]));

        addButton = new JButton("Add");
        addButton.addActionListener(new AddButtonListener());

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelButtonListener());

        add(new JLabel("Title:"));
        add(titleField);

        add(new JLabel("Author:"));
        add(authorComboBox);

        add(new JLabel("Publisher:"));
        add(publisherComboBox);

        add(addButton);
        add(cancelButton);

        pack();
        setLocationRelativeTo(parent);
    }

    public Book getNewBook() {
        return newBook;
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = titleField.getText();
            Author author = (Author) authorComboBox.getSelectedItem();
            Publisher publisher = (Publisher) publisherComboBox.getSelectedItem();

            newBook = new Book();
            newBook.setTitle(title);
            newBook.setAuthor(author);
            newBook.setPublisher(publisher);

            setVisible(false);
        }
    }

    private class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            newBook = null;
            setVisible(false);
        }
    }
}


