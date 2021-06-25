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
 * 测试长连接
 * @author fguohao
 * @date 2021/06/25
 */
public class KeepAliveTest {
    Client client = new Client();
    private void normal(String path,boolean enableAlive) throws URISyntaxException {
        RequsetLine requsetLine = new RequsetLine(Method.GET,path);
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.put(Header.Host,"localhost:5000");
        if(enableAlive){
            messageHeader.put(Header.Connection,"keep-alive");
        }
        messageHeader.put(Header.Content_Length,"0");
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
    public void keepalivetest() throws URISyntaxException {
        client.setLogLevel(History.LOG_LEVEL_INFO);
        //连续调用6次同一请求，其中前三次不携带keep-alive头，后三次携带keep-alive头
        normal("/favicon.ico",false);
        normal("/favicon.ico",false);
        normal("/favicon.ico",false);
        normal("/favicon.ico",true);
        //此后复用上次创建的连接，所以client server created只有4次
        normal("/favicon.ico",true);
        normal("/favicon.ico",true);
    }
}
