import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Book {
    int id;
    String name;
    String author;
    boolean available;

    public Book(int id, String name, String author, boolean available) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.available = available;
    }
}

public class LibraryManagementSystem extends JFrame {

    private ArrayList<Book> bookList = new ArrayList<>();
    private HashMap<Integer, Book> hashTable = new HashMap<>();

    private DefaultTableModel tableModel;

    public LibraryManagementSystem() {

        // ------------------ MODERN UI LOOK ------------------
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {}

        setTitle("Library Management System");
        setSize(1050, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ------------------ HEADER PANEL ------------------
        JPanel header = new JPanel();
        header.setBackground(new Color(30, 70, 140));
        JLabel title = new JLabel("Library Management System");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ------------------ TABLE ------------------
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Author", "Available"}, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Center column values
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(3).setCellRenderer(center);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // ------------------ SIDE BUTTON PANEL ------------------
        JPanel side = new JPanel();
        side.setLayout(new GridLayout(8, 1, 10, 10));
        side.setBackground(new Color(240, 240, 240));
        side.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton addBtn = modernButton("Add Book");
        JButton deleteBtn = modernButton("Delete Book");
        JButton searchIdBtn = modernButton("Search by ID");
        JButton searchNameBtn = modernButton("Search by Name");
        JButton updateBtn = modernButton("Update Availability");
        JButton sortBtn = modernButton("Sort by ID");
        JButton refreshBtn = modernButton("Refresh Table");
        JButton exitBtn = modernButton("Exit");

        side.add(addBtn);
        side.add(deleteBtn);
        side.add(searchIdBtn);
        side.add(searchNameBtn);
        side.add(updateBtn);
        side.add(sortBtn);
        side.add(refreshBtn);
        side.add(exitBtn);

        add(side, BorderLayout.EAST);

        // ------------------ BUTTON ACTIONS ------------------
        addBtn.addActionListener(e -> addBook());
        deleteBtn.addActionListener(e -> deleteBook());
        searchIdBtn.addActionListener(e -> searchById());
        searchNameBtn.addActionListener(e -> searchByName());
        updateBtn.addActionListener(e -> updateAvailability());
        sortBtn.addActionListener(e -> sortBooks());
        refreshBtn.addActionListener(e -> refreshTable());
        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    // ------------------ MODERN BUTTON STYLE ------------------
    private JButton modernButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(200, 225, 255));
        btn.setBorder(BorderFactory.createLineBorder(new Color(30, 70, 140), 2));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ---------------------- FUNCTIONALITY ----------------------

    private void addBook() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Book ID:"));
            String name = JOptionPane.showInputDialog("Enter Book Name:");
            String author = JOptionPane.showInputDialog("Enter Author:");
            boolean available = JOptionPane.showConfirmDialog(this, "Is it available?") == 0;

            Book book = new Book(id, name, author, available);
            bookList.add(book);
            hashTable.put(id, book);

            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }

    private void deleteBook() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Book ID to delete:"));

            Book book = hashTable.get(id);
            if (book != null) {
                bookList.remove(book);
                hashTable.remove(id);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Book deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Book not found.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    private void searchById() {
        sortBooks();
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Book ID:"));

            int left = 0, right = bookList.size() - 1;

            while (left <= right) {
                int mid = (left + right) / 2;
                Book b = bookList.get(mid);

                if (b.id == id) {
                    JOptionPane.showMessageDialog(this,
                            "📘 BOOK FOUND:\n\nID: " + b.id + "\nName: " + b.name +
                                    "\nAuthor: " + b.author + "\nAvailable: " + b.available);
                    return;
                }
                if (b.id < id) left = mid + 1;
                else right = mid - 1;
            }

            JOptionPane.showMessageDialog(this, "Book not found!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    private void searchByName() {
        String name = JOptionPane.showInputDialog("Enter Book Name:");

        for (Book b : bookList) {
            if (b.name.equalsIgnoreCase(name)) {
                JOptionPane.showMessageDialog(this,
                        "📘 BOOK FOUND:\n\nID: " + b.id + "\nName: " + b.name +
                                "\nAuthor: " + b.author + "\nAvailable: " + b.available);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Book not found!");
    }

    private void updateAvailability() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Book ID:"));
            Book book = hashTable.get(id);

            if (book != null) {
                book.available = !book.available;
                refreshTable();
                JOptionPane.showMessageDialog(this, "Availability Updated!");
            } else {
                JOptionPane.showMessageDialog(this, "Book not found!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    private void sortBooks() {
        for (int i = 0; i < bookList.size() - 1; i++) {
            for (int j = 0; j < bookList.size() - i - 1; j++) {
                if (bookList.get(j).id > bookList.get(j + 1).id) {
                    Collections.swap(bookList, j, j + 1);
                }
            }
        }
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Book b : bookList) {
            tableModel.addRow(new Object[]{
                    b.id, b.name, b.author, b.available ? "Yes" : "No"
            });
        }
    }

    public static void main(String[] args) {
        new LibraryManagementSystem();
    }
}
