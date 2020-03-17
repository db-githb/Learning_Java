import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;


public class TestDriver {

	public static void main(String[] args) {
		
		
		BufferedReader reader;
		PrintWriter writer;
		
		int i=1;
		try {
			reader = new BufferedReader(new FileReader("C:\\Users\\Damian\\Documents\\Repos\\Learning_Java\\Calculator_Stack_Recursion\\test.txt"));
			writer = new PrintWriter("C:\\Users\\Damian\\Documents\\Repos\\Learning_Java\\Calculator_Stack_Recursion\\output.txt", "UTF-8");
			String line = reader.readLine();
			while (line != null) {
				String outStr = i+") "+line+": "+"\n   stack calc:     " +testStackCalc(line)+"\n   recursive calc: "+testRecCalc(line);
				System.out.println(outStr);
				writer.println(outStr);
				System.out.println();
				writer.println();
				i++;
				// read next line
				line = reader.readLine();
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static String testStackCalc(String inExp) {
		StackCalc testStackCalc = new StackCalc();

		return testStackCalc.EvalExp(inExp);
	}

	static String testRecCalc(String inExp
			) {
		RecursiveCalc testRecCalc = new RecursiveCalc();

		return (testRecCalc.evalExp(inExp));
	}

	static void testTokenizer() {
		String arithmeticExp = "4 > 5 == 10 < 3";

		Tokenizer token = new Tokenizer(arithmeticExp);
		String currentToken;
		do {
			currentToken = token.getNext();
			System.out.println(currentToken + " " + token.getType());
		} while (token.getType() != Tokenizer.Type.none && !(currentToken.equals("$")));
	}
}
