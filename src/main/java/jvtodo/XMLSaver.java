package jvtodo;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import jvtodo.View.Task;

public class XMLSaver {

  public Task task;

  public XMLSaver(Task task) {
    this.task = task;
  }

  public void saveTask(File directory) {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();

      Element taskElement = document.createElement("task");
      Element id = document.createElement("id");
      Text idText = document.createTextNode(String.valueOf(task.getId()));
      Element parentId = document.createElement("parentId");
      Text parentIdText = document.createTextNode(String.valueOf(task.getParentId()));
      Element queue = document.createElement("queue");
      Text queueText = document.createTextNode(String.valueOf(task.getQueue()));
      Element done = document.createElement("done");
      Text doneText = document.createTextNode(String.valueOf(task.isDone()));
      Element title = document.createElement("title");
      Text titleText = document.createTextNode(String.valueOf(task.getTitle()));
      Element description = document.createElement("description");
      Text descriptionText = document.createTextNode(String.valueOf(task.getDescription()));

      document.appendChild(taskElement);
      taskElement.appendChild(id);
      taskElement.appendChild(parentId);
      taskElement.appendChild(queue);
      taskElement.appendChild(done);
      taskElement.appendChild(title);
      taskElement.appendChild(description);
      id.appendChild(idText);
      parentId.appendChild(parentIdText);
      queue.appendChild(queueText);
      done.appendChild(doneText);
      title.appendChild(titleText);
      description.appendChild(descriptionText);

      Transformer trans = TransformerFactory.newInstance().newTransformer();
      trans.transform(new DOMSource(document), new StreamResult(new File(directory.getAbsolutePath() + "/" + String.valueOf(task.getId()) + ".xml")));
      System.out.println("Saved to XML : " + task.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
