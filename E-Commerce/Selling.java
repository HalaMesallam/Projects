import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Tenzin Dorjee
 *
 */
public class Selling {
	private String name;
	private String price;
	private String category;
	private String size;
	private String color;
	private String datePosted;

	private static ArrayList<String> itemSellArr;

	public Selling(String name, String sellItemPrice, String category, String size, String color, String datePosted) {
		this.name = name;
		this.price = sellItemPrice;
		this.category = category;
		this.size = size;
		this.color = color;
		this.datePosted = datePosted;
		itemSellArr = new ArrayList<String>();
		itemSellArr.add("Name: " + name);
		itemSellArr.add("Price: " + price);
		itemSellArr.add("Category: " + category);
		itemSellArr.add("Size: " + size);
		itemSellArr.add("Color: " + color);
		itemSellArr.add("Date Posted: " + datePosted);

		for (int i = 0; i <itemSellArr.size(); i++) {
			System.out.println(itemSellArr.get(i));
		}
	}
	public static ArrayList<String> getList(){
		if (itemSellArr.size() == 0) {
			return null;
		}
		else {
		return itemSellArr;
		}
	}

	public String getName(){
	
		return name;
	}

	public void setName(String name) {
		
		this.name = name;
	}
	
	public String getPrice(){
		return price;
	}

	public void setPrice(String price) {
		
		itemSellArr.add(price);
		this.price = price;
	}

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		
		this.category = category;
		itemSellArr.add(category);
	}

	public String getSize() {
		
		return size;
	}
	public void setSize(String size) {

		this.size = size;
		itemSellArr.add(size);
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		
		this.color = color;
		itemSellArr.add(color);
	}

	public String getDatePosted() {
		
		return datePosted;
	}

	public void setDatePosted() {
		
		this.datePosted = datePosted;
		itemSellArr.add(datePosted);
	}
}