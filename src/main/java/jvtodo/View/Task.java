package jvtodo.View;

public class Task {
  
  private int id = this.hashCode();
  private int parentId = 0;
  private int queue;
  private boolean done;
  private String title = "";
  private String description = "";

  @Override
  public String toString() {
    return "Task{" +
        "id=" + id +
        ", parentId=" + parentId +
        ", done=" + done +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        '}';
  }

  public String getTitle() {
    return title;
  }

  public int getQueue() {
    return queue;
  }

  public int getId() {
    return id;
  }

  public int getParentId() {
    return parentId;
  }

  public String getDescription() {
    return description;
  }

  public boolean isDone() {
    return done;
  }

  public void setId(int id) {
    if(id == 0){
      return;
    }
    this.id = id;
  }

  public void setQueue(int queue) {
    this.queue = queue;
  }
  
  public void setParentId(int parentId) {
    this.parentId = parentId;
  }

  public void setDone(boolean done) {
    this.done = done;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
