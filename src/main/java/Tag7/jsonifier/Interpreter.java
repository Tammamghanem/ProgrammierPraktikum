package Tag7.jsonifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jdk.internal.org.jline.utils.InfoCmp.Capability.lines;

public class Interpreter {
	private Shape shape;
	private Map<String, Object> variables = new HashMap<>();


	Shape getShape() {
		return shape;
	}

	String interpret(List<Token> tokens) {
		List<List<Token>> lines = new ArrayList<>();
		List<Token> redLine = new ArrayList<>();
		for (Token token : tokens) {


			// handle jumps
			if (token.getTokenType().equals(TokenType.KEYWORD) && token.getContent().toString().equals("jmp")) {
				int jumpToLine = Integer.parseInt(tokens.get(1).getContent().toString());
				int currentLine = 0;
				for (List<Token> line : lines) {
					if (line.contains(token)) {
						break;
					}
					currentLine++;
				}
				if (currentLine < jumpToLine) {
					for (int i = currentLine; i < jumpToLine; i++) {
						lines.get(i).clear();
					}
				} else {
					for (int i = currentLine; i > jumpToLine; i--) {
						lines.get(i).clear();
					}
				}
			}


			if (token.type == TokenType.LINEBREAK && !redLine.isEmpty()) {
				lines.add(redLine);
				redLine = new ArrayList<>();
			} else {
				redLine.add(token);
			}
		}
		if (!redLine.isEmpty()) {
			lines.add(redLine);
		}
		for (List<Token> line : lines) {
			Token firstToken = line.get(0);
			List<Token> restList = line.subList(1, line.size());
			switch (firstToken.getTokenType()) {
				case KEYWORD -> {
					switch (firstToken.getContent().toString()) {
						case "draw" -> {
							this.parseDraw(restList);
						}
						case "var" -> {
							this.parseVar(restList);
						}
						case "if" -> {
							this.parseIfThenElse(restList);
						}
						case "fi" -> {
							this.parseIfThenElse(restList);
						}

						// handle jumps
						case "jmp" -> {
							int jumpToLine = Integer.parseInt(restList.get(0).getContent().toString());
							int currentLine = 0;
							for (List<Token> line2 : lines) {
								if (line2.contains(firstToken)) {
									break;
								}
								currentLine++;
							}
							if (currentLine < jumpToLine) {
								for (int i = currentLine; i < jumpToLine; i++) {
									lines.get(i).clear();
								}
							} else {
								for (int i = currentLine; i > jumpToLine; i--) {
									lines.get(i).clear();
								}
							}
						}
					}
				}
				case CHARSPECIAL -> {
				}
				case SYMBOLCPD -> {
				}
				case LABEL -> {
				}
				default -> {
				}
			}
		}
		if (shape != null) {
			return shape.toJson();
		}
		return "";
	}

	boolean listContainsExpression(List<Token> tokens) {
		for (Token token : tokens) {
			if (token.getTokenType().equals(TokenType.CHARSPECIAL) || token.getTokenType().equals(TokenType.SYMBOLCPD)) {
				return true;
			}
		}
		return false;
	}

	Object parseExpression(List<Token> tokens) {
		if (tokens.size() == 1) {
			return parseIdentifier(tokens.get(0));
		}
		Object value2 = parseIdentifier(tokens.get(tokens.size() - 1));
		Object value1 = parseExpression(tokens.subList(0, tokens.size() - 2));
		String operator = tokens.get(tokens.size() - 2).getContent().toString();
		if (operator.equals("+")) {
			return Integer.parseInt(value1.toString()) + Integer.parseInt(value2.toString());
		}
		if (operator.equals("-")) {
			return Integer.parseInt(value1.toString()) - Integer.parseInt(value2.toString());
		}
		if (operator.equals("/")) {
			return Integer.parseInt(value1.toString()) / Integer.parseInt(value2.toString());
		}
		return Integer.parseInt(value1.toString()) * Integer.parseInt(value2.toString());
	}

	void parseVar(List<Token> tokens) {
		Object content = tokens.get(2).getContent();
		if (listContainsExpression(tokens.subList(2, tokens.size()))) {
			content = parseExpression(tokens.subList(2, tokens.size()));
		}
		variables.put(tokens.get(0).getContent().toString(), content);
	}

	String parseIdentifier(Token token) {
		if (token.getTokenType().equals(TokenType.IDENTIFIER)) {
			return variables.get(token.getContent().toString()).toString();
		}
		return token.getContent().toString();
	}

	void parseDraw(List<Token> tokens) {
		Token type = tokens.get(0);
		String contentType = parseIdentifier(type);
		Token color = tokens.get(1);
		String contentColor = parseIdentifier(color);
		Token fillColor = tokens.get(2);
		String contentFill = parseIdentifier(fillColor);
		Token lineWidth = tokens.get(3);
		Integer contentWidth = Integer.valueOf(parseIdentifier(lineWidth));
		Token posX = tokens.get(4);
		Integer contentX = Integer.valueOf(parseIdentifier(posX));
		Token posY = tokens.get(5);
		Integer contentY = Integer.valueOf(parseIdentifier(posY));
		Token scaleX = tokens.get(6);
		Integer contentScaleX = Integer.valueOf(parseIdentifier(scaleX));
		Token scaleY = tokens.get(7);
		Integer contentScaleY = Integer.valueOf(parseIdentifier(scaleY));
		Token rotation = tokens.get(8);
		Integer contentRot = Integer.valueOf(parseIdentifier(rotation));
		Shape parsedShape = new Shape(Type.valueOf(contentType.toUpperCase()), contentColor, contentFill, contentWidth, contentX, contentY, contentScaleX, contentScaleY, contentRot);
		if (this.shape == null) {
			this.shape = parsedShape;
		} else {
			Shape innerMost = this.shape;
			while (innerMost.content != null) {
				innerMost = innerMost.content;
			}
			innerMost.content = parsedShape;
		}
	}

	boolean parseBoolean(Token token) {
		if (token.getTokenType().equals("true")) {
			return true;
		} else if (token.getTokenType().equals("false")) {
			return false;
		}
		return false;
	}

	boolean parseBooleanExpression(List<Token> tokens) {
		if (tokens.size() == 1) {
			Token token = tokens.get(0);
			if (token.getTokenType().equals(TokenType.SYMBOLCPD)) {
				return Boolean.parseBoolean(token.getContent().toString());
			} else if (token.getTokenType().equals(TokenType.IDENTIFIER)) {
				String var = token.getContent().toString();
				if (variables.containsKey(var)) {
					return Boolean.parseBoolean(variables.get(var).toString());
				}

			}
		} else if (tokens.size() >= 3) {
			Token operator = tokens.get(1);
			List<Token> left = tokens.subList(0, 1);
			List<Token> right = tokens.subList(2, tokens.size());

			boolean leftValue = parseBooleanExpression(left);
			boolean rightValue = parseBooleanExpression(right);

			if (operator.getContent().equals("∧")) {
				return leftValue && rightValue;
			} else if (operator.getContent().equals("∨")) {
				return leftValue || rightValue;
			} else if (operator.getContent().equals("¬")) {
				return !rightValue;
			}
		}
		return false;
	}

	void parseIfThenElse(List<Token> tokens) {
		List<Token> conditionTokens = new ArrayList<>();
		List<Token> thenTokens = new ArrayList<>();
		List<Token> elseTokens = new ArrayList<>();
		boolean inThen = false;
		boolean inElse = false;

		for (Token token : tokens) {
			if (!inThen && !inElse) {
				if (token.getContent().equals("then")) {
					inThen = true;
				}
				conditionTokens.add(token);
			} else if (inThen) {
				if (token.getContent().equals("else")) {
					inThen = false;
					inElse = true;
				} else {
					thenTokens.add(token);
				}
			} else if (inElse) {
				elseTokens.add(token);
			}

		}
	}
}
