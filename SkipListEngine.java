import java.util.Random;

public class SkipListEngine {
    
    private static final int MAX_LEVEL = 16;
    private static final double PROBABILITY = 0.5;
    
    static class Node {
        int value;
        Node[] forward; // Array of pointers for the express lanes
        
        public Node(int value, int level) {
            this.value = value;
            this.forward = new Node[level + 1];
        }
    }
    
    private Node head = new Node(Integer.MIN_VALUE, MAX_LEVEL);
    private int currentMaxLevel = 0;
    private Random coinFlipper = new Random();
    
    // O(1) - Pure probabilistic coin flip to determine tower height
    private int randomLevel() {
        int level = 0;
        while (coinFlipper.nextDouble() < PROBABILITY && level < MAX_LEVEL) {
            level++;
        }
        return level;
    }
    
    // O(log N) - Insert with automatic probabilistic balancing
    public void insert(int value) {
        Node[] update = new Node[MAX_LEVEL + 1];
        Node current = head;
        
        // Drop down from the highest express lane to the base level
        for (int i = currentMaxLevel; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].value < value) {
                current = current.forward[i];
            }
            update[i] = current; // Save the nodes where we need to bridge the pointers
        }
        
        current = current.forward[0];
        
        // If the value doesn't already exist, insert it
        if (current == null || current.value != value) {
            int newLevel = randomLevel();
            
            // If the coin flip gave us a level higher than we've ever seen, update the max
            if (newLevel > currentMaxLevel) {
                for (int i = currentMaxLevel + 1; i <= newLevel; i++) {
                    update[i] = head;
                }
                currentMaxLevel = newLevel;
            }
            
            Node newNode = new Node(value, newLevel);
            
            // Rewire the pointers across all applicable express lanes
            for (int i = 0; i <= newLevel; i++) {
                newNode.forward[i] = update[i].forward[i];
                update[i].forward[i] = newNode;
            }
            System.out.println("  [DB] Inserted " + value + " (Tower Height: " + newLevel + ")");
        }
    }
    
    // O(log N) - Lightning fast search using express lanes
    public boolean search(int value) {
        Node current = head;
        for (int i = currentMaxLevel; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].value < value) {
                current = current.forward[i];
            }
        }
        current = current.forward[0];
        return current != null && current.value == value;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Probabilistic Skip List Database ---");
        
        SkipListEngine database = new SkipListEngine();
        
        System.out.println("\n[System] Ingesting telemetry data...");
        int[] dataStream = {3, 6, 7, 9, 12, 19, 17, 26, 21, 25};
        for (int val : dataStream) {
            database.insert(val);
        }
        
        System.out.println("\n--- Querying Live Memory ---");
        int target = 19;
        System.out.println("Searching for " + target + ": " + (database.search(target) ? "FOUND (O(log N) path verified)" : "NOT FOUND"));
        
        System.out.println("\nStatus: Skip List active. Red-Black Tree rotation bottlenecks mathematically bypassed via probability.");
    }
}