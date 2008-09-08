/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dto;

import java.io.Serializable;
import java.util.List;


/**
 * A dto that describes an option.
 * @param <T> The type of the option.
 *
 * @author Civic Computing Ltd.
 */
public class OptionDTO<T extends DTO> implements Serializable {


    /** serialVersionUID : long. */
    private static final long serialVersionUID = 1L;

    /**
     * Valid types for an {@link OptionDTO}.
     *
     * @author Civic Computing Ltd.
     */
    public enum Type {
        /** TEXT_SINGLE_LINE : Type. */
        TEXT_SINGLE_LINE,
        /** CHOICES : Type. */
        CHOICES
    }

    private T _currentValue;
    private Type _type;
    private List<T> _choices;
    private boolean _hasChanged = false;

    @SuppressWarnings("unused") // Necessary for serialisation.
    private OptionDTO() { super(); }

    /**
     * Constructor.
     *
     * @param currentValue The current value of the option.
     * @param choices The possible values for the option.
     * @param type The type of the option.
     */
    public OptionDTO(final T currentValue,
                     final List<T> choices,
                     final Type type) {

        _currentValue = currentValue;
        _type = type;
        _choices = choices;
    }

    /**
     * Accessor for the current value.
     *
     * @return The current value of this option.
     */
    public T getCurrentValue() {
        return _currentValue;
    }

    /**
     * Accessor for type.
     *
     * @return The type of this option.
     */
    public Type getType() {
        return _type;
    }

    /**
     * Retrieve a list of the valid choices for this option.
     *
     * @return A list of choices.
     */
    public List<T> getChoices() {
        return _choices;
    }

    /**
     * Perform an explicit cast to convert the type parameter for this object.
     * TODO: Better way to do this???
     *
     * @param <R> The type parameter for the {@link OptionDTO}.
     * @return 'this' as type OptionDTO<R>.
     */
    @SuppressWarnings("unchecked")
    public <R extends DTO> OptionDTO<R> makeTypeSafe() {
        return (OptionDTO<R>) this;
    }

    /**
     * Set the current value.
     *
     * @param newCurrentValue The new current value.
     */
    public void setCurrentValue(final T newCurrentValue) {
        if ((null==newCurrentValue && null==_currentValue)
            || (null!=newCurrentValue
                && newCurrentValue.equals(_currentValue))) {
            return;
        }

        _currentValue = newCurrentValue;
        _hasChanged = true;
    }

    /**
     * Query whether the current value has changed.
     *
     * @return true if the current value has changed, false otherwise.
     */
    public boolean hasChanged() {
        return _hasChanged;
    }

}
