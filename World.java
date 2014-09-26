import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.HashSet;

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
  
  /* 
   * Essentially, a list of users in the world that are 
   * mapped by their name/username
   */
  private Map<String, User> users;

  /* Constructor
   * Initializes World by allocating lists.
   */
  private World() {
    users = new HashMap<String, User>();
    numInfections = 0;
    latest_version = 0;
  }

  public static World getInstance() {
    if(_instance == null)
      _instance = new World();

    return _instance;
  }

  public void setUsersList(Map<String, User> users) {
    this.users = users;
    
    //initialize numInfections
    for(String s : users.keySet()) {
      if(users.get(s).getVersion() == getLatestVersion()) {
	numInfections++;
	for(User coach : users.get(s).getCoaches()) {
	  coach.incrementInfectedStudents();
	}
      }
      // A user should not be allowed to have a higher version than
      // what the world considers to be the highest version. Infect
      // this user and reset his/her version
      else if(users.get(s).getVersion() > getLatestVersion()) {
	users.get(s).setVersion(getLatestVersion());
	numInfections++;
	for(User coach : users.get(s).getCoaches()) {
	  coach.incrementInfectedStudents();
	}
      }
    }
  }
  
  public User putUser(String name, User user) {
    return users.put(name, user);
  }
  
  public int getNumberOfInfections() {
    return numInfections;
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

    LinkedList<User> usersInfected = new LinkedList<User>();
    usersInfected.add(user);
    
    //Infect user here if not previously infected
    tryInfect(user);
    
    // Check for other users that have been infected already,
    // and add them to the usersInfected list
    for(String s : users.keySet()) {
      if(users.get(s).getVersion() == getLatestVersion()) {
	if(users.get(s) != user)
	  usersInfected.add(users.get(s));
      }
    }
    
    while(!usersInfected.isEmpty()){
      User newlyInfected = usersInfected.pop();
      
      // Add students and coaches to the list
      // in each case, if the user's version is less than the latest version,
      // then we add them to the list. Otherwise, they've already been infected.
      HashSet<User> students = (HashSet) newlyInfected.getStudents();
      for(User student : students) {
	if(tryInfect(student))
	  usersInfected.addLast(student);
      }
      HashSet<User> coaches = (HashSet) newlyInfected.getCoaches();
      for(User coach : coaches) {
	if(tryInfect(coach))
	  usersInfected.addLast(coach);
      }
    }
  }
  
  public void limitInfection(User user, int maxInfections) {
    // Check base cases before attempting expensive iterations
    
    // return if there are already enough infections in the world that satisfy 
    // the parameter
    if(numInfections >= maxInfections)
      return;
      
    // If maxInfections > users.size(), simply infect all users and return.
    if(maxInfections >= users.size()) {
      for(User u : users.values()) {
	tryInfect(u);
      }
      return;
    }
      
    // infect passed-in user if user is not already infected
    tryInfect(user);
     
    while(numInfections < maxInfections) {

      // Find coach with greatest number of infected students
	// infect all students of found coach and infect coach
      User coach = getCoachWithMostInfected();	// This gets an UNINFECTED coach, with most INFECTED students
      if(coach != null) {
	tryInfect(coach);
	for(User student : coach.getStudents()) {
	  tryInfect(student);
	}
      }
      // If no coach is found, break out of loop
      else {
	break;
      }
    }
    
    // begin infecting users until max is reached
    for(User u : users.values()) {
      if(numInfections >= maxInfections)
	break;
      else
	tryInfect(u);
    }
  }
  
  public User getUser(String name) {
    return users.get(name.toLowerCase());
  }
  
  public int getNumberOfUsers() {
    return users.size();
  }
  
  public int getLatestVersion() {
    return latest_version;
  }
  
  /*
   * A version can only be updated. Doing so "uninfects" every user
   * because no user should now have the latest version.
   * NOTE: a way to break this logic is if we had a user with a greater
   * version than the latest_version originally, but I will not account 
   * for that case here.
   */
  public void incrementLatestVersion() {
    // increment
    latest_version++;
      
    // Reset infections
    numInfections = 0;
    
    // Reset number of infected students for every coach
    for(User user : users.values()) {
      user.setNumInfectedStudents(0);
    }
  }
  
  public User getCoachWithMostInfected() {
    // first, filter out all coaches that are infected - O(n)
    // Whenever an uninfected coach is found,
      // find what number of students of his/hers are infected - O(m)
    User bestCoach = null;
    int mostStudentsInfected = 0;
    for(User user : users.values()) {
      if(user.getNumInfectedStudents() > mostStudentsInfected &&
	  user.getVersion() < getLatestVersion()) {
	bestCoach = user;
	mostStudentsInfected = user.getNumInfectedStudents();
      }
    }
    
    return bestCoach;
  }
  
  /*
   * Whenever we infect a user, we also want to increment numInfections. 
   * this function is to maintain that the two go hand in hand.
   * Returns true if infection was successful. Otherwise, false
   * On top of that, we increment the this user's coaches' number of
   * infected students. This is useful when we perform limited infections,
   */
  private boolean tryInfect(User user) {
    if(user.getVersion() != latest_version) {
      user.setVersion(latest_version);
      numInfections++;
      for(User coach : user.getCoaches()) {
	coach.incrementInfectedStudents();
      }
      return true;
    }
    return false;
  }
  /*
   * Simply prints all users that are infected in this world.
   */
  public void printInfected() {
    System.out.print("Users infected: ");
    for(User user : users.values()) {
      if(user.getVersion() == latest_version)
	System.out.print(user.getName() + " ");
    }
    System.out.println();
  }
}