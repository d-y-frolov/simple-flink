package com.example.simpleflink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class SocketWriter implements Runnable{
	public final static int PORT = 9999;
	public final static String URL = "localhost";
	private final static int GENERATED_NUMBERS_QUANTITY = 30;
	private final static long SLEEP_INTERVAL = 1_000L;

	@Override
	public void run() {

		try(ServerSocket serverSocket= new ServerSocket(PORT)) {
         	
			Socket socket = serverSocket.accept();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
            				new InputStreamReader(socket.getInputStream()));
            Random random = new Random(System.nanoTime());
 
            for ( int i = 1; i <= GENERATED_NUMBERS_QUANTITY; i++ ) {
                out.printf("%d ( %d of %d )\n",random.nextInt(), i, GENERATED_NUMBERS_QUANTITY);
                try {
					Thread.sleep(SLEEP_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }

            in.close();
            out.close();
            socket.close();

		}
        catch (IOException e) {
            e.printStackTrace();
        }
	}
}


