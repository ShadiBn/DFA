import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CheckEquivalent {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input for the first DFA
        DFA dfa1 = getDFAInput(sc, "Enter inputs for the first DFA");

        // Input for the second DFA
        DFA dfa2 = getDFAInput(sc, "Enter inputs for the second DFA");

        // Minimize both DFAs
        dfa1.minimizeDFA();
        dfa2.minimizeDFA();


        String[] dfa1Strings = dfa1.generateStrings();
        // Generate accepted and rejected strings for DFA1
        String acceptedString1 = dfa1Strings[0];
        String acceptedString2 = dfa1Strings[1];
        String notAcceptedString1 = dfa1Strings[2];
        String notAcceptedString2 = dfa1Strings[3];

        // Validate strings for DFA2
        int accepted1InDFA2 = dfa2.validateString(acceptedString1);
        int accepted2InDFA2 = dfa2.validateString(acceptedString2);
        int notAccepted1InDFA2 = dfa2.validateString(notAcceptedString1);
        int notAccepted2InDFA2 = dfa2.validateString(notAcceptedString2);

        // Check equivalence based on the specified criteria
        boolean areEquivalent = accepted1InDFA2 == 0 && accepted2InDFA2 == 0 &&
                notAccepted1InDFA2 == notAccepted2InDFA2 &&
                checkStartingFinalStateEquivalence(dfa1, dfa2);

        System.out.println("DFAs are " + (areEquivalent ? "equivalent" : "not equivalent"));

        // Close the scanner
        sc.close();
    }

    private static boolean checkStartingFinalStateEquivalence(DFA dfa1, DFA dfa2) {
        // Check if one DFA has starting state as a final state, the other should have the same property
        return (dfa1.finalStates.contains(dfa1.startingState) && dfa2.finalStates.contains(dfa2.startingState))
                || (!dfa1.finalStates.contains(dfa1.startingState) && !dfa2.finalStates.contains(dfa2.startingState));
    }

    private static DFA getDFAInput(Scanner sc, String message) {
        System.out.println(message);

        String inputString = Utility.getInput("Enter valid inputs (comma-separated): ", sc);
        Set<String> validInputs = new HashSet<>(Arrays.asList(inputString.split(",")));

        String statesInput = Utility.getInput("Enter states (comma-separated): ", sc);
        Set<String> states = new HashSet<>(Arrays.asList(statesInput.split(",")));

        String startingState = Utility.getInput("Enter starting state: ", sc);

        String finalStatesInput = Utility.getInput("Enter all final states (comma-separated): ", sc);
        Set<String> finalStates = new HashSet<>(Arrays.asList(finalStatesInput.split(",")));

        HashMap<String, HashMap<String, String>> transitions = new HashMap<>();

        for (String state : states) {
            HashMap<String, String> transition = new HashMap<>();
            for (String validInput : validInputs) {
                String nextState = Utility.getInput(
                        String.format("Enter transition state for state '%s' with input '%s': ", state, validInput), sc);
                transition.put(validInput, nextState);
            }
            transitions.put(state, transition);
        }

        return new DFA(startingState, finalStates, transitions);
    }
}
