package jvtodo;


public class ModeHandler {
  
  public enum Mode {
    RAW, COOKED
  }

  public static void setMode(Mode mode) {
    switch (mode) {
      case RAW:
        try {
          String[] cmd = {"/bin/sh", "-c", "stty raw </dev/tty"};
          Runtime.getRuntime().exec(cmd).waitFor();
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      case COOKED:
        try {
          String[] cmd = {"/bin/sh", "-c", "stty cooked </dev/tty"};
          Runtime.getRuntime().exec(cmd).waitFor();
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
    }
    return;
  }
}
