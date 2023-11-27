package dialogs;

import models.Publisher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PublisherAddDialog extends JDialog {

    private JTextField nameField;
    private JTextField addressField;
    private JButton addButton;
    private JButton cancelButton;
    private Publisher newPublisher;

    public PublisherAddDialog() {
        setTitle("Add Publisher");
        setModal(true);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        inputPanel.add(addressField);

        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("Add");
        addButton.addActionListener(new AddButtonListener());
        buttonPanel.add(addButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();

            if (!name.isEmpty() && !address.isEmpty()) {
                newPublisher = new Publisher();
                newPublisher.setName(name);
                newPublisher.setAddress(address);
                dispose();
            } else {
                JOptionPane.showMessageDialog(PublisherAddDialog.this, "Please fill in all fields.");
            }
        }
    }

    public Publisher getNewPublisher() {
        return newPublisher;
    }
}
