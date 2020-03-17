import java.util.*;

public class StackCalc {
	// ENUM DEFINES PRECEDENCE OF OPERATIONS
	public enum OpCategory {
		eol, bracket, eqNotEq, relational, plusMinus, multiDiv, exponent, minusUnary, factorial
	}

	Stack<Double> numbers;
	Stack<String> operators;
	Hashtable<String, OpCategory> opPrecedenceMap;
	boolean relationalFlag;

	StackCalc() {
		numbers = new Stack<Double>();
		operators = new Stack<String>();
		relationalFlag = false;

		initializePrecMap();
	}

	String stackCalc(String input) {
		return input;

	}

	// Hash table mapping operators to their respective precedence defined in the enum above
	void initializePrecMap() {
		opPrecedenceMap = new Hashtable<String, OpCategory>();
		
		opPrecedenceMap.put("!", OpCategory.factorial);
		opPrecedenceMap.put("~", OpCategory.minusUnary);
		opPrecedenceMap.put("^", OpCategory.exponent);
		opPrecedenceMap.put("*", OpCategory.multiDiv);
		opPrecedenceMap.put("/", OpCategory.multiDiv);
		opPrecedenceMap.put("+", OpCategory.plusMinus);
		opPrecedenceMap.put("-", OpCategory.plusMinus);
		opPrecedenceMap.put(">", OpCategory.relational);
		opPrecedenceMap.put(">=", OpCategory.relational);
		opPrecedenceMap.put("<", OpCategory.relational);
		opPrecedenceMap.put("<=", OpCategory.relational);
		opPrecedenceMap.put("==", OpCategory.eqNotEq);
		opPrecedenceMap.put("!=", OpCategory.eqNotEq);
		opPrecedenceMap.put(")", OpCategory.bracket);
		opPrecedenceMap.put("(", OpCategory.bracket);
		opPrecedenceMap.put("$", OpCategory.eol);
	}

	String EvalExp(String inExp) {
		
		Tokenizer tokenParser = new Tokenizer(inExp);
		String currentToken;
		
		// Loop breaks up arithmetic expression string into sub-strings of operators and numbers
		do {
			currentToken = tokenParser.getNext();

			if (tokenParser.getType() == Tokenizer.Type.number) {
				numbers.push(Double.parseDouble(currentToken));
			} 
			else if (currentToken.equals("(")) {
				operators.push("(");
			}
			else if (currentToken.equals("~")&& (operators.getCount()==0 || operators.peek().equals("~"))) {
				operators.push("~");
			}
			else {
				repeatOp(currentToken);
				if (!currentToken.equals(")")) {
					operators.push(currentToken);
				}
			}
		} while (tokenParser.getType() != Tokenizer.Type.none && !(currentToken.equals("$")));

		// repeatOp(currentToken);
		
		// CHECK IF THERE IS MORE THAN ONE CHAR
		if(relationalFlag) {
			if (numbers.peek() == 0) {
				return "FALSE";
			}
			else {
				return "TRUE";
			}
		}
		return Double.toString(numbers.peek());
	}

	void repeatOp(String inOp) {
		while ((numbers.getCount() > 0 && operators.getCount() > 0
				&& precedence(inOp).ordinal() <= precedence(operators.peek()).ordinal())) {

			if (inOp.equals(")") && operators.peek().equals("(")) {
				operators.pop();
				break;
			}
			doOp();
		}
	}

	OpCategory precedence(String inOp1) {
		return opPrecedenceMap.get(inOp1);
	}

	void doOp() {

		String operator = operators.pop();

		double right = numbers.pop();
		
		/*
		 * Unary Operators
		 * only need the top of the stack (right most number)
		 */
		if (operator.equals("!")) {
			double factorial = 1;
			for (int j = 1; j <= right; j++) {
				factorial *= j;
			}
			numbers.push(factorial);
			return;
		}

		if (operator.equals("~")) {
			double positiveNum = right;
			numbers.push(-positiveNum);
			return;
		}

		double left = numbers.pop();
		
		/*
		 * Binary Operators
		 * need two numeric values therefore requires a second pop for the left value in binary expression
		 */
		switch (operator) {
		case "^":
			boolean negative = false;
			if (right < 0) {
				right = -right;
				negative = true;
			}
			double temp = 1;
			for (int i = 0; i < right; i++) {
				temp = temp * left;
			}
			numbers.push(negative ? 1.0 / temp : temp);
			break;
		case "*":
			numbers.push(right * left);
			break;
		case "/":
			numbers.push(left / right);
			break;
		case "+":
			numbers.push(right + left);
			break;
		case "-":
			numbers.push(left - right);
			break;
		/*
		 * Relational Operators
		 * return boolean so set flag
		 */
		case "<":
			relationalFlag = true;
			numbers.push((left < right) ? 1.0 : 0.0);
			break;
		case ">":
			relationalFlag = true;
			numbers.push((left > right) ? 1.0 : 0.0);
			break;
		case ">=":
			relationalFlag = true;
			numbers.push((left >= right) ? 1.0 : 0.0);
			break;
		case "<=":
			relationalFlag = true;
			numbers.push((left <= right) ? 1.0 : 0.0);
			break;
		case "!=":
			relationalFlag = true;
			numbers.push((left != right) ? 1.0 : 0.0);
			break;
		case "==":
			relationalFlag = true;
			numbers.push((left == right) ? 1.0 : 0.0);
			break;
		}
	}	

}
