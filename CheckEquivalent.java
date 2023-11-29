import java.util.*;

public class CheckEquivalent {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input for the first DFA
        DFA dfa1 = getDFAInput(sc, "Enter inputs for the first DFA");

        // Input for the second DFA
        DFA dfa2 = getDFAInput(sc, "Enter inputs for the second DFA");

        // Check if the DFAs are equivalent
        boolean isEquivalent = DFA.areEquivalent(dfa1, dfa2);

        // Output the result
        if (isEquivalent) {
            System.out.println("The DFAs are equivalent.");
        } else {
            System.out.println("The DFAs are not equivalent.");
        }
    }

    // Check if two DFAs are equivalent
    

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
