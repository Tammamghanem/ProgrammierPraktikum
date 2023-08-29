package Tag7.jsonifier;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Main {
	private static final String FILEPATH = "C:\\Users\\tamma\\Desktop\\";
	private static final String FILENAME = "input.txt";

	public static void main(String[] args) throws URISyntaxException, IOException {
		Tokenizer translator = new Tokenizer();
		String data = "";
		BufferedReader reader = new BufferedReader(new FileReader(FILEPATH + FILENAME));
		String line;
		while((line = reader.readLine()) != null){
			data += line + "\n";
		}
	    ArrayList<Token> tokenlist = translator.tokenizeProgram(data);
		Interpreter interpreter = new Interpreter();
		interpreter.interpret(tokenlist);
		String json = interpreter.getShape().toJson();
		BufferedWriter writer = new BufferedWriter(new FileWriter(FILEPATH + "output.json"));
		writer.write(json);
		writer.flush();
		writer.close();
	}
}
