import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class ArithmeticBinaryTree {

	// ENUM DEFINES PRECEDENCE OF OPERATIONS
	public enum OpCategory {
		eol, bracket, plusMinus, multiDiv, maxPrecedence
	}

	class Node {
		Node leftChild;
		Node rightChild;
		Node parent;
		Tokenizer.Token token;
		OpCategory precedence;
		
		Node () {
			
		}
		
		Node(Tokenizer.Token inToken){
			token = inToken;
		}
	}

	Tokenizer tokenParser;
	Hashtable<String, OpCategory> opPrecedenceMap;

	// Hash table mapping operators to their respective precedence defined in the
	// enum above
	void initializePrecMap() {
		opPrecedenceMap = new Hashtable<String, OpCategory>();

		opPrecedenceMap.put("*", OpCategory.multiDiv);
		opPrecedenceMap.put("/", OpCategory.multiDiv);
		opPrecedenceMap.put("+", OpCategory.plusMinus);
		opPrecedenceMap.put("-", OpCategory.plusMinus);
		opPrecedenceMap.put(")", OpCategory.bracket);
		opPrecedenceMap.put("(", OpCategory.bracket);
		opPrecedenceMap.put("$", OpCategory.eol);
	}

	ArithmeticBinaryTree(String inExp) {
		tokenParser = new Tokenizer(inExp);
		initializePrecMap();

	}

	/*
	 * Tree creation complexity
	 * Time complexity: O(n) - algorithm iterates through each token in the expression string
	 * Space complexity: O(n)
	 * 	because in the worst case each operand and each operation is wrapped in brackets
	 * causing a recursive call to evaluate the "subexpressions" in the brackets
	 * each recursive call requires a single unit on the stack
	 */
	// createTree returns the root node (of either full tree or of a sub tree when
	// called recursively)
	Node createTree() {
		Node root = null;
		// first operand should always be a number
		Tokenizer.Token currentToken;
		do {
			currentToken = tokenParser.getNext();
			// treat sub expressions in brackets as sub trees
			if (currentToken.element.equals("(")) {
				root = insertSubTree(root);
			} else if (currentToken.type == Tokenizer.Type.operator) {
				root = insertOp(currentToken, root);
			}

			else if (currentToken.type == Tokenizer.Type.number || currentToken.type == Tokenizer.Type.variable) {
				root = insertNum(currentToken, root);
			}
			
		} while ((!currentToken.element.equals("$")) && (!currentToken.element.equals(")")));
		
		// set close bracket precedence to max precedence in order to ensure that the subtree is an operand of the previous or next operator
		if(currentToken.element.equals(")")){
			root.precedence = OpCategory.maxPrecedence;
		}

		return root;
	}

	/*
	 * left to right expression evaluation ensures that the parenthetical subexpressions are right-hand operands
	 * method only traverses the right most side of the tree to look for the appropriate node to attach the parenthetical subtree
	 */
	Node insertSubTree(Node root) {
		//if the parenthetical subexpression is at the start of the expression then it becomes the initial root node
		if (root == null) {
			return createTree();
		}
		Node temp = root;
		while (temp.rightChild != null) {
			temp = temp.rightChild;
		}
		temp.rightChild = createTree();
		return temp;
	}

	Node insertOp(Tokenizer.Token currentToken, Node root) {
		
		// correct arithmetic expressions will always start with an operand which will be the first root of the tree
		if (root == null) {
			System.out.println("Unexpected null root in insertOp");
			return null;
		}
		
		// the root’s right child becomes the current operator’s left operand and the current operator becomes the right child of the root
		if (root.token.type == Tokenizer.Type.operator
				&& precedence(currentToken.element).ordinal() > root.precedence.ordinal()) {
			Node inOpNode = new Node(currentToken);
			inOpNode.precedence = precedence(currentToken.element);
			inOpNode.leftChild = root.rightChild;
			root.rightChild = inOpNode;
		} 
		// the current tree becomes the left subtree and the currentToken becomes the new root
		else {
			Node temp = root;
			root = new Node(currentToken);
			root.precedence = precedence(currentToken.element);
			root.leftChild = temp;
		}

		return root;
	}

	Node insertNum(Tokenizer.Token currentToken, Node root) {
		// first number of every expression briefly becomes the root of the tree
		if(root == null) {
			return new Node(currentToken);
		}
		
		/*
		 * with exception of the first operand, all following operands are the right-hand argument 
		 * to an operator thus insert the currentToken as the right most child of the tree
		 */
		Node temp = root;
		while (temp.rightChild != null) {
			temp = temp.rightChild;
		}
		temp.rightChild = new Node(currentToken);
		return root;
	}

	OpCategory precedence(String inOp) {
		return opPrecedenceMap.get(inOp);
	}
	
	/*
	 * Time complexity: O(n) where n is the number of "tokens" or nodes in the tree
	 * Space complexity: O(n) because each recursive call for each node inserts a unit on the call stack
	 */
	double evalTree(Node root) {
		if (root.token.type == Tokenizer.Type.number) {
			return Double.parseDouble(root.token.element);
		}
		else if (root.token.type == Tokenizer.Type.variable) {
			Scanner keyboard = new Scanner(System.in);
			System.out.print("Variable encountered during evaluation. Please provide a value for the variable.\n"+root.token.element+" = ");
			double input = keyboard.nextDouble();
			return input;
		}
		
		switch(root.token.element) {
		case "+":
			return evalTree(root.leftChild) + evalTree(root.rightChild);
		case "-":
			return evalTree(root.leftChild) - evalTree(root.rightChild);
		case "*":
			return evalTree(root.leftChild) * evalTree(root.rightChild);
		case "/":
			 double rightChild = evalTree(root.rightChild);
			 if(rightChild == 0) {
				 System.out.println("undefined");
				 System.exit(0);
			 }
			 else {
				 return evalTree(root.leftChild)/rightChild;
			 }
		default:
			System.out.println("error in switch statement of evalTree");
		}
		
		return Double.NaN; 
	}
	
	// method prints each level of the tree line by line
		void printTree(Node root) {

			// arraylist of the current level
			ArrayList<Node> current = new ArrayList<Node>();
			current.add(root);

			while (current.size() > 0) {

				// array list of the next level
				ArrayList<Node> next = new ArrayList<Node>();

				/*
				 * The inner loop iterates through the nodes of the arraylist of the level the
				 * method is currently on. For each node of the current arraylist, the inner
				 * loop prints the node's element to console. The if-statements put the children
				 * of the current node of the loop into an arraylist that represents the next
				 * level
				 * 
				 */
				for (Node node : current) {
					System.out.print(node.token.element + " ");

					if (node.leftChild != null) {
						next.add(node.leftChild);
					}

					if (node.rightChild != null) {
						next.add(node.rightChild);
					}
				}
				// move to the next arraylist which represents the next level in the arithmetic
				// binary tree
				System.out.println();
				current = next;
			}
			
		}
		
}
