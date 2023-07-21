package jvtodo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Loader{
  
  private File file;

  public Loader(File file) {
    this.file = file;
  }

  public ArrayList<String> getData() {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(file);
      doc.getDocumentElement().normalize();
      NodeList nodes = doc.getElementsByTagName("task");
      ArrayList<String> data = new ArrayList<String>();
      for (int i = 0; i < nodes.getLength(); i++) {
        Node node = nodes.item(i);
        if(node.getNodeType() == Node.ELEMENT_NODE) {
          Element element = (Element) node;
          data.add(element.getElementsByTagName("id").item(0).getTextContent());
          data.add(element.getElementsByTagName("parentId").item(0).getTextContent());
          data.add(element.getElementsByTagName("queue").item(0).getTextContent());
          data.add(element.getElementsByTagName("done").item(0).getTextContent());
          data.add(element.getElementsByTagName("title").item(0).getTextContent());
          data.add(element.getElementsByTagName("description").item(0).getTextContent());
        }
      }
      return data;
    } catch (IOException | SAXException | ParserConfigurationException e) {
       System.out.println("Invalid task file: " + file.getName());
       System.out.println("Trying to delete...");
       file.delete();
       System.out.println("Deleted");
    }
    return null;
  }
}
