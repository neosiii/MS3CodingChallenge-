import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
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
		String query = "SELECT A FROM records";
		Statement printTable = null;
		try {
			printTable = link.createStatement();
			ResultSet records = printTable.executeQuery(query);
			
			while(records.next()) {
				//String toPrint = records.getString("A");
				System.out.println(records);
			}
			printTable.close();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static String[] fixParse(String[] input) {
		String toResplit = "";
		int index = 0;
		while(index < input.length) {
			if(index != 4) {
				toResplit += input[index] + " ";
			}else {
				//this is to fix column e where spliting by command messes the program up.
				toResplit += input[index]+","+input[index+1]+" ";
				index+= 2; // this is to jump ahead after recombining the two columns.
			}
		}
		
		String[] returnArray = toResplit.split(" ");
		return returnArray;
	}

	private static void addToDB(String[] goodParse, Connection link) {
		String insertInto = "INSERT INTO records VALUES(";
		for(String item : goodParse) {
			insertInto += item +",";
		}
		insertInto = insertInto.substring(0, insertInto.length()-1) + ")";
		Statement insert = null;
		try {
			insert = link.createStatement();
			insert.executeUpdate(insertInto);
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
				System.out.println(line);
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
		System.out.println(fileName);
		//This print out is just to ensure that file name is correctly switched.
		SQLiteDataSource database = new SQLiteDataSource();
		System.out.println(database.getUrl());
		database.setUrl("jdbc:sqlite::memory:");
		// only way I can get it to store in memory is without an actual file name.

		System.out.println(database.getUrl());
		Connection link = null;
		try {
			link = database.getConnection();
			System.out.println(link);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		//System.out.println(link);
		
		
		return database;
	}
	private static void createTable(String[] inputArray, String dbName, Connection link) {
		String tableCreateStat = "CREATE TABLE records (";
		//String tableCreateStat = "CREATE TABLE records";
		//the table inside the database will be named records.
		//Connection link = database.getConnection()
		for(String item : inputArray) {
			if(item == "E") {
				tableCreateStat += " " + item + " MEDIUMTEXT NOT NULL,";
				// this would be that giant link column.
			}else {
				tableCreateStat += " " + item + " TINYTEXT NOT NULL,";
				// In theory everything else should fit into the TINYTEXT size limit?
				// Trying to not take up more space than need be, but don't know what the true size limits are.
			}
		}
		tableCreateStat = tableCreateStat.substring(0, tableCreateStat.length()-1) +")";
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
		System.out.println(tableCreateStat);
	}
	public static ArrayList<String[]> parser(String fileName){
		ArrayList<String[]> returnList = new ArrayList<String[]>();
		
		SQLiteDataSource database = createDB(fileName);
		

		System.out.println(database.getUrl());
		
		
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
			String line = parser.readLine();
			String[] Splits = line.split(",");
			Connection link = database.getConnection();
			System.out.println("Test");
			System.out.println(link);
			createTable(Splits, fileName.replace(".csv",".db"), link);
			
			line = parser.readLine(); // this skips the a,b,c,d titles. Probably a better way of doing
			//that. Look back on it later.
			
			
			while(line != null) {
				Splits = line.split(",");
				//for(String split : Splits) System.out.println(split);
				
				if(lengthTest(Splits)) { //This test will determine what is going to the database, and what is going to the bad.CSV file.
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
			//readDB(link);
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
			if(element.length() < 1) {
				return false;
			}
		}
		return true;
		// should only return if every element in input is of a length greater than 0.
		// This will find all lines that don't have the proper number of elements.
	}
}

