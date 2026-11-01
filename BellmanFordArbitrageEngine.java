import java.util.*;

public class BellmanFordArbitrageEngine {

    static class ExchangeRate {
        int sourceCurrency;
        int targetCurrency;
        double rate;
        double negativeLogWeight;

        public ExchangeRate(int source, int target, double rate) {
            this.sourceCurrency = source;
            this.targetCurrency = target;
            this.rate = rate;
            // The Mathematical Core: Convert multiplication to addition via Logarithms
            // A negative cycle in this transformed graph equals an exchange loop > 1.0 (Arbitrage)
            this.negativeLogWeight = -Math.log(rate);
        }
    }

    private final int numCurrencies;
    private final List<ExchangeRate> marketGraph;
    private final String[] currencyNames;

    public BellmanFordArbitrageEngine(int currencies, String[] names) {
        this.numCurrencies = currencies;
        this.marketGraph = new ArrayList<>();
        this.currencyNames = names;
    }

    public void addExchangeRate(int source, int target, double rate) {
        marketGraph.add(new ExchangeRate(source, target, rate));
    }

    // O(V * E) - Sweeps the market to isolate negative cycles
    public void executeArbitrageSweep() {
        System.out.println("  [System] Executing Bellman-Ford Negative Logarithmic Sweep...");
        
        double[] minCost = new double[numCurrencies];
        int[] predecessor = new int[numCurrencies];
        
        Arrays.fill(minCost, Double.MAX_VALUE);
        Arrays.fill(predecessor, -1);
        
        // Start the algorithm from currency 0 (e.g., USD)
        minCost[0] = 0.0;

        // Phase 1: Relax all edges V-1 times
        for (int i = 1; i < numCurrencies; i++) {
            for (ExchangeRate edge : marketGraph) {
                if (minCost[edge.sourceCurrency] != Double.MAX_VALUE && 
                    minCost[edge.sourceCurrency] + edge.negativeLogWeight < minCost[edge.targetCurrency]) {
                    
                    minCost[edge.targetCurrency] = minCost[edge.sourceCurrency] + edge.negativeLogWeight;
                    predecessor[edge.targetCurrency] = edge.sourceCurrency;
                }
            }
        }

        // Phase 2: The V-th iteration. If an edge can still be relaxed, a negative cycle exists.
        int arbitrageVertex = -1;
        for (ExchangeRate edge : marketGraph) {
            if (minCost[edge.sourceCurrency] != Double.MAX_VALUE && 
                minCost[edge.sourceCurrency] + edge.negativeLogWeight < minCost[edge.targetCurrency]) {
                
                arbitrageVertex = edge.targetCurrency;
                break;
            }
        }

        if (arbitrageVertex == -1) {
            System.out.println("  [Market Status] No risk-free arbitrage opportunities exist. Market is perfectly efficient.");
        } else {
            System.out.println("  [ALERT] Negative Cycle Detected! Extracting Arbitrage Loop...");
            isolateArbitrageLoop(arbitrageVertex, predecessor);
        }
    }

    private void isolateArbitrageLoop(int startVertex, int[] predecessor) {
        // Move backward to ensure we are strictly inside the cycle
        int current = startVertex;
        for (int i = 0; i < numCurrencies; i++) {
            current = predecessor[current];
        }

        List<Integer> arbitrageCycle = new ArrayList<>();
        int cycleStart = current;
        
        // Trace the cycle backward until we hit the start node again
        do {
            arbitrageCycle.add(current);
            current = predecessor[current];
        } while (current != cycleStart);
        
        arbitrageCycle.add(cycleStart);
        Collections.reverse(arbitrageCycle);

        System.out.print("    -> Optimal Execution Path: ");
        for (int i = 0; i < arbitrageCycle.size(); i++) {
            System.out.print(currencyNames[arbitrageCycle.get(i)] + (i == arbitrageCycle.size() - 1 ? "" : " --> "));
        }
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("--- Booting High-Frequency Arbitrage Engine ---");
        
        String[] tickers = {"USD", "EUR", "JPY", "GBP"};
        BellmanFordArbitrageEngine quantEngine = new BellmanFordArbitrageEngine(4, tickers);
        
        // Simulated FX Market Rates
        quantEngine.addExchangeRate(0, 1, 0.95);  // USD to EUR
        quantEngine.addExchangeRate(1, 2, 160.5); // EUR to JPY
        quantEngine.addExchangeRate(2, 3, 0.005); // JPY to GBP
        
        // THE INEFFICIENCY: GBP converts back to USD at a slightly inflated rate
        quantEngine.addExchangeRate(3, 0, 1.35);  
        
        // 0.95 * 160.5 * 0.005 * 1.35 = 1.029 (A 2.9% risk-free profit loop)
        quantEngine.executeArbitrageSweep();
        
        System.out.println("\nStatus: Multiplicative market inefficiencies mathematically isolated via negative logarithmic geometry.");
    }
}