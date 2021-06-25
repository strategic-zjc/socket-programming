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
public class Test301 {
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
        //服务端地址```localhost:5000/movedPic.png```已经永久变更为了```localhost:5000/pic.png```,我们尝试进行两次请求
        client.setLogLevel(History.LOG_LEVEL_DETAIL);
        normal("/movedPic.png",false);
        //收到301码
        //保存新路径供下次使用
        //请求新路径
        //会复用连接
        //LocalMessage: Just for log是在客户端手动添加的，仅用于展示该条目来自本地信息
        normal("/movedPic.png",false);
        normal("/pic.png",false);
    }
}
