package ui;

import dao.AuthorDAO;
import dao.BookDAO;
import dao.PublisherDAO;
import dialogs.BookAddDialog;
import dialogs.BookUpdateDialog;
import models.Author;
import models.Book;
import models.Publisher;
import tableModels.BookTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BookPanel extends JPanel {

    private JTable bookTable;
    private BookTableModel bookTableModel;
    private JScrollPane scrollPane;
    private BookDAO bookDAO;
    private JTextField searchField;

    private JComboBox<Author> authorComboBox;
    private JComboBox<Publisher> publisherComboBox;
    private AuthorDAO authorDAO;
    private PublisherDAO publisherDAO;


    public BookPanel() {
        setLayout(new BorderLayout());

        bookDAO = new BookDAO();
        bookTableModel = new BookTableModel(bookDAO.getAllBooks());
        bookTable = new JTable(bookTableModel);
        scrollPane = new JScrollPane(bookTable);

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new AddButtonListener());
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new UpdateButtonListener());
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeleteButtonListener());
        buttonPanel.add(deleteButton);

        searchField = new JTextField(15);
        buttonPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchButtonListener());
        buttonPanel.add(searchButton);

        authorDAO = new AuthorDAO();
        List<Author> authors = authorDAO.getAllAuthors();
        authorComboBox = new JComboBox<>(authors.toArray(new Author[0]));

        publisherDAO = new PublisherDAO();
        List<Publisher> publishers = publisherDAO.getAllPublishers();
        publisherComboBox = new JComboBox<>(publishers.toArray(new Publisher[0]));

        buttonPanel.add(new JLabel("Author:"));
        buttonPanel.add(authorComboBox);

        buttonPanel.add(new JLabel("Publisher:"));
        buttonPanel.add(publisherComboBox);

        JButton searchByAuthorAndPublisherButton = new JButton("Search by Author & Publisher");
        searchByAuthorAndPublisherButton.addActionListener(new SearchByAuthorAndPublisherButtonListener());
        buttonPanel.add(searchByAuthorAndPublisherButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(BookPanel.this);
            BookAddDialog bookAddDialog = new BookAddDialog(parentFrame);
            bookAddDialog.setVisible(true);
            Book newBook = bookAddDialog.getNewBook();
            if (newBook != null) {
                bookDAO.insertBook(newBook);
                bookTableModel.updateBooks(bookDAO.getAllBooks());
                bookTableModel.fireTableDataChanged();
            }
        }
    }

    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow >= 0) {
                Book selectedBook = bookTableModel.getBookAt(selectedRow);
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(BookPanel.this);

                BookUpdateDialog bookUpdateDialog = new BookUpdateDialog(parentFrame, selectedBook);
                bookUpdateDialog.setVisible(true);
                bookTableModel.updateBooks(bookDAO.getAllBooks());
            } else {
                JOptionPane.showMessageDialog(BookPanel.this, "Please select a book to update.", "Update Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow >= 0) {
                int result = JOptionPane.showConfirmDialog(BookPanel.this, "Are you sure you want to delete this book?", "Delete book", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    Book bookToDelete = bookTableModel.getBookAt(selectedRow);
                    bookDAO.deleteBook(bookToDelete.getId());
                    bookTableModel.updateBooks(bookDAO.getAllBooks());
                    bookTableModel.fireTableDataChanged();
                }
            } else {
                JOptionPane.showMessageDialog(BookPanel.this, "Please select a book to delete.");
            }
        }
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                List<Book> searchedBooks = bookDAO.searchBooksByName(searchTerm);
                bookTableModel.updateBooks(searchedBooks);
                bookTableModel.fireTableDataChanged();
            } else {
                bookTableModel.updateBooks(bookDAO.getAllBooks());
                bookTableModel.fireTableDataChanged();
            }
        }
    }

    private class SearchByAuthorAndPublisherButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Author selectedAuthor = (Author) authorComboBox.getSelectedItem();
            Publisher selectedPublisher = (Publisher) publisherComboBox.getSelectedItem();

            if (selectedAuthor != null && selectedPublisher != null) {
                List<Book> books = bookDAO.getBooksByAuthorAndPublisher(selectedAuthor.getId(), selectedPublisher.getId());
                bookTableModel.updateBooks(books);
                bookTableModel.fireTableDataChanged();
            }
        }
    }


}