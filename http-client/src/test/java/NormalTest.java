import com.networkcourse.httpclient.client.Client;
import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.history.History;
import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;
import com.networkcourse.httpclient.message.component.commons.Header;
import com.networkcourse.httpclient.message.component.commons.MessageBody;
import com.networkcourse.httpclient.message.component.commons.MessageHeader;
import com.networkcourse.httpclient.message.component.request.Method;
import com.networkcourse.httpclient.message.component.request.RequsetLine;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

/**
 * @author fguohao
 * @date 2021/06/25
 */
public class NormalTest {
    Client client = new Client();
    private void normalGet() throws URISyntaxException {
        RequsetLine requsetLine = new RequsetLine(Method.GET,"/");
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.put(Header.Host,"ditu.yjdy.org");
        messageHeader.put(Header.Content_Length,"0");
        MessageBody messageBody = new MessageBody();
        HttpRequest httpRequest = new HttpRequest(requsetLine,messageHeader, messageBody);
        try {
            HttpResponse httpResponse  = client.sendHttpRequest(httpRequest);
            httpResponse.saveBody("normalTest1.html");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MissingHostException e) {
            e.printStackTrace();
        } catch (UnsupportedHostException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void normalPost() throws URISyntaxException {
        RequsetLine requsetLine = new RequsetLine(Method.POST,"/login");
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.put(Header.Host,"localhost:5000");
        messageHeader.put(Header.Content_Type,"application/x-www-form-urlencoded");
        messageHeader.put(Header.Content_Length,"30");
        MessageBody messageBody = new MessageBody();
        messageBody.setBody("username=admin&password=123456".getBytes());
        HttpRequest httpRequest = new HttpRequest(requsetLine,messageHeader, messageBody);
        try {
            HttpResponse httpResponse  = client.sendHttpRequest(httpRequest);
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

    private void normalCheck() throws URISyntaxException {
        RequsetLine requsetLine = new RequsetLine(Method.GET,"/");
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.put(Header.Host,"ditu.yjdy.org");
        //messageHeader.put(Header.Content_Length,"0");
        MessageBody messageBody = new MessageBody();
        HttpRequest httpRequest = new HttpRequest(requsetLine,messageHeader, messageBody);
        try {
            HttpResponse httpResponse  = client.sendHttpRequest(httpRequest);
            httpResponse.saveBody("normalTest1.html");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MissingHostException e) {
            e.printStackTrace();
        } catch (UnsupportedHostException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void gettest() throws URISyntaxException {
        client.setLogLevel(History.LOG_LEVEL_DETAIL);
        normalGet();
        normalPost();
    }

    @Test
    public void checkTest() throws URISyntaxException {
        client.setLogLevel(History.LOG_LEVEL_INFO);
        normalCheck();
    }
}
