public class DynamicStack extends CustomStack {

    public DynamicStack() {
        super(); // Calls the default constructor of CustomStack
    }

    public DynamicStack(int size) {
        super(size); // Calls the parameterized constructor
    }

    @Override
    public boolean push(int item) {
        // If the original array is full, we intercept the crash
        if (this.isFull()) {
            System.out.println("Capacity Reached. Dynamically doubling memory architecture...");
            
            // 1. Create a new array twice the size
            int[] temp = new int[data.length * 2];
            
            // 2. Copy all previous items into the new memory block
            for (int i = 0; i < data.length; i++) {
                temp[i] = data[i];
            }
            
            // 3. Reassign the pointer to the new, larger array
            data = temp;
        }

        // Now that we know memory is available, use the standard push logic
        return super.push(item);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("--- Booting Dynamic LIFO Architecture ---");
        
        // Initializing with a tiny capacity of 3
        DynamicStack stack = new DynamicStack(3);
        
        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println("Initial capacity filled. Top is: " + stack.peek());

        // Pushing a 4th item will trigger the dynamic memory expansion
        stack.push(40);
        stack.push(50);
        
        System.out.println("Expansion successful. New top is: " + stack.peek());
    }
}