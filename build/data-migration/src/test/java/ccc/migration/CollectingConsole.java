package ccc.migration;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
final class CollectingConsole implements Console {

    List<String> inputList = new ArrayList<String>();

    @Override
    public void print(String input) {
        inputList.add(input);
    }
    
}