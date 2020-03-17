public class Tokenizer {
	public enum Type {
		number, operator, variable, bracket, none
	}

	public class Token{
		String element;
		Type type;
		
		Token(String inElem, Type inType){
			element = inElem;
			type = inType;
		}
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

	Token getNext() {
		prevString = returnString;
		prevType = type;
		returnString = "";
		type = Type.none;

		// END OF LINE TOKEN
		if (currentChar == 0) {
			type = Type.none;
			return new Token("$", type);
		}

		// CYCLE OVER WHITE SPACE
		while (currentChar == ' ') {
			strPosition += 1;
			currentChar = strPosition < input.length() ? input.charAt(strPosition) : null;
		}
		
		// TOKENIZE VARIABLES
		while('a'<= currentChar && currentChar <= 'z' || 'A'<= currentChar && currentChar <= 'Z') {
				type = Type.variable;
				returnString = returnString + currentChar;
				strPosition += 1;
				currentChar = strPosition < input.length() ? input.charAt(strPosition) : 0;
		}
		if (type == Type.variable) {
			return new Token(returnString, type);
		}

		// TOKENIZE REAL NUMBERS
		while ('0' <= currentChar && currentChar <= '9' || currentChar == '.') {
			type = Type.number;
			returnString = returnString + currentChar;
			strPosition += 1;
			currentChar = strPosition < input.length() ? input.charAt(strPosition) : 0;
		}
		if (type == Type.number) {
			return new Token(returnString, type);
		}
		
		// TOKENIZE OPERATORS
		switch (currentChar) {

		case ')':
		case '(':
			type = Type.bracket;
			returnString += currentChar;
			break;
		case '+':
		case '*':
		case '/':
		case '-':
			type = Type.operator;
			returnString += currentChar;
			break;
		}
		incrPos();
		return new Token(returnString, type);
	}

	/*
	 Type getType() {
		return type;
	}
	 */
	

	void incrPos() {
		strPosition += 1;
		currentChar = strPosition < input.length() ? input.charAt(strPosition) : 0;
	}
}