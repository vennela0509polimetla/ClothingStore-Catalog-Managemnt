
import java.io.*;
import java.util.ArrayList;

public class ProductStorage {
    private static final String FILE_NAME = "products.dat";

    public void saveProducts(ArrayList<Product> products) {
        try (ObjectOutputStream output =
                 new ObjectOutputStream(
                     new FileOutputStream(FILE_NAME))) {

            output.writeObject(products);

        } catch (IOException e) {
            System.out.println("Error saving products: " + e.getMessage());
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
            System.out.println("Error loading products: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
