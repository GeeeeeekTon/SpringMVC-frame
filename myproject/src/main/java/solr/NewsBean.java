package solr;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

class NewsBean {
    @Field
    private String id;

    @Field
    private String name;

    @Field
    private String author;

    @Field
    private String description;

    @Field("links")
    private List<String> relatedLinks;

    public NewsBean() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRelatedLinks() {
        return relatedLinks;
    }

    public void setRelatedLinks(List<String> relatedLinks) {
        this.relatedLinks = relatedLinks;
    }
}