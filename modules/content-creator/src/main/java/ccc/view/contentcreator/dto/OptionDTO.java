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
package ccc.view.contentcreator.dto;

import java.io.Serializable;
import java.util.List;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class OptionDTO<T> implements Serializable {


    /**
     * TODO Add Description for this type.
     *
     * @author Civic Computing Ltd.
     */
    public enum Type {
        TEXT_SINGLE_LINE, CHOICES

    }

    private T _currentValue;
    private Type _type;
    private List<T> _choices;
    private boolean _hasChanged = false;

    private OptionDTO() { super(); }

    /**
     * Constructor.
     *
     * @param currentValue
     * @param choices
     * @param type
     */
    public OptionDTO(final T currentValue,
                     final List<T> choices,
                     final Type type) {

        _currentValue = currentValue;
        _type = type;
        _choices = choices;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public T getCurrentValue() {
        return _currentValue;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public Type getType() {
        return _type;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public List<T> getChoices() {
        return _choices;
    }

    /**
     * TODO: Add a description of this method.
     * TODO: Better way to do this???
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public <R> OptionDTO<R> makeTypeSafe() {
        return (OptionDTO<R>) this;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param expected
     */
    public void setCurrentValue(final T newCurrentValue) {
        if ((null==newCurrentValue && null==_currentValue)
            || (null!=newCurrentValue && newCurrentValue.equals(_currentValue))) {
            return;
        } else {
            _currentValue = newCurrentValue;
            _hasChanged = true;
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public boolean hasChanged() {
        return _hasChanged;
    }

}
