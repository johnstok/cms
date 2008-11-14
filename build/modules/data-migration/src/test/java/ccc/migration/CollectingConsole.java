package ccc.migration;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
final class CollectingConsole implements Console {

    private List<String> _inputList = new ArrayList<String>();

    /** {@inheritDoc} */
    @Override
    public void print(final String input) {
        _inputList.add(input);
    }
}
