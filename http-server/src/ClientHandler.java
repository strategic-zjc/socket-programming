
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler implements Runnable{
    // new thread
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
        // do something to handle the request
    }
}