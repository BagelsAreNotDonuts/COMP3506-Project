public class LoginSystem extends LoginSystemBase {
    //Static size initialization
    int size = 101;
    String[][] hashTable;

    public LoginSystem() {
        this.hashTable = new String[this.size][];
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int getNumUsers() {
        int counter = 0;
        for (String[] entry : this.hashTable) {
            if (entry != null) {
                counter++;
            }
        }
        return counter;
    }

    @Override
    public int hashCode(String key) {
        //Converts the given string to an ascii array.
        final int constant = 31;
        int keyLength = key.length();
        int[] asciiValues = new int[keyLength];
        char[] testChars = key.toCharArray();
        for (int i = 0 ; i < keyLength ; i++) {
            int ascii = testChars[i];
            asciiValues[i] = ascii;
        }

        //Initializing variable associated with polynomial accumulation
        int polyAccumulation;
        //Base case, only one character in the string
        if (keyLength == 1) {
            polyAccumulation = asciiValues[0];
            return polyAccumulation;
            //When the length of the string is longer than one, conduct iterative polynomial accumulation.
        } else {
            polyAccumulation = asciiValues[0] * constant + asciiValues[1];
            for (int i = 2 ; i < keyLength ; i++) {
                polyAccumulation = polyAccumulation * constant + asciiValues[i];
            }
            return polyAccumulation;
        }

    }

    @Override
    public boolean addUser(String email, String password) {

        //Checks for duplicates of the email in the hashtable, and if they exist, it doesn't add the User.
        if (checkDuplicates(email)) {
            return false;
        }

        //Triples the size of the Hashtable if the array is full when trying to add.
        if (getNumUsers() == this.size) {
            this.size = this.size*3;
            String[][] oldHashTable = this.hashTable;
            this.hashTable = new String[this.size][];
            //Rehashes all entries and adds them to the new Hashtable
            for (String[] entry : oldHashTable) {
                addUser(entry[0],entry[1]);
            }
        }

        //Hashes the inputted password and string
        int hashedPassword = hashCode(password);
        int hashedEmail = hashCode(email);

        //Turns hashed password to a string to allow for adding into an entry
        String passwordEntry = String.valueOf(hashedPassword);
        String[] entry = {email,passwordEntry};

        //Uses email as the key to map to a specific bucket.
        int initialBucket = hashCompression(hashedEmail);
        //System.out.println(initialBucket + " Initial bucket");

        //Linear probing and insertion of entry into the hash table.
        int circularIndex;

        //Where i is equal to the number of cells probed
        for (int i = 0 ; i < this.size ; i++ ) {
            circularIndex = (initialBucket + i) % this.size;
            if (hashTable[circularIndex] == null) {
                hashTable[circularIndex] = entry;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeUser(String email, String password) {

        int passwordCheck = checkPassword(email,password);
        if (passwordCheck < 0) {
            return false;
        } else {
            hashTable[passwordCheck] = null;
            return true;
        }
    }

    @Override
    public int checkPassword(String email, String password) {
        //Going through the process of linear probing again, similar to addUser.
        String passwordEntry = String.valueOf(hashCode(password));
        int hashedEmail = hashCode(email);
        int initialBucket = hashCompression(hashedEmail);
        int circularIndex;

        //Checks if the user is in the system
        for (int i = 0 ; i < this.size ; i++ ) {
            circularIndex = (initialBucket + i) % this.size;
            if (hashTable[circularIndex] != null && hashTable[circularIndex][0].equals(email)
                    && hashTable[circularIndex][1].equals(passwordEntry)) {
                return circularIndex;
            } else if (hashTable[circularIndex] != null && hashTable[circularIndex][0].equals(email)
                    && !hashTable[circularIndex][1].equals(passwordEntry)) {
                return -2;
            }
        }
        return -1;
    }

    @Override
    public boolean changePassword(String email, String oldPassword, String newPassword) {

        //Checks if the password is valid
        int passwordCheck = checkPassword(email, oldPassword);

        //Depending on the results of password check will change the password stored in the hashtable.
        if (passwordCheck < 0) {
            return false;
        } else {
            String newPasswordEntry = String.valueOf(hashCode(newPassword));
            hashTable[passwordCheck][1] = newPasswordEntry;
            return true;
        }
    }

    public int hashCompression (int hashCode) {
        //Returns the table index of the hash
        return hashCode % this.size;

    }

    public boolean checkDuplicates(String email) {

        //Linear probing to check if passed user's email is in the system.
        int hashedEmail = hashCode(email);
        int initialBucket = hashCompression(hashedEmail);
        int circularIndex;

        for (int i = 0 ; i < this.size ; i++ ) {
            circularIndex = (initialBucket + i) % this.size;
            if (hashTable[circularIndex] != null && hashTable[circularIndex][0].equals(email)) {
                return true;
            }
        }
        return false;
    }

}
