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

        // Generate strings without user input
        System.out.println("Generating random strings...");
        String[] generatedStrings = dfa.generateStrings();

        //Store generated strings in variables
        String Accepted1 = generatedStrings[0];
        String Accepted2 = generatedStrings[1];
        String Rejected1 = generatedStrings[2];
        String Rejected2 = generatedStrings[3];

        //Display the generated strings
        System.out.println("Accepted String 1: " + Accepted1);
        System.out.println("Accepted String 2: " + Accepted2);
        System.out.println("Not Accepted String 1: " + Rejected1);
        System.out.println("Not Accepted String 2: " + Rejected2);


        // Consume any remaining newline characters in the input buffer
        sc.nextLine();

        sc.close();
    }
}
