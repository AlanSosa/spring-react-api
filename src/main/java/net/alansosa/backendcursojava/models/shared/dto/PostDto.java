package net.alansosa.backendcursojava.models.shared.dto;

import java.io.Serializable;
import java.util.Date;

public class PostDto implements Serializable {

    private static final long serialversionUID = 1l;
    private long id;
    private String postId;
    private String title;
    private String content;
    private Date expiredAt;
    private Date createdAt;
    private UserDto user;
    private ExposureDto exposure;

    public static long getSerialversionUID() {
        return serialversionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public ExposureDto getExposure() {
        return exposure;
    }

    public void setExposure(ExposureDto exposure) {
        this.exposure = exposure;
    }
}
