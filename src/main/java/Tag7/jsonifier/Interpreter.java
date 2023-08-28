package Tag7.jsonifier;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Tag7.jsonifier.Token;
import Tag7.jsonifier.TokenType;
import Tag7.jsonifier.Shape;


public class Interpreter {
    private List<Token> tokens;
    private Map<String, String> variables = new HashMap<>();
    TokenType type;
    Object content;
    StringBuilder jsonOutput = new StringBuilder();

    public Interpreter(List<Token> tokens) {
        this.tokens = tokens;
    }

    public String interpret(){
        for (Token token : tokens) {
            if (token.getTokenType() == TokenType.VAR_DECLARATION) {
                // Interpretiere das var-Statement
                String varName = token.getArguments().get(0).toString();
                String varValue = token.getArguments().get(1).toString();
                variables.put(varName, varValue);
            }
            else if (token.getTokenType() == TokenType.DRAW) {
            // Interpretiere das draw-Statement
            String type = token.getArguments().get(0).toString();
            String color = token.getArguments().get(1).toString();
            String fillColor = token.getArguments().get(2).toString();
            String replacedColor = replaceVariableWithValues(color);
            double lineWidth = Double.parseDouble(token.getArguments().get(3).toString());
            double positionX = Double.parseDouble(token.getArguments().get(4).toString());
            double positionY = Double.parseDouble(token.getArguments().get(5).toString());
            double scaleX = Double.parseDouble(token.getArguments().get(6).toString());
            double scaleY = Double.parseDouble(token.getArguments().get(7).toString());
            double rotation = Double.parseDouble(token.getArguments().get(8).toString());

            Shape shape = new Shape(type, color, fillColor, lineWidth,positionX, positionY, scaleX, scaleY, rotation, null);
            jsonOutput.append(shape.toJson()).append("\n");
        }
    }

        return jsonOutput.toString();
    }

    private String replaceVariableWithValues(String input) {
        for(String varName : variables.keySet()) {
            String varValue = variables.get(varName);
            input = input.replace(varName, varValue);
        }
        return input;
    }



    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Main <input_file>");
            return;
        }

        String inputFile = args[0];

        try {
            List<String> lines = Files.readAllLines(Paths.get(inputFile));
            Tokenizer tokenizer = new Tokenizer();
            Interpreter interpreter = new Interpreter(tokenizer.tokenizeProgram(String.join("\n", lines)));

            ArrayList<Token> tokens = tokenizer.tokenizeProgram(String.join("\n", lines));
            String jsonOutput = interpreter.interpret();

            System.out.println(jsonOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

