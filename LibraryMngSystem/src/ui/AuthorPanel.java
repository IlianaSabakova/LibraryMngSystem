package ui;

import dao.AuthorDAO;
import models.Author;
import tableModels.AuthorTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AuthorPanel extends JPanel {

    private JTable authorTable;
    private AuthorTableModel authorTableModel;
    private JScrollPane scrollPane;
    private AuthorDAO authorDAO;
    private JTextField searchField;


    public AuthorPanel() {
        setLayout(new BorderLayout());

        authorDAO = new AuthorDAO();
        authorTableModel = new AuthorTableModel(authorDAO.getAllAuthors());
        authorTable = new JTable(authorTableModel);
        scrollPane = new JScrollPane(authorTable);

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

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }


    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String firstName = JOptionPane.showInputDialog("Enter author's first name:");
            String lastName = JOptionPane.showInputDialog("Enter author's last name:");

            if (firstName != null && !firstName.trim().isEmpty() && lastName != null && !lastName.trim().isEmpty()) {
                Author author = new Author();
                author.setFirstName(firstName);
                author.setLastName(lastName);

                authorDAO.insertAuthor(author);
                authorTableModel.updateAuthors(authorDAO.getAllAuthors());
                authorTableModel.fireTableDataChanged();
            }
        }
    }


    // UpdateButtonListener
    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = authorTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(AuthorPanel.this, "Please select an author to update.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Author selectedAuthor = authorTableModel.getAuthorAt(selectedRow);
            String newFirstName = JOptionPane.showInputDialog("Enter author's new first name:", selectedAuthor.getFirstName());
            String newLastName = JOptionPane.showInputDialog("Enter author's new last name:", selectedAuthor.getLastName());

            if (newFirstName != null && newLastName != null) {
                selectedAuthor.setFirstName(newFirstName);
                selectedAuthor.setLastName(newLastName);
                authorDAO.updateAuthor(selectedAuthor);
                authorTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
            }
        }
    }

    // DeleteButtonListener
    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = authorTable.getSelectedRow();
            if (selectedRow >= 0) {
                Author authorToDelete = authorTableModel.getAuthorAt(selectedRow);
                int result = JOptionPane.showConfirmDialog(AuthorPanel.this, "Are you sure you want to delete this author?", "Delete author", JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    if (canDeleteAuthor(authorToDelete.getId())) {
                        authorDAO.deleteAuthor(authorToDelete.getId());
                        authorTableModel.updateAuthors(authorDAO.getAllAuthors());
                        authorTableModel.fireTableDataChanged();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(AuthorPanel.this, "Please select an author to delete.");
            }
        }
    }

    // SearchButtonListener
    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchLastName = searchField.getText();
            List<Author> authors = authorDAO.searchAuthorsByLastName(searchLastName);
            authorTableModel.setAuthors(authors);
        }
    }

    private boolean canDeleteAuthor(int authorId) {
        AuthorDAO authorDAO = new AuthorDAO();
        if (authorDAO.hasRelatedBooks(authorId)) {
            JOptionPane.showMessageDialog(this, "Cannot delete author. There are books associated with this author.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
