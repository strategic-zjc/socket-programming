import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class SimpleServer {
    ArrayList<PrintWriter> clientOutputStreams;

    public class ClientHandler implements Runnable{
        BufferedReader reader;
        Socket socket;

        public ClientHandler(Socket clientSock){
            try{
                socket = clientSock;
                InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(isReader);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) { // readline 会阻塞，直到有输入
                    System.out.println("read "+message);
                    tellClients(message);
                }
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        SimpleServer server = new SimpleServer();
        server.go();
    }
    private void go(){
        clientOutputStreams  = new ArrayList<>(); // 初始化
        try {
            ServerSocket serverSocket = new ServerSocket(5000);// 先创建一个

            while(true){
                Socket socket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                clientOutputStreams.add(writer);

                Thread readThread = new Thread(new ClientHandler(socket));
                readThread.start();

                System.out.println("Got a connection");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void tellClients(String message){
        Iterator it = clientOutputStreams.iterator();
        while(it.hasNext()){
            try{
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush(); // 立刻发送
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
