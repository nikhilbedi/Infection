import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/*
 * A class to help run various tests
 */
public class Scene {
  
  public static void main(String args[]) {
    String filename;
    try {
      filename = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e) {
      System.out.println("No arguments were provided! Exiting the program.");
      return;
    }
    
    // Begin creating the world.
    World world = World.getInstance();
    world.setUsersList(parseXmlForUsers(filename));
    
  }
  
  public static HashMap<String, User> parseXmlForUsers(String filename) {
    HashMap<String, User> userObjects = new HashMap<String, User>();
    
    // Try to open a file from args. Otherwise quit the program
    try {
      // Set up file
      File file = new File(filename);
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(file);
      doc.getDocumentElement().normalize();
      
      // Grab data
      NodeList users = doc.getElementsByTagName("user");
      for(int i = 0; i < users.getLength(); i++) {
	Node user = users.item(i);
	if(user.getNodeType() == Node.ELEMENT_NODE) {
	  // Variables to create the user
	  String name;
	  int version;
	  HashSet<User> students = new HashSet<User>();
	
	  // Create an Element to make it easier to grab information
	  Element userElement = (Element) user;
	  
	  // Grab the unique string identifer (i.e. name)
	  NodeList namesList = userElement.getElementsByTagName("name");
	  Element nameElement = (Element) namesList.item(0);
	  Node nameNode = (Node) nameElement.getChildNodes().item(0);
	  name = nameNode.getNodeValue();
	  System.out.println("Name: " + nameNode.getNodeValue());
	  
	  // Grab the version this user has (if it is >= latest_version, this user is infected!)
	  NodeList versionsList = userElement.getElementsByTagName("version");
	  Element versionElement = (Element) versionsList.item(0);
	  Node versionNode = (Node) versionElement.getChildNodes().item(0);
	  System.out.println("Version: " + versionNode.getNodeValue());
	  
	  // Now that we have the basic info for this user, check if he/she exists already
	  User newUserObject = checkUser(name, userObjects);
	  
	  // Grab all this user's students
	  NodeList studentNodesList = userElement.getElementsByTagName("students");
	  // There will be some xml nodes without students, so we should account for that
	  // with this if statement
	  if(studentNodesList.getLength() > 0) {
	    Element studentsListElement = (Element) studentNodesList.item(0);
	    NodeList actualStudentNodesList = studentsListElement.getElementsByTagName("student");
	    System.out.print("Students: ");
	    for(int j = 0; j < actualStudentNodesList.getLength(); j++) {
	      Element studentElement = (Element) actualStudentNodesList.item(j);
	      Node studentNode = (Node) studentElement.getChildNodes().item(0);
	      System.out.print(studentNode.getNodeValue() + " ");
	      // Check if this user already exists. If so, add the existing user
	      // Otherwise, add a new user
	    }
	    System.out.println();
	  }
	 
	  // You may notice we didn't grab coaches;
	  // This is because the undirected relationship between users:
	    // That is, if a user has a student, then that student's coach is that user.
	    // We set both at the same time.
	    
	  // Now we have enough information to create a user (or many users, if students do not yet exist)
	//  String name = nameNode.getNodeValue();
	//  int version = Integer.parseInt(versionNode.getNodeValue());
	  
	}
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    catch (SAXException e) {
      e.printStackTrace();
    }
    catch (ParserConfigurationException e) {
      e.printStackTrace();
    }
    
    return userObjects;
  }
  
  /*
   * This will either return the user that exists in the set,
   * or return a new user with String name after adding it to the set
   */
  public static User checkUser(String name, HashMap<String, User> users) {
    //boolean exists = users.contains(new User
    return null;
  }
}