package tableModels;

import models.Author;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class AuthorTableModel extends AbstractTableModel {

    private List<Author> authors;
    private final String[] columnNames = {"First Name", "Last Name"};

    public AuthorTableModel(List<Author> authors) {
        this.authors = authors;
    }

    @Override
    public int getRowCount() {
        return authors.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Author author = authors.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return author.getFirstName();
            case 1:
                return author.getLastName();
            default:
                return null;
        }
    }

    public Author getAuthorAt(int rowIndex) {
        return authors.get(rowIndex);
    }

    public void addRow(Author author) {
        authors.add(author);
        fireTableRowsInserted(authors.size() - 1, authors.size() - 1);
    }

    public void updateAuthors(List<Author> authors) {
        this.authors = authors;
    }


    public void setAuthors(List<Author> authors) {
        this.authors = authors;
        fireTableDataChanged();
    }
}
