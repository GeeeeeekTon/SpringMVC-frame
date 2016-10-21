package solr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

public class AddDocsDemo {
    public static final String SOLR_URL = "http://127.0.0.1:8080/solr";

    public static void main(String[] args) {
        // 通过浏览器查看结果
        // http://172.168.63.233:8983/solr/collection1/select?q=name%3A%E6%94%B9%E9%9D%A9&wt=json&indent=true
        // AddDocs();
        delDocs();
    }

    public static void AddDocs() {
        String[] words = { "中央全面深化改革领导小组", "第四次会议", "审议了国企薪酬制度改革", "考试招生制度改革", "传统媒体与新媒体融合等", "相关内容文件", "习近平强调要", "逐步规范国有企业收入分配秩序", "实现薪酬水平适当",
                "结构合理、管理规范、监督有效", "对不合理的偏高", "过高收入进行调整", "深化考试招生制度改革", "总的目标是形成分类考试", "综合评价", "多元录取的考试招生模式", "健全促进公平", "科学选才", "监督有力的体制机制", "着力打造一批形态多样",
                "手段先进", "具有竞争力的新型主流媒体", "建成几家拥有强大实力和传播力", "公信力", "影响力的新型媒体集团" };
        long start = System.currentTimeMillis();
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        for (int i = 1; i < 300; i++) {
            SolrInputDocument doc1 = new SolrInputDocument();
            doc1.addField("id", "id" + i, 1.0f);
            doc1.addField("name", words[i % 21], 1.0f);
            doc1.addField("price", 10 * i);
            docs.add(doc1);
        }
        try {
            HttpSolrServer server = new HttpSolrServer(SOLR_URL);
            // 可以通过三种方式增加docs,其中server.add(docs.iterator())效率最高
            // 增加后通过执行commit函数commit (936ms)
            // server.add(docs);
            // server.commit();

            // 增加doc后立即commit (946ms)
            // UpdateRequest req = new UpdateRequest();
            // req.setAction(ACTION.COMMIT, false, false);
            // req.add(docs);
            // UpdateResponse rsp = req.process(server);

            // the most optimal way of updating all your docs
            // in one http request(432ms)
            server.add(docs.iterator());
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("time elapsed(ms):" + (System.currentTimeMillis() - start));
    }

    public static void delDocs() {
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