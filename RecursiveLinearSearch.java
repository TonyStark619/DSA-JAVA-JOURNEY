import java.util.ArrayList;

public class RecursiveLinearSearch {
    public static void main(String[] args) {
        int[] arr = {2, 3, 1, 4, 4, 5};
        int target = 4;
        
        // Pass an empty ArrayList into the recursive function to collect answers
        ArrayList<Integer> ans = findAllIndices(arr, target, 0, new ArrayList<>());
        
        System.out.println("Target " + target + " found at indices: " + ans);
    }

    static ArrayList<Integer> findAllIndices(int[] arr, int target, int index, ArrayList<Integer> list) {
        // Base condition: If we reach the end of the array, return the list
        if (index == arr.length) {
            return list;
        }
        
        // If the target matches, add its index to our list
        if (arr[index] == target) {
            list.add(index);
        }
        
        // Move to the next index, passing the same list forward
        return findAllIndices(arr, target, index + 1, list);
    }
}