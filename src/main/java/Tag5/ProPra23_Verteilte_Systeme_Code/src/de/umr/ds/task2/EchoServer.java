package Tag5.ProPra23_Verteilte_Systeme_Code.src.de.umr.ds.task2;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer implements Runnable{

	int PORT = 12345;
	ServerSocket serverSocket;
	ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(3);


	EchoServer(){
		System.out.println("Server: Created");
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	@Override
	public void run() {
		System.out.println("Server: Start");
		Socket socket = null;
		while (true){
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (socket != null){
				System.out.println("Server: new socket detected!");
				Socket finalSocket = socket;
				threadPoolExecutor.submit(new Runnable() {
					@Override
					public void run() {
						try {
							//Server output
							OutputStream outputStream = finalSocket.getOutputStream();
							OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
							PrintWriter printWriter = new PrintWriter(outputStreamWriter);

							//Server input
							InputStream inputStream = finalSocket.getInputStream();
							InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
							BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

							String input;
							String output;



							input = bufferedReader.readLine();
							output = "Eco: "  + input;
							System.out.println("Server: in = " + input);

							printWriter.println(output);
							printWriter.flush();

							finalSocket.close();

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				socket = null;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new Thread(new EchoServer()).start();
	}

	public void stop() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}