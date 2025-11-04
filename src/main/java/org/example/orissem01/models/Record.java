package org.example.orissem01.models;

public class Record {
    private Long id;
    private User user;
    private Slot slot;
    private Integer chatsCount;
    private String status;
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Integer getChatsCount() {
        return chatsCount;
    }

    public void setChatsCount(Integer chatsCount) {
        this.chatsCount = chatsCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", user=" + user +
                ", slot=" + slot +
                ", chatsCount=" + chatsCount +
                ", status='" + status + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
