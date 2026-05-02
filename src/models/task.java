
package models;


public class task {
    
    private int id ;
    private String title;
    private String status;
    private String addedBy;
    private String creationDate;

    public task(int id, String title, String status, String addedBy, String creationDate) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.addedBy = addedBy;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
    
     @Override
    public String toString() {
        return id + "  " + title + "  " + " by "+ addedBy + "  " + " [" + creationDate + "]" + "  " + " (" + status + ")" ;
    }
}
