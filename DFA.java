import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DFA {

    String startingState;
    Set<String> finalStates;
    HashMap<String, HashMap<String, String>> transitions;

    public DFA(String startingState, Set<String> finalStates, HashMap<String, HashMap<String, String>> transitions) {
        this.startingState = startingState;
        this.finalStates = finalStates;
        this.transitions = transitions;
    }

    public String getStartingState(){
        return startingState;
    }

    public Set<String> getFinalStates() {
        return finalStates;
    }

    public HashMap<String, HashMap<String, String>> getTransitions() {
        return transitions;
    }

    public int validateString(String validateString) {
        String currentState = startingState;
        int counter = 0;
        // Iterating over the validateString string
        for (int i = 0; i < validateString.length(); i++) {
            char c = validateString.charAt(i);
            HashMap<String, String> currentStateTransition = transitions.get(currentState);
    
            String nextState = currentStateTransition.get(String.valueOf(c));
    
            // If transition for the current state with input 'c' is not present
            if (nextState == null) {
                // Input is invalid
                Utility.showError(String.format("Your string contains the value '%c', which is not present in the input set", c));
                return -1;
            }
    
            // Now we have the next state as the current state for the next iteration
            currentState = nextState;
            counter++ ;
        }
    
        // If the current state is in the final state, then the string is valid
        if (finalStates.contains(currentState)) {
            System.out.println("Output: Your string is valid");
            return 0; // Return 0 to indicate success
        } else {
            // String ends at a state which is not a final state
            Utility.showError(String.format("Your string ends at state '%s', which is not a final state", currentState));
            return counter; // Return counter to indicate False
        }
    }
    
    

    public Set<String> getReachableStates(String state, Set<String> reachableStates) {

        reachableStates.add(state);

        HashMap<String, String> currentStateTransition = transitions.get(state);
        // iterating over all the transitions of current state
        for (String transitionState : currentStateTransition.values()) {
            // if next state is not already in reachableStates set
            if (!reachableStates.contains(transitionState)) {
                // add next state to reachableStates set
                reachableStates.addAll(getReachableStates(transitionState, reachableStates));
            }
        }

        return reachableStates;
    }

    public void replaceState(String state, String replaceWith, boolean sameType) {
        // replace state in transitions
        for (HashMap<String, String> currentStateTransition : transitions.values()) {
            for (Map.Entry<String, String> entry : currentStateTransition.entrySet()) {
                String input = entry.getKey();
                String nextState = entry.getValue();
    
                // Check if the states should be of the same type for merging
                if (sameType) {
                    boolean isFinalState = finalStates.contains(nextState);
                    boolean isFinalReplaceWith = finalStates.contains(replaceWith);
    
                    if (isFinalState != isFinalReplaceWith) {
                        continue; // Skip this transition if the types are different
                    }
                }
    
                if (nextState.equals(state)) {
                    currentStateTransition.put(input, replaceWith);
                }
            }
        }
    }
    
    public void minimizeDFA() {
        System.out.println("Minimizing DFA...");
        // get reachableStates states from start state
        Set<String> reachableStates = getReachableStates(startingState, new HashSet<>());
        System.out.println("Removing unreachable states from transitions...");
    
        // Step 1: remove unreachable states from transitions
        transitions.entrySet().removeIf(entry -> !reachableStates.contains(entry.getKey()));
        System.out.println("Transitions after removing unreachable states: " + transitions);
    
        System.out.println("Merging states with similar transitions...");
    
        // Step 2: merge states with similar transitions
        ArrayList<String> mergedStates = new ArrayList<>();
    
        for (String state : reachableStates) {
            if (!mergedStates.contains(state)) {
                for (String otherState : reachableStates) {
                    if (!state.equals(otherState) && !mergedStates.contains(otherState)) {
                        // Check if the transitions have the same type (final or non-final) for all inputs
                        // and if the states themselves have the same type
                        if (transitionsSameType(transitions.get(state), transitions.get(otherState))
                                && stateTypeSame(state, otherState)) {
                            // merge other state with the current state
                            replaceState(otherState, state, true); // Pass true to indicate same type requirement
                            // mark other state as merged
                            mergedStates.add(otherState);
                        }
                    }
                }
            }
        }
    
        // Step 3: remove merged states
        transitions.keySet().removeAll(mergedStates);
    
        // Update final states after minimization to include only existing states
        finalStates.retainAll(transitions.keySet());
    
        // Display final states after minimization
        System.out.println("Minimized Transitions: " + transitions);
        System.out.println("Final states after minimization: " + finalStates);
        System.out.println("New DFA after minimization:");
        System.out.println("Transitions: " + transitions);
    }
    
    
    // Helper function to check if the type of two states is the same (final or non-final)
    private boolean stateTypeSame(String state1, String state2) {
        boolean isFinalState1 = finalStates.contains(state1);
        boolean isFinalState2 = finalStates.contains(state2);
        return isFinalState1 == isFinalState2;
    }
    

    public boolean transitionsSameType(HashMap<String, String> transition1, HashMap<String, String> transition2) {
        // Iterate over the inputs in the transitions
        for (String input : transition1.keySet()) {
            // Get the next states after applying the input for both transitions
            String nextState1 = transition1.get(input);
            String nextState2 = transition2.get(input);
    
            // Check if the next states have the same type (final or non-final)
            boolean isFinalState1 = finalStates.contains(nextState1);
            boolean isFinalState2 = finalStates.contains(nextState2);
    
            // If the type of the next states is different for the same input, transitions are not equal
            if (isFinalState1 != isFinalState2) {
                return false;
            }
        }
        // All input transitions have the same type (final or non-final) for both states
        return true;
    }
    

    
    public String[] generateStrings() {
        // Check if there is only one state, and it is both the starting and final state
        if (finalStates.size() == transitions.size() && finalStates.contains(startingState)) {
            return generateSpecialCaseStrings(4); // Change the count as needed
        }
        
        String acceptedString1 = generateAcceptedString(6);
        String acceptedString2 = generateAcceptedString(6);

        
        String notAcceptedString1 = generateNotAcceptedString(6);
        String notAcceptedString2 = generateNotAcceptedString(6);
        
        return new String[]{acceptedString1, acceptedString2, notAcceptedString1, notAcceptedString2};
    }
    
    

    private String[] generateSpecialCaseStrings(int count) {
    List<String> generatedStrings = new ArrayList<>();

    for (int i = 0; i < count; i++) {
        StringBuilder generatedString = new StringBuilder();
        String currentState = startingState;

        while (generatedString.length() < 6) {
            HashMap<String, String> currentStateTransitions = transitions.get(currentState);
            if (currentStateTransitions == null || currentStateTransitions.isEmpty()) {
                break; // Handle the case when there are no transitions for the current state
            }
            String[] inputs = currentStateTransitions.keySet().toArray(new String[0]);
            String randomInput = inputs[(int) (Math.random() * inputs.length)];
            generatedString.append(randomInput);
            currentState = currentStateTransitions.get(randomInput);
        }

        generatedStrings.add(generatedString.toString());
    }

    return generatedStrings.toArray(new String[0]);
}
    
    
    
    private String generateAcceptedString(int minLength) {
        int minimum = minLength;
        StringBuilder generatedString = new StringBuilder();
        String currentState = startingState;
        int steps = 4;
        int stepCounter = 0;

        //this while loop make sure to have at most 4 strings generated in each Minimumlength
        while (stepCounter < steps) {

            //generate the string with the specified lenght
            while (generatedString.length() <= minimum) {
                HashMap<String, String> currentStateTransitions = transitions.get(currentState);
    
                String[] inputs = currentStateTransitions.keySet().toArray(new String[0]);
                String randomInput = inputs[(int) (Math.random() * inputs.length)];
    
                generatedString.append(randomInput);
                currentState = currentStateTransitions.get(randomInput);
            }

            //check if the last state isn't final and if True repeat the generation
            if (!finalStates.contains(currentState)) {
                currentState = startingState;
                generatedString.setLength(0);
                ;
            } 
            //return the not accepted string
            else {
                return generatedString.toString();
            }
            stepCounter++;
        }
    
        //recursive call to the function if the string lenght was 0 for all the steps that means there is no 
        //accepted string in this length
        if (generatedString.length() == 0) {
            minimum--;
            return generateAcceptedString(minimum); // Fix: Return the result of the recursive call
        }
    
        return generatedString.toString();
    }

    
    //generate the not accepted String and validate it
    public String generateNotAcceptedString(int minLength) {
        int minimum = minLength;
        StringBuilder generatedString = new StringBuilder();
        String currentState = startingState;
        int steps = 4;
        int stepCounter = 0;


        //this while loop make sure to have at most 4 strings generated in each Minimumlength
        while (stepCounter < steps) {

            //generate the string with the specified lenght
            while (generatedString.length() <= minimum) {
                HashMap<String, String> currentStateTransitions = transitions.get(currentState);
    
                String[] inputs = currentStateTransitions.keySet().toArray(new String[0]);
                String randomInput = inputs[(int) (Math.random() * inputs.length)];
    
                generatedString.append(randomInput);
                currentState = currentStateTransitions.get(randomInput);
            }

            //check if the last state isn't final for the string to be rejected and set the string to empty if 
            //it is accepted
            if (finalStates.contains(currentState)) {
                currentState = startingState;
                generatedString.setLength(0);
                ;
            } 
            //return the not accepted string
            else {
                return generatedString.toString();
            }
            stepCounter++;
        }
    
        //recursive call to the function if the string lenght was 0 for all the steps that means there is no
        //rejected string in this length so it decrement the Mininum length and call the function with the new Length
        if (generatedString.length() == 0) {
            minimum--;
            return generateNotAcceptedString(minimum); // Fix: Return the result of the recursive call
        }
    
        return generatedString.toString();
    }
    

    


}