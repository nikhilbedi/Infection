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
  
  /* Constructor
   * Allocate memory for lists.
   * Assign version to passed-in parameter.
   */
  public User(int version) {
    coaches = new ArrayList<User>();
    students = new ArrayList<User>();
    this.version = version;
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