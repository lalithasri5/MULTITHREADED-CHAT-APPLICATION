package practice;
import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to chat server.");
            System.out.println(in.readLine()); // Welcome message

            // Thread for receiving messages from server
            Thread receiveThread = new Thread(() -> {
                String response;
                try {
                    while ((response = in.readLine()) != null) {
                        System.out.println("Server: " + response);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });
            receiveThread.start();

            // Main thread for sending messages
            String message;
            while ((message = keyboard.readLine()) != null) {
                out.println(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}