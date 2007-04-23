/*
 * @(#)AbstractIntelliHints.java 7/24/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */
package com.jidesoft.hints;

import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.DelegateAction;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;


/**
 * <code>AbstractIntelliHints</code> is an abstract implementation of {@link com.jidesoft.hints.IntelliHints}. It covers
 * functions such as showing the hint popup at the correct position, delegating keystrokes,
 * updating and selecting hint. The only thing that is left out to subclasses
 * is the creation of the hint popup.
 *
 * @author Santhosh Kumar T
 * @author JIDE Software, Inc.
 */
public abstract class AbstractIntelliHints implements IntelliHints {

    /**
     * The key of a client property. If a component has intellihints registered, you can use this client
     * property to get the IntelliHints instance.
     */
    public static final String CLIENT_PROPERTY_INTELLI_HINTS = "INTELLI_HINTS"; //NOI18N

    private JidePopup _popup;
    private JTextComponent _textComponent;

    private boolean _followCaret = false;

    // we use this flag to workaround the bug that setText() will trigger the hint popup.
    private boolean _keyTyped = false;

    /**
     * Creates an IntelliHints object for a given JTextComponent.
     *
     * @param textComponent the text component.
     */
    public AbstractIntelliHints(JTextComponent textComponent) {
        _textComponent = textComponent;
        getTextComponent().putClientProperty(CLIENT_PROPERTY_INTELLI_HINTS, this);

        _popup = createPopup();

        getTextComponent().getDocument().addDocumentListener(documentListener);
        getTextComponent().addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if (KeyEvent.VK_ESCAPE != e.getKeyCode()) {
                    setKeyTyped(true);
                }
            }
        });
        getTextComponent().addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
                Container topLevelAncestor = _popup.getTopLevelAncestor();
                if (topLevelAncestor == null) {
                    return;
                }
                Component oppositeComponent = e.getOppositeComponent();
                if (topLevelAncestor == oppositeComponent || topLevelAncestor.isAncestorOf(oppositeComponent)) {
                    return;
                }
                hideHintsPopup();
            }
        });

        DelegateAction.replaceAction(getTextComponent(), JComponent.WHEN_FOCUSED, getShowHintsKeyStroke(), showAction);

        KeyStroke[] keyStrokes = getDelegateKeyStrokes();
        for (int i = 0; i < keyStrokes.length; i++) {
            KeyStroke keyStroke = keyStrokes[i];
            DelegateAction.replaceAction(getTextComponent(), JComponent.WHEN_FOCUSED, keyStroke, new LazyDelegateAction(keyStroke));
        }

        getDelegateComponent().setRequestFocusEnabled(false);
        getDelegateComponent().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                hideHintsPopup();
                setHintsEnabled(false);
                acceptHint(getSelectedHint());
                setHintsEnabled(true);
            }
        });
    }

    protected JidePopup createPopup() {
        JidePopup popup = new JidePopup();
        popup.setLayout(new BorderLayout());
        popup.setResizable(true);
        popup.setPopupBorder(BorderFactory.createLineBorder(UIDefaultsLookup.getColor("controlDkShadow"), 1));
        popup.setMovable(false);
        popup.add(createHintsComponent());
        popup.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                DelegateAction.restoreAction(getTextComponent(), JComponent.WHEN_FOCUSED, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), hideAction);
                DelegateAction.restoreAction(getTextComponent(), JComponent.WHEN_FOCUSED, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), acceptAction);
            }

            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        popup.setTransient(true);
        return popup;
    }

    public JTextComponent getTextComponent() {
        return _textComponent;
    }


    /**
     * After user has selected a item in the hints popup, this method will update JTextComponent accordingly
     * to accept the hint.
     * <p/>
     * For JTextArea, the default implementation will insert the hint into current caret position.
     * For JTextField, by default it will replace the whole content with the item user selected. Subclass can
     * always choose to override it to accept the hint in a different way. For example, {@link com.jidesoft.hints.FileIntelliHints}
     * will append the selected item at the end of the existing text in order to complete a full file path.
     */
    public void acceptHint(Object selected) {
        if (selected == null)
            return;

        if (getTextComponent() instanceof JTextArea) {
            int pos = getTextComponent().getCaretPosition();
            String text = getTextComponent().getText();
            int start = text.lastIndexOf("\n", pos - 1);
            String remain = pos == -1 ? "" : text.substring(pos);
            text = text.substring(0, start + 1);
            text += selected;
            pos = text.length();
            text += remain;
            getTextComponent().setText(text);
            getTextComponent().setCaretPosition(pos);
        }
        else {
            String hint = "" + selected;
            getTextComponent().setText(hint);
            getTextComponent().setCaretPosition(hint.length());
        }
    }

    /**
     * Shows the hints popup which contains the hints.
     * It will call {@link #updateHints(Object)}. Only if it returns true,
     * the popup will be shown.
     */
    protected void showHintsPopup() {
        if (getTextComponent().isEnabled() && getTextComponent().hasFocus() && updateHints(getContext())) {
            DelegateAction.replaceAction(getTextComponent(), JComponent.WHEN_FOCUSED, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), hideAction);
            DelegateAction.replaceAction(getTextComponent(), JComponent.WHEN_FOCUSED, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), acceptAction, true);

            int x = 0;
            int y = 0;
            int height = 0;

            try {
                int pos = getCaretPositionForPopup();
                Rectangle position = getCaretRectangleForPopup(pos);
                y = position.y;
                x = position.x;
                height = position.height;
            }
            catch (BadLocationException e) {
                // this should never happen!!!
                e.printStackTrace();
            }

            _popup.setOwner(getTextComponent());
            _popup.showPopup(new Insets(y, x, getTextComponent().getHeight() - height - y, 0));
        }
        else {
            _popup.hidePopup();
        }
    }

    /**
     * Gets the caret rectangle where caret is displayed. The popup will be show around the area so that the returned rectangle area
     * is always visible. This method will be called twice.
     *
     * @param caretPosition the caret position.
     * @return the popup position relative to the text component. <br>Please note, this position is actually a rectangle area. The reason is the popup could be
     *         shown below or above the rectangle. Usually, the popup will be shown below the rectangle. In this case, the x and y of the rectangle will
     *         be the top-left corner of the popup. However if there isn't enough space for the popup because it's close to screen bottom border, we will
     *         show the popup above the rectangle. In this case, the bottom-left corner of the popup will be at x and (y - height). Simply speaking,
     *         the popup will never cover the area specified by the rectangle (either below it or above it).
     * @throws BadLocationException if the given position does not represent a valid location in the associated document.
     */
    protected Rectangle getCaretRectangleForPopup(int caretPosition) throws BadLocationException {
        return getTextComponent().getUI().modelToView(getTextComponent(), caretPosition);
    }


    /**
     * Gets the caret position which is used as the anchor point to display the popup.
     * By default, it {@link #isFollowCaret()} is true, it will return caret position.
     * Otherwise it will return the caret position at the beginning of the caret line.
     * Subclass can override to return any caret position.
     *
     * @return the caret position which is used as the anchor point to display the popup.
     */
    protected int getCaretPositionForPopup() {
        int caretPosition = Math.min(getTextComponent().getCaret().getDot(), getTextComponent().getCaret().getMark());
        if (isFollowCaret()) {
            return caretPosition;
        }
        else {
            try {
                Rectangle viewRect = getTextComponent().getUI().modelToView(getTextComponent(), caretPosition);
                viewRect.x = 0;
                return getTextComponent().getUI().viewToModel(getTextComponent(), viewRect.getLocation());
            }
            catch (BadLocationException e) {
                return 0;
            }
        }
    }

    /**
     * Gets the context for hints. The context is the information that IntelliHints needs
     * in order to generate a list of  hints. For example, for code-completion, the context is
     * current word the cursor is on. for file completion, the context is the full string starting from
     * the file system root.
     * <p>We provide a default context in AbstractIntelliHints. If it's a JTextArea,
     * the context will be the string at the caret line from line beginning to the caret position. If it's a JTextField,
     * the context will be whatever string in the text field. Subclass can always
     * override it to return the context that is appropriate.
     *
     * @return the context.
     */
    protected Object getContext() {
        if (getTextComponent() instanceof JTextArea) {
            int pos = getTextComponent().getCaretPosition();
            if (pos == 0) {
                return "";
            }
            else {
                String text = getTextComponent().getText();
                int start = text.lastIndexOf("\n", pos - 1);
                return text.substring(start + 1, pos);
            }
        }
        else {
            return getTextComponent().getText();
        }
    }

    /**
     * Hides the hints popup.
     */
    protected void hideHintsPopup() {
        if (_popup != null) {
            _popup.hidePopup();
        }
        setKeyTyped(false);
    }

    /**
     * Enables or disables the hints popup.
     *
     * @param enabled true to enable the hints popup. Otherwise false.
     */
    public void setHintsEnabled(boolean enabled) {
        if (!enabled) {
            // disable show hint temporarily
            getTextComponent().getDocument().removeDocumentListener(documentListener);
        }
        else {
            // enable show hint again
            getTextComponent().getDocument().addDocumentListener(documentListener);
        }

    }

    /**
     * Checks if the hints popup is visible.
     *
     * @return true if it's visible. Otherwise, false.
     */
    public boolean isHintsPopupVisible() {
        return _popup != null && _popup.isPopupVisible();
    }

    /**
     * Should the hints popup follows the caret.
     *
     * @return true if the popup shows up right below the caret. False if the popup always shows
     *         at the bottom-left corner (or top-left if there isn't enough on the bottom of the screen)
     *         of the JTextComponent.
     */
    public boolean isFollowCaret() {
        return _followCaret;
    }

    /**
     * Sets the position of the hints popup. If followCaret is true, the popup
     * shows up right below the caret. Otherwise, it will stay at the bottom-left corner
     * (or top-left if there isn't enough on the bottom of the screen) of JTextComponent.
     *
     * @param followCaret true or false.
     */
    public void setFollowCaret(boolean followCaret) {
        _followCaret = followCaret;
    }


    /**
     * Gets the delegate keystrokes.
     * <p/>
     * When hint popup is visible, the keyboard focus never leaves the text component.
     * However the hint popup usually contains a component that user will try to use navigation key to
     * select an item. For example, use UP and DOWN key to navigate the list.
     * Those keystrokes, if the popup is visible, will be delegated
     * to the the component that returns from {@link #getDelegateComponent()}.
     *
     * @return an array of keystrokes that will be delegate to {@link #getDelegateComponent()} when hint popup is shown.
     */
    abstract protected KeyStroke[] getDelegateKeyStrokes();

    /**
     * Gets the delegate component in the hint popup.
     *
     * @return the component that will receive the keystrokes that are delegated to hint popup.
     */
    abstract protected JComponent getDelegateComponent();

    /**
     * Gets the keystroke that will trigger the hint popup. Usually the hints popup
     * will be shown automatically when user types. Only when the hint popup is hidden
     * accidentally, this keystroke will show the popup again.
     * <p/>
     * By default, it's the DOWN key for JTextField and CTRL+SPACE for JTextArea.
     *
     * @return the keystroek that will trigger the hint popup.
     */
    protected KeyStroke getShowHintsKeyStroke() {
        if (getTextComponent() instanceof JTextField) {
            return KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
        }
        else {
            return KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_MASK);
        }
    }

    private DelegateAction acceptAction = new DelegateAction() {
        public boolean delegateActionPerformed(ActionEvent e) {
            JComponent tf = (JComponent) e.getSource();
            AbstractIntelliHints hints = (AbstractIntelliHints) tf.getClientProperty(CLIENT_PROPERTY_INTELLI_HINTS);
            if (hints != null) {
                hints.hideHintsPopup();
                if (hints.getSelectedHint() != null) {
                    hints.setHintsEnabled(false);
                    hints.acceptHint(hints.getSelectedHint());
                    hints.setHintsEnabled(true);
                    return true;
                }
                else if (getTextComponent().getRootPane() != null) {
                    JButton button = getTextComponent().getRootPane().getDefaultButton();
                    if (button != null) {
                        button.doClick();
                        return true;
                    }
                }
            }
            return false;
        }
    };

    private static DelegateAction showAction = new DelegateAction() {
        public boolean delegateActionPerformed(ActionEvent e) {
            JComponent tf = (JComponent) e.getSource();
            AbstractIntelliHints hints = (AbstractIntelliHints) tf.getClientProperty(CLIENT_PROPERTY_INTELLI_HINTS);
            if (hints != null && tf.isEnabled() && !hints.isHintsPopupVisible()) {
                hints.showHintsPopup();
                return true;
            }
            return false;
        }
    };

    private DelegateAction hideAction = new DelegateAction() {
        public boolean isEnabled() {
            return _textComponent.isEnabled() && isHintsPopupVisible();
        }

        public boolean delegateActionPerformed(ActionEvent e) {
            if (isEnabled()) {
                hideHintsPopup();
                return true;
            }
            return false;
        }
    };

    private DocumentListener documentListener = new DocumentListener() {
        private Timer timer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isKeyTyped()) {
                    showHintsPopup();
                    setKeyTyped(false);
                }
            }
        });

        public void insertUpdate(DocumentEvent e) {
            startTimer();
        }

        public void removeUpdate(DocumentEvent e) {
            startTimer();
        }

        public void changedUpdate(DocumentEvent e) {
        }

        void startTimer() {
            if (timer.isRunning()) {
                timer.restart();
            }
            else {
                timer.setRepeats(false);
                timer.start();
            }
        }
    };

    private boolean isKeyTyped() {
        return _keyTyped;
    }

    private void setKeyTyped(boolean keyTyped) {
        _keyTyped = keyTyped;
    }

    private static class LazyDelegateAction extends DelegateAction {
        private KeyStroke _keyStroke;

        public LazyDelegateAction(KeyStroke keyStroke) {
            _keyStroke = keyStroke;
        }

        public boolean delegateActionPerformed(ActionEvent e) {
            JComponent tf = (JComponent) e.getSource();
            AbstractIntelliHints hints = (AbstractIntelliHints) tf.getClientProperty(CLIENT_PROPERTY_INTELLI_HINTS);
            if (hints != null && tf.isEnabled()) {
                if (hints.isHintsPopupVisible()) {
                    Object key = hints.getDelegateComponent().getInputMap().get(_keyStroke);
                    key = key == null ? hints.getTextComponent().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(_keyStroke) : key;
                    if (key != null) {
                        Object action = hints.getDelegateComponent().getActionMap().get(key);
                        if (action instanceof Action) {
                            ((Action) action).actionPerformed(new ActionEvent(hints.getDelegateComponent(), 0, "" + key));
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }
}