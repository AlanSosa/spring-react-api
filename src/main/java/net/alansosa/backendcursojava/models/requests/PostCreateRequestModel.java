package net.alansosa.backendcursojava.models.requests;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PostCreateRequestModel {

    @NotEmpty(message = "title is a required field")
    private String title;

    @NotEmpty(message = "content is a required field")
    private String content;

    //Here we're validing a long so we use @NotNull
    @NotNull(message = "exposureId is a required field")
    @Range(min = 1, max = 2, message = "exposureId is invalid")
    private long exposureId;

    @NotNull(message = "expirationTime is a required field")
    @Range(min = 0, max = 1440, message = "expirationTime is invalid")
    private int expirationTime;

    @Override
    public String toString() {
        return "PostCreateRequestModel{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", exposureId=" + exposureId +
                ", expirationTime=" + expirationTime +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getExposureId() {
        return exposureId;
    }

    public void setExposureId(long exposureId) {
        this.exposureId = exposureId;
    }

    public int getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(int expirationTime) {
        this.expirationTime = expirationTime;
    }
}
