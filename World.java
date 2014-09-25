import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ArrayList;

/* Singleton Design */
public class World {

  /* Self-reference object */
  private static World _instance = null;
  
  /* private variables */
  
  /* 
   * holds the number of infected users 
   */
  private int numInfections;
  
  /* 
   * the latest version KA has put out. 
   * This will determine which users are considered infected 
   */
  private int latest_version;
  
  /* Essentially, a list of users in the world */
  private Set<User> users;

  /* Constructor
   * Initializes World by allocating lists.
   */
  private World() {
    users = new HashSet<User>();
    numInfections = 0;
  }

  public static World getInstance() {
    if(_instance == null)
      _instance = new World();

    return _instance;
  }

  public void setUsersList(Set<User> users) {
    _instance.users = users;
  }
  
  public boolean addUser(User user) {
    return _instance.users.add(user);
  }
  
  public int getNumberOfInfections() {
    return _instance.numInfections;
  }
  
  /*
   * Total infection infects all users adjacent to the passed in User.
   * Adjacent, in this case, means is a student or coach.
   * For every newly infected user, repeat the same process from the first line.
   * At a high level, this is implemented as a Breadth-First Traversal.
  */
  public void totalInfection(User user) {
    if(user == null)
      return;

    LinkedList<User> usersToBeInfected = new LinkedList<User>();
    usersToBeInfected.add(user);
    
    while(!usersToBeInfected.isEmpty()){
      User newlyInfected = usersToBeInfected.pop();
      newlyInfected.setVersion(latest_version);
      
      // Add students and coaches to the list
      // in each case, if the user's version is less than the latest version,
      // then we add them to the list. Otherwise, they've already been infected.
      ArrayList<User> students = newlyInfected.getStudents();
      for(int i = 0; i < students.size(); i++) {
	if(students.get(i).getVersion() < latest_version) {
	  usersToBeInfected.addLast(students.get(i));
	}
      }
      ArrayList<User> coaches = newlyInfected.getCoaches();
      for(int i = 0; i < coaches.size(); i++) {
	if(coaches.get(i).getVersion() < latest_version) {
	  usersToBeInfected.addLast(coaches.get(i));
	}
      }
    }
  }
  
  public boolean removeUser(User user) {
    // Not implemented
    return true;
  }
  
  public int getNumberOfUsers() {
    return _instance.users.size();
  }
}