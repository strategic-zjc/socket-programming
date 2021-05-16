import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SimpleClient {

    public static void main(String[] args) {
        SimpleClient simpleClient = new SimpleClient();
        simpleClient.go();
    }
    private void go(){
        // todo
        // initialize gui and
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
