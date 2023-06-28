import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {
    public static final int DEFAULT_PORT = 1234;
    public static final String DEFAULT_HOSTNAME = "localhost";
    private Socket clientSocket;
    private String message;
    private BufferedReader in;
    private PrintWriter out;


    public static void main(String[] args) {
        try {
            int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
            String hostName = args.length > 0 ? args[1] : DEFAULT_HOSTNAME;
            Client client = new Client();
            client.start(hostName, port);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void start(String hostName, int port) {
        try {
            clientSocket = new Socket(hostName, port);
            System.out.println("Client connected to port " + port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        message();
    }

    private void message() {
        try {
            Thread thread = new Thread(new readThread());
            thread.start();
            while (clientSocket.isConnected()) {
                message = in.readLine(); //reading from server and prints to the console
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class readThread implements Runnable {
        @Override
        public void run() {
            while (clientSocket.isConnected()) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    message = reader.readLine();
                    out.write(message);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
