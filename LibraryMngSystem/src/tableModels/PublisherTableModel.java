package tableModels;

import models.Publisher;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PublisherTableModel extends AbstractTableModel {

    private List<Publisher> publishers;
    private final String[] columnNames = {"Name", "Address"};

    public PublisherTableModel(List<Publisher> publishers) {
        this.publishers = publishers;
    }

    @Override
    public int getRowCount() {
        return publishers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Publisher publisher = publishers.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return publisher.getName();
            case 1:
                return publisher.getAddress();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Publisher getPublisherAt(int rowIndex) {
        return publishers.get(rowIndex);
    }

    public void setPublishers(List<Publisher> publishers) {
        this.publishers = publishers;
        fireTableDataChanged();
    }

    public void updatePublishers(List<Publisher> publishers) {
        this.publishers = publishers;
    }

}

