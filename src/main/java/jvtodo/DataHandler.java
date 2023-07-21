package jvtodo;

import java.io.File;
import java.util.ArrayList;

import jvtodo.ModeHandler.Mode;
import jvtodo.View.Task;

public class DataHandler extends Handler{

  File rootDir;
  public ArrayList<Task> tasks;

  public void init(){
    System.out.println(this.getClass().getName() + " started.");
    rootDir = new File("/Users/raretorch/jvtodo-tasks");
    if (!rootDir.exists()) {
      System.out.println("Creating " + rootDir.getName() + "...");
      rootDir.mkdir();
    }
  }

  public void load(){
    ModeHandler.setMode(Mode.COOKED);
    File[] taskBlobs = rootDir.listFiles();
    System.out.println("Finded " + taskBlobs.length + " files...");
    tasks = new ArrayList<Task>();
    for (File taskBlob : taskBlobs) {
      if (!taskBlob.getName().startsWith(".")){
        ArrayList<String> data = new Loader(taskBlob).getData();
        if(data == null){
          continue;
        }
        Task task = new Task();
        task.setId(Integer.parseInt(data.get(0)));
        task.setParentId(Integer.parseInt(data.get(1)));
        task.setQueue(Integer.parseInt(data.get(2)));
        task.setDone(Boolean.parseBoolean(data.get(3)));
        task.setTitle(data.get(4));
        task.setDescription(data.get(5));
        tasks.add(task);
        System.out.println("Loaded " + task.toString());
        System.out.println(taskBlob.getName());
      }
    }
    System.out.println("Loaded " + tasks.size() + " tasks.");
    ModeHandler.setMode(Mode.RAW);
  }

  void clearData(){
    File[] taskBlobs = rootDir.listFiles();
    System.out.println("Finded " + taskBlobs.length + " files...");
    for (File taskBlob : taskBlobs) {
      taskBlob.delete();
    }
  }

  public void save(){
    clearData();
    for(Task task : tasks){
      XMLSaver saver = new XMLSaver(task);
      saver.saveTask(rootDir);
    }
  }

  public void save(Task task){
    XMLSaver saver = new XMLSaver(task);
    saver.saveTask(rootDir);
  }

}
