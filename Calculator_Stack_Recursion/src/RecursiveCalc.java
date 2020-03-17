import java.util.Hashtable;

public class RecursiveCalc {
	public enum OpCategory {
		eol, bracket, eqNotEq, relational, plusMinus, multiDiv, exponent, minusUnary, factorial
	}

	Tokenizer tokenParser;
	Hashtable<String, OpCategory> opPrecedenceMap;
	int tokenIndex;
	String[] tokenArr;
	Tokenizer.Type[] typeArr;
	int bracketCount;

	RecursiveCalc() {
		bracketCount = 0;
		initializePrecMap();
	}

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

	String evalExp(String inExp) {
		tokenArr = new String[inExp.length() + 1];
		typeArr = new Tokenizer.Type[inExp.length() + 1];
		tokenParser = new Tokenizer(inExp);

		int i = 0;
		String arrInput;
		do {
			arrInput = tokenParser.getNext();
			tokenArr[i] = arrInput;
			typeArr[i] = tokenParser.getType();
			i++;
		} while (!arrInput.equals("$"));

		String returnString = doOp(null, -1);
		return returnString;
	}

	String doOp(String leftToken, int inPriority) {
		String currentToken = tokenArr[tokenIndex];
		int savedIndex = tokenIndex;
		int myPriority;

		if (typeArr[savedIndex] == Tokenizer.Type.number) {
			tokenIndex += 1;
			return doOp(currentToken, inPriority);
		}

		double left = Double.NaN;
		boolean leftBool = false;
		if (leftToken != null) {
			if (leftToken.equals("true")) {
				leftBool = true;
			} else if (leftToken.equals("false")) {
				leftBool = false;
			} else {
				left = Double.parseDouble(leftToken);
			}
		}

		switch (currentToken) {
		// Base Cases
		case "$":
			return leftToken;
		case ")":
			bracketCount -= 1;
			return leftToken;

		case "(":
			myPriority = opPrecedenceMap.get("(").ordinal();
			tokenIndex += 1;
			bracketCount += 1;
			int saveBracketCount = bracketCount;
			String value = "";
			while (bracketCount >= saveBracketCount) {
				value = doOp(null, -1);
			}
			tokenIndex += 1;
			return doOp(value, inPriority);

		// Recursive Calls
		case "*":
			myPriority = opPrecedenceMap.get("*").ordinal();

			if (myPriority <= inPriority) {
				return leftToken;
			}
			tokenIndex += 1;
			String valueMult = Double.toString(left * Double.parseDouble(doOp(null, myPriority)));
			return doOp(valueMult, inPriority);

		case "/":
			myPriority = opPrecedenceMap.get("/").ordinal();

			if (myPriority <= inPriority) {
				return leftToken;
			}
			tokenIndex += 1;
			String valueDiv = Double.toString(left / Double.parseDouble(doOp(null, myPriority)));

			return doOp(valueDiv, inPriority);
		case "+":
			myPriority = opPrecedenceMap.get("+").ordinal();

			if (myPriority <= inPriority) {
				return leftToken;
			}
			tokenIndex += 1;
			String valuePlus = Double.toString(left + Double.parseDouble(doOp(null, myPriority)));
			return doOp(valuePlus, inPriority);

		case "-":
			myPriority = opPrecedenceMap.get("-").ordinal();

			if (myPriority <= inPriority) {
				return leftToken;
			}
			tokenIndex += 1;
			String valueSub = Double.toString(left - Double.parseDouble(doOp(null, myPriority)));

			return doOp(valueSub, inPriority);

		case "^":
			myPriority = opPrecedenceMap.get("^").ordinal();

			if (myPriority <= inPriority) {
				return leftToken;
			}
			tokenIndex += 1;
			boolean negative = false;
			double right = Double.parseDouble(doOp(null, myPriority));

			if (right < 0) {
				right = -right;
				negative = true;
			}
			double temp = 1.0;
			for (int i = 0; i < right; i++) {
				temp = temp * left;
			}
			if (negative) {
				temp = 1.0 / temp;
			}
			return doOp(Double.toString(temp), inPriority);

		// Relational Recursive Calls
		case "<":
			myPriority = opPrecedenceMap.get("<").ordinal();

			if (myPriority <= inPriority) {
				return leftToken;
			}
			tokenIndex += 1;
			String lessThanValue = Boolean.toString(left < Double.parseDouble(doOp(null, myPriority)));
			return doOp(lessThanValue, inPriority);

		case ">":
			myPriority = opPrecedenceMap.get(">").ordinal();
			if (myPriority <= inPriority) {
				return leftToken;
			}
			tokenIndex += 1;
			String greaterThanValue = Boolean.toString(left > Double.parseDouble(doOp(null, myPriority)));
			return doOp(greaterThanValue, inPriority);

		case ">=":
			myPriority = opPrecedenceMap.get(">=").ordinal();

			if (myPriority <= inPriority) {
				return leftToken;
			}
			tokenIndex += 1;
			String greaterThanEqValue = Boolean.toString(left >= Double.parseDouble(doOp(null, myPriority)));
			return doOp(greaterThanEqValue, inPriority);

		case "<=":
			myPriority = opPrecedenceMap.get("<=").ordinal();

			if (myPriority <= inPriority) {
				return leftToken;
			}
			tokenIndex += 1;
			String lessThanEqValue = Boolean.toString(left <= Double.parseDouble(doOp(null, myPriority)));
			return doOp(lessThanEqValue, inPriority);

		case "!=":
			myPriority = opPrecedenceMap.get("!=").ordinal();

			if (myPriority <= inPriority) {
				return leftToken;
			}
		
			tokenIndex += 1;
			
			String rightStrNE = doOp(null, myPriority);
			double rightDblNE = Double.NaN;
			boolean rightBoolNE = false;
			if (rightStrNE.equals("true")) {
				rightBoolNE = true;
			} else if (rightStrNE.equals("false")) {
				rightBoolNE = false;
			} else if (rightStrNE != null) {
				rightDblNE = Double.parseDouble(rightStrNE);
			}

			String notEq;

			if (Double.isNaN(rightDblNE)) {
				notEq = Boolean.toString(leftBool != rightBoolNE);
			} else {
				notEq = Boolean.toString(left != rightDblNE);
			}
			
			
			return doOp(notEq, inPriority);

		case "==":
			myPriority = opPrecedenceMap.get("==").ordinal();

			if (myPriority <= inPriority) {
				return leftToken;
			}
			tokenIndex += 1;

			String rightStr = doOp(null, myPriority);
			double rightDbl = Double.NaN;
			boolean rightBool = false;
			if (rightStr.equals("true")) {
				rightBool = true;
			} else if (rightStr.equals("false")) {
				rightBool = false;
			} else if (rightStr != null) {
				rightDbl = Double.parseDouble(rightStr);
			}

			String equalEq;

			if (Double.isNaN(rightDbl)) {
				equalEq = Boolean.toString(leftBool == rightBool);
			} else {
				equalEq = Boolean.toString(left == rightDbl);
			}
			return doOp(equalEq, inPriority);

		// Unary Recursive Calls
		case "!":
			myPriority = opPrecedenceMap.get("!").ordinal();

			if (myPriority <= inPriority) {
				return leftToken;
			}

			double factorial = 1;
			tokenIndex += 1;
			for (int j = 1; j <= left; j++) {
				factorial *= j;
			}
			return doOp(Double.toString(factorial), inPriority);

		case "~":
			myPriority = opPrecedenceMap.get("~").ordinal();
			
			if (myPriority <= inPriority && leftToken != null) {
				return leftToken;
			}
			tokenIndex += 1;
			double positiveNum = Double.parseDouble(doOp(null, myPriority));
			return doOp(Double.toString(-positiveNum), inPriority);

		}
		return "ERROR";
	}
}
