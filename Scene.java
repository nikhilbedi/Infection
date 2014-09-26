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
    // We need four arguments for full customization
    // One for the filename of users
    // The other for which user to target
    // The third for either "total" or "limit"
    // The final for how many infections to create (only useful for limit)
    // Default is "default.xml total nikhil 0"
  
    String filename = "tests/default.xml";
    String user = "nikhil";
    String command = "total";
    int maxInfections = 0;
    try {
      filename = args[0];
      user = args[1].toLowerCase();
      command = args[2];
      maxInfections = Integer.parseInt(args[3]);
    }
    catch(ArrayIndexOutOfBoundsException e) { }
    catch(NumberFormatException e) {
      System.out.println("Please use a number for your fourth argument.");
      maxInfections = 0;
    }
    
    // Begin creating the world.
    World world = World.getInstance();
    
    // Everything naturally begins at 0
    // Most users are at 0 for their version. For the purposes of infection,
    // Some users will be at 1 to signify they are infected.
    // The world begins at version 0, so signify what should be the infected 
    // State, increment the latest version to 1.
    world.incrementLatestVersion(); // should now be 1.
    world.setUsersList(parseXmlForUsers(filename));
    
    System.out.println("Before any functions are run, the world consists of " + world.getNumberOfInfections() + " infection(s)");
    
    // Calculate the infections! >:D
    if(command.equalsIgnoreCase("limit"))
      world.limitInfection(world.getUser(user.toLowerCase()), maxInfections); 
    
    // default to total if the user did not offer a command/ offered an 
    // incorrect command
    else
      world.totalInfection(world.getUser(user.toLowerCase()));
      
    System.out.println("After using the " + command + " function on " + user + ", the world now consists of " + world.getNumberOfInfections() + " infection(s)");
    
    world.printInfected();
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
    User newUser = new User(name);
    users.put(name, newUser);
    return newUser;
  }
}