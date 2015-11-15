import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

class GUI extends JFrame
{
	//GUI application and functions
	/**
	 * 
	 */
	private DBApp itemQueries;
	private ArrayList<Item> results;
	private boolean correctpass;
	
	private static final long serialVersionUID = 1L;
	
	private JPanel east = new JPanel();
	private JPanel west = new JPanel();
	private JPanel north = new JPanel();
	private JPanel south =  new JPanel();
	
	private JLabel item = new JLabel("Item");
	private JLabel quantity = new JLabel("Quantity");
	private JLabel cost = new JLabel("Cost");
	private JLabel date = new JLabel("Date Added");
	private JLabel itemID = new JLabel("Item ID: ");
	
	private JTextField theItem = new JTextField(20);
	private JTextField theQuantity = new JTextField(20);
	private JTextField theCost = new JTextField(20);
	private JTextField theDate = new JTextField(20);
	
	private JComboBox<Object> itembox = new JComboBox<>();
	
	private JButton add = new JButton("Add");
	private JButton delete = new JButton("Delete");
	private JButton change = new JButton("Change");
	private JButton clear = new JButton("Clear");
	private JButton exit = new JButton("Exit");
	
	public GUI()
	{
		super("Products");
		
		//Password and username info
		correctpass = false;
		while(correctpass == false)
		{
			String username = null;
			String password = null;
			JLabel jUserName = new JLabel("User Name");
			JTextField userName = new JTextField();
			JLabel jPassword = new JLabel("Password");
			JPasswordField pass = new JPasswordField();
			Object[] ob = {jUserName, userName, jPassword, pass};
			int result =  JOptionPane.showConfirmDialog(null, ob, "Login", JOptionPane.OK_CANCEL_OPTION);
				        
			if(result == JOptionPane.OK_OPTION)
			{
			    username = userName.getText();
			    String str = new String(pass.getPassword());
			    password = str;
			}
			else if (result ==  JOptionPane.CANCEL_OPTION)
			{
			    System.exit(-1);
			}
		
			try 
			{
				itemQueries = new DBApp(username, password);
				correctpass = true;
			} 
			catch (SQLException e1) 
			{
				JOptionPane.showMessageDialog(null, "The username and/or password was incorrect", "Invalid Login", JOptionPane.ERROR_MESSAGE);
				correctpass = false;
			}
		}
		
		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		
		this.add(east, BorderLayout.EAST);
		this.add(north, BorderLayout.NORTH);
		this.add(west, BorderLayout.WEST);
		this.add(south, BorderLayout.SOUTH);
		
		north.add(itemID);
		north.add(itembox);
		
		west.setLayout(new GridLayout(0,1));
		east.setLayout(new GridLayout(0,1));
		west.add(item);
		east.add(theItem);
		west.add(quantity);
		east.add(theQuantity);
		west.add(cost);
		east.add(theCost);
		west.add(date);
		east.add(theDate);
		
		south.add(add);
		south.add(change);
		south.add(delete);
		south.add(clear);
		south.add(exit);
		
		//Populate itembox with item Ids
		populate();
		
		itembox.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						int itemid = (int) itembox.getSelectedItem();
						Item requestedItem = itemQueries.getSelectedItem(itemid);
						String desc = requestedItem.getDescription();
						int quantity = requestedItem.getQuantity();
						String quantityAsString = String.valueOf(quantity);
						double cost = requestedItem.getCost();
						String costAsString = String.valueOf(cost);
						GregorianCalendar date = requestedItem.getDateAdded();
						SimpleDateFormat dateFM = new SimpleDateFormat("MM/dd/yyyy");
						dateFM.setCalendar(date);
						String dateFormatted = dateFM.format(date.getTime());
						
						theItem.setText(desc);
						theQuantity.setText(quantityAsString);
						theCost.setText(costAsString);
						theDate.setText(dateFormatted);
					}
				});
		
		add.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						JLabel id = new JLabel("Enter new ID");
						JTextField enterid = new JTextField();
						Object[] ob = {id, enterid};
						JOptionPane.showConfirmDialog(null, ob, "Item ID", JOptionPane.OK_OPTION);
						
						String txtID = enterid.getText();
						String txtQuantity = theQuantity.getText();
						String txtCost = theCost.getText();
						String txtDate = theDate.getText();
						
						int itemid = 0;
						int intQuantity = 0;
						double intCost = 0.0;
						java.util.Date parsed = null;
						GregorianCalendar cal = null;
						SimpleDateFormat format;
						boolean correctEntrys = false;
						String errorMsg = "Please enter a valid item Id.";
						try
						{
							itemid = Integer.parseInt(txtID);
							errorMsg = "Please enter a valid quantity.";
							intQuantity = Integer.parseInt(txtQuantity);
							if(intQuantity <= 0 )
							{
								errorMsg = "Quantity must be greater than 0";
								throw new IllegalArgumentException();
							}
							errorMsg = "Please enter a valid amount.";
							intCost = Double.parseDouble(txtCost);
							if(intCost < 0 )
							{
								errorMsg = "Amount must be greater than 0";
								throw new IllegalArgumentException();
							}
							format = new SimpleDateFormat("MM/dd/yyyy");
							try 
							{
								parsed = format.parse(txtDate);
								cal = new GregorianCalendar();
								cal.setTime(parsed);
								correctEntrys = true;
								
							} 
							catch (ParseException | NullPointerException e) 
							{
								errorMsg = "Please enter the date in dd/mm/yyyy format";
								JOptionPane.showMessageDialog(null, errorMsg, "Invalid Date", JOptionPane.ERROR_MESSAGE);
								correctEntrys = false;
							}
						}
						catch(IllegalArgumentException e)
						{
							JOptionPane.showMessageDialog(null, errorMsg, "Invalid Entry", JOptionPane.ERROR_MESSAGE);
							correctEntrys = false;
						}
				        results = itemQueries.getAllItems();
				        for(Item item : results)
				        {
				        	int itemids = item.getItemId();
				        	if(itemids == itemid)
				        	{
				        		errorMsg = "The Item Id is already used.";
				        		JOptionPane.showMessageDialog(null, errorMsg, "Invalid ID", JOptionPane.ERROR_MESSAGE);
				        		correctEntrys = false;
				        	}
				        	
				        }
						if(correctEntrys == true)
						{
							Item makeItem = new Item(itemid,intQuantity, intCost , theItem.getText(), cal);
							itemQueries.addItem(makeItem);
							itembox.addItem(itemid);
							JOptionPane.showMessageDialog(null, "Item Added!", "Success!", JOptionPane.INFORMATION_MESSAGE);
							itembox.setSelectedItem(itemid);
						}
					}
				});
		
		change.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						int itemid = (int) itembox.getSelectedItem();
						String txtDesc = theItem.getText();
						String txtQuantity = theQuantity.getText();
						String txtCost = theCost.getText();
						String txtDate = theDate.getText();
						
						int intQuantity = 0;
						double intCost = 0.0;
						java.util.Date parsed = null;
						GregorianCalendar cal = null;
						SimpleDateFormat format;
						boolean correctEntrys = false;
						String errorMsg = "Please enter a valid quantity.";
						try
						{
							intQuantity = Integer.parseInt(txtQuantity);
							if(intQuantity <= 0 )
							{
								errorMsg = "Quantity must be greater than 0";
								throw new IllegalArgumentException();
							}
							errorMsg = "Please enter a valid amount.";
							intCost = Double.parseDouble(txtCost);
							if(intCost < 0 )
							{
								errorMsg = "Amount must be greater than 0";
								throw new IllegalArgumentException();
							}
							format = new SimpleDateFormat("MM/dd/yyyy");
							try 
							{
								parsed = format.parse(txtDate);
								cal = new GregorianCalendar();
								cal.setTime(parsed);
								correctEntrys = true;
								
							} 
							catch (ParseException | NullPointerException e) 
							{
								errorMsg = "Please enter the date in dd/mm/yyyy format";
								JOptionPane.showMessageDialog(null, errorMsg, "Invalid Date", JOptionPane.ERROR_MESSAGE);
								correctEntrys = false;
							}
						}
						catch(IllegalArgumentException e)
						{
							JOptionPane.showMessageDialog(null, errorMsg, "Invalid Entry", JOptionPane.ERROR_MESSAGE);
							correctEntrys = false;
						}
						
						if(correctEntrys == true)
						{
							Item itemChange = new Item(itemid, intQuantity, intCost, txtDesc, cal);
							itemQueries.changeItem(itemChange);
							JOptionPane.showMessageDialog(null, "Item changed!", "Success!", JOptionPane.INFORMATION_MESSAGE);
							itembox.setSelectedItem(itemid);
						}
					}
				});
		
		delete.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						int itemToRemove = 0;
						//This try is for the case that the user tries to delete an item when the application starts and no item selected
						try
						{
							itemToRemove = (int) itembox.getSelectedItem();
						}
						catch(NullPointerException e)
						{
							JOptionPane.showMessageDialog(null, "Please select and item to delete.", "Invalid item", JOptionPane.OK_OPTION);
						}
						int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?", "Confirm delete", JOptionPane.OK_CANCEL_OPTION);
						if(confirm == JOptionPane.OK_OPTION)
						{
							itemQueries.removeItem(itemToRemove);
							itembox.removeItem(itemToRemove);
							JOptionPane.showConfirmDialog(null, "Item successfully deleted!" , "Delete Confirmation" , JOptionPane.OK_OPTION);
						}
					}
				});
		
		clear.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						theItem.setText("");
						theQuantity.setText("");
						theCost.setText("");
						theDate.setText("");
					}
				});
		
		exit.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						itemQueries.close();
						System.exit(-1);
					}
				});
}

	public void populate()
	{
		results = itemQueries.getAllItems();
		for(Item item : results)
		{
			int itemId = item.getItemId();
			itembox.addItem(itemId);
		}
	}
	
	public static void main(String[] args)
	{
		GUI App = new GUI();
		App.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		App.setVisible(true);
		App.pack();
		App.setResizable(false);
		App.setLocationRelativeTo(null);
	}
}
