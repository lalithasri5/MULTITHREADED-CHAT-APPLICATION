package practice;
import java.io.*;
import java.net.*;
import java.util.*;

public class MultiClientChatServer {
    private static final int PORT = 1234;
    private static Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        System.out.println("Server started. Waiting for clients...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected.");
                ClientHandler clientThread = new ClientHandler(socket);
                clients.add(clientThread);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class for handling individual client
    static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Welcome to the chat!");
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    broadcast(message, this);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected.");
            } finally {
                try {
                    socket.close();
                    clients.remove(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Send message to all clients except sender
        private void broadcast(String message, ClientHandler sender) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.out.println(message);
                }
            }
        }
    }
}