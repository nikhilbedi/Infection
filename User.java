import java.util.ArrayList;

public class User {

  /* private variables */
  
  /*
   * A list of this user's coaches.
   */
  private ArrayList<User> coaches;
  
  /*
   * A list of this user's students.
   */
  private ArrayList<User> students;
  
  /*
   * The most up-to-date version the user currently owns.
   */
  private int version;
  
  /*
   * A unique string identifier for each user.
   */
   private String name;
  
  /* Constructor
   * Allocate memory for lists.
   * Assign version to passed-in parameter.
   */
  public User(int version, String name) {
    this.coaches = new ArrayList<User>();
    this.students = new ArrayList<User>();
    this.version = version;
    this.name = name;
  }
  
  /* Constructor
   * In case we don't immediately know what the user's version is
   */
  public User(String name) {
    this.coaches = new ArrayList<User>();
    this.students = new ArrayList<User>();
    this.version = 0;
    this.name = name;
  }
  
  public void setVersion(int version) {
    this.version = version;
  }
  
  public int getVersion() {
    return version;
  }
  
  public ArrayList<User> getCoaches() {
    return coaches;
  }
  
  public ArrayList<User> getStudents() {
    return students;
  }
}