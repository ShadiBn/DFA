import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class DFATestGenerateString {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String message;

        message = "Enter valid inputs: ";
        String inputString = Utility.getInput(message, sc);
        Set<String> validInputs = new HashSet<>(Arrays.asList(inputString.split(",")));

        message = "Enter states: ";
        String statesInput = Utility.getInput(message, sc);
        Set<String> states = new HashSet<>(Arrays.asList(statesInput.split(",")));

        message = "Enter starting state: ";
        String startingState = Utility.getInput(message, sc);

        message = "Enter all final states: ";
        String finalStatesInput = Utility.getInput(message, sc);
        Set<String> finalStates = new HashSet<>(Arrays.asList(finalStatesInput.split(",")));

        HashMap<String, HashMap<String, String>> transitions = new HashMap<>();

        for (String state : states) {
            HashMap<String, String> transition = new HashMap<>();
            for (String validInput : validInputs) {
                message = String.format("Enter transition state for state '%s' with input '%s': ", state, validInput);
                String nextState = Utility.getInput(message, sc);
                transition.put(validInput, nextState);
            }
            transitions.put(state, transition);
        }

        DFA dfa = new DFA(startingState, finalStates, transitions);

        // Generate a string without user input
        System.out.println("Generating a random valid string...");
        dfa.generateString();
        //System.out.println("Generated String: " + generatedString);

        // Consume any remaining newline characters in the input buffer
        sc.nextLine();

        sc.close();
    }
}
