import java.util.HashSet;

/* Singleton Design */
public class World {

	/* private variables */
	
	/* Self-reference object */
	private static World _instance = null;
	
	/* Essentially, a list of users in the world */
	private static Set<User> users = new HashSet<User>();
	
	/* Constructor
	 * Initializes World by allocating lists.
	 */
	/*public World() {
		users = new ArrayList<User>();
	}*/
	
	public static World getInstance() {
		if(_instance = null)
			_instance = new World();

		return _instance;
	}
	
	public static boolean addUser(User user){
		return users.add(user);
	}
}