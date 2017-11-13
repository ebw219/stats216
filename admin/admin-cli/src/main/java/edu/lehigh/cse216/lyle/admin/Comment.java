package edu.lehigh.cse216.lyle.admin;

public class Comment{
    int commentId;
    int userId;
    int messageId;
    String body;

    public Comment(int commentId, int userId, int messageId, String body) {
        this.commentId = commentId;
        this.userId = userId;
        this.messageId = messageId;
        this.body = body;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return  (commentId + "\t"+ userId +"\t" + messageId + "\t" + body);
    }
}