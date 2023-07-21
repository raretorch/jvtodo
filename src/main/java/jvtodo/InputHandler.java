package jvtodo;

import java.io.Console;

import jvtodo.ModeHandler.Mode;
import jvtodo.View.View;
import jvtodo.View.View.ActionType;

public class InputHandler extends Handler implements Runnable{

  static Console console = System.console();
  static Render render;
  static View view;
  static Thread t;
  public void init() {
    System.out.println(this.getClass().getName() + " started.");
    render = (Render)App.getHandler(Render.class);
    view = (View)App.getHandler(View.class);
  }

  public void start(){
    t = new Thread(this);
    t.start();
  }

  public void exit() {
    System.out.println(this.getClass().getName() + " stopped.");
  }
  
  @Override
  public void run(){
    ModeHandler.setMode(Mode.COOKED);
    render.render(view.createView(ActionType.MOVE));
    while(true){
      ModeHandler.setMode(Mode.RAW);
      String input = console.readLine();
      ModeHandler.setMode(Mode.COOKED);
      if (input.equals("k")) {
        view.cursor--;
        render.render(view.createView(ActionType.MOVE));
        continue;
      }
      if (input.equals("y")){
        render.render(view.createView(ActionType.YES));
        render.render(view.createView(ActionType.UPDATE));
      }
      if (input.equals("n")){
        render.render(view.createView(ActionType.UPDATE));
      }
      if(input.equals("a")){
        render.render(view.createView(ActionType.ADD));
        render.render(view.createView(ActionType.UPDATE));
      }
      if(input.equals("d")){
        render.render(view.createView(ActionType.HIDE));
        render.render(view.createView(ActionType.REMOVE));
        render.render(view.createView(ActionType.UPDATE));
      }
      if(input.equals("A")){
        render.render(view.createView(ActionType.OPEN));
        render.render(view.createView(ActionType.ADDSUBTASK));
        render.render(view.createView(ActionType.UPDATE));
      }
      if(input.equals(" ")){
        render.render(view.createView(ActionType.SELECT));
      }
      if(input.equals("l")){
        render.render(view.createView(ActionType.OPEN));
        render.render(view.createView(ActionType.UPDATE));
        continue;
      }
      if(input.equals("e")){
        render.render(view.createView(ActionType.EDIT));
        render.render(view.createView(ActionType.UPDATE));
        continue;
      }
      if(input.equals("1")){
        render.render(view.createView(ActionType.EDIT1));
        render.render(view.createView(ActionType.UPDATE));
        continue;
      }
      if(input.equals("2")){
        render.render(view.createView(ActionType.EDIT2));
        render.render(view.createView(ActionType.UPDATE));
        continue;
      }
      if(input.equals("h")){
        render.render(view.createView(ActionType.HIDE));
        render.render(view.createView(ActionType.UPDATE));
        continue;
      }
      if (input.equals("j")) {
        view.cursor++;
        render.render(view.createView(ActionType.MOVE));
        continue;
      }
      if (input.equals("y")) {
        render.render(view.createView(ActionType.YANK));
        render.render(view.createView(ActionType.UPDATE));
        continue;
      }
      if (input.equals("p")) {
        render.render(view.createView(ActionType.PASTE));
        render.render(view.createView(ActionType.UPDATE));
        continue;
      }
      if (input.equals("P")) {
        render.render(view.createView(ActionType.PASTEFORWARD));
        render.render(view.createView(ActionType.UPDATE));
        render.render(view.createView(ActionType.UPDATE));
        continue;
      }
      if (input.equals("c")) {
        render.render(view.createView(ActionType.DONE));
        render.render(view.createView(ActionType.UPDATE));
        continue;
      }
      if(input.equals("s")){
        ((DataHandler)App.getHandler(DataHandler.class)).save();
      }
      if (input.equals("q")) {
        ((DataHandler)App.getHandler(DataHandler.class)).save();
        break;
      }
    }
    App.exitApp();
    return;
  }
  
}
