import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Scanner;

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
    
    // Handle menu inputs
    /*Scanner sc = new Scanner(System.in);
    System.out.println("Continue?[Y/N]");
    while (sc.hasNext() && (sc.nextLine().equalsIgnoreCase("y"))) {
      
      System.out.println("Enter first name");
      String name = sc.nextLine();
      System.out.println("Enter surname");
      String surname = sc.nextLine();
      System.out.println("Enter version number (" + world.getLatestVersion() + " is currently the latest)");
      int number = 0;
      try {
	  number = Integer.parseInt(sc.nextLine());
      } catch (IllegalArgumentException e) {
	  e.printStackTrace();
      }
      System.out.println("Continue?[Y/N]");
    }*/
    
    world.incrementLatestVersion();
    
    System.out.println(world.getNumberOfInfections());
    world.totalInfection(world.getUser("stacey"));
    System.out.println(world.getNumberOfInfections());
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
	  version = Integer.parseInt(versionNode.getNodeValue());
	  System.out.println("Version: " + versionNode.getNodeValue());
	  
	  // Now that we have the basic info for this user, check if he/she exists already
	  User current = checkUser(name, userObjects);
	  current.setVersion(version);
	  
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
	      
	      // Check if this user already exists. If so, add the existing user to students list
	      // Otherwise, create a new user and add to students list
	      User student = checkUser(studentNode.getNodeValue().toLowerCase(), userObjects);
	      current.addStudent(student);
	      student.addCoach(current);
	    }
	    System.out.println();
	  }
	 
	  // You may notice we didn't grab coaches;
	  // This is because of the undirected relationship between users:
	    // That is, if a user has a student, then that student's coach is that user.
	    // We set both at the same time.
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
    
    System.out.println(userObjects.size() + " Users Created.");
    return userObjects;
  }
  
  /*
   * This will either return the user that exists in the map,
   * or return a new user with String name after adding it to the map
   */
  public static User checkUser(String name, HashMap<String, User> users) {
    name = name.toLowerCase();
    boolean exists = users.containsKey(name);
    if(exists)
      return users.get(name);
      
    //Else, create a new user object and add to HashMap
    User newUser = new User();
    users.put(name, newUser);
    return newUser;
  }
}