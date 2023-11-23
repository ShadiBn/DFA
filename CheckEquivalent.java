import java.util.*;

public class CheckEquivalent {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter details for DFA 1:");
        DFA dfa1 = createDFA(sc);

        System.out.println("Enter details for DFA 2:");
        DFA dfa2 = createDFA(sc);

        // Check if DFAs are equivalent
        boolean areEquivalent = areDFAsEquivalent(dfa1, dfa2);

        // Display the result
        if (areEquivalent) {
            System.out.println("DFAs are equivalent.");
        } else {
            System.out.println("DFAs are not equivalent.");
        }

        sc.close();
    }

    // Other methods...

    private static boolean areDFAsEquivalent(DFA dfa1, DFA dfa2) {
        dfa1.minimizeDFA();
        dfa2.minimizeDFA();

        Set<String> alphabet = dfa1.getTransitions().keySet();

        // Create sets for distinguishing and non-distinguishing states
        Set<String> distinguishing = new HashSet<>(dfa1.getFinalStates());
        Set<String> nonDistinguishing = new HashSet<>(dfa2.getFinalStates());
        nonDistinguishing.addAll(dfa1.getReachableStates(dfa1.getStartingState(), new HashSet<>()));
        nonDistinguishing.removeAll(distinguishing);

        // Use a queue for processing states
        Queue<Pair<String, String>> queue = new LinkedList<>();

        // Initialize the queue with pairs of distinguishing and non-distinguishing states
        for (String a : alphabet) {
            for (String b : nonDistinguishing) {
                queue.add(new Pair<>(dfa1.getTransitions().get(dfa1.getStartingState()).get(a),
                        dfa2.getTransitions().get(dfa2.getStartingState()).get(a + "_" + b)));
            }
        }

        // Process the pairs using the Nearly Linear algorithm
        while (!queue.isEmpty()) {
            Pair<String, String> pair = queue.poll();
            String state1 = pair.getFirst();
            String state2 = pair.getSecond();

            for (String a : alphabet) {
                String nextState1 = dfa1.getTransitions().get(state1).get(a);
                String nextState2 = dfa2.getTransitions().get(state2).get(a);

                Pair<String, String> nextPair = new Pair<>(nextState1, nextState2);

                if (!nextState1.equals(nextState2) && !distinguishing.contains(nextState1) && !distinguishing.contains(nextState2)) {
                    distinguishing.add(nextState1);
                    distinguishing.add(nextState2);
                    queue.addAll(getPairs(nextPair, dfa1, dfa2, alphabet));
                } else if (distinguishing.contains(nextState1) && distinguishing.contains(nextState2)) {
                    queue.addAll(getPairs(nextPair, dfa1, dfa2, alphabet));
                }
            }
        }

        // If the initial states are in the same set, the DFAs are equivalent
        Set<String> initialStateClass1 = findEquivalenceClass(dfa1.getStartingState(), distinguishing);
        Set<String> initialStateClass2 = findEquivalenceClass(dfa2.getStartingState(), distinguishing);

        return initialStateClass1.equals(initialStateClass2);
    }

    // Other helper methods...

    private static List<Pair<String, String>> getPairs(Pair<String, String> pair, DFA dfa1, DFA dfa2, Set<String> alphabet) {
        List<Pair<String, String>> pairs = new ArrayList<>();

        for (String a : alphabet) {
            String nextState1 = dfa1.getTransitions().get(pair.getFirst()).get(a);
            String nextState2 = dfa2.getTransitions().get(pair.getSecond()).get(a);

            pairs.add(new Pair<>(nextState1, nextState2));
        }

        return pairs;
    }
}

class Pair<T, U> {
    private final T first;
    private final U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }
}
