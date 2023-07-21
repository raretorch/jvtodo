package jvtodo;

public abstract class Handler{

  public void init(){
    System.out.println(this.getClass().getName() + " started.");
  }

  public void exit(){
    System.out.println(this.getClass().getName() + " stopped.");
  }

}
