package cat.kaminght.crawler;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Java爬虫执行入口
 *
 * @author kaminght
 * @create 2019/12/28-22:15
 */
public class Main {
    public static void main(String[] args) throws IOException {
    
        List<String> linkPool = new ArrayList<>();
        Set<String> processedLinks = new HashSet<>();
        linkPool.add("https://sina.cn");
    
        while (true) {
            if (linkPool.isEmpty()) {
                break;
            }
            String link = linkPool.remove(linkPool.size() - 1);
        
            if (processedLinks.contains(link)) {
                continue;
            }
            if (!link.contains("passport.sina.cn") && link.contains("news.sina.cn") || "https://sina.cn".equals(link)) {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGet get = new HttpGet(link);
                get.addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; " +
                    "Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79" +
                    ".0.3945.130 Mobile Safari/537.36");
                try (CloseableHttpResponse response = httpClient.execute(get)) {
                    HttpEntity entity = response.getEntity();
                    System.out.println(link);
                    System.out.println(response.getStatusLine());
                    String html = EntityUtils.toString(entity);
                    Document htmlDoc = Jsoup.parse(html);
                    Elements aTags = htmlDoc.select("a");
                    for (Element aTag : aTags) {
                        linkPool.add(aTag.attr("href"));
                    }
                    Elements articleTags = htmlDoc.select("article");
                    if (!articleTags.isEmpty()) {
                        for (Element articleTag : articleTags) {
                            String title = articleTag.child(0).text();
                            System.out.println(title);
                        }
                    }
                    processedLinks.add(link);
                }
            }
        }
    }
    
}
