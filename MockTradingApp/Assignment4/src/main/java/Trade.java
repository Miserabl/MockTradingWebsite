
public class Trade {
	public String ticker;
	public int num;
	public double price;
	public double total_cost;
	
	public Trade(String Ticker, int num, double price) {
		this.ticker = Ticker;
		this.num = num;
		this.price = price;
		this.total_cost = price * num;
	}
	
	

}
