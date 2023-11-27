package ui;

import dao.PublisherDAO;
import dialogs.PublisherAddDialog;
import dialogs.PublisherUpdateDialog;
import models.Publisher;
import tableModels.PublisherTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PublisherPanel extends JPanel {

    private JTable publisherTable;
    private PublisherTableModel publisherTableModel;
    private JScrollPane scrollPane;
    private PublisherDAO publisherDAO;
    private JTextField searchField;

    public PublisherPanel() {
        setLayout(new BorderLayout());

        publisherDAO = new PublisherDAO();
        publisherTableModel = new PublisherTableModel(publisherDAO.getAllPublishers());
        publisherTable = new JTable(publisherTableModel);
        scrollPane = new JScrollPane(publisherTable);

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
            PublisherAddDialog publisherAddDialog = new PublisherAddDialog();
            publisherAddDialog.setVisible(true);
            Publisher newPublisher = publisherAddDialog.getNewPublisher();

            if (newPublisher != null && !newPublisher.getName().trim().isEmpty() && !newPublisher.getAddress().trim().isEmpty()) {
                publisherDAO.insertPublisher(newPublisher);
                publisherTableModel.setPublishers(publisherDAO.getAllPublishers());
            }
        }
    }


    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = publisherTable.getSelectedRow();

            if (selectedRow >= 0) {
                Publisher selectedPublisher = publisherTableModel.getPublisherAt(selectedRow);
                PublisherUpdateDialog publisherUpdateDialog = new PublisherUpdateDialog(selectedPublisher);
                publisherUpdateDialog.setVisible(true);
                Publisher updatedPublisher = publisherUpdateDialog.getUpdatedPublisher();

                if (updatedPublisher != null) {
                    publisherDAO.updatePublisher(updatedPublisher);
                    publisherTableModel.setPublishers(publisherDAO.getAllPublishers());
                }
            } else {
                JOptionPane.showMessageDialog(PublisherPanel.this, "Please select a publisher to update.");
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = publisherTable.getSelectedRow();
            if (selectedRow >= 0) {
                int result = JOptionPane.showConfirmDialog(PublisherPanel.this, "Are you sure you want to delete this publisher?", "Delete publisher", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    Publisher publisherToDelete = publisherTableModel.getPublisherAt(selectedRow);
                    if (canDeletePublisher(publisherToDelete.getId())) {
                        publisherDAO.deletePublisher(publisherToDelete.getId());
                        publisherTableModel.updatePublishers(publisherDAO.getAllPublishers());
                        publisherTableModel.fireTableDataChanged();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(PublisherPanel.this, "Please select a publisher to delete.");
            }
        }
    }


    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText();
            if (!searchTerm.isEmpty()) {
                publisherTableModel.setPublishers(publisherDAO.searchPublishers(searchTerm));
            } else {
                publisherTableModel.setPublishers(publisherDAO.getAllPublishers());
            }
        }
    }

    private boolean canDeletePublisher(int publisherId) {
        PublisherDAO publisherDAO = new PublisherDAO();
        if (publisherDAO.hasRelatedBooks(publisherId)) {
            JOptionPane.showMessageDialog(this, "Cannot delete publisher. There are books associated with this publisher.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

}
