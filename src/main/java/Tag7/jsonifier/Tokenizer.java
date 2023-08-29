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
		for (String snippet : snippets) {
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

					// BOOLEANS
				} else if (snippet.equals("∧")) {
					tokens.add(new Token("&&", TokenType.SYMBOLCPD));
				}
				else if (snippet.equals("∨")) {
					tokens.add(new Token("||", TokenType.SYMBOLCPD));
				}
				else if (snippet.equals("¬")) {
					tokens.add(new Token("!", TokenType.CHARSPECIAL));
				}
				else if (snippet.equals(">")) {
					tokens.add(new Token(">", TokenType.CHARSPECIAL));
				}
				else if (snippet.equals("<")) {
					tokens.add(new Token("<", TokenType.CHARSPECIAL));
				}
				else if (snippet.equals("=")) {
					tokens.add(new Token("=", TokenType.CHARSPECIAL));

					// Check for control flow keywords
				}
				else if (snippet.equals("if")) {
					tokens.add(new Token("IF", TokenType.KEYWORD));
				} else if (snippet.equals("then")) {
					tokens.add(new Token("THEN", TokenType.KEYWORD));
				} else if (snippet.equals("else")) {
					tokens.add(new Token("ELSE", TokenType.KEYWORD));
				} else if (snippet.equals("fi")) {
					tokens.add(new Token("FI", TokenType.KEYWORD));

					// Check for jump keyword
				} else if (snippet.equals("jmp")) {
					tokens.add(new Token("JMP", TokenType.KEYWORD));
			} else if (snippet.endsWith(":")) {
					tokens.add(new Token(snippet, TokenType.LABEL));
				}

				else {
					tokens.add(new Token(snippet, TokenType.UNKNOWN));
					System.out.println("unknown token added");
				}

			}
		}
		return tokens;
	}

}
