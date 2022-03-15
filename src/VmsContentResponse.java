public class VmsContentResponse {
    private Long id;
    private String brightnessMode;
    private String displayMode;
    private String contentType;
    private String data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrightnessMode() {
        return brightnessMode;
    }

    public void setBrightnessMode(String brightnessMode) {
        this.brightnessMode = brightnessMode;
    }

    public String getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }

    public VmsContentResponse() {
    }

    public VmsContentResponse(Long id, String brightnessMode, String displayMode, String contentType, String data, String imageURI) {
        this.id = id;
        this.brightnessMode = brightnessMode;
        this.displayMode = displayMode;
        this.contentType = contentType;
        this.data = data;
        this.imageURI = imageURI;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    private String imageURI;

    private enum BrightnessEnum {
        AUTOMATED,
        NIGHT,
        DAY,
        HIGHLIGHTED
    }

    public enum DisplayModeEnum {
        OFF,
        FIXED,
        ALTERNATE,
        BLINKING,
        NEUTRAL
    }
}
