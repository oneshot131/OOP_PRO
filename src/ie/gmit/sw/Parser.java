
package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

public class Parser implements Runnable{
	
	private Database db = null;
	private String file;
	private int k;
	
	public Parser(String file, int k)
	{
		this.file = file;
		this.k = k;
	}
	
	public void setDb(Database db)
	{
		this.db = db;
	}
	
	@Override
	public void run()
	{
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("wiki.txt")));
			String line = null;
			
			while((line = br.readLine()) != null)
			{
				String [] record = line.toUpperCase().trim().split("@");
				if(record.length != 2)continue;
				parse(record[0], record[1]);
			}
			
			br.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parse(String text, String lang, int...ks )
	{
		Language l = Language.valueOf(lang);
		
		for(int i=0; i<text.length() - k; i++)
		{
			//get 3-mer
			CharSequence kmer = text.substring(i,i+k);
			db.add(kmer, l);
		}
	}
	
	public static void main(String[] args) throws Throwable
	{
		System.out.println("in main");
		Parser p = new Parser("wili-2018-Small-11750-Edited.txt", 1);
		
		Database db = new Database();
		p.setDb(db);
		System.out.println(db);
		Thread t = new Thread(p);
		t.start();
		t.join();
		
		db.resize(300);
	}

}