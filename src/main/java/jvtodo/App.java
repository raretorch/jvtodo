package jvtodo;

import jvtodo.ModeHandler.Mode;
import jvtodo.View.View;

public class App {

  static Handler handlers[] = {
    new Render(),
    new DataHandler(),
    new View(),
    new InputHandler()
  };


  public static void main( String[] args ) {
    // Initialize handlers
    for (Handler handler : handlers) {
      handler.init();
    }
    ((DataHandler)getHandler(DataHandler.class)).load();
    ((View)getHandler(View.class)).makeViewElements();
    ((InputHandler)getHandler(InputHandler.class)).start();
  }

  public static Handler getHandler(Class<?> c){
    for (Handler handler : handlers) {
      if (handler.getClass().equals(c)) {
        return handler;
      }
    }
    return null;
  }

  public static void exitApp() {
    ModeHandler.setMode(Mode.COOKED);
    for (Handler handler : handlers) {
      handler.exit();
    }
    System.out.println("jvtodo.App exiting...");
    System.exit(0);
  }
}
