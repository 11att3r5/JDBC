import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class DBApp 
{
	//DAO Class
	static String DATABASE_URL = "jdbc:mysql://localhost:3306/products";
	static Connection connection;
	
	public DBApp(String username , String password) throws SQLException
	{
			connection = DriverManager.getConnection(DATABASE_URL, username, password);
	}
	
	public ArrayList<Item> getAllItems()
	{
		ArrayList<Item> results = null;
		ResultSet resultset = null;
		
		try
		{
			PreparedStatement selectAllItems = connection.prepareStatement("SELECT * FROM item");
			resultset = selectAllItems.executeQuery();
			results = new ArrayList<Item>();
			
			while(resultset.next())
			{
				int itemInt = resultset.getInt("ItemID");
				int itemQuantity = resultset.getInt("Quantity");
				double itemCost = resultset.getDouble("Cost");
				String itemDesc = resultset.getString("ItemDesc");
				Date itemDate = resultset.getDate("DateAdded");
				LocalDate localdate = itemDate.toLocalDate();
				int year = localdate.getYear();
				int month = localdate.getMonthValue();
				int day = localdate.getDayOfMonth();
				GregorianCalendar itemGCDate = new GregorianCalendar(year,month -1 ,day);
				results.add(new Item(itemInt, itemQuantity, itemCost, itemDesc, itemGCDate));		
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				resultset.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
				close();
			}
		}
		return results;
	}
	
	public Item getSelectedItem(int ItemID)
	{
		ResultSet resultset = null;
		Item results = null;
		
		try
		{
			PreparedStatement selectItem = connection.prepareStatement("SELECT * FROM item WHERE ItemID = ?");
			
			selectItem.setInt(1, ItemID);
			
			resultset = selectItem.executeQuery();
			while(resultset.next())
			{
				int itemInt = resultset.getInt("ItemID");
				int itemQuantity = resultset.getInt("Quantity");
				double itemCost = resultset.getDouble("Cost");
				String itemDesc = resultset.getString("ItemDesc");
				Date itemDate = resultset.getDate("DateAdded");
				LocalDate localdate = itemDate.toLocalDate();
				int year = localdate.getYear();
				int month = localdate.getMonthValue();
				int day = localdate.getDayOfMonth();
				GregorianCalendar itemGCDate = new GregorianCalendar(year,month -1 ,day);
				results = new Item(itemInt, itemQuantity, itemCost, itemDesc, itemGCDate);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				resultset.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
				close();
			}
		}
		return results;
	}
	
	public int addItem(Item item)
	{
		int result = 0;
		
		try
		{
			PreparedStatement insertNewItem = connection.prepareStatement(
					"INSERT INTO Item "
					+ "(ItemID ,ItemDesc, DateAdded, Quantity, Cost ) "
					+ "VALUES(?,?,?,?,?)");
			GregorianCalendar date = item.getDateAdded();
			java.sql.Date sqlDate = new java.sql.Date(date.getTimeInMillis());
			insertNewItem.setInt(1, item.getItemId());
			insertNewItem.setString(2, item.getDescription());
			insertNewItem.setInt(4, item.getQuantity());
			insertNewItem.setDouble(5, item.getCost());
			insertNewItem.setDate(3, sqlDate);
			
			result = insertNewItem.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			close();
		}
		return result;
	}
	
	public int removeItem(int itemid)
	{
		int result = 0;
		try
		{
			PreparedStatement removeItem = connection.prepareStatement("DELETE FROM Item "
					+ "WHERE ItemID = ?");
			
				removeItem.setInt(1, itemid);
				result = removeItem.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			close();
		}
		return result;
	}
	
	public int changeItem(Item item)
	{
		int result = 0;
		
		try
		{
		PreparedStatement changeitem = connection.prepareStatement("UPDATE item SET ItemDesc = ?, DateAdded = ?, Quantity = ?, Cost = ? WHERE ItemID = ?");
		
		GregorianCalendar date = item.getDateAdded();
		java.sql.Date sqlDate = new java.sql.Date(date.getTimeInMillis());
		changeitem.setString(1, item.getDescription());
		changeitem.setInt(3, item.getQuantity());
		changeitem.setDouble(4, item.getCost());
		changeitem.setDate(2, sqlDate);
		changeitem.setInt(5, item.getItemId());
		
		result = changeitem.executeUpdate();
		
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			close();
		}
		return result;
	}
	
	public void close()
	{
		try
		{
			connection.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}