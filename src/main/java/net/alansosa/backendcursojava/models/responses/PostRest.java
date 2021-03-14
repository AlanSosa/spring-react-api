package net.alansosa.backendcursojava.models.responses;

import net.alansosa.backendcursojava.models.shared.dto.ExposureDto;
import net.alansosa.backendcursojava.models.shared.dto.UserDto;

import java.util.Date;

public class PostRest {

    private String postId;
    private String title;
    private String content;
    private Date expiredAt;
    private Date createdAt;
    private UserRest user;
    private ExposureRest exposure;
    private boolean expired;

    public boolean isExpired() {
        //Validate if the post has expired
        this.expired = this.getExpiredAt().compareTo(new Date(System.currentTimeMillis())) < 0;
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UserRest getUser() {
        return user;
    }

    public void setUser(UserRest user) {
        this.user = user;
    }

    public ExposureRest getExposure() {
        return exposure;
    }

    public void setExposure(ExposureRest exposure) {
        this.exposure = exposure;
    }
}
