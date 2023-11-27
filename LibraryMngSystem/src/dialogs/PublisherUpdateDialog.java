package dialogs;

import models.Publisher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PublisherUpdateDialog extends JDialog {

    private JTextField nameField;
    private JTextField addressField;
    private JButton updateButton;
    private JButton cancelButton;
    private Publisher updatedPublisher;

    public PublisherUpdateDialog(Publisher publisher) {
        setTitle("Update Publisher");
        setModal(true);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField(publisher.getName());
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Address:"));
        addressField = new JTextField(publisher.getAddress());
        inputPanel.add(addressField);

        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        updateButton = new JButton("Update");
        updateButton.addActionListener(new UpdateButtonListener(publisher));
        buttonPanel.add(updateButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class UpdateButtonListener implements ActionListener {
        private Publisher publisher;

        public UpdateButtonListener(Publisher publisher) {
            this.publisher = publisher;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();

            if (!name.isEmpty() && !address.isEmpty()) {
                updatedPublisher = new Publisher();
                updatedPublisher.setId(publisher.getId());
                updatedPublisher.setName(name);
                updatedPublisher.setAddress(address);
                dispose();
            } else {
                JOptionPane.showMessageDialog(PublisherUpdateDialog.this, "Please fill in all fields.");
            }
        }
    }

    public Publisher getUpdatedPublisher() {
        return updatedPublisher;
    }
}
