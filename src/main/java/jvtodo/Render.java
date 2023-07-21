package jvtodo;

import java.util.ArrayList;

import jvtodo.View.View;
import jvtodo.View.ViewElement;
import jvtodo.View.ViewElement.RenderType;

public class Render extends Handler{

  static View view;

  @Override
  public void init(){
    System.out.println(this.getClass().getName() + " started.");
    view = (View)App.getHandler(View.class);
  }

  public void render(ArrayList<ViewElement> view){
    System.out.print("\033[H\033[J");
    for(ViewElement element : view){
      ViewElement.RenderType renderType = element.renderType;
      if(element.getQueue() == Render.view.cursor){
        colorThis(RenderType.HIGHLIGHT);
      }
      colorThis(renderType);
      System.out.print(concatSpaces(element.flops, "   "));
      if(!element.isText()){
        System.out.print("● ");
        for(ViewElement child : element.childs){
          System.out.print("|");
        }
        System.out.print(" ");
        if(element.getTask().isDone()){
          System.out.print("√ ");
        }
        System.out.print(element.getTask().getTitle());
        if(element.childs.size() > 0){
          if(element.isOpen){
            System.out.print(" ▼");
          } else {
            System.out.print(" ▶");
          }
        }
      } else {
        System.out.print(element.getText());
      }
      System.out.println();
      uncolorThis();
    }
  }

  void colorThis(ViewElement.RenderType renderType){
    switch(renderType){
      case YANKED:
        System.out.print("\033[38;5;208m");
        break;
      case DONE:
        System.out.print("\033[38;5;82m");
        break;
      case GRAY:
        System.out.print("\033[38;5;243m");
        break;
      case HIGHLIGHT:
        System.out.print("\033[48;5;242m");
        break;
      case DEFAULT:
        System.out.print("\033[37m");
        break;
    }
  }

  void uncolorThis(){
    System.out.print("\033[0m");
  }

  String concatSpaces(int count, String str){
    String spaces = "";
    for(int i = 0; i < count; i++){
      spaces += str;
    }
    return spaces;
  }
}
