import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int DEFAULT_PORT = 1234;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String message;


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
            Server server = new Server();
            server.start(port);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void start(int port) {

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server connected to port " + port);
            clientSocket = serverSocket.accept();
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(System.out, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        message();
    }

    private void message() {
        try {
            while (!serverSocket.isClosed()) {
                message = in.readLine();
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                message = reader.readLine();
                out.println(message);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class ClientHandler implements Runnable {

        static Socket socket;

        public ClientHandler(){
            try{
                clientSocket = serverSocket.accept();
            } catch(IOException e){
                e.printStackTrace();
            }


        }

        @Override
        public void run() {
        }
    }
}
