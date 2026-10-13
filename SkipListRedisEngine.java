import java.util.Arrays;
import java.util.Random;

public class SkipListRedisEngine {

    // The maximum number of express lanes (layers) allowed
    private static final int MAX_LEVEL = 16;
    // The probability of promoting a node to the next level (50%)
    private static final double PROBABILITY = 0.5;

    static class Node {
        int value;
        // Array of forward pointers. forward[i] points to the next node at level i.
        Node[] forward;

        public Node(int value, int level) {
            this.value = value;
            this.forward = new Node[level + 1];
        }
    }

    private Node head;
    private int currentMaxLevel;
    private Random random;

    public SkipListRedisEngine() {
        this.head = new Node(Integer.MIN_VALUE, MAX_LEVEL);
        this.currentMaxLevel = 0;
        this.random = new Random();
    }

    // O(1) - The mathematical coin flip to determine the node's geometric height
    private int flipCoinForLevel() {
        int level = 0;
        while (level < MAX_LEVEL && random.nextDouble() < PROBABILITY) {
            level++;
        }
        return level;
    }

    // O(log N) - Core Insertion Engine
    public void insert(int value) {
        Node[] update = new Node[MAX_LEVEL + 1];
        Node current = head;

        // Traverse from the highest active express lane down to the base lane
        for (int i = currentMaxLevel; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].value < value) {
                current = current.forward[i];
            }
            // Record the path we took so we can wire the new node in
            update[i] = current;
        }

        current = current.forward[0];

        // If the value doesn't already exist, lock it into the geometry
        if (current == null || current.value != value) {
            int newLevel = flipCoinForLevel();

            // If the new node is taller than our current highest express lane, initialize the new lanes
            if (newLevel > currentMaxLevel) {
                for (int i = currentMaxLevel + 1; i <= newLevel; i++) {
                    update[i] = head;
                }
                currentMaxLevel = newLevel;
            }

            Node newNode = new Node(value, newLevel);

            // Wire the new node into all the necessary express lanes mathematically
            for (int i = 0; i <= newLevel; i++) {
                newNode.forward[i] = update[i].forward[i];
                update[i].forward[i] = newNode;
            }
            System.out.println("  [System] Value " + value + " inserted. Promoted to Level " + newLevel);
        }
    }

    // O(log N) - Rapid Express Lane Search
    public boolean search(int value) {
        System.out.print("  -> Searching for " + value + " via Express Lanes: ");
        Node current = head;

        for (int i = currentMaxLevel; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].value < value) {
                current = current.forward[i];
                System.out.print("[L" + i + " Skip] ");
            }
        }
        
        current = current.forward[0];
        boolean found = (current != null && current.value == value);
        System.out.println(found ? " TARGET ISOLATED." : " TARGET NOT FOUND.");
        return found;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Skip List (Redis Sorted Set) Architecture ---");
        
        SkipListRedisEngine redisDb = new SkipListRedisEngine();
        
        int[] payload = {3, 6, 7, 9, 12, 19, 17, 26, 21, 25};
        System.out.println("\n[Database] Ingesting Payload...");
        for (int val : payload) {
            redisDb.insert(val);
        }
        
        System.out.println("\n--- Executing O(log N) Queries ---");
        redisDb.search(19); // Will skip through high-level lanes
        redisDb.search(10); // Will drop to Level 0 and fail
        
        System.out.println("\nStatus: Multi-layered probabilistic geometry perfectly synchronized. BST rotation bottlenecks mathematically bypassed.");
    }
}