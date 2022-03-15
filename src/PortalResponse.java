public class PortalResponse {
    private String vmsId;
    private VmsContentResponse vmsContent;

    public String getVmsId() {
        return vmsId;
    }

    public PortalResponse(String vmsId, VmsContentResponse vmsContent) {
        this.vmsId = vmsId;
        this.vmsContent = vmsContent;
    }

    public void setVmsId(String vmsId) {
        this.vmsId = vmsId;
    }

    public PortalResponse() {
    }

    public VmsContentResponse getVmsContent() {
        return vmsContent;
    }

    public void setVmsContent(VmsContentResponse vmsContent) {
        this.vmsContent = vmsContent;
    }
}
