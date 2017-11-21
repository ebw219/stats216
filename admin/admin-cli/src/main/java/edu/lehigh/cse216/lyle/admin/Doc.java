package edu.lehigh.cse216.lyle.admin;

public class Doc{
    private int ownerId;
    private int docId;
    private String title;

    public Doc(int ownerId, int docId, String title) {
        this.ownerId = ownerId;
        this.docId = docId;
        this.title = title;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return  (ownerId + "\t"+ docId +"\t" + title);
    }
}
