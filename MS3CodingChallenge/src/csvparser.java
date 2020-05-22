import java.io.*;
import java.util.*;

public class csvparser {


	public static void main(String[] args) {
		
		ArrayList<String[]> parsed = parser("Entry Level Coding Challenge Page 2.csv");
		/*
		for(String[] parse : parsed) {
			for(String element : parse) System.out.println(element);
		}
		*/
		/*
		int index = 0;
		while(index < 5) {
			if(lengthTest(parsed.get(index))) {
				System.out.println(parsed.get(index).length);
				System.out.println("True");
			}
			index++;
		}
		*/
		int totalRecs = 0;
		for(String total : parsed.get(parsed.size()-1)) {
			System.out.println(total);
			totalRecs += Integer.parseInt(total);
		}
		System.out.println("Total recs: " + totalRecs);
		//System.out.println(parsed.get(parsed.size()-1));
		
		//readingBadparse("Entry Level Coding Challenge Page 2-bad.csv");
		// above function works.
		System.out.println("Simulation completed! Have a nice day!");
		
	}
	
	private static void addToCSV(String[] badParse, String fileName) {
		
	}
	private static void addToDB(String[] goodParse, String dbName) {
		
	}
	private static void readingBadparse(String fileName) {
		try {
			BufferedReader badparser = new BufferedReader(new FileReader("Entry Level Coding Challenge Page 2-bad.csv"));
			String line = badparser.readLine();
			while(line != null) {
				System.out.println(line);
				line = badparser.readLine();
			}
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
			System.out.println("File not found");
		}catch(IOException e) {
			e.printStackTrace();
			System.out.println("line not read");
		}
	}
	public static ArrayList<String[]> parser(String fileName){
		ArrayList<String[]> returnList = new ArrayList<String[]>();
		
		
		File badParses = new File(fileName.replace(".csv", "")+"-bad.csv");
		try {
			badParses.createNewFile();
		}catch(IOException e) {
			e.printStackTrace();
		}
		// Probably could do this with out File badParses, test for later. Currently playing safe.
		
		
		
		int TotalRecsSuccess = 0;
		int TotalRecsfailed = 0;
		
		try {
			FileWriter badParseWrite = new FileWriter(fileName.replace(".csv", "")+"-bad.csv");
			//System.out.println(badParses.getName());
			BufferedReader parser = new BufferedReader(new FileReader(fileName));
			String line = parser.readLine();
			line = parser.readLine(); // this skips the a,b,c,d titles. Probably a better way of doing
			//that. Look back on it later.
			
			
			while(line != null) {
				String[] Splits = line.split(",");
				//for(String split : Splits) System.out.println(split);
				
				if(lengthTest(Splits)) { //This test will determine what is going to the database, and what is going to the bad.CSV file.
					returnList.add(Splits);
					TotalRecsSuccess++;
					//System.out.println("True");
				}else {
					TotalRecsfailed++;
					badParseWrite.write(line+"\n"); //So just taking one CSV line and throwing it back into a new CSV. At least that's the idea.
					//System.out.println("False");
				}
				line = parser.readLine();
				//test
			}
			parser.close();
			badParseWrite.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
		//returnList.add({String.valueOf(TotalRecsSuccess), String.valueOf(TotalRecsfailed)});
		/* Apparently That doesn't work atm. Look into.
		If it stays in here, it's to show original thought process. Would prefer to
		not have to declare another variable.*/
		

		String[] _totals = {String.valueOf(TotalRecsSuccess), String.valueOf(TotalRecsfailed)};
		returnList.add(_totals);
		
		return returnList;
		
	}
	//Using the below function for sorting what goes to array and what goes to the bad.csv.
	public static boolean lengthTest(String[] input) {
		for(String element : input) {
			if(element.length() < 1) {
				return false;
			}
		}
		return true;
		// should only return if every element in input is of a length greater than 0.
		// This will find all lines that don't have the proper number of elements.
	}
}

