import java.util.Set;
import java.util.HashSet;

public class User {

  /* private variables */
  
  /*
   * A list of this user's coaches.
   */
  private Set<User> coaches;
  
  /*
   * A list of this user's students.
   */
  private Set<User> students;
  
  /*
   * The most up-to-date version the user currently owns.
   */
  private int version;
  
  /*
   * The number of infected students this user has
   */
  private int numInfectedStudents;
  
  /* Constructor
   * Allocate memory for lists.
   * Assign version to passed-in parameter.
   */
  public User(int version) {
    this.coaches = new HashSet<User>();
    this.students = new HashSet<User>();
    this.version = version;
  }
  
  /* Default Constructor
   * In case we don't immediately know what the user's version is
   */
  public User() {
    this.coaches = new HashSet<User>();
    this.students = new HashSet<User>();
    this.version = 0;
  }
  
  public void setVersion(int version) {
    this.version = version;
  }
  
  public int getVersion() {
    return version;
  }
  
  public Set<User> getCoaches() {
    return coaches;
  }
  
  public Set<User> getStudents() {
    return students;
  }
  
  public boolean addCoach(User coach) {
    return coaches.add(coach);
  }
  
  public boolean addStudent(User student) {
    return students.add(student);
  }
  
  // TODO decide whether you will store in a variable,
  // or will iterate to find...
  public void incrementInfectedStudents() {
    numInfectedStudents++;
  }
  
  public void setNumInfectedStudents(int value) {
    numInfectedStudents = value;
  }
  
  public int getNumInfectedStudents() {
    return numInfectedStudents;
  }
  
  // Brute force, find number of students infected
  public int getNumberOfInfectedStudents(int latest_version) {
    int numInfected = 0;
    for(User user : students) {
      if(user.getVersion() == latest_version) {
	numInfected++;
      }
    }
    return numInfected;
  }
}