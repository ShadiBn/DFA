import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

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

        String startingState = null;
        // Ensure that the entered starting state is a valid state
        while (startingState == null || !states.contains(startingState)) {
            message = "Enter starting state: ";
            startingState = Utility.getInput(message, sc);
            if (!states.contains(startingState)) {
                System.out.println("Invalid state. Please enter a valid state.");
            }
        }

        message = "Enter all final states: ";
        String finalStatesInput = Utility.getInput(message, sc);
        Set<String> finalStates = new HashSet<>(Arrays.asList(finalStatesInput.split(",")));

        HashMap<String, HashMap<String, String>> transitions = new HashMap<>();

        for (String state : states) {
            HashMap<String, String> transition = new HashMap<>();
            for (String validInput : validInputs) {
                message = String.format("Enter transition state for state '%s' with input '%s': ", state, validInput);
                String nextState = null;
                // Ensure that the entered transition state is a valid state
                while (nextState == null || !states.contains(nextState)) {
                    nextState = Utility.getInput(message, sc);
                    if (!states.contains(nextState)) {
                        System.out.println("Invalid state. Please enter a valid state.");
                    }
                }
                transition.put(validInput, nextState);
            }
            transitions.put(state, transition);
        }

        DFA dfa = new DFA(startingState, finalStates, transitions);

        // Generate strings without user input
        System.out.println("Generating random strings...");
        String[] generatedStrings = dfa.generateStrings();

        // Store generated strings in variables
        String Accepted1 = generatedStrings[0];
        System.out.println("Accepted String 1: " + Accepted1);

        String Accepted2 = generatedStrings[1];
        System.out.println("Accepted String 2: " + Accepted2);

        String Rejected1 = generatedStrings[2];
        System.out.println("Not Accepted String 1: " + Rejected1);

        String Rejected2 = generatedStrings[3];
        System.out.println("Not Accepted String 2: " + Rejected2);

       
        // Consume any remaining newline characters in the input buffer
        sc.nextLine();

        sc.close();
    }
}
