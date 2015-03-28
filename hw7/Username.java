public class Username {

    // Potentially useless note: (int) '0' == 48, (int) 'a' == 97

    // Instance Variables (remember, they should be private!)
    private String username;
    private int hash;

    public Username() {
        hash = 0;
        username = "";

        int length = (int) (2 * Math.random()) + 2;
        for (int i = 0; i < length; i++) {
            int random = (int) (35 * Math.random());
            hash += random * (int) Math.pow(35, i);
            if (random < 10) {
                random += 48;
            } else {
                random += 87;
            }
            username += (char) random;
        }
    }

    public Username(String reqName) {
        if (reqName == null) {
            throw new NullPointerException("Requested username is null!");
        }
        if (reqName.length() != 2 && reqName.length() != 3) {
            String msg = "Your username isn't of length 2 or 3 characters!";
            throw new IllegalArgumentException(msg);
        }
        reqName = reqName.toLowerCase();
        for (int i = reqName.length() - 1; i >= 0; i--) {
            int charValue = (int) reqName.charAt(i);
            if (!(charValue >= 48 && charValue < 58 ||
                charValue >= 97 && charValue < 123)) {
                String msg = "Characters in your username are invalid!";
                throw new IllegalArgumentException(msg);
            } else if (charValue >= 48 && charValue < 58) {
                charValue -= 48;
            } else {
                charValue -= 87;
            }
            hash += charValue * (int) Math.pow(35, i);
        }
        username = reqName;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Username) {
            return this.hashCode() == ((Username) o).hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() { 
        return hash;
    }

    public static void main(String[] args) {
        // You can put some simple testing here.
        try {
            Username bullshlachat = new Username("Ya&");
        } catch (IllegalArgumentException ex) {
            System.out.println("Caught the error");
        }

        Username a = new Username("Y87");
        Username b = new Username("y87");
        System.out.println("Must be true: " + a.equals(b));
    }
}