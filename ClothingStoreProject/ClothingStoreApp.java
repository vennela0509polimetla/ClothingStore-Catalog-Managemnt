
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

// FRONTEND
public class ClothingStoreApp extends JFrame {

    JTextField name, color, price, stock, search;
    JComboBox<String> category, size;
    JCheckBox clearance;
    JTable table;
    DefaultTableModel model;

    ProductService service = new ProductService();

    // Products currently displayed in the table
    ArrayList<Product> displayedProducts = new ArrayList<>();

    Color pink = new Color(220, 60, 125);
    Color purple = new Color(125, 70, 170);
    Color background = new Color(255, 239, 246);

    ClothingStoreApp() {

        setTitle("Fashion Hub - Clothing Store Management");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setBackground(background);
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel(
            "FASHION HUB - CLOTHING STORE", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setForeground(pink);

        // PRODUCT FORM
        JPanel form = new JPanel(new GridLayout(7, 2, 10, 10));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createTitledBorder(
            "Product Details"));

        name = new JTextField();
        color = new JTextField();
        price = new JTextField();
        stock = new JTextField();

        category = new JComboBox<>(new String[]{
            "Men", "Women", "Kids", "Ethnic Wear",
            "Western Wear", "Accessories"
        });

        size = new JComboBox<>(new String[]{
            "XS", "S", "M", "L", "XL", "XXL", "Free Size"
        });

        clearance = new JCheckBox("Apply 20% Discount");
        clearance.setBackground(Color.WHITE);

        addField(form, "Product Name:", name);
        addField(form, "Category:", category);
        addField(form, "Size:", size);
        addField(form, "Color:", color);
        addField(form, "Price (Rs.):", price);
        addField(form, "Stock:", stock);
        addField(form, "Clearance:", clearance);

        // PRODUCT TABLE
        model = new DefaultTableModel(
            new String[]{
                "Name", "Category", "Size", "Color",
                "Price", "Selling Price", "Stock"
            }, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(255, 210, 230));

        table.getTableHeader().setBackground(pink);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 14));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedProduct();
            }
        });

        JScrollPane scroll = new JScrollPane(table);

        // BUTTONS
        JButton add = createButton(
            "Add Product", new Color(40, 150, 90));
        JButton edit = createButton("Edit Product", purple);
        JButton delete = createButton(
            "Delete Product", new Color(210, 55, 65));
        JButton update = createButton(
            "Update Stock", new Color(40, 120, 200));
        JButton clear = createButton("Clear Fields", Color.GRAY);

        add.addActionListener(e -> addProduct());
        edit.addActionListener(e -> editProduct());
        delete.addActionListener(e -> deleteProduct());
        update.addActionListener(e -> updateStock());
        clear.addActionListener(e -> clearFields());

        JPanel buttons = new JPanel();
        buttons.setBackground(background);
        buttons.add(add);
        buttons.add(edit);
        buttons.add(delete);
        buttons.add(update);
        buttons.add(clear);

        // SEARCH PANEL
        search = new JTextField(18);

        JButton searchButton = createButton("Search", pink);
        JButton showAll = createButton("Show All", purple);

        searchButton.addActionListener(e -> searchProduct());
        showAll.addActionListener(e -> showAllProducts());

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(background);
        searchPanel.add(new JLabel("Search Name/Category:"));
        searchPanel.add(search);
        searchPanel.add(searchButton);
        searchPanel.add(showAll);

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBackground(background);
        top.add(heading, BorderLayout.NORTH);
        top.add(form, BorderLayout.CENTER);
        top.add(searchPanel, BorderLayout.SOUTH);

        main.add(top, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);

        setContentPane(main);

        refreshTable();
        setVisible(true);
    }

    // ADD FORM FIELD
    void addField(JPanel panel, String label, JComponent field) {
        JLabel text = new JLabel(label);
        text.setFont(new Font("Arial", Font.BOLD, 14));
        text.setForeground(purple);

        panel.add(text);
        panel.add(field);
    }

    // CREATE BUTTON
    JButton createButton(String text, Color buttonColor) {
        JButton button = new JButton(text);
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    // VALIDATE INPUT
    boolean validateFields() {

        if (name.getText().trim().isEmpty()
                || color.getText().trim().isEmpty()
                || price.getText().trim().isEmpty()
                || stock.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                "Please fill all product details.");
            return false;
        }

        try {
            double p = Double.parseDouble(price.getText().trim());
            int q = Integer.parseInt(stock.getText().trim());

            if (!Double.isFinite(p) || p <= 0 || q < 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Enter valid price and stock values.");
            return false;
        }

        return true;
    }

    // GET PRODUCT DETAILS FROM FORM
    Product getProductFromForm() {

        return new Product(
            name.getText().trim(),
            category.getSelectedItem().toString(),
            size.getSelectedItem().toString(),
            color.getText().trim(),
            Double.parseDouble(price.getText().trim()),
            Integer.parseInt(stock.getText().trim()),
            clearance.isSelected()
        );
    }

    // ADD PRODUCT
    void addProduct() {

        if (!validateFields()) return;

        Product product = getProductFromForm();

        service.addProduct(product);

        refreshTable();

        JOptionPane.showMessageDialog(this,
            "Product Added Successfully!");

        clearFields();
    }

    // EDIT PRODUCT
    void editProduct() {

        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a product to edit.");
            return;
        }

        if (!validateFields()) return;

        Product oldProduct = displayedProducts.get(row);
        Product updatedProduct = getProductFromForm();

        if (service.updateProduct(oldProduct, updatedProduct)) {

            refreshTable();

            JOptionPane.showMessageDialog(this,
                "Product Updated Successfully!");

            clearFields();
        }
    }

    // DELETE PRODUCT
    void deleteProduct() {

        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a product.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this product?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            Product selectedProduct = displayedProducts.get(row);

            service.deleteProduct(selectedProduct);

            refreshTable();

            JOptionPane.showMessageDialog(this,
                "Product Deleted Successfully!");

            clearFields();
        }
    }

    // UPDATE STOCK
    void updateStock() {

        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a product.");
            return;
        }

        String value = JOptionPane.showInputDialog(
            this, "Enter New Stock Quantity:");

        if (value == null) return;

        try {
            int quantity = Integer.parseInt(value.trim());

            if (quantity < 0) {
                throw new NumberFormatException();
            }

            Product selectedProduct = displayedProducts.get(row);

            service.updateStock(selectedProduct, quantity);

            refreshTable();

            JOptionPane.showMessageDialog(this,
                "Stock Updated Successfully!");

            clearFields();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Enter a valid non-negative whole number.");
        }
    }

    // SEARCH PRODUCT
    void searchProduct() {

        String keyword = search.getText().trim();

        ArrayList<Product> result =
            service.searchProducts(keyword);

        displayProducts(result);
    }

    // SHOW ALL PRODUCTS
    void showAllProducts() {
        search.setText("");
        refreshTable();
    }

    // REFRESH TABLE
    void refreshTable() {
        displayProducts(service.getAllProducts());
    }

    // DISPLAY PRODUCTS
    void displayProducts(ArrayList<Product> products) {

        displayedProducts = new ArrayList<>(products);

        model.setRowCount(0);

        for (Product p : displayedProducts) {

            model.addRow(new Object[]{
                p.getName(),
                p.getCategory(),
                p.getSize(),
                p.getColor(),
                String.format("%.2f", p.getPrice()),
                String.format("%.2f", p.getSellingPrice()),
                p.getStock()
            });
        }
    }

    // LOAD SELECTED PRODUCT INTO FORM
    void loadSelectedProduct() {

        int row = table.getSelectedRow();

        if (row < 0 || row >= displayedProducts.size()) {
            return;
        }

        Product p = displayedProducts.get(row);

        name.setText(p.getName());
        category.setSelectedItem(p.getCategory());
        size.setSelectedItem(p.getSize());
        color.setText(p.getColor());
        price.setText(String.valueOf(p.getPrice()));
        stock.setText(String.valueOf(p.getStock()));
        clearance.setSelected(p.isClearance());
    }

    // CLEAR FIELDS
    void clearFields() {

        name.setText("");
        color.setText("");
        price.setText("");
        stock.setText("");

        category.setSelectedIndex(0);
        size.setSelectedIndex(0);
        clearance.setSelected(false);

        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClothingStoreApp::new);
    }
}


// PRODUCT MODEL
class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String category;
    private String size;
    private String color;
    private double price;
    private int stock;
    private boolean clearance;

    Product(String name, String category, String size,
            String color, double price, int stock,
            boolean clearance) {

        this.name = name;
        this.category = category;
        this.size = size;
        this.color = color;
        this.price = price;
        this.stock = stock;
        this.clearance = clearance;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public boolean isClearance() {
        return clearance;
    }

    public double getSellingPrice() {
        return clearance ? price * 0.80 : price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}


// PRODUCT STORAGE
class ProductStorage {

    private static final String FILE_NAME = "products.dat";

    public void saveProducts(ArrayList<Product> products) {

        try (ObjectOutputStream output =
                 new ObjectOutputStream(
                     new FileOutputStream(FILE_NAME))) {

            output.writeObject(products);

        } catch (IOException e) {

            JOptionPane.showMessageDialog(null,
                "Error saving products: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Product> loadProducts() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream input =
                 new ObjectInputStream(
                     new FileInputStream(FILE_NAME))) {

            return (ArrayList<Product>) input.readObject();

        } catch (IOException | ClassNotFoundException e) {

            JOptionPane.showMessageDialog(null,
                "Error loading products: " + e.getMessage());

            return new ArrayList<>();
        }
    }
}


// PRODUCT SERVICE - BACKEND LOGIC
class ProductService {

    private ArrayList<Product> products;
    private ProductStorage storage;

    ProductService() {
        storage = new ProductStorage();
        products = storage.loadProducts();
    }

    public ArrayList<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    // ADD
    public void addProduct(Product product) {
        products.add(product);
        storage.saveProducts(products);
    }

    // EDIT
    public boolean updateProduct(Product oldProduct,
                                 Product newProduct) {

        int index = products.indexOf(oldProduct);

        if (index < 0) return false;

        products.set(index, newProduct);
        storage.saveProducts(products);

        return true;
    }

    // DELETE
    public boolean deleteProduct(Product product) {

        boolean removed = products.remove(product);

        if (removed) {
            storage.saveProducts(products);
        }

        return removed;
    }

    // UPDATE STOCK
    public boolean updateStock(Product product, int quantity) {

        if (quantity < 0) return false;

        int index = products.indexOf(product);

        if (index < 0) return false;

        products.get(index).setStock(quantity);
        storage.saveProducts(products);

        return true;
    }

    // SEARCH
    public ArrayList<Product> searchProducts(String keyword) {

        ArrayList<Product> result = new ArrayList<>();

        String search = keyword.toLowerCase();

        for (Product p : products) {

            if (p.getName().toLowerCase().contains(search)
                    || p.getCategory().toLowerCase().contains(search)) {

                result.add(p);
            }
        }

        return result;
    }
}
