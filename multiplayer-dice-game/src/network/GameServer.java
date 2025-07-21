package network;

import java.io.*;
import java.net.*;
import java.util.Random;

public class GameServer {
    private ServerSocket serverSocket;
    private int code;
    private int port;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public GameServer() throws IOException {
        this.code = 10000 + new Random().nextInt(90000); // 5-digit code
        this.port = code; // Port is the code itself
        this.serverSocket = new ServerSocket(port);
    }

    public int getCode() {
        return code;
    }

    public void waitForClient() throws IOException {
        System.out.println("Waiting for player to join on code: " + code);
        clientSocket = serverSocket.accept();
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        System.out.println("Player joined!");
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public String receiveMessage() throws IOException {
        return in.readLine();
    }

    public void close() throws IOException {
        if (clientSocket != null) clientSocket.close();
        if (serverSocket != null) serverSocket.close();
    }
}
