import java.util.GregorianCalendar;

public class Item 
{
	int itemId, quantity;
	double cost;
	String description;
	GregorianCalendar dateAdded;
	
	public int getItemId()
	{
		return itemId;
	}
	
	public void setItemId(int id)
	{
		this.itemId  = id;
	}
	
	public int getQuantity()
	{
		return quantity;
	}
	
	public void setQuantity(int num)
	{
		this.quantity = num;
	}
	
	public double getCost()
	{
		return cost;
	}
	
	public void setCost(double amt)
	{
		this.cost = amt;
	}
	
	public GregorianCalendar getDateAdded()
	{
		return dateAdded;
	}
	
	public void setDateAdded(GregorianCalendar date)
	{
		this.dateAdded = date;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}

	public Item()
	{
		
	}
	
	public Item(int itemId, int quantity, double cost, String description, GregorianCalendar dateAdded)
	{
		setItemId(itemId);
		setQuantity(quantity);
		setCost(cost);
		setDescription(description);
		setDateAdded(dateAdded);
		
	}
}
