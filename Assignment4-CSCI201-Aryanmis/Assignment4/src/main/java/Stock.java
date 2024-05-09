
public class Stock {
	public String ticker;
	public String name;
	public String exchange;
	public double price;
	public double change;
	public double change_per;
	public boolean open;
	public double lastTime;
	public double highPrice;
	public double lowPrice;
	public double openPrice;
	public double closePrice;
	public String ipoDate;
	public double marketCap;
	public double outstanding;
	public String website;
	public String phone;
	public int quant;
	public double total;
	
	public Stock(Profile p, Quote Q) {
		this.ticker = p.ticker;
		this.name = p.name;
		this.exchange = p.exchange;
		this.price = Q.c;
		this.change = Q.d;
		this.change_per = Q.dp;
		this.open = true;
		this.lastTime = Q.t;
		this.highPrice = Q.h;
		this.lowPrice = Q.l;
		this.openPrice = Q.o;
		this.closePrice = Q.pc;
		this.ipoDate = p.ipo;
		this.marketCap = p.marketCapitalization;
		this.outstanding = p.shareOutstanding;
		this.website = p.weburl;
		this.phone = p.phone;
		this.quant = 0;
		this.total = 0;
		
	}

}




