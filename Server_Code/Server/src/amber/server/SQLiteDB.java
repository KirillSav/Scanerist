package amber.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class SQLiteDB {


    private static Connection connect() {
        Connection connection = null;

        String url = "jdbc:sqlite:ScaneristDB_v4.db";
        try {
        	connection = DriverManager.getConnection(url);
        } catch (SQLException ex) {
        	System.out.println("no connection to database");
            System.out.println(ex);
        }
        return connection;
    }

    static public boolean insertProduct(Product product) {
        String sql = "INSERT INTO [Product] (barcode, name, productType, amount, "
                + "datetimeModified, price, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        if(!productExists(product.barCode)){
	        try (Connection conn = connect();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        	
	            // Set the values
	        	pstmt.setString(1, product.barCode);
	            pstmt.setString(2, product.productName);
	            pstmt.setString(3, product.productType);
	            pstmt.setInt(4, product.amount);
	            pstmt.setString(5,"datetime(\'now\')");
	            pstmt.setDouble(6, product.price);
	            pstmt.setString(7, product.description);
	
	            // Execute query
	            pstmt.executeUpdate();
	        } catch (SQLException ex) {
	        	System.out.println("fail");
	            System.out.println(ex);
	            return false;
	        }
	        return true;
	    }
        return false;
        
    }

    static public void selectProducts() {
        String sql = "SELECT * FROM [Product]";

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            while (rs.next()) {
                System.out.println(rs.getInt("product_id") +  "\t" +
                                   rs.getString("barcode")  +  "\t" +
                                   rs.getString("name")  +  "\t" +
                                   rs.getString("productType")  +  "\t" +
                                   rs.getInt("amount")  +  "\t" +
                                   rs.getString("datetimeModified")  +  "\t" +
                                   rs.getDouble("price")  +  "\t" +
                                   rs.getString("description")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    static public boolean productExists(String barcode) {
        String sql = "SELECT barcode FROM [Product] where barcode = ?";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the values
            pstmt.setString(1, barcode);

            ResultSet resultset = pstmt.executeQuery();

            if(resultset.next()) {
            	return true;
            }

            // Execute query
            pstmt.executeUpdate();
        } catch (SQLException ex) {}
        return false;
    }
    
    static public Product findProduct(String barcode){
    	if(productExists(barcode)){
    		String sql="SELECT * FROM [Product] Where barcode = '"+barcode+"'";
    		try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
    			// Set the values
    			ResultSet resultset = pstmt.executeQuery();
                Product product;
                product=new Product(resultset.getInt(1),resultset.getString(2),resultset.getString(3),
            						resultset.getString(4),resultset.getInt(5),
            						resultset.getString(6),resultset.getLong(7),resultset.getString(8));
            	return product;
            } catch (SQLException ex) {}

    	}
    	return null;
    }
    
    static public boolean changeProductAmount(String barCode,int amount,boolean add){
    	if(productExists(barCode)){
    		int curramount=0;
    		String sql="SELECT amount From [product] Where barcode="+barCode;
    		try (Connection conn = connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

    				// Set the values
    			   ResultSet resultset = pstmt.executeQuery();
                   if(resultset.next()){
                	   curramount=resultset.getInt("amount");
                   }
                   if(!add) amount=-amount;
                   sql="Update [product] SET amount="+(curramount+amount)+" Where barcode="+barCode;
            	   PreparedStatement pstmt2=conn.prepareStatement(sql);
            	   pstmt2.executeUpdate();
            	   return true;
                   
    		} catch (SQLException ex) {
    			System.out.println("exception:"+ex.getStackTrace());
    		}

    	}
    	return false;
    }
    
    static public boolean UpdateProduct(Product product){
    	if(productExists(product.barCode)) {
    		String sql="Update [Product] SET name = ? , productType = ?  Where barcode="+product.barCode+";";
    		try(Connection conn= connect();
    			PreparedStatement pstmt = conn.prepareStatement(sql)){
    			//Set values
    			pstmt.setString(1, product.productName);
    			pstmt.setString(2, product.productType);
    			pstmt.executeUpdate();
    			return true;
    		}catch(Exception e){
    			System.out.println("Update exception:"+e.getStackTrace()+" | "+e.getMessage());
    		}
    	}
    	return false;
    }
    
    static public List<Product> GetList(String str){
    	List<Product> list=new ArrayList<Product>();
    	if(str==null) return null;
    	String sql;
    	if(str.equals("")){
    		sql="Select * From [Product] Order by productType,name";
    	}else{
    		sql="Select * From [Product]"+
    			" Where (name like \"%"+str+"%\") Order by productType,name";
    	}
    	try(Connection conn=connect();
    		PreparedStatement pstmt = conn.prepareStatement(sql)){
    		ResultSet res=pstmt.executeQuery();
    		while(res.next()){
    			list.add(new Product(res.getInt(1),res.getString(2),res.getString(3),res.getString(4), 
    								 res.getInt(5) , res.getString(6) , res.getLong(7) ,res.getString(8)));
    		}
    	}catch(Exception e){}
    	return list;
    }
    
    static public boolean deleteProduct(String barcode) {
    	if(productExists(barcode)) {
    		String sql="DELETE FROM [product] WHERE barcode = ?";
    		try(Connection conn= connect();
    			PreparedStatement pstmt = conn.prepareStatement(sql)){
    			//Set values
    			pstmt.setString(1, barcode);
    			
    			pstmt.executeUpdate();
    			return true;
    		}catch(Exception e){}
    	}
    	return false;
    }
}
