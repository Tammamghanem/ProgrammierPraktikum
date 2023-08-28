package Tag7.jsonifier;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws URISyntaxException, IOException {
		Tokenizer translator = new Tokenizer();
		String data =
				"var lineColor = #000000ff" +
						"var degree = 0" +
						"draw circle lineColor #00ffffaa 2 20 20 10 20 degree" +
						"var degree = degree + 90" +
						"draw triangle lineColor #f0daaaaa 2 20 20 10 20 degree" +
						"draw quad lineColor #00ffffaa 2 20 20 10 20 degree"
				;
	    ArrayList<Token> tokenlist = translator.tokenizeProgram(data);


	    System.out.println(tokenlist);
	}
}
