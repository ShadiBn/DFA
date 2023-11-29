import java.util.*;

public class CheckEquivalent {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input for the first DFA
        DFA dfa1 = getDFAInput(sc, "Enter inputs for the first DFA");

        // Input for the second DFA
        DFA dfa2 = getDFAInput(sc, "Enter inputs for the second DFA");

        // Check if the DFAs are equivalent
        boolean isEquivalent = areEquivalent(dfa1, dfa2);

        // Output the result
        if (isEquivalent) {
            System.out.println("The DFAs are equivalent.");
        } else {
            System.out.println("The DFAs are not equivalent.");
        }
    }

    // Check if two DFAs are equivalent
    public static boolean areEquivalent(DFA dfa1, DFA dfa2) {

        // Minimize both DFAs
        dfa1.minimizeDFA();
        dfa2.minimizeDFA();
    
        // Initialize the initial pair
        String startingState1 = dfa1.getStartingState();
        String startingState2 = dfa2.getStartingState();
    
        Set<String> initialPair = new HashSet<>();
        initialPair.add(startingState1);
        initialPair.add(startingState2);
    
        // Set to keep track of processed pairs
        Set<Set<String>> processedPairs = new HashSet<>();
    
        // Queue for BFS traversal of pairs
        Queue<Set<String>> queue = new ArrayDeque<>();
        queue.add(initialPair);
        processedPairs.add(initialPair);
    
        while (!queue.isEmpty()) {
            Set<String> currentPair = queue.poll();
    
            // Get the transitions for both DFAs
            HashMap<String, HashMap<String, String>> transition_dfa1 = dfa1.getTransitions();
            HashMap<String, HashMap<String, String>> transition_dfa2 = dfa2.getTransitions();
    
            // Get valid inputs for transitions
            HashMap<String, String> currentStateTransitions1 = transition_dfa1.get(currentPair.iterator().next());
            HashMap<String, String> currentStateTransitions2 = transition_dfa2.get(currentPair.toArray()[1]);
    
            // Check if either set of transitions is null
            if (currentStateTransitions1 == null || currentStateTransitions2 == null) {
                continue;
            }
    
            for (String input : currentStateTransitions1.keySet()) {
                Set<String> nextPair = new HashSet<>();
    
                // Get the next states for both DFAs using the current input
                String nextState1 = currentStateTransitions1.get(input);
                String nextState2 = currentStateTransitions2.get(input);
    
                // Add states to the next pair set
                nextPair.add(nextState1);
                nextPair.add(nextState2);
    
                if (!processedPairs.contains(nextPair)) {
                    processedPairs.add(nextPair);
                    queue.add(nextPair);
                }
    
                // Check if the states in the pair are of the same type (final or non-final)
                if (!sameType(dfa1, dfa2, nextPair)) {
                    return false; // DFAs are not equivalent
                }
            }
        }
    
        return true; // DFAs are equivalent
    }
    

    // Check if states in a pair are of the same type
    private static boolean sameType(DFA dfa1, DFA dfa2, Set<String> pair) {
        boolean isFinalState1 = dfa1.getFinalStates().contains(pair.iterator().next());
        boolean isFinalState2 = dfa2.getFinalStates().contains(pair.toArray()[1]);

        return isFinalState1 == isFinalState2;
    }

    // Get DFA input from the user
    private static DFA getDFAInput(Scanner sc, String message) {
        System.out.println(message);

        // Using Utility class for input validation
        String inputString = Utility.getInput("Enter valid inputs (comma-separated): ", sc);
        Set<String> validInputs = new HashSet<>(Arrays.asList(inputString.split(",")));

        String statesInput = Utility.getInput("Enter states (comma-separated): ", sc);
        Set<String> states = new HashSet<>(Arrays.asList(statesInput.split(",")));

        String startingState = null;
        // Ensure that the entered starting state is a valid state
        while (startingState == null || !states.contains(startingState)) {
            startingState = Utility.getInput("Enter starting state: ", sc);
            if (!states.contains(startingState)) {
                System.out.println("Invalid state. Please enter a valid state.");
            }
        }

        String finalStatesInput = Utility.getInput("Enter all final states (comma-separated): ", sc);
        Set<String> finalStates = new HashSet<>(Arrays.asList(finalStatesInput.split(",")));

        // Input for transitions
        HashMap<String, HashMap<String, String>> transitions = new HashMap<>();

        for (String state : states) {
            HashMap<String, String> transition = new HashMap<>();
            for (String validInput : validInputs) {
                String nextState = null;
                // Ensure that the entered transition state is a valid state
                while (nextState == null || !states.contains(nextState)) {
                    nextState = Utility.getInput(
                            String.format("Enter transition state for state '%s' with input '%s': ", state, validInput), sc);
                    if (!states.contains(nextState)) {
                        System.out.println("Invalid state. Please enter a valid state.");
                    }
                }
                transition.put(validInput, nextState);
            }
            transitions.put(state, transition);
        }

        return new DFA(startingState, finalStates, transitions);
    }
}
