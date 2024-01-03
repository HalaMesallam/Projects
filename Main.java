public class Main extends HomePage{
	public static void main(String args[]) {
		System.out.println("Welcome to our Ecommerce Application!");
		HomePage UserTestIn = new HomePage();
		UserTestIn.HomeOptions();

		while (true) {
			UserTestIn.HomeOptions();
		}
	}
}


