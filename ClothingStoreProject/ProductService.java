
import java.util.ArrayList;

public class ProductService {
    private ArrayList<Product> products;
    private ProductStorage storage;

    public ProductService() {
        storage = new ProductStorage();
        products = storage.loadProducts();
    }

    public ArrayList<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public void addProduct(Product product) {
        products.add(product);
        storage.saveProducts(products);
    }

    public boolean deleteProduct(int index) {
        if (index < 0 || index >= products.size()) {
            return false;
        }

        products.remove(index);
        storage.saveProducts(products);
        return true;
    }

    public boolean updateStock(int index, int newStock) {
        if (index < 0 || index >= products.size() || newStock < 0) {
            return false;
        }

        products.get(index).setStock(newStock);
        storage.saveProducts(products);
        return true;
    }

    public boolean updateProduct(int index, Product updatedProduct) {
        if (index < 0 || index >= products.size()) {
            return false;
        }

        products.set(index, updatedProduct);
        storage.saveProducts(products);
        return true;
    }

    public ArrayList<Product> searchProducts(String keyword) {
        ArrayList<Product> result = new ArrayList<>();
        String search = keyword.toLowerCase();

        for (Product product : products) {
            if (product.getName().toLowerCase().contains(search)
                    || product.getCategory().toLowerCase().contains(search)) {
                result.add(product);
            }
        }

        return result;
    }
}
