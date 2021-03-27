import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveTask;

public class LinkExecutor extends RecursiveTask<String> {
    private String url;
    private static String startUrl;
    private static CopyOnWriteArraySet<String> allLinks = new CopyOnWriteArraySet<>();

    public LinkExecutor(String url) {
        this.url = url.trim();
    }

    public LinkExecutor(String url, String startUrl) {
        this.url = url.trim();
        LinkExecutor.startUrl = startUrl.trim();
    }

    @Override
    protected String compute() {
        StringBuffer sb = new StringBuffer(url + "\n\t");
        Set<LinkExecutor> subTask = new HashSet<>();

        getChildren(subTask);

        for (LinkExecutor link : subTask) {
            sb.append(link.join());
        }
        return sb.toString();
    }

    private void getChildren(Set<LinkExecutor> subTask) {
        Document doc;
        Elements elements;
        try {
            Thread.sleep(2);
            doc = Jsoup.connect(url).userAgent("Chrome").get();
            elements = doc.select("a");
            for(int i=0; i<elements.size(); i++) {
                    String attr = elements.get(i).attr("abs:href");
                    if (!attr.isEmpty() && attr.startsWith(startUrl) && !allLinks.contains(attr) && !attr
                            .contains("#")) {
                        LinkExecutor linkExecutor = new LinkExecutor(attr);
                        System.out.println(attr);
                        linkExecutor.fork();
                        subTask.add(linkExecutor);
                        allLinks.add(attr);
                    }
                }
        } catch (InterruptedException | IOException ignored) {
        }
    }
}