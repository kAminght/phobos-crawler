package cat.kaminght.crawler;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;

/**
 * Java爬虫执行入口
 *
 * @author kaminght
 * @create 2019/12/28-22:15
 */
public class Main {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://github.com/gradle/gradle/issues");
        CloseableHttpResponse response = httpclient.execute(httpGet);
// The underlying HTTP connection is still held by the response object
// to allow the response content to be streamed directly from the network socket.
// In order to ensure correct deallocation of system resources
// the user MUST call CloseableHttpResponse#close() from a finally clause.
// Please note that if response content is not fully consumed the underlying
// connection cannot be safely re-used and will be shut down and discarded
// by the connection manager.
        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            InputStream input = entity.getContent();
            String content = IOUtils.toString(input, "utf-8");
            // jsoup解析HTML为DOM树
            Document document = Jsoup.parse(content);
            Elements els = document.select(".js-issue-row");
            for (Element el : els) {
                System.out.println(el.child(0).child(1).child(0).attr("href"));
                System.out.println(el.child(0).child(1).child(0).text());
            }
            // and ensure it is fully consumed
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
    }
    
    public static int test(int i) {
        if (i == 1) {
            throw new RuntimeException();
        }
        
        if (i < 0) {
            System.out.println("minus");
            return i - 1;
        } else if (i == 0) {
            System.out.println("zero");
            return i + 1;
        } else {
            return i + 2;
        }
    }
}
