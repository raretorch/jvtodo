package jvtodo.View;

import java.util.ArrayList;
import java.util.Collections;

import jvtodo.App;
import jvtodo.DataHandler;

public class ViewElement implements Comparable<ViewElement>{

  public enum RenderType{
    DEFAULT("DEFAULT"),
    GRAY("GRAY"),
    YANKED("YANKED"),
    HIGHLIGHT("HIGHLIGHT"),
    DONE("DONE");


    public String s;

    RenderType(String s){
      this.s = s;
    }
  }
  public RenderType renderType = RenderType.DEFAULT;
  String text;
  public int flops;
  public int queue;
  public int childQueue;
  Task task;
  public boolean isOpen = false;
  public boolean yanked = false;
  public ViewElement parent;
  public ArrayList<ViewElement> childs = new ArrayList<ViewElement>();

  public ViewElement(String text, int queue){
    this.text = text;
    this.queue = queue;
  }

  public ViewElement(Task task){
    this.task = task;
  }

  public Task getTask(){
    return task;
  }

  public void recursiveClose(){
    isOpen = false;
    for(ViewElement element : childs){
      element.recursiveClose();
    }
  }

  public void recursiveDelete(){
    ((DataHandler)App.getHandler(DataHandler.class)).tasks.remove(getTask());
    for(ViewElement element : childs){
      element.recursiveDelete();
    }
  }

  public void recursiveYank(){
    yanked = false;
    renderType = RenderType.DEFAULT;
    for(ViewElement element : childs){
      element.recursiveYank();
    }
  }

  public int recursiveFlop(){
    int flop = 0;
    if(!isText()){
      if(parent != null){
        flop++;
        flops = flop + parent.recursiveFlop();
      }
    }
    return flops;
  }

  @Override
  public int compareTo(ViewElement o){
    if (this.isText()) {
      if (o.isText()) {
        return Integer.valueOf(queue).compareTo(o.queue);
      } else {
        return Integer.valueOf(queue).compareTo(o.getTask().getQueue());
      }
    } else {
      if (o.isText()) {
        return Integer.valueOf(task.getQueue()).compareTo(o.queue);
      } else {
        return Integer.valueOf(task.getQueue()).compareTo(o.getTask().getQueue());
      }
    }
  }

  public void sortChilds(){
    Collections.sort(childs);
    for(ViewElement element : childs){
      element.sortChilds();
    }
  }

  public boolean findParent(ArrayList<ViewElement> elements){
    for(ViewElement element : elements){
      if(!element.isText()){
        if(element.getTask().getId() == task.getParentId()){
          element.childs.add(this);
          parent = element;
          return true;
        }
      }
    }
    return false;
  }

  public boolean isText(){
    if(text != null){
      return true;
    } else {
      return false;
    }
  }

  public String getText(){
    return text;
  }

  public void setText(String text){
    this.text = text;
  }

  public ViewElement getRecursiveClone(int objectId){
    ViewElement clone = new ViewElement(new Task());
    if(isText()) {clone.text = new String(text);}
    else {
      clone.setQueue(queue);
      clone.parent = parent;
      clone.getTask().setDescription(new String(task.getDescription()));
      clone.getTask().setTitle(new String(task.getTitle()));
      clone.getTask().setQueue(task.getQueue());
      clone.getTask().setDone(task.isDone());
      if(parent != null){clone.getTask().setParentId(objectId);}
      else {clone.getTask().setParentId(0);}
      clone.childs = new ArrayList<ViewElement>();
      for(ViewElement element : childs){
        clone.childs.add(element.getRecursiveClone(clone.getTask().getId()));
      }
    }
    return clone;
  }

  @Override
  public String toString(){
    if(isText()){
      return "[-]" + "[" + renderType.s + "]" + text;
    } else {
      return "[" + childs.size() + "]" + "[" + renderType.s + "]" + task.toString();
    }
  }

  public int getQueue() {
    if(isText()){
      return queue;
    } else {
      return task.getQueue();
    }
  }

  public void setQueue(int queue) {
    if(isText()){
      this.queue = queue;
    } else {
      task.setQueue(queue);
    }
  }
}
