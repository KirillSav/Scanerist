package amber.server;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CAUTION: by running this test class you 
 * risk of affecting data in ScaneristDB_v4.db.
 * Please make a backup database file just in case.
 * @author Peteris Caurs
 */
public class SQLiteDBTest {
    
    /**
     * Test of deleteProduct method, of class SQLiteDB.
     */
    @Test
    public void testDeleteProductShouldReturnTrue() {
        Product product = new Product("BARCODE", "Product", 42);
        SQLiteDB.insertProduct(product);
        assertTrue(SQLiteDB.deleteProduct("BARCODE"));
    }

    /**
     * Test of insertProduct method, of class SQLiteDB.
     */
    @Test
    public void testDuplicateBarcodeShouldNotBeInserted() {
        Product product1 = new Product("BARCODE", "Product", 42);
        Product product2 = new Product("BARCODE", "Product with same barcode", 24);   
        assertEquals(true, SQLiteDB.insertProduct(product1));
        assertEquals(false, SQLiteDB.insertProduct(product2)); // insert duplicate
        SQLiteDB.deleteProduct("BARCODE"); // leave the database unaffected
    }
    
    /**
     * Test of insertProduct method, of class SQLiteDB.
     */
    @Test
    public void testProductWithNullDatetimeShouldBeGeneratedInTheDatabase() {
        Product product = new Product("BARCODE", "Product", 42);
        assertEquals(true, SQLiteDB.insertProduct(product));
        Product retrieved = SQLiteDB.findProduct("BARCODE");
        assertTrue(retrieved.getDatetime() != null); // DEFAULT CURRENT_TIMESTAMP
        SQLiteDB.deleteProduct("BARCODE"); // leave the database unaffected
    }
    

    /**
     * Test of productExists method, of class SQLiteDB.
     */
    @Test
    public void testProductBarcodeShouldExist() {
        boolean result = SQLiteDB.productExists(null);
        assertEquals(false, result);
        Product product = new Product("BARCODE", "Product", 42);
        SQLiteDB.insertProduct(product);
        assertTrue(SQLiteDB.productExists("BARCODE"));
        SQLiteDB.deleteProduct("BARCODE"); // leave the database unaffected
    }

    /**
     * Test of findProduct method, of class SQLiteDB.
     */
    @Test
    public void testFindProductShouldNotReturnNull() {
        Product product = new Product("BARCODE", "Product", 42);
        SQLiteDB.insertProduct(product);
        assertTrue(SQLiteDB.findProduct("BARCODE") != null);
        SQLiteDB.deleteProduct("BARCODE"); // leave the database unaffected
    }
    
    /**
     * Test of GetList method, of class SQLiteDB.
     */
    @Test
    public void testGetListNoDuplicateId() {
        Product product1 = new Product("1111", "NAME", 50);
        Product product2 = new Product("2222", "NAME", 100);
        Product product3 = new Product("3333", "NAME", 150);
        Product product4 = new Product("4444", "NAME", 230);
        SQLiteDB.insertProduct(product1);
        SQLiteDB.insertProduct(product2);
        SQLiteDB.insertProduct(product3);
        SQLiteDB.insertProduct(product4);
        List<Product> products = SQLiteDB.GetList("NAME");
        assertTrue(products.get(0).getProduct_id() != products.get(1).getProduct_id());
        SQLiteDB.deleteProduct("1111");
        SQLiteDB.deleteProduct("2222");
        SQLiteDB.deleteProduct("3333");
        SQLiteDB.deleteProduct("4444");
        
    }
    
}
