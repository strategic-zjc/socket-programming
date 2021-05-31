import com.networkcourse.httpclient.client.Client;
import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.history.History;
import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.MessageBody;
import com.networkcourse.httpclient.message.component.commons.MessageHeader;
import com.networkcourse.httpclient.message.component.request.Method;
import com.networkcourse.httpclient.message.component.request.RequsetLine;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import java.net.URISyntaxException;
import java.text.ParseException;

/**
 * @author fguohao
 * @date 2021/05/31
 */

public class GetTest {
    Client client = new Client();
    History history = History.getINSTANCE();
    @After
    public void printHistory(){
        history.printHistory();
    }

    private void normal(String path){
        RequsetLine requsetLine = new RequsetLine(Method.GET,path);
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.put(Header.Host,"localhost:5000");
        MessageBody messageBody = new MessageBody();
        HttpRequest httpRequest = new HttpRequest(requsetLine,messageHeader, messageBody);
        try {
            client.sendHttpRequest(httpRequest);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MissingHostException e) {
            e.printStackTrace();
        } catch (UnsupportedHostException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGet1(){
        normal("/movedPic.png");
    }


    @Test
    public void testGet2(){
        normal("/movedIndex.html");
    }

    @Test
    public void testGet3(){
        normal("/movedIndex2.html");
    }

    @Test
    public void testGet4(){
        normal("/movedPic.png");
    }

    @Test
    public void testGet5(){
        normal("/pic.png");
    }

    @Test
    public void testGet6(){
        normal("/index.html");
        history.printHistory();
    }
}
