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
            return counter; // Return 1 to indicate False
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

    public void replaceState(String state, String replaceWith) {
        // replace state in transitions
        for (HashMap<String, String> currentStateTransition : transitions.values()) {
            for (Map.Entry<String, String> entry : currentStateTransition.entrySet()) {
                String input = entry.getKey();
                String nextState = entry.getValue();
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
                        if (transitionsEqual(transitions.get(state), transitions.get(otherState))) {
                            // merge other state with the current state
                            replaceState(otherState, state);
                            // mark other state as merged
                            mergedStates.add(otherState);
                        }
                    }
                }
            }
        }

        // Step 3: remove merged states
        transitions.keySet().removeAll(mergedStates);

        System.out.println("Minimized Transitions: " + transitions);
    }

    public boolean transitionsEqual(HashMap<String, String> transition1, HashMap<String, String> transition2) {
        if (transition1.size() != transition2.size()) {
            return false;
        }

        for (String input : transition1.keySet()) {
            String nextState1 = transition1.get(input);
            String nextState2 = transition2.get(input);

            boolean isFinalState1 = finalStates.contains(nextState1);
            boolean isFinalState2 = finalStates.contains(nextState2);

            if (isFinalState1 != isFinalState2) {
                return false;
            }
        }

        return true;
    }

    
    public String[] generateStrings() {
        // Check if there is only one state, and it is both the starting and final state
        if (finalStates.size() == 1 && finalStates.contains(startingState) && transitions.size() == 1) {
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
        StringBuilder generatedString = new StringBuilder();
        String currentState = startingState;
    
        while (!finalStates.contains(currentState) || generatedString.length() < minLength) {
            HashMap<String, String> currentStateTransitions = transitions.get(currentState);
            String[] inputs = currentStateTransitions.keySet().toArray(new String[0]);
            String randomInput = inputs[(int) (Math.random() * inputs.length)];
            generatedString.append(randomInput);
            currentState = currentStateTransitions.get(randomInput);
        }
    
        return generatedString.toString();
    }
    
    private String generateNotAcceptedString(int minLength) {
        StringBuilder generatedString = new StringBuilder();
        String currentState = startingState;
    
        while (generatedString.length() < minLength) {
            HashMap<String, String> currentStateTransitions = transitions.get(currentState);
            String[] inputs = currentStateTransitions.keySet().toArray(new String[0]);
            String randomInput = inputs[(int) (Math.random() * inputs.length)];
            generatedString.append(randomInput);
            currentState = currentStateTransitions.get(randomInput);
    
            // If the generated string reaches a final state, remove the last input and continue
            if (finalStates.contains(currentState)) {
                generatedString.deleteCharAt(generatedString.length() - 1);
            }
        }
    
        return generatedString.toString();
    }
    
    


}