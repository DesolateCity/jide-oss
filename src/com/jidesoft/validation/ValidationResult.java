/*
 * @(#)ValidationResult.java
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */
package com.jidesoft.validation;

/**
 * ValidationResult is the object that returns from the {@link Validator#validating(ValidationObject)}.
 * There are three things on the result.
 * <ul>
 * <li> valid: whether the result is valid. It can be true or false.
 * <li> id: an int value of the result. It's better to reserve value 0 for valid result.
 * For invalid result, you can use whatever value as long as it's consistent across your application.
 * <li> message: a String value of result. You can use this string value to put a message to indicate why the validation
 * failed.
 * </ul>
 * Users can extend this class to create their own ValidationResult to provide
 * additional information that needed to be returned from Validator.
 */
public class ValidationResult {
    private int _id;
    private boolean _valid;
    private int _failBehavior = FAIL_BEHAVIOR_REVERT;
    private String _message;

    /**
     * When validation fails, reverts back to the previous valid value.
     */
    public final static int FAIL_BEHAVIOR_REVERT = 0;

    /**
     * When validation fails, do not stop cell editing until user enters a valid value or press ESCAPE to cancel the editing.
     */
    public final static int FAIL_BEHAVIOR_PERSIST = 1;

    /**
     * When validation fails, reset the value to null.
     */
    public final static int FAIL_BEHAVIOR_RESET = 2;

    /**
     * The shared ValidationResult when the validation result is valid.
     */
    public static final ValidationResult OK = new ValidationResult(true);

    /**
     * Creates an empty ValidationResult. The valid is set to false.
     */
    public ValidationResult() {
        this(false);
    }

    /**
     * Creates an invalid ValidationResult with an id and no message.
     *
     * @param id
     */
    public ValidationResult(int id) {
        this(id, false, null);
    }

    /**
     * Creates an empty ValidationResult.
     *
     * @param valid
     */
    public ValidationResult(boolean valid) {
        this(0, valid, null);
    }

    /**
     * Creates an invalid ValidationResult with an id and a message.
     *
     * @param id
     * @param message
     */
    public ValidationResult(int id, String message) {
        this(id, false, message);
    }

    /**
     * Creates an ValidationResult with an id and a message.
     *
     * @param id
     * @param valid
     * @param message
     */
    public ValidationResult(int id, boolean valid, String message) {
        _id = id;
        _valid = valid;
        _message = message;
    }

    /**
     * Creates an ValidationResult with an id and an error behavior.
     *
     * @param id
     * @param valid
     * @param failBehavoir
     */
    public ValidationResult(int id, boolean valid, int failBehavoir) {
        _id = id;
        _valid = valid;
        _failBehavior = failBehavoir;
    }

    /**
     * Creates an ValidationResult with an id, a message and an error behavior.
     *
     * @param id
     * @param valid
     * @param failBehavoir
     * @param message
     */
    public ValidationResult(int id, boolean valid, int failBehavoir, String message) {
        _id = id;
        _valid = valid;
        _failBehavior = failBehavoir;
        _message = message;
    }

    /**
     * Gets the id of the ValidationResult.
     *
     * @return the id.
     */
    public int getId() {
        return _id;
    }

    /**
     * Sets the id of the ValidationResult.
     *
     * @param id
     */
    public void setId(int id) {
        _id = id;
    }

    /**
     * Checks if the validation state is valid.
     *
     * @return the validation state. True means valid. Otherwise, false.
     */
    public boolean isValid() {
        return _valid;
    }

    /**
     * Sets the validation state.
     *
     * @param valid
     */
    public void setValid(boolean valid) {
        _valid = valid;
    }

    /**
     * Gets the message associated with the ValidationResult.
     *
     * @return the message.
     */
    public String getMessage() {
        return _message;
    }

    /**
     * Sets the message associated with the ValidationResult.
     *
     * @param message the new message.
     */
    public void setMessage(String message) {
        _message = message;
    }

    /**
     * Gets the behavior if validation fails.
     *
     * @return the behavior if validation fails.
     */
    public int getFailBehavior() {
        return _failBehavior;
    }

    /**
     * Sets the behavior if validation fails. Valid values are {@link #FAIL_BEHAVIOR_PERSIST}, {@link #FAIL_BEHAVIOR_REVERT}, and {@link #FAIL_BEHAVIOR_REVERT}.
     *
     * @param failBehavior
     */
    public void setFailBehavior(int failBehavior) {
        _failBehavior = failBehavior;
    }

    public String toString() {
        String properties =
                " id=" + getId() + " message=" + getMessage() + " ";
        return getClass().getName() + "[" + properties + "]";
    }
}
