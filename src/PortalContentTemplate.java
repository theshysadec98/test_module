import java.io.Serializable;
import java.util.*;

public class PortalContentTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String description;
    private Date lastUpdateTime;

    private String tags;


    private Set<PortalContentTemplateList> portalContentTemplateList = new LinkedHashSet<>();

    public List<String> getTags() {
        return tags != null ? Arrays.asList(tags.split(",")) : new ArrayList<>();
    }

    public void setTags(List<String> tags) {
        this.tags = String.join(",", tags);
    }

    public PortalContentTemplate() {
    }

    public PortalContentTemplate(String id, String name, String description, Date lastUpdateTime, String tags, Set<PortalContentTemplateList> portalContentTemplateList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lastUpdateTime = lastUpdateTime;
        this.tags = tags;
        this.portalContentTemplateList = portalContentTemplateList;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Set<PortalContentTemplateList> getPortalContentTemplateList() {
        return portalContentTemplateList;
    }

    public void setPortalContentTemplateList(Set<PortalContentTemplateList> portalContentTemplateList) {
        this.portalContentTemplateList = portalContentTemplateList;
    }
}
