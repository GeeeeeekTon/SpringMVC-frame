package solr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

public class AddBeansDemo {
    public static final String SOLR_URL = "http://127.0.0.1:8080/solr";

    public static void main(String[] args) {
        // 通过浏览器查看结果
        // 要保证bean中各属性的名称在conf/schema.xml中存在，如果查询，要保存被索引
        // http://172.168.63.233:8983/solr/collection1/select?q=description%3A%E6%94%B9%E9%9D%A9&wt=json&indent=true
        // delBeans();
        AddBeans();
    }

    public static Random rand = new Random(47);
    public static String[] authors = { "张三", "李四", "王五", "赵六", "张飞", "刘备", "关云长" };
    public static String[] links = { "http://repository.sonatype.org/content/sites/forge-sites/m2e/", "http://news.ifeng.com/a/20140818/41626965_0.shtml",
            "http://news.ifeng.com/a/20140819/41631363_0.shtml?wratingModule_1_9_1", "http://news.ifeng.com/topic/19382/", "http://news.ifeng.com/topic/19644/" };

    public static String genAuthors() {
        List<String> list = Arrays.asList(authors).subList(0, rand.nextInt(7));
        String str = "";
        for (String tmp : list) {
            str += " " + tmp;
        }
        return str;
    }

    public static List<String> genLinks() {
        return Arrays.asList(links).subList(0, rand.nextInt(5));
    }

    public static void AddBeans() {
        String[] words = { "中央全面深化改革领导小组", "第四次会议", "审议了国企薪酬制度改革", "考试招生制度改革", "传统媒体与新媒体融合等", "相关内容文件", "习近平强调要", "逐步规范国有企业收入分配秩序", "实现薪酬水平适当",
                "结构合理、管理规范、监督有效", "对不合理的偏高", "过高收入进行调整", "深化考试招生制度改革", "总的目标是形成分类考试", "综合评价", "多元录取的考试招生模式", "健全促进公平", "科学选才", "监督有力的体制机制", "着力打造一批形态多样",
                "手段先进", "具有竞争力的新型主流媒体", "建成几家拥有强大实力和传播力", "公信力", "影响力的新型媒体集团" };

        long start = System.currentTimeMillis();
        Collection<NewsBean> docs = new ArrayList<NewsBean>();
        // DocumentObjectBinder binder = new DocumentObjectBinder();
        for (int i = 1; i < 300; i++) {
            NewsBean news = new NewsBean();
            news.setId("id" + i);
            news.setName("news" + i);
            news.setAuthor(genAuthors());
            news.setDescription(words[i % 21]);
            news.setRelatedLinks(genLinks());
            // SolrInputDocument doc1 = binder.toSolrInputDocument(news);
            docs.add(news);
        }
        try {
            HttpSolrServer server = new HttpSolrServer(SOLR_URL);
            server.setRequestWriter(new BinaryRequestWriter());
            // 可以通过二种方式增加docs,其中server.add(docs.iterator())效率最高
            // 增加后通过执行commit函数commit (981ms)
            // server.addBeans(docs);
            // server.commit();

            // the most optimal way of updating all your docs
            // in one http request(481ms)
            server.addBeans(docs.iterator());
            server.optimize(); // time elasped 1176ms
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("time elapsed(ms):" + (System.currentTimeMillis() - start));
    }

    public static void delBeans() {
        long start = System.currentTimeMillis();
        try {
            HttpSolrServer server = new HttpSolrServer(SOLR_URL);
            List<String> ids = new ArrayList<String>();
            for (int i = 1; i < 300; i++) {
                ids.add("id" + i);
            }
            server.deleteById(ids);
            server.commit();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("time elapsed(ms):" + (System.currentTimeMillis() - start));
    }
}