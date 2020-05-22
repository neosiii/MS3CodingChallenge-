import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.sqlite.*;


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
	/*
	private static void addToCSV(String[] badParse, String fileName) {
		//determined this was unneeded.
	}*/
	
	private static void readDB(Connection link) {
		String query = "SELECT * FROM records";
		Statement printTable = null;
		int index = 1;
		try {
			printTable = link.createStatement();
			ResultSet records = printTable.executeQuery(query);
			
			while(records.next()) {
				String toPrint = records.getString("A");
				toPrint += ", " + records.getString("B");
				toPrint += ", " + records.getString("C");
				toPrint += ", " + records.getString("D");
				toPrint += ", " + records.getString("E");
				toPrint += ", " + records.getString("F");
				toPrint += ", " + records.getString("G");
				toPrint += ", " + records.getString("H");
				toPrint += ", " + records.getString("I");
				toPrint += ", " + records.getString("J");
				System.out.println(index + " : " + toPrint);
				index++;
			}
			printTable.close();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static String[] fixParse(String[] input) {
		String toResplit = "";
		int index = 0;
		//System.out.println(input[4]);
		//System.out.println(input[5]);
		while(index < input.length) {
			if(index != 4) {
				toResplit += input[index] + "`";
				index++;
			}else {
				//this is to fix column e where spliting by command messes the program up.
				toResplit += input[index]+","+input[index+1]+"`";
				index+= 2; // this is to jump ahead after recombining the two columns.
			}
		}
		//System.out.println(toResplit);
		String[] returnArray = toResplit.split("`");
		//System.out.println("test");
		//System.out.println(returnArray[2]);
		//System.out.println("test");
		//System.out.println(returnArray[4]);
		//System.out.println("test");
		return returnArray;
	}

	private static void addToDB(String[] goodParse, Connection link) {
		String insertInto = "INSERT INTO records VALUES(?,?,?,?,?,?,?,?,?,?)";
		//System.out.println(insertInto);
		/*
		for(String item : goodParse) {
			insertInto += item +",";
		}*/
		// above was a bad idea.
		
		try {
			PreparedStatement insert = link.prepareStatement(insertInto);
			insert.setString(1, goodParse[0]);
			insert.setString(2, goodParse[1]);
			insert.setString(3, goodParse[2]);
			insert.setString(4, goodParse[3]);
			insert.setString(5, goodParse[4]);
			insert.setString(6, goodParse[5]);
			insert.setString(7, goodParse[6]);
			insert.setString(8, goodParse[7]);
			insert.setString(9, goodParse[8]);
			insert.setString(10, goodParse[9]);
			insert.executeUpdate();
			insert.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	private static void readingBadparse(String fileName) {
		try {
			BufferedReader badparser = new BufferedReader(new FileReader("Entry Level Coding Challenge Page 2-bad.csv"));
			String line = badparser.readLine();
			while(line != null) {
				//System.out.println(line);
				line = badparser.readLine();
			}
			badparser.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
			System.out.println("File not found");
		}catch(IOException e) {
			e.printStackTrace();
			System.out.println("line not read");
		}
	}
	
	private static SQLiteDataSource createDB(String fileName){
		fileName = fileName.replace(".csv",".db");
		//System.out.println(fileName);
		//This print out is just to ensure that file name is correctly switched.
		SQLiteDataSource database = new SQLiteDataSource();
		//System.out.println(database.getUrl());
		database.setUrl("jdbc:sqlite::memory:");
		// only way I can get it to store in memory is without an actual file name.

		//System.out.println(database.getUrl());
		Connection link = null;
		try {
			link = database.getConnection();
			//System.out.println(link);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		//System.out.println(link);
		
		
		return database;
	}
	private static void createTable( Connection link) {
		
		//String tableCreateStat = "CREATE TABLE records";
		//the table inside the database will be named records.
		//Connection link = database.getConnection()
		/*
		for(String item : inputArray) {
			if(item == "E") {
				tableCreateStat += " " + item + " MEDIUMTEXT NOT NULL,";
				// this would be that giant link column.
			}else if(item == "C") {
				tableCreateStat += " " + item + "VARCHAR(255)";
			}else {
				tableCreateStat += " " + item + " TINYTEXT NOT NULL,";
				// In theory everything else should fit into the TINYTEXT size limit?
				// Trying to not take up more space than need be, but don't know what the true size limits are.
			}
		}*/
		
		String tableCreateStat = "CREATE TABLE records (A TEXT, B TEXT, C TEXT, D TEXT, E TEXT, F TEXT, G TEXT, H TEXT, I TEXT, J TEXT)";
		//tableCreateStat = tableCreateStat.substring(0, tableCreateStat.length()-1) +")";
		Statement createTable = null;
		try {
			createTable = link.createStatement();
			createTable.executeUpdate(tableCreateStat);
			createTable.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (createTable != null) {
				try{
					createTable.close();
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println(tableCreateStat);
	}
	public static ArrayList<String[]> parser(String fileName){
		ArrayList<String[]> returnList = new ArrayList<String[]>();
		
		SQLiteDataSource database = createDB(fileName);
		

		//System.out.println(database.getUrl());
		
		
		File badParses = new File(fileName.replace(".csv", "-bad.csv"));
		try {
			badParses.createNewFile();
		}catch(IOException e) {
			e.printStackTrace();
		}
		// Probably could do this with out File badParses, test for later. Currently playing safe.
		
		
		
		int TotalRecsSuccess = 0;
		int TotalRecsfailed = 0;
		
		try {
			FileWriter badParseWrite = new FileWriter(fileName.replace(".csv", "-bad.csv"));
			//System.out.println(badParses.getName());
			BufferedReader parser = new BufferedReader(new FileReader(fileName));
			//System.out.println(parser.)
			String line = parser.readLine();
			//System.out.println(line);
			
			String[] Splits = line.split(",");
			Connection link = database.getConnection();
			//System.out.println("Test");
			//System.out.println(link);
			createTable(link);
			
			line = parser.readLine(); // this skips the a,b,c,d titles. Probably a better way of doing
			//that. Look back on it later.
			
			
			while(line != null) {
				
				Splits = line.split(",");
				//for(String split : Splits) System.out.println(split);
				
				if(lengthTest(Splits) == true) { //This test will determine what is going to the database, and what is going to the bad.CSV file.
					//System.out.println(line);
					Splits = fixParse(Splits);
					returnList.add(Splits);
					TotalRecsSuccess++;
					addToDB(Splits, link);
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
			readDB(link);
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}catch(SQLException e) {
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
			if(element.length() < 2) {
				return false;
			}
		}
		return true;
		// should only return if every element in input is of a length greater than 0.
		// This will find all lines that don't have the proper number of elements.
	}
}

