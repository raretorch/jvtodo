package jvtodo.View;

import java.util.ArrayList;
import java.util.Collections;

import jvtodo.App;
import jvtodo.DataHandler;
import jvtodo.Handler;
import jvtodo.View.ViewElement.RenderType;

public class View extends Handler {

  public int cursor = 2;
  public String[] StartMessage = new String[]{
    "--------------------------------------------------------------------",
    "---------------------Task Manager by Raretorch----------------------"
  };
  public String[] LastMessage = new String[]{
    "--------------------------------------------------------------------",
    "-------------------------------Actions------------------------------",
    "Add Task: 'a', Remove Task: 'd', Edit Task: 'e', Select Task: 'Space'",
    "Move Cursor: 'h-j-k-l', Yank: 'y', Paste: 'p', Done Task: 'c'"
  };
  public ArrayList<ViewElement> allElements = new ArrayList<ViewElement>();
  public ArrayList<ViewElement> elements = new ArrayList<ViewElement>();
  public ArrayList<ViewElement> addingQueue = new ArrayList<ViewElement>();
  
  @Override
  public void init(){
    System.out.println(this.getClass().getName() + " started.");
  }

  public void makeViewElements(){
    ArrayList<Task> tasks = ((DataHandler)App.getHandler(DataHandler.class)).tasks;
    for(Task task : tasks){
      ViewElement element = new ViewElement(task);
      allElements.add(element);
    }
    for(ViewElement element : allElements){
      if(!element.isText()){
        element.findParent(allElements);
      }
    }
    for(ViewElement element : allElements){
      if(element.isText()){
        elements.add(element);
        continue;
      }
      if(element.getTask().getParentId() == 0){
        elements.add(element);
      }
    }
    allElements.clear();
    Collections.sort(elements);
    for(ViewElement element : elements){
      if(!element.isText()){
        element.sortChilds();
      }
    }
  }

  public enum ActionType{
    NO,
    YES,
    UPDATE,
    MOVE,
    ADD, 
    ADDSUBTASK,
    REMOVE, 
    EDIT, 
    EDIT1, 
    EDIT2,
    SELECT,
    PASTE,
    PASTEFORWARD,
    YANK,
    OPEN, 
    HIDE, 
    DONE;
  }

  public boolean addRootPaste;
  public boolean editTitle;
  public boolean editDescription;
  public boolean waitForInputTitle;
  public boolean waitForInputAskDescription;
  public boolean waitForInputDescription;
  public String getInputTitle(String prompt){
      System.out.print("\033[" + (cursor + StartMessage.length) + "d");
      return System.console().readLine(prompt);
  }

  public String getInputDescription(String prompt){
      System.out.print("\033[" + (cursor + StartMessage.length) + "d");
      return System.console().readLine(prompt);
  }
  
  ViewElement created = new ViewElement(new Task());
  ViewElement removed = new ViewElement(new Task());
  ViewElement buffer = new ViewElement(new Task());

  public ArrayList<ViewElement> createView(ActionType type){
    ArrayList<ViewElement> ve = new ArrayList<ViewElement>();
    if(waitForInputDescription){
      created.yanked = false;
      created.getTask().setDescription(getInputDescription("Description: "));
      ((DataHandler)App.getHandler(DataHandler.class)).save();
      waitForInputDescription = false;
    }
    if(waitForInputAskDescription){
      if(type == ActionType.YES){
        waitForInputDescription = true;
      }
      if(created.parent != null && type == ActionType.NO){
        addingQueue.add(created);
        ((DataHandler)App.getHandler(DataHandler.class)).save();
      }
      waitForInputAskDescription = false;
    }
    if(waitForInputTitle){
      created.getTask().setTitle(getInputTitle("Title: "));
      if(created.parent == null){
        elements.add(created);
        ((DataHandler)App.getHandler(DataHandler.class)).save();
      }
      cursor++;
      waitForInputTitle = false;
      waitForInputAskDescription = true;
    }
    if(addRootPaste){
      elements.add(buffer);
      buffer = buffer.getRecursiveClone(buffer.getTask().getId());
      addRootPaste = false;
    }
    int x = StartMessage.length;
    for(String str : StartMessage){
      x--;
      ve.add(0, new ViewElement(str, x));
    }
    x = StartMessage.length;
    if(removed.parent == null){
      elements.remove(removed);
    } else {
      removed.parent.childs.remove(removed);
      addingQueue.remove(removed);
    }
    ArrayList<ViewElement> buff = new ArrayList<ViewElement>();
    for(ViewElement element : elements){
      buff.add(element);
    }
    elements.addAll(addingQueue);
    if(elements.size() == 0){
      cursor = StartMessage.length;
      elements.add(new ViewElement("No Tasks, press 'a' for create some.", x));
    }
    addingQueue.clear();
    Collections.sort(elements);
    for(ViewElement element : elements){
      element.setQueue(x);
      ve.add(element);
      if(type == ActionType.SELECT && x == cursor){
        if(!element.isText()){
          x++;
          ViewElement b = new ViewElement("Description: " + element.getTask().getDescription(), x);
          b.renderType = RenderType.GRAY;
          b.flops = element.flops;
          ve.add(b);
        }
      }
      if(type == ActionType.DONE && x == cursor){
        if(element.getTask().isDone()){
          element.getTask().setDone(false);
          element.renderType = RenderType.DEFAULT;
          ((DataHandler)App.getHandler(DataHandler.class)).save();
        } else {
          element.getTask().setDone(true);
          element.renderType = RenderType.DONE;
          ((DataHandler)App.getHandler(DataHandler.class)).save();
        }
      }
      if(waitForInputDescription && x == cursor){
        x++;
        ve.add(new ViewElement(" ", x));
      }
      if(waitForInputAskDescription && x == cursor){
        x++;
        ViewElement v = new ViewElement("Write Description? Y/N", x);
        v.flops = element.flops;
        ve.add(v);
      }
      if(type == ActionType.ADD){
        element.isOpen = false;
      }
      if(type == ActionType.ADD && x == cursor && element.parent == null){
        created = new ViewElement(new Task());
        ((DataHandler)App.getHandler(DataHandler.class)).tasks.add(created.getTask());
        ve.add(new ViewElement(" ", x));
        created.getTask().setQueue(x);
        created.getTask().setId(created.getTask().hashCode());
        x++;
        waitForInputTitle = true;
      }
      if(type == ActionType.ADDSUBTASK && x == cursor && !element.isText()){
        created = new ViewElement(new Task());
        ((DataHandler)App.getHandler(DataHandler.class)).tasks.add(created.getTask());
        ve.add(new ViewElement(" ", x));
        created.getTask().setQueue(x);
        created.getTask().setId(created.getTask().hashCode());
        created.getTask().setParentId(element.getTask().getId());
        created.parent = element;
        element.childs.add(created);
        x++;
        waitForInputTitle = true;
      }
      if(type == ActionType.PASTEFORWARD && !element.isText() && x == cursor){
        if(element.parent != null){
          buffer.getTask().setParentId(element.parent.getTask().getId());
          buffer.setQueue(x);
          buffer.parent = element.parent;
          element.parent.childs.add(buffer);
          ((DataHandler)App.getHandler(DataHandler.class)).tasks.add(buffer.getTask());
          ((DataHandler)App.getHandler(DataHandler.class)).save();
          element.parent.sortChilds();
          element.parent.isOpen = true;
          buffer = buffer.getRecursiveClone(buffer.getTask().getParentId());
        } else {
          buffer.getTask().setParentId(0);
          buffer.setQueue(x);
          buffer.parent = null;
          addRootPaste = true;
          buffer = buffer.getRecursiveClone(buffer.getTask().getParentId());
        }
      }
      if(type == ActionType.PASTE && !element.isText() && x == cursor){
        buffer.getTask().setParentId(element.getTask().getId());
        buffer.setQueue(x);
        buffer.parent = element;
        element.childs.add(buffer);
        ((DataHandler)App.getHandler(DataHandler.class)).tasks.add(buffer.getTask());
        ((DataHandler)App.getHandler(DataHandler.class)).save();
        element.sortChilds();
        element.isOpen = true;
        buffer = buffer.getRecursiveClone(buffer.getTask().getParentId());
      }
      if(type == ActionType.YANK){
        element.yanked = false;
        element.renderType = RenderType.DEFAULT;
        element.recursiveYank();
      }
      if(type == ActionType.YANK && x == cursor){
        element.yanked = true;
        if (element == created){
          element.yanked = false;
          element.renderType = RenderType.DEFAULT;
          created = null;
        } else {
          element.renderType = RenderType.YANKED;
        }
        buffer = new ViewElement(new Task());
        if(element.parent != null){
          buffer = element.getRecursiveClone(element.parent.getTask().getId());
        } else {
          buffer = element.getRecursiveClone(element.getTask().getId());
        }
        buffer.setQueue(x);
      }
      if(type == ActionType.REMOVE && x == cursor){
        element.isOpen = false;
        removed = new ViewElement(new Task());
        removed = element;
        removed.recursiveDelete();
        ((DataHandler)App.getHandler(DataHandler.class)).save();
      }
      if(editTitle && x == cursor){
        x++;
        element.getTask().setTitle(getInputTitle("Title: "));
        editTitle = false;
      }
      if(editDescription && x == cursor){
        x++;
        element.getTask().setDescription(getInputTitle("Description: "));
        editDescription = false;
      }
      if(type == ActionType.EDIT1 && x == cursor){
        x++;
        ve.add(new ViewElement(" ", x));
        editTitle = true;
      }
      if(type == ActionType.EDIT2 && x == cursor){
        x++;
        ve.add(new ViewElement(" ", x));
        editDescription = true;
      }
      if(type == ActionType.EDIT && x == cursor){

        ViewElement v = new ViewElement("Edit Title[1]", x);
        v.flops = element.flops;
        addingQueue.add(v);
        x++;
        v = new ViewElement("Edit Description[2]", x);
        v.flops = element.flops;
        addingQueue.add(v);
        x++;
      }
      if(type == ActionType.OPEN && x == cursor){
        element.isOpen = true;
      }
      if(type == ActionType.HIDE && x == cursor){
        element.isOpen = false;
        for(ViewElement e : element.childs){
          e.recursiveClose();
        }
      }
      if(element.isOpen && element.childs.size() > 0){
        int i = x;
        for(ViewElement e : element.childs){
          e.setQueue(i);
          e.recursiveFlop();
          addingQueue.add(e);
        }
      }
      x++;
    }
    elements = buff;
    for(String str : LastMessage){
      ve.add(new ViewElement(str, x));
      x++;
    }
    if(cursor >= ve.size() - (LastMessage.length + 1)){
      cursor = ve.size() - (LastMessage.length + 1);
    }
    if(cursor <= 0 + StartMessage.length){
      cursor = 0 + StartMessage.length;
    }
    return ve;
  }
}
