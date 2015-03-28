import java.util.Map;
import java.util.HashMap;

public class UsernameBank {

    private static final int charMax = 35;
    private static final int capacity = (int) (Math.pow(charMax, 3)) + 
        (int) (Math.pow(charMax, 2));

    // Instance variables (remember, they should be private!)
    HashMap<String, String> mapWithUsernameKeys;
    HashMap<String, String> mapWithEmailKeys;

    HashMap<String, Integer> badUsernames;
    HashMap<String, Integer> badEmails;

    public UsernameBank() {
        mapWithUsernameKeys = new HashMap<String, String>(capacity);
        mapWithEmailKeys = new HashMap<String, String>(capacity);

        badUsernames = new HashMap<String, Integer>();
        badEmails = new HashMap<String, Integer>();
    }

    public void generateUsername(String username, String email) {
        if (username == null || email == null) {
            throw new NullPointerException("Inputted data is null!");
        }
        Username usr = new Username(username);
        if (mapWithUsernameKeys.get(username) != null) {
            throw new IllegalArgumentException("Username already exists."); 
        }
        mapWithUsernameKeys.put(username, email);
        mapWithEmailKeys.put(email, username);
    }

    public String getEmail(String username) {
        if (username == null) {
            throw new NullPointerException("Requested username is null!");
        }
        try {
            Username usr = new Username(username);
        } catch (IllegalArgumentException exc) {
            recordBadUsername(username);
            return null;
        }
        return mapWithUsernameKeys.get(username);
    }

    public String getUsername(String userEmail)  {
        if (userEmail == null) {
            throw new NullPointerException("Requested email is null!");
        }
        String username = mapWithEmailKeys.get(userEmail);
        if (username == null) {
            recordBadEmail(userEmail);
        }
        return mapWithEmailKeys.get(userEmail);
    }

    public Map<String, Integer> getBadEmails() {
        return badEmails;
    }

    public Map<String, Integer> getBadUsernames() {
        return badUsernames;
    }

    public String suggestUsername() {
        String username = null;
        boolean valid = false;
        while (!valid) {
            username = randomUsername();
            valid = true;
            try {
                Username user = new Username(username);
            } catch (IllegalArgumentException exc) {
                valid = false;
            }
        }
        return username;
    }

    private String randomUsername() {
        String username = "";
        int length = (int) (2 * Math.random()) + 2;
        for (int i = 0; i < length; i++) {
            int random = (int) (35 * Math.random());
            if (random < 10) {
                random += 48;
            } else {
                random += 87;
            }
            username += (char) random;
        }
        return username;
    }

    // The answer is somewhere in between 3 and 1000.
    public static final int followUp() {
        return 6;
    }

    // Optional, suggested method. Use or delete as you prefer.
    private void recordBadUsername(String username) {
        if (badUsernames.containsKey(username)) {
            int value = badUsernames.get(username);
            badUsernames.put(username, value + 1);
        } else {
            badUsernames.put(username, 1);
        }
    }

    // Optional, suggested method. Use or delete as you prefer.
    private void recordBadEmail(String email) {
        if (badEmails.containsKey(email)) {
            int value = badEmails.get(email);
            badEmails.put(email, value + 1);
        } else {
            badEmails.put(email, 1);
        }
    }
}