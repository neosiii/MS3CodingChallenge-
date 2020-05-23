import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.sqlite.*;


public class csvparser {


	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter in the file name for the csv file.\nIncluding the .csv");
		String fileName = input.nextLine();
		//gets the file name that the program will utilize for the rest of its execution.
		System.out.println(fileName);
		Connection dataLink = parser(fileName);
	
		int loopIndex = 1;
		
		while(loopIndex == 1) {
			System.out.println("Would you like to print:\n1: Database\n2: bad.csv\n3: Exit");
			int option = input.nextInt();
			switch(option) {
			case 1:
				readDB(dataLink); //reads database created on run.
				break;
			case 2:
				readingBadparse("Entry Level Coding Challenge Page 2-bad.csv");
				break;
			case 3:
				loopIndex++; //breaks you out of loop.
				break;
			default:
				System.out.println("Invalid input please try again.");
			}
		}

		System.out.println("Simulation completed! Have a nice day!");
		
		
	}
	
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
		String[] returnArray = toResplit.split("`");
		return returnArray;
	}

	private static void addToDB(String[] goodParse, Connection link) {
		String insertInto = "INSERT INTO records VALUES(?,?,?,?,?,?,?,?,?,?)";
		
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
			BufferedReader badparser = new BufferedReader(new FileReader(fileName));
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
		SQLiteDataSource database = new SQLiteDataSource();
		database.setUrl("jdbc:sqlite::memory:");
		// only way I can get it to store in memory is without an actual file name.
		//database.setUrl("jdbc:sqlite:"+fileName);
		//comment above should work if you wish to have the file name.
		//But that will make it a file within the program.
		//Which after communicating with recruiter, wasn't the intended purpose.

		Connection link = null;
		try {
			link = database.getConnection();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		
		return database;
	}
	private static void createTable( Connection link) {
		
		
		String tableCreateStat = "CREATE TABLE records (A TEXT, B TEXT, C TEXT, D TEXT, E TEXT, F TEXT, G TEXT, H TEXT, I TEXT, J TEXT)";
		
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
	}
	public static Connection parser(String fileName){
		
		SQLiteDataSource database = createDB(fileName);
		
		
		File badParses = new File(fileName.replace(".csv", "-bad.csv"));
		try {
			badParses.createNewFile();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
		
		int TotalRecsSuccess = 0;
		int TotalRecsfailed = 0;
		Connection link = null;
		try {
			FileWriter badParseWrite = new FileWriter(fileName.replace(".csv", "-bad.csv"));

			BufferedReader parser = new BufferedReader(new FileReader(fileName));

			String line = parser.readLine();

			
			String[] Splits = line.split(",");
			link = database.getConnection();

			createTable(link);
			
			line = parser.readLine(); // this skips the a,b,c,d titles. In Theory.
			
			
			while(line != null) {
				
				Splits = line.split(",");

				
				if(lengthTest(Splits) == true) { //This test will determine what is going to the database, and what is going to the bad.CSV file.

					Splits = fixParse(Splits);

					TotalRecsSuccess++;
					addToDB(Splits, link);

				}else {
					TotalRecsfailed++;
					badParseWrite.write(line+"\n"); //So just taking one CSV line and throwing it back into a new CSV. At least that's the idea.

				}
				line = parser.readLine();

			}
			parser.close();
			badParseWrite.close();

		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		logRecords(TotalRecsSuccess, TotalRecsfailed++);
		//Success go first, failures go second.

		

		String[] _totals = {String.valueOf(TotalRecsSuccess), String.valueOf(TotalRecsfailed)};

		
		

		return link;
		
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
	private static void logRecords(int goodRecords, int badRecords) {
		FileHandler logFile;
		try {
			logFile = new FileHandler("Records.log", true);
			Logger librarian = Logger.getLogger("RecordsLog");
			librarian.addHandler(logFile);
			
			librarian.info("Good Records Parsed: " + goodRecords);
			librarian.info("Bad Records Parsed: "+ badRecords);
			int TotalRecords = goodRecords + badRecords;
			librarian.info("Total Records Parsed: " + TotalRecords);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

