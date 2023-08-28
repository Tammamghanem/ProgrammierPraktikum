package Tag7.jsonifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.Map.entry;

public class Tokenizer {
	private static Pattern IDTF = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*$");
	private static Pattern NUM = Pattern.compile("^[0-9]+$");
	private static Pattern RGBA = Pattern.compile("^#([0-9a-fA-F]{6}|[0-9a-fA-F]{8})$");
	private static Pattern LAB = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*:$");
	
	static Map<String, TokenType> immutableMap = Map.ofEntries(
			entry("var", TokenType.KEYWORD),
			entry("if", TokenType.KEYWORD),
			entry("fi", TokenType.KEYWORD),
			entry("def", TokenType.KEYWORD),
			entry("draw", TokenType.KEYWORD),
			entry("jmp", TokenType.KEYWORD),
			entry("then", TokenType.KEYWORD),
			entry("else", TokenType.KEYWORD),
			entry("true", TokenType.BOOLEAN),
			entry("false", TokenType.BOOLEAN),
			entry("linebreak", TokenType.LINEBREAK),
			entry("circle", TokenType.SHAPETYPE),
			entry("quad", TokenType.SHAPETYPE),
			entry("triangle", TokenType.SHAPETYPE),
			entry("=", TokenType.CHARSPECIAL),
			entry("+", TokenType.CHARSPECIAL),
			entry("-", TokenType.CHARSPECIAL),
			entry("*", TokenType.CHARSPECIAL),
			entry("/", TokenType.CHARSPECIAL),
			entry("<", TokenType.CHARSPECIAL),
			entry(">", TokenType.CHARSPECIAL),
			entry("∧", TokenType.CHARSPECIAL),
			entry("∨", TokenType.CHARSPECIAL),
			entry("¬", TokenType.CHARSPECIAL),
			entry("==", TokenType.SYMBOLCPD),
			entry(">=", TokenType.SYMBOLCPD),
			entry("<=", TokenType.SYMBOLCPD),
			entry("¬=", TokenType.SYMBOLCPD));
			
	private static Map <String, TokenType> tokenMap = new HashMap<>(immutableMap);
	//double braces direct initialization has major performance issues
	//public static Map<String, TokenType> tokenMap = new HashMap<>();
	
	public static Map getTokenMap() {
		return tokenMap;
	}

	public ArrayList<Token> tokenizeProgram(String program){
		ArrayList<Token> tokens = new ArrayList<Token>();
		program = program.replace("\n", " linebreak ");
		String[] snippets = program.trim().split("\\s+");
		System.out.println("\n\n\n");
		for (int i = 0; i < snippets.length; i++) {
			String snippet = snippets[i];
			if (tokenMap.keySet().contains(snippet)) {
				tokens.add(new Token(snippet, tokenMap.get(snippet)));
			}
			else {
				if (IDTF.matcher(snippet).matches()) {
					tokens.add(new Token(snippet, TokenType.IDENTIFIER));
				}
				else if (NUM.matcher(snippet).matches()) {
					int snipNum = 0;
					try {
						snipNum = Integer.parseInt(snippet);
					}
					catch (NumberFormatException e) {
						//very basic error handling
						System.out.println("could not parse token " + snippet + " to int for some reason, set to 0");
					}
					tokens.add(new Token(snipNum, TokenType.NUMBER));
				}
				else if (RGBA.matcher(snippet).matches()) {
					tokens.add(new Token(snippet, TokenType.RGBA));
				}
				else if (LAB.matcher(snippet).matches()) {
					tokens.add(new Token(snippet, TokenType.LABEL));

				}
				// Variable assignment
				else
				if (snippet.equals("var")) {
					if (i+3 < snippets.length && snippets[i+2].equals("=")){
						String varName = snippets[i+1];
						String varValue = snippets[i+3];
						tokens.add(new Token("var", TokenType.KEYWORD));
						tokens.add(new Token(varName, TokenType.IDENTIFIER));
						tokens.add(new Token("=", TokenType.CHARSPECIAL));
						tokens.add(new Token(varValue, TokenType.NUMBER));
						i += 3;
					}
					// Check for arithmetic operators and numbers
					else if (snippet.matches("[+-]?\\d+")) {
						tokens.add(new Token(snippet, TokenType.NUMBER));
					}
					else if (snippet.matches("[+\\-*/]")) {
						tokens.add(new Token(snippet, TokenType.ARITHMETIC_OPERATOR));
					} else {
						tokens.add(new Token(snippet, TokenType.UNKNOWN));
						System.out.println("unknown token added");
					}
			}
		}
	}
		return tokens;
	}
}