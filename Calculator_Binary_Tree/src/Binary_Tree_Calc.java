import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class main_BinaryTreeCalc {

	public static void main(String[] args) {
		int input = 0;
		BufferedReader reader;
		Scanner keyboard = new Scanner(System.in);
		while(input != 5) {
		System.out.println("Would you like to (1) enter an arithmetic expression (2) load a set or (3) quit?");
		input = keyboard.nextInt();
		String flush = keyboard.nextLine();
		
		String expression;
		
		switch (input) {
		case 1:
			System.out.println("What expression would you like to evaluate?");
			expression = keyboard.nextLine();
			printTreeAndEvaluate(expression);
			System.out.println();
			break;
		case 2:
			try {
				reader = new BufferedReader(new FileReader("C:\\Users\\Damian\\Documents\\Repos\\Learning_Java\\Calculator_Binary_Tree\\testExpressions.txt"));

				String line = reader.readLine();
				System.out.println("Loading test set: ");
				while (line != null) {
					printTreeAndEvaluate(line);
					System.out.println();
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println();
			break;
		default:
			System.out.println("Fin");
			System.exit(0);
		}
		}
	}

	public static void printTreeAndEvaluate(String expression) {
		ArithmeticBinaryTree testTree = new ArithmeticBinaryTree(expression);
		ArithmeticBinaryTree.Node root = testTree.createTree();
		testTree.printTree(root);
		System.out.println();
		System.out.println(expression+" = "+testTree.evalTree(root));
	}
}
