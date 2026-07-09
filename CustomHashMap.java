import java.util.LinkedList;

public class CustomHashMap {
    // An Entity stores the Key-Value pair
    private class Entity {
        int key;
        String value;
        public Entity(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    // The core architecture: An array of Linked Lists
    private LinkedList<Entity>[] map;
    private int size;

    @SuppressWarnings("unchecked")
    public CustomHashMap(int capacity) {
        this.size = capacity;
        map = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            map[i] = new LinkedList<>();
        }
    }

    // The Mathematical Hash Function: Maps an infinite range of keys to our limited array size
    private int getHash(int key) {
        return Math.abs(key % size);
    }

    // O(1) Amortized Time - Insertion and Collision Handling
    public void put(int key, String value) {
        int hash = getHash(key);
        LinkedList<Entity> chain = map[hash];

        // Step 1: Check if the key already exists and update it
        for (Entity entity : chain) {
            if (entity.key == key) {
                entity.value = value;
                return;
            }
        }
        // Step 2: If no collision with existing keys, append a new Entity to the chain
        chain.add(new Entity(key, value));
    }

    // O(1) Amortized Time - High-Speed Lookup
    public String get(int key) {
        int hash = getHash(key);
        LinkedList<Entity> chain = map[hash];

        for (Entity entity : chain) {
            if (entity.key == key) {
                return entity.value; // Target Acquired
            }
        }
        return null; // Key does not exist
    }

    public static void main(String[] args) {
        System.out.println("--- Booting O(1) Chained Hash Architecture ---");
        
        CustomHashMap database = new CustomHashMap(10);
        
        // Inserting data
        database.put(101, "SAP Placement Prep");
        database.put(205, "TCS CodeVita");
        database.put(309, "Amazon DSA");

        // Forcing a collision (Both 101 and 111 will hash to index 1)
        database.put(111, "Collision Target");

        System.out.println("Executing O(1) Data Retrieval...");
        System.out.println("Key 101: " + database.get(101));
        System.out.println("Key 205: " + database.get(205));
        
        System.out.println("\nTesting Collision Integrity...");
        System.out.println("Key 111: " + database.get(111));
        System.out.println("Status: Separate Chaining successfully resolved memory overlap.");
    }
}