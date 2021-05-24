package Server;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class SimpleServer {
    ArrayList<PrintWriter> clientOutputStreams;
    static HashMap<String, String>  login = new HashMap<>();
    public static void main(String[] args) {
        SimpleServer server = new SimpleServer();
        server.go();
    }
    public static HashMap<String,String> getLogin(){
        return login;
    }
    private void go(){
        clientOutputStreams  = new ArrayList<>(); // 初始化
        try {
            ServerSocket serverSocket = new ServerSocket(5000);// 先创建一个
            System.out.println("http://localhost:5000");

            while(true){
                Socket socket = serverSocket.accept();
                // 这里会阻塞，程序会停在这里
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                clientOutputStreams.add(writer);

                //
                Thread readThread = new Thread(new ClientHandler(socket));
                readThread.start();

                System.out.println("Got a connection from " + socket.getInetAddress().getHostAddress());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
