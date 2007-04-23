/*
 * @(#)$FileName Copyright 2002 - 2004 JIDE Software Inc. All rights reserved.
 */

package com.jidesoft.plaf.office2003;

import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.plaf.vsnet.VsnetJideTabbedPaneUI;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.JideTabbedPane;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * JideTabbedPane UI implementation
 */
public class Office2003JideTabbedPaneUI extends VsnetJideTabbedPaneUI {
    public void installColorTheme() {
        super.installColorTheme();

        if (_tabPane.getColorTheme() != JideTabbedPane.COLOR_THEME_OFFICE2003)
            return;

        switch (getTabShape()) {
            case JideTabbedPane.SHAPE_ROUNDED_FLAT:
                _selectColor1 = getPainter().getControlShadow();
                _selectColor2 = getPainter().getControlShadow();
                _unselectColor1 = _selectColor1;
                _unselectColor2 = _selectColor2;
                break;
            case JideTabbedPane.SHAPE_BOX:
                _selectColor1 = getPainter().getControlShadow();
                _selectColor2 = getPainter().getControlShadow();
                _unselectColor1 = getPainter().getControlShadow();
                _unselectColor2 = _lightHighlight;
                break;
            case JideTabbedPane.SHAPE_EXCEL:
                _selectColor1 = getPainter().getControlShadow();
                _selectColor2 = null;
                _selectColor3 = null;
                _unselectColor1 = getPainter().getControlShadow();
                _unselectColor2 = null;
                _unselectColor3 = null;
                break;
            case JideTabbedPane.SHAPE_WINDOWS:
            case JideTabbedPane.SHAPE_WINDOWS_SELECTED:
                _selectColor1 = _lightHighlight;
                _selectColor2 = getPainter().getControlDk();
                _selectColor3 = getPainter().getControlShadow();
                _unselectColor1 = _selectColor1;
                _unselectColor2 = _selectColor2;
                _unselectColor3 = _selectColor3;
                break;
            case JideTabbedPane.SHAPE_VSNET:
                _selectColor1 = getPainter().getControlShadow();
                _selectColor2 = getPainter().getControlShadow();
                _unselectColor1 = getPainter().getControlShadow();
                break;
            case JideTabbedPane.SHAPE_OFFICE2003:
            default:
                _selectColor1 = getPainter().getControlShadow();
                _unselectColor1 = getPainter().getControlShadow();
                _unselectColor2 = _lightHighlight;
                _unselectColor3 = getPainter().getControlDk();
                break;
        }

        installBackgroundColor();

    }

    protected void installBackgroundColor() {
        if (_tabPane.getColorTheme() == JideTabbedPane.COLOR_THEME_OFFICE2003) {
            _backgroundSelectedColorStart = getPainter().getSelectionSelectedLt();
            _backgroundSelectedColorEnd = getPainter().getSelectionSelectedDk();
            if (getTabShape() == JideTabbedPane.SHAPE_BOX) {
                _backgroundUnselectedColorStart = null;
                _backgroundUnselectedColorEnd = null;
            }
            else {
                _backgroundUnselectedColorStart = getPainter().getBackgroundLt();
                _backgroundUnselectedColorEnd = getPainter().getBackgroundDk();
            }
        }
        else {
            super.installBackgroundColor();
        }
    }

    // paint the background of the _tabScroller
    public void paintBackground(Graphics g, Component c) {
        if (_tabPane.isOpaque()) {
            int width = c.getWidth();
            int height = c.getHeight();
            int h = 0;
            int w = 0;
            Graphics2D g2d = (Graphics2D) g;

            if (_tabPane.getTabCount() > 0) {
                h = _tabPane.getBoundsAt(0).height;
                w = _tabPane.getBoundsAt(0).width;
            }
            else {
                return;
            }

            int temp1 = -1;
            int temp2 = -1;
            if (isTabLeadingComponentVisible()) {
                if (h < _tabLeadingComponent.getSize().height) {
                    h = _tabLeadingComponent.getSize().height;
                    temp1 = _tabLeadingComponent.getSize().height;
                }
                if (w < _tabLeadingComponent.getSize().width) {
                    w = _tabLeadingComponent.getSize().width;
                    temp2 = _tabLeadingComponent.getSize().width;
                }
            }

            if (isTabTrailingComponentVisible()) {
                if (h < _tabTrailingComponent.getSize().height && temp1 < _tabTrailingComponent.getSize().height) {
                    h = _tabTrailingComponent.getSize().height;
                }
                if (w < _tabTrailingComponent.getSize().width && temp2 < _tabTrailingComponent.getSize().width) {
                    w = _tabTrailingComponent.getSize().width;
                }
            }

            if (getTabShape() != JideTabbedPane.SHAPE_BOX && _tabPane.getColorTheme() == JideTabbedPane.COLOR_THEME_OFFICE2003) {// the color set is office2003
                Rectangle rect = null;
                if (_tabPane.getTabPlacement() == TOP) {
                    rect = new Rectangle(0, 0, width, h + 2);

                }
                else if (_tabPane.getTabPlacement() == BOTTOM) {
                    rect = new Rectangle(0, height - h - 2, width, h + 2);

                }
                else if (_tabPane.getTabPlacement() == LEFT) {
                    rect = new Rectangle(0, 0, w + 2, height);

                }
                else if (_tabPane.getTabPlacement() == RIGHT) {
                    rect = new Rectangle(width - w - 2, 0, w + 2, height);
                }
                if (rect != null) {
                    paintTabAreaBackground(g, rect, _tabPane.getTabPlacement());
                }
            }
            else {
                super.paintBackground(g, c);
            }
        }
    }

    public static ComponentUI createUI(JComponent c) {
        return new Office2003JideTabbedPaneUI();
    }

    protected void paintTabAreaBackground(Graphics g, Rectangle rect, int tabPlacement) {
        if (getColorTheme() != JideTabbedPane.COLOR_THEME_OFFICE2003) {
            super.paintTabAreaBackground(g, rect, tabPlacement);
        }
        else {
            // set color of the tab area
            if (_tabPane.isOpaque()) {
                if (getTabShape() != JideTabbedPane.SHAPE_BOX) {
                    Graphics2D g2d = (Graphics2D) g;
                    Color startColor = getPainter().getTabAreaBackgroundDk();
                    Color endColor = getPainter().getTabAreaBackgroundLt();
                    if (_tabPane.getTabPlacement() == TOP) {
                        JideSwingUtilities.fillGradient(g2d, new Rectangle(rect.x, rect.y, rect.width, rect.height), startColor, endColor, true);
                    }
                    else if (_tabPane.getTabPlacement() == BOTTOM) {
                        JideSwingUtilities.fillGradient(g2d, new Rectangle(rect.x, rect.y, rect.width, rect.height), endColor, startColor, true);
                    }
                    else if (_tabPane.getTabPlacement() == LEFT) {
                        JideSwingUtilities.fillGradient(g2d, new Rectangle(rect.x, rect.y, rect.width, rect.height), startColor, endColor, false);
                    }
                    else if (_tabPane.getTabPlacement() == RIGHT) {
                        JideSwingUtilities.fillGradient(g2d, new Rectangle(rect.x, rect.y, rect.width, rect.height), endColor, startColor, false);
                    }
                }
                else {
                    g.setColor(UIDefaultsLookup.getColor("control"));
                    g.fillRect(rect.x, rect.y, rect.width, rect.height);
                }
            }
        }
    }

    // paint the content top line when the tab is on the top
    protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
        if (!PAINT_CONTENT_BORDER_EDGE) {
            return;
        }

        if (getColorTheme() != JideTabbedPane.COLOR_THEME_OFFICE2003) {
            super.paintContentBorderTopEdge(g, tabPlacement, selectedIndex, x, y, w, h);
            return;
        }

        if (selectedIndex < 0) {
            return;
        }

        Rectangle selRect = getTabBounds(selectedIndex, _calcRect);

        Rectangle viewRect = _tabScroller.viewport.getViewRect();
        Rectangle r = _rects[selectedIndex];

        int tabShape = getTabShape();

        Insets contentInsets = getContentBorderInsets(tabPlacement);

        if (_tabPane.getColorTheme() == JideTabbedPane.COLOR_THEME_OFFICE2003) {
            if (tabShape == JideTabbedPane.SHAPE_OFFICE2003) {
                g.setColor(getPainter().getControlShadow());

                if (_alwaysShowLineBorder || _tabPane.hasFocusComponent()) {
                    if (contentInsets.left > 0) {
                        g.drawLine(x, y, x, y + h - 1);// left
                    }
                    if (contentInsets.right > 0) {
                        g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);// right
                    }
                    if (contentInsets.bottom > 0) {
                        g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);// bottom
                    }

                    if (contentInsets.top > 0) {
                        if (!_tabPane.isTabShown() || r.x >= viewRect.x + viewRect.width) {
                            g.drawLine(x, y, x + w - 1, y);// top
                        }
                        else {
                            g.drawLine(x, y, selRect.x - selRect.height + 2, y);// top left
                            g.drawLine(selRect.x + selRect.width, y, x + w - 1, y);// top right
                        }
                    }
                }
                else if (_tabPane.isTabShown()) {
                    if (r.x >= viewRect.x + viewRect.width) {
                        g.drawLine(x, y, x + w - 1, y);// top
                    }
                    else {
                        g.drawLine(x, y, selRect.x - selRect.height + 2, y);// top left
                        g.drawLine(selRect.x + selRect.width, y, x + w - 1, y);// top right
                    }
                }

            }
            else if (tabShape == JideTabbedPane.SHAPE_EXCEL) {
                g.setColor(getPainter().getControlShadow());

                g.drawLine(x, y, selRect.x - selRect.height / 2 + 4, y);// top left

                if (contentInsets.left > 0) {
                    g.drawLine(x, y, x, y + h - 1);// left
                }
                if (contentInsets.right > 0) {
                    g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);// right
                }
                if (contentInsets.bottom > 0) {
                    g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);// bottom
                }


                if (!_tabPane.isTabShown() || r.x >= viewRect.x + viewRect.width) {
                    g.drawLine(x, y, x + w - 1, y);// top
                }
                else {
                    if (!_tabPane.isShowIconsOnTab()
                            && !_tabPane.isUseDefaultShowIconsOnTab()) {
                        g.drawLine(selRect.x + selRect.width + selRect.height / 2 - 4,
                                y, x + w - 1, y);// top right
                    }
                    else {
                        g.drawLine(selRect.x + selRect.width + selRect.height / 2 - 6,
                                y, x + w - 1, y);// top right
                    }
                }
            }
            else if (tabShape == JideTabbedPane.SHAPE_WINDOWS
                    || tabShape == JideTabbedPane.SHAPE_WINDOWS_SELECTED) {
                g.setColor(getBorderEdgeColor());

                if (_tabPane.getColorTheme() == JideTabbedPane.COLOR_THEME_VSNET
                        || _tabPane.getColorTheme() == JideTabbedPane.COLOR_THEME_WINXP) {// the color set is winxp
                    g.drawLine(x, y, selRect.x - 2, y);// top left
                }
                else {// the color set is default or office2003
                    g.drawLine(x, y, selRect.x - 1, y);// top left
                }

                if (contentInsets.left > 0) {
                    g.drawLine(x, y, x, y + h - 1);// left
                }

                if (!_tabPane.isTabShown() || r.x >= viewRect.x + viewRect.width) {
                    g.setColor(_lightHighlight);
                    g.drawLine(x, y, x + w - 1, y);// top
                }
                else {
                    g.setColor(_lightHighlight);
                    g.drawLine(selRect.x + selRect.width + 3, y, x + w - 1, y);// top right
                }


                g.setColor(getPainter().getControlDk());
                if (contentInsets.right > 0) {
                    g.drawLine(x + w - 2, y + 1, x + w - 2, y + h - 2);// right
                }
                if (contentInsets.bottom > 0) {
                    g.drawLine(x + 1, y + h - 2, x + w - 2, y + h - 2);// bottom
                }

                g.setColor(getPainter().getControlShadow());
                if (contentInsets.right > 0) {
                    g.drawLine(x + w - 1, y + 1, x + w - 1, y + h - 1);// right
                }
                if (contentInsets.bottom > 0) {
                    g.drawLine(x + 1, y + h - 1, x + w - 1, y + h - 1);// bottom
                }
            }
            else {
                super.paintContentBorderTopEdge(g, tabPlacement, selectedIndex, x, y, w, h);
            }
        }
        else {
            super.paintContentBorderTopEdge(g, tabPlacement, selectedIndex, x, y, w, h);
        }
    }

    // paint the content bottom line when the tab is on the bottom
    protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
        if (!PAINT_CONTENT_BORDER_EDGE) {
            return;
        }

        if (getColorTheme() != JideTabbedPane.COLOR_THEME_OFFICE2003) {
            super.paintContentBorderBottomEdge(g, tabPlacement, selectedIndex, x, y, w, h);
            return;
        }

        if (selectedIndex < 0) {
            return;
        }

        Rectangle selRect = getTabBounds(selectedIndex, _calcRect);

        Rectangle viewRect = _tabScroller.viewport.getViewRect();
        Rectangle r = _rects[selectedIndex];

        int tabShape = getTabShape();

        Insets contentInsets = getContentBorderInsets(tabPlacement);

        if (tabShape == JideTabbedPane.SHAPE_OFFICE2003) {
            g.setColor(getPainter().getControlShadow());

            if (_alwaysShowLineBorder || _tabPane.hasFocusComponent()) {
                if (contentInsets.left > 0) {
                    g.drawLine(x, y, x, y + h - 1);// left
                }
                if (contentInsets.right > 0) {
                    g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);// right
                }
                if (contentInsets.top > 0) {
                    g.drawLine(x, y, x + w - 1, y);// top
                }

                if (contentInsets.bottom > 0) {
                    if (!_tabPane.isTabShown() || r.x >= viewRect.x + viewRect.width) {
                        g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);// bottom
                    }
                    else {
                        g.drawLine(x, y + h - 1, selRect.x - selRect.height + 2, y + h - 1);// bottom left
                        g.drawLine(selRect.x + selRect.width, y + h - 1, x + w - 1, y + h - 1);// bottom right
                    }
                }
            }
            else if (_tabPane.isTabShown()) {
                if (r.x >= viewRect.x + viewRect.width) {
                    g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);// bottom
                }
                else {
                    g.drawLine(x, y + h - 1, selRect.x - selRect.height + 2, y + h - 1);// bottom left
                    g.drawLine(selRect.x + selRect.width, y + h - 1, x + w - 1, y + h - 1);// bottom right
                }
            }
        }
        else if (tabShape == JideTabbedPane.SHAPE_EXCEL) {
            g.setColor(getPainter().getControlShadow());

            g.drawLine(x, y + h - 1, selRect.x - selRect.height / 2 + 4, y + h - 1);// bottom left

            if (contentInsets.left > 0) {
                g.drawLine(x, y, x, y + h - 1);// left
            }
            if (contentInsets.right > 0) {
                g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);// right
            }
            if (contentInsets.top > 0) {
                g.drawLine(x, y, x + w - 1, y);// top
            }

            if (!_tabPane.isTabShown() || r.x >= viewRect.x + viewRect.width) {
                g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);// bottom
            }
            else {
                if (!_tabPane.isShowIconsOnTab()
                        && !_tabPane.isUseDefaultShowIconsOnTab()) {
                    g.drawLine(selRect.x + selRect.width + selRect.height / 2 - 4,
                            y + h - 1, x + w - 1, y + h - 1);// bottom right
                }
                else {
                    g.drawLine(selRect.x + selRect.width + selRect.height / 2 - 6,
                            y + h - 1, x + w - 1, y + h - 1);// bottom right
                }
            }
        }
        else if (tabShape == JideTabbedPane.SHAPE_WINDOWS
                || tabShape == JideTabbedPane.SHAPE_WINDOWS_SELECTED) {
            g.setColor(getPainter().getControlShadow());
            g.drawLine(x + 1, y + h - 1, selRect.x - 2, y + h - 1);
            if (contentInsets.right > 0) {
                g.drawLine(x + w - 1, y + h - 1, x + w - 1, y + 1);
            }

            if (!_tabPane.isTabShown() || r.x >= viewRect.x + viewRect.width) {
                g.setColor(getPainter().getControlShadow());
                g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
            }
            else {
                g.setColor(getPainter().getControlShadow());
                g.drawLine(selRect.x + selRect.width, y + h - 1, x + w - 1, y + h - 1);
                g.setColor(getPainter().getControlDk());
                g.drawLine(selRect.x + selRect.width, y + h - 2, x + w - 2, y + h - 2);

                g.setColor(getPainter().getControlDk());
                g.drawLine(x + 1, y + h - 2, selRect.x - 2, y + h - 2);
                if (contentInsets.right > 0) {
                    g.drawLine(x + w - 2, y + 1, x + w - 2, y + h - 2);
                }
                g.drawLine(selRect.x + selRect.width - 1, y + h - 1, selRect.x + selRect.width - 1, y + h - 1);

//                g.setColor(new Color(255, 199, 115));
//                g.drawLine(selRect.x, y + h - 1, selRect.x + selRect.width - 3, y + h - 1);
//                g.drawLine(selRect.x, y + h - 2, selRect.x + selRect.width, y + h - 2);

                g.setColor(new Color(255, 255, 255));
                g.drawLine(selRect.x - 1, y + h - 1, selRect.x - 1, y + h - 1);
                g.drawLine(selRect.x - 1, y + h - 2, selRect.x - 1, y + h - 2);

                if (contentInsets.left > 0) {
                    g.drawLine(x, y, x, y + h - 2);
                }
                if (contentInsets.top > 0) {
                    g.drawLine(x, y, x + w - 2, y);
                }
            }

        }
        else {
            super.paintContentBorderBottomEdge(g, tabPlacement, selectedIndex, x, y, w, h);
        }
    }

    // paint the content left line when the tab is on the left
    protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {

        if (!PAINT_CONTENT_BORDER_EDGE) {
            return;
        }

        if (getColorTheme() != JideTabbedPane.COLOR_THEME_OFFICE2003) {
            super.paintContentBorderLeftEdge(g, tabPlacement, selectedIndex, x, y, w, h);
            return;
        }

        if (selectedIndex < 0) {
            return;
        }

        Rectangle selRect = getTabBounds(selectedIndex, _calcRect);

        Rectangle viewRect = _tabScroller.viewport.getViewRect();
        Rectangle r = _rects[selectedIndex];

        int tabShape = getTabShape();

        Insets contentInsets = getContentBorderInsets(tabPlacement);

        if (tabShape == JideTabbedPane.SHAPE_OFFICE2003) {
            g.setColor(getPainter().getControlShadow());

            if (_alwaysShowLineBorder || _tabPane.hasFocusComponent()) {
                if (contentInsets.top > 0) {
                    g.drawLine(x, y, x + w - 1, y);// top
                }
                if (contentInsets.right > 0) {
                    g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);// right
                }
                if (contentInsets.bottom > 0) {
                    g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);// bottom
                }

                if (contentInsets.left > 0) {
                    if (!_tabPane.isTabShown() || r.y >= viewRect.y + viewRect.height) {
                        g.drawLine(x, y, x, y + h - 1);// left
                    }
                    else {
                        g.drawLine(x, y, x, selRect.y - selRect.width + 2);// left top
                        g.drawLine(x, selRect.y + selRect.height, x, y + h - 1);// left bottom
                    }
                }
            }
            else if (_tabPane.isTabShown()) {
                if (r.y >= viewRect.y + viewRect.height) {
                    g.drawLine(x, y, x, y + h - 1);// left
                }
                else {
                    g.drawLine(x, y, x, selRect.y - selRect.width + 2);// left top
                    g.drawLine(x, selRect.y + selRect.height, x, y + h - 1);// left bottom
                }
            }
        }
        else if (tabShape == JideTabbedPane.SHAPE_EXCEL) {
            g.setColor(getPainter().getControlShadow());

            g.drawLine(x, y, x, selRect.y - selRect.width / 2 + 4);// left top

            if (contentInsets.top > 0) {
                g.drawLine(x, y, x + w - 1, y);// top
            }
            if (contentInsets.right > 0) {
                g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);// right
            }
            if (contentInsets.bottom > 0) {
                g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);// bottom
            }

            if (!_tabPane.isTabShown() || r.y >= viewRect.y + viewRect.height) {
                g.drawLine(x, y, x, y + h - 1);// left
            }
            else {
                g.drawLine(x, selRect.y + selRect.height + selRect.width / 2 - 4,
                        x, y + h - 1);// left bottom
            }
        }
        else if (tabShape == JideTabbedPane.SHAPE_WINDOWS
                || tabShape == JideTabbedPane.SHAPE_WINDOWS_SELECTED) {
            g.setColor(getPainter().getControlShadow());
            if (contentInsets.right > 0) {
                g.drawLine(x + w - 1, y + 1, x + w - 1, y + h - 1);// right
            }
            if (contentInsets.bottom > 0) {
                g.drawLine(x + 1, y + h - 1, x + w - 1, y + h - 1);// bottom
            }

            g.setColor(getPainter().getControlDk());
            g.drawLine(x + w - 2, y + 1, x + w - 2, y + h - 2);
            g.drawLine(x + 1, y + h - 2, x + w - 2, y + h - 2);

            g.setColor(new Color(255, 255, 255));
            if (contentInsets.top > 0) {
                g.drawLine(x, y, x + w - 2, y);// top
            }
            g.drawLine(x, y, x, selRect.y - 1);

            if (!_tabPane.isTabShown() || r.y >= viewRect.y + viewRect.height) {
                g.drawLine(x, y, x, y + h - 2);
            }
            else {
                g.drawLine(x, selRect.y + selRect.height + 1, x, y + h - 2);
            }
        }
        else {
            super.paintContentBorderLeftEdge(g, tabPlacement, selectedIndex, x, y, w, h);
        }
    }

    // paint the content right line when the tab is on the right
    protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
        if (!PAINT_CONTENT_BORDER_EDGE) {
            return;
        }

        if (getColorTheme() != JideTabbedPane.COLOR_THEME_OFFICE2003) {
            super.paintContentBorderRightEdge(g, tabPlacement, selectedIndex, x, y, w, h);
            return;
        }

        if (selectedIndex < 0) {
            return;
        }

        Rectangle selRect = getTabBounds(selectedIndex, _calcRect);

        Rectangle viewRect = _tabScroller.viewport.getViewRect();
        Rectangle r = _rects[selectedIndex];

        int tabShape = getTabShape();

        Insets contentInsets = getContentBorderInsets(tabPlacement);

        if (tabShape == JideTabbedPane.SHAPE_OFFICE2003) {
            g.setColor(getPainter().getControlShadow());

            if (_alwaysShowLineBorder || _tabPane.hasFocusComponent()) {
                if (contentInsets.top > 0) {
                    g.drawLine(x, y, x + w - 1, y);// top
                }
                if (contentInsets.left > 0) {
                    g.drawLine(x, y, x, y + h - 1);// left
                }
                if (contentInsets.bottom > 0) {
                    g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);// bottom
                }

                if (contentInsets.right > 0) {
                    if (!_tabPane.isTabShown() || r.y >= viewRect.y + viewRect.height) {
                        g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);// right
                    }
                    else {
                        g.drawLine(x + w - 1, y, x + w - 1, selRect.y - selRect.width + 2);// right top
                        g.drawLine(x + w - 1, selRect.y + selRect.height, x + w - 1, y + h - 1);// right bottom
                    }
                }
            }
            else if (_tabPane.isTabShown()) {
                if (r.y >= viewRect.y + viewRect.height) {
                    g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);// right
                }
                else {
                    g.drawLine(x + w - 1, y, x + w - 1, selRect.y - selRect.width + 2);// right top
                    g.drawLine(x + w - 1, selRect.y + selRect.height, x + w - 1, y + h - 1);// right bottom
                }
            }
        }
        else if (tabShape == JideTabbedPane.SHAPE_EXCEL) {
            g.setColor(getPainter().getControlShadow());

            g.drawLine(x + w - 1, y, x + w - 1, selRect.y - selRect.width / 2 + 4);// right top

            if (contentInsets.top > 0) {
                g.drawLine(x, y, x + w - 1, y);// top
            }
            if (contentInsets.left > 0) {
                g.drawLine(x, y, x, y + h - 1);// left
            }
            if (contentInsets.bottom > 0) {
                g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);// bottom
            }

            if (!_tabPane.isTabShown() || r.y >= viewRect.y + viewRect.height) {
                g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);// right
            }
            else {
                g.drawLine(x + w - 1, selRect.y + selRect.height + selRect.width / 2 - 4, x + w - 1, y + h - 1);// right bottom
            }
        }
        else if (tabShape == JideTabbedPane.SHAPE_WINDOWS
                || tabShape == JideTabbedPane.SHAPE_WINDOWS_SELECTED) {
            g.setColor(getPainter().getControlDk());
            g.drawLine(x + w - 2, y + 1, x + w - 2, selRect.y - 2);
            g.drawLine(x + 1, y + h - 2, x + w - 2, y + h - 2);

            g.setColor(getPainter().getControlShadow());
            g.drawLine(x + w - 1, y + 1, x + w - 1, selRect.y - 2);
            if (contentInsets.bottom > 0) {
                g.drawLine(x + 1, y + h - 1, x + w - 1, y + h - 1);// bottom
            }

            if (!_tabPane.isTabShown() || r.y >= viewRect.y + viewRect.height) {
                g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
                g.setColor(getPainter().getControlDk());
                g.drawLine(x + w - 2, y + 1, x + w - 2, y + h - 2);
            }
            else {
                g.drawLine(x + w - 1, selRect.y + selRect.height + 1, x + w - 1, y + h - 1);
                g.setColor(getPainter().getControlDk());
                g.drawLine(x + w - 2, selRect.y + selRect.height + 2, x + w - 2, y + h - 2);
                g.drawLine(x + w - 1, selRect.y + selRect.height, x + w - 1, selRect.y + selRect.height);
            }

            g.setColor(new Color(255, 255, 255));
            if (contentInsets.top > 0) {
                g.drawLine(x, y, x + w - 1, y);// top
            }
            if (contentInsets.left > 0) {
                g.drawLine(x, y, x, y + h - 1);// left
            }
        }
        else {
            super.paintContentBorderRightEdge(g, tabPlacement, selectedIndex, x, y, w, h);
        }
    }
}
