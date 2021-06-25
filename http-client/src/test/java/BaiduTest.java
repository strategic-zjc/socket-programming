import com.networkcourse.httpclient.client.Client;
import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
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
public class BaiduTest {

    @Test
    public void baiduTest() throws URISyntaxException {
        RequsetLine requsetLine = new RequsetLine(Method.GET,"/");
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.put(Header.Host,"www.baidu.com");
        messageHeader.put(Header.Accept,"*/*");
        messageHeader.put(Header.Connection,"keep-alive");
        messageHeader.put(Header.Accept_Encoding,"gzip, deflate, br");
        MessageBody messageBody= new MessageBody();
        HttpRequest httpRequest = new HttpRequest(requsetLine,messageHeader,messageBody);
        Client client = new Client();
        try {
            HttpResponse httpResponse =client.sendHttpRequest(httpRequest);
            httpResponse.saveBody("./123.html");
        } catch (MissingHostException e) {
            e.printStackTrace();
        } catch (UnsupportedHostException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
