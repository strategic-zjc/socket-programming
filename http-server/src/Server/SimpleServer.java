package Server;

import RequestExecutor.BasicExecutor;
import RequestExecutor.LoginExecutor;
import RequestExecutor.RegisterExecutor;
import java.util.Timer;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class SimpleServer {
    public static ArrayList<BasicExecutor> Executors = new ArrayList<>();
    public static Timer timer = new Timer();
    public static void main(String[] args) {
        SimpleServer server = new SimpleServer();
        server.go();
    }

    private void go(){

        Executors.add(new LoginExecutor());
        Executors.add(new RegisterExecutor());

        try {
            ServerSocket serverSocket = new ServerSocket(5000);// 先创建一个
            System.out.println("http://localhost:5000");

            while(true){
                Socket socket = serverSocket.accept();

                Thread readThread = new Thread(new ClientHandler(socket));

                readThread.start();


                System.out.println("Got a connection from " + socket.getInetAddress().getHostAddress());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
