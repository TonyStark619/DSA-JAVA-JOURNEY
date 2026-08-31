// Java: Stream API for Filtering and Mapping
import java.util.List;
import java.util.stream.Collectors;

public class DataProcessor {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        
        // Filter even numbers and square them
        List<Integer> squaredEvens = numbers.stream()
            .filter(n -> n % 2 == 0)
            .map(n -> n * n)
            .collect(Collectors.toList());
            
        System.out.println("Java Processed: " + squaredEvens); 
        // Output: [4, 16, 36, 64]
    }
}