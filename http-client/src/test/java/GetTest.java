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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

/**
 * @author fguohao
 * @date 2021/05/31
 */

public class GetTest {
    Client client = new Client();

    private void normal(String path) throws URISyntaxException {
        RequsetLine requsetLine = new RequsetLine(Method.GET,path);
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.put(Header.Host,"localhost:5000");
        messageHeader.put(Header.Connection,"keep-alive");
        MessageBody messageBody = new MessageBody();
        HttpRequest httpRequest = new HttpRequest(requsetLine,messageHeader, messageBody);
        try {
            HttpResponse httpResponse  = client.sendHttpRequest(httpRequest);
            httpResponse.saveBody(path.substring(1));
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
    public void testGet1() throws URISyntaxException {
        normal("/movedPic.png");
        normal("/movedIndex.html");
        normal("/movedIndex2.html");
        normal("/movedPic.png");
        normal("/pic.png");
        normal("/index.html");
    }


}
