import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SimpleClient {

    public static void main(String[] args) {
        SimpleClient simpleClient = new SimpleClient();
        simpleClient.go();
    }
    private void go(){
        // todo
        // initialize gui and
        try {
            Socket clientSocket = new Socket("localhost", 5000);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            String sentence = inFromUser.readLine();
            outToServer.writeBytes(sentence);
            String modifiedSentence = inFromServer.readLine();
            System.out.println("FROM SERVER: " + modifiedSentence);
            clientSocket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    class listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }
    class IncomingReader implements Runnable{
        @Override
        public void run() {
            // receive response in another thread
        }
    }

}
