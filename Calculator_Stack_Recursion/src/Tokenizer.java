public class Tokenizer {
	public enum Type {
		number, operator, none
	}

	String input;
	int strPosition;
	char currentChar;
	Type type = Type.none;
	Type prevType;
	String returnString = "";
	String prevString;

	Tokenizer(String inInput) {
		input = inInput;
		strPosition = 0;
		currentChar = input.charAt(0);
	}

	String getNext() {
		prevString = returnString;
		prevType = type;
		returnString = "";
		type = Type.none;

		// END OF LINE TOKEN
		if (currentChar == 0) {
			type = Type.operator;
			return "$";
		}

		// CYCLE OVER WHITE SPACE
		while (currentChar == ' ') {
			strPosition += 1;
			currentChar = strPosition < input.length() ? input.charAt(strPosition) : null;
		}

		// TOKENIZE REAL NUMBERS
		while ('0' <= currentChar && currentChar <= '9' || currentChar == '.') {
			type = Type.number;
			returnString = returnString + currentChar;
			strPosition += 1;
			currentChar = strPosition < input.length() ? input.charAt(strPosition) : 0;
		}
		if (type == Type.number) {
			return returnString;
		}
		
		// TOKENIZE OPERATORS
		switch (currentChar) {

		case ')':
		case '(':
		case '+':
		case '*':
		case '/':
		case '^':
			type = Type.operator;
			returnString = returnString + currentChar;
			incrPos();
			break;
		case '!':
			type = Type.operator;
			returnString = checkExclamation();
			break;
		case '>':
		case '<':
			type = Type.operator;
			returnString = checkRelational(currentChar);
			break;
		case '=':
			type = Type.operator;
			incrPos();
			incrPos();
			returnString = "==";
			break;
		case '-':
			type = Type.operator;
			if (prevType == Type.none
					|| (prevType == Type.operator && !prevString.equals(")") && !prevString.equals("!"))) {
				returnString = "~";
			} else {
				returnString = "-";
			}
			incrPos();
			break;
		}

		return returnString;
	}

	Type getType() {
		return type;
	}

	void incrPos() {
		strPosition += 1;
		currentChar = strPosition < input.length() ? input.charAt(strPosition) : 0;
	}

	// Method to check if exclamation mark is factorial, not equal, or not logically equal
	String checkExclamation() {
		// check ! =
		if ((input.length() - strPosition) > 1 && (input.charAt(strPosition + 1) == '=')) {
			// check !==
			// all input is arithmetically correct therefore after = there will be, at
			// least, one more char
			// therefore not necessary to check length
			if ((input.length() - strPosition) > 2 && (input.charAt(strPosition + 2) == '=')) {
				incrPos();
				// only return ! because we know that the next string position contains == which
				// will get passed on the next time through
				return "!";
			} else {
				incrPos();
				incrPos();
				return "!=";
			}

		} else {
			incrPos();
			return "!";
		}
	}

	// ALL RELATIONAL OPERATORS ARE MULTIPLE CHARACTERS
	// method checks the next few characters to determine what relational operator it is
	String checkRelational(char currentChar) {
		String returnString = "";
		// can only have one relational op for each expression therefore there will be,
		// at least, one more char
		// therefore not necessary to check length
		if ((input.charAt(strPosition + 1) == '=')) {
			incrPos();
			incrPos();
			return returnString + currentChar + "=";
		} else {
			incrPos();
			return returnString + currentChar;
		}
	}
}
