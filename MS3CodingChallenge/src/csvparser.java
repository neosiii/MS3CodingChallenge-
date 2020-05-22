import java.io.*;

public class csvparser {

	//test
	public static void main(String[] args) {
		try {
		BufferedReader parser = new BufferedReader(new FileReader("Entry Level Coding Challenge Page 2.csv"));
		String line = parser.readLine();
		while(line != null) {
			String[] Splits = line.split(",");
			//for(String split : Splits) System.out.println(split);
			line = parser.readLine();
			//test
		}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
