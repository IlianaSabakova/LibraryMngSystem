package tableModels;

import models.Book;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class BookTableModel extends AbstractTableModel {

    private List<Book> books;
    private final String[] columnNames = {"ID", "Title", "Author", "Publisher"};

    public BookTableModel(List<Book> books) {
        this.books = books;
    }

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = books.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return book.getId();
            case 1:
                return book.getTitle();
            case 2:
                return book.getAuthor().getLastName() + ", " + book.getAuthor().getFirstName();
            case 3:
                return book.getPublisher().getName();
            default:
                return null;
        }
    }

    public void updateBooks(List<Book> books) {
        this.books = books;
        fireTableDataChanged();
    }

    public Book getBookAt(int rowIndex) {
        return books.get(rowIndex);
    }
}

