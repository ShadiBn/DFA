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

        // Generate accepted and rejected strings for both DFAs
        String[] dfa1Strings = dfa1.generateStrings();
        String[] dfa2Strings = dfa2.generateStrings();

        System.out.println("Accepted String 1: " + dfa1Strings[0]);
        System.out.println("Accepted String 2: " + dfa2Strings[0]);
        System.out.println("Not Accepted String 1: " + dfa1Strings[2]);
        System.out.println("Not Accepted String 2: " + dfa2Strings[2]);

        // Validate accepted and rejected strings for equivalence
        validateEquivalence(dfa1, dfa2, dfa1Strings[0], dfa2Strings[0]);
        validateEquivalence(dfa1, dfa2, dfa1Strings[2], dfa2Strings[2]);

        // Close the scanner
        sc.close();
    }

    private static DFA getDFAInput(Scanner sc, String message) {
        System.out.println(message);

        String inputString = Utility.getInput("Enter valid inputs: ", sc);
        Set<String> validInputs = new HashSet<>(Arrays.asList(inputString.split(",")));

        String statesInput = Utility.getInput("Enter states: ", sc);
        Set<String> states = new HashSet<>(Arrays.asList(statesInput.split(",")));

        String startingState = Utility.getInput("Enter starting state: ", sc);

        String finalStatesInput = Utility.getInput("Enter all final states: ", sc);
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

    private static void validateEquivalence(DFA dfa1, DFA dfa2, String string1, String string2) {
        System.out.println("Validating equivalence for strings:");
        System.out.println("String 1: " + string1);
        System.out.println("String 2: " + string2);

        boolean isEquivalent = dfa1.validateString(string1).equals(dfa2.validateString(string2));

        System.out.println("Result: Strings are " + (isEquivalent ? "equivalent" : "not equivalent"));
    }
}
