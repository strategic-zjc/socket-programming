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
import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

/**
 * @author fguohao
 * @date 2021/05/31
 */
public class PostTest {

    Client client = new Client();
    History history = History.getINSTANCE();
    @Test
    public void testPost(){
        try {
            postTest1();
            postTest2();
            postTest3();
        } catch (MissingHostException e) {
            e.printStackTrace();
        } catch (UnsupportedHostException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        history.printHistory();
    }

    private HttpRequest normal(String path){
        RequsetLine requsetLine = new RequsetLine(Method.POST,path);
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.put(Header.Host,"localhost:5000");
        MessageBody messageBody = new MessageBody();
        HttpRequest httpRequest = new HttpRequest(requsetLine,messageHeader, messageBody);
        return httpRequest;
    }


    public void postTest1() throws MissingHostException, UnsupportedHostException, ParseException, URISyntaxException {
        HttpRequest httpRequest  =normal("/login");
        httpRequest.getMessageHeader().put(Header.Content_Type,"application/x-www-form-urlencoded");
        httpRequest.getMessageBody().setBody("username=admin&password=123456".getBytes());
        client.sendHttpRequest(httpRequest);
    }


    public void postTest2() throws MissingHostException, UnsupportedHostException, ParseException, URISyntaxException {
        HttpRequest httpRequest  =normal("/register");
        httpRequest.getMessageHeader().put(Header.Content_Type,"application/x-www-form-urlencoded");
        httpRequest.getMessageBody().setBody("username=admin&password=123456".getBytes(StandardCharsets.UTF_8));
        client.sendHttpRequest(httpRequest);
    }

    public void postTest3() throws MissingHostException, UnsupportedHostException, ParseException, URISyntaxException {
        HttpRequest httpRequest  =normal("/login");
        httpRequest.getMessageHeader().put(Header.Content_Type,"application/x-www-form-urlencoded");
        httpRequest.getMessageBody().setBody("username=admin&password=123456".getBytes());
        client.sendHttpRequest(httpRequest);
    }
}
