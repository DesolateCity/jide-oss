/*
 * @(#)EclipseWindowsUtils.java
 *
 * Copyright 2002 JIDE Software Inc. All rights reserved.
 */
package com.jidesoft.plaf.eclipse;

import com.jidesoft.icons.IconsFactory;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.ExtWindowsDesktopProperty;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.WindowsDesktopProperty;
import com.jidesoft.plaf.basic.BasicRangeSliderUI;
import com.jidesoft.plaf.basic.Painter;
import com.jidesoft.plaf.vsnet.ConvertListener;
import com.jidesoft.plaf.vsnet.HeaderCellBorder;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.JideTabbedPane;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.beans.Beans;

/**
 * Utility Class for WindowsLookAndFeel to add Eclipse related LookAndFeel style
 */
public class EclipseWindowsUtils extends EclipseLookAndFeelExtension {

    /**
     * Initializes class defaults with menu components UIDefaults.
     *
     * @param table
     */
    public static void initClassDefaultsWithMenu(UIDefaults table) {
        EclipseLookAndFeelExtension.initClassDefaultsWithMenu(table);
        initClassDefaults(table);
    }

    /**
     * Initializes class defaults.
     *
     * @param table
     */
    public static void initClassDefaults(UIDefaults table) {
        EclipseLookAndFeelExtension.initClassDefaults(table);

        int products = LookAndFeelFactory.getProductsUsed();

        table.put("JidePopupMenuUI", "com.jidesoft.plaf.windows.WindowsJidePopupMenuUI");

        if ((products & PRODUCT_GRIDS) != 0) {
            table.put("NestedTableHeaderUI", "com.jidesoft.plaf.windows.WindowsNestedTableHeaderUI");
            table.put("EditableTableHeaderUI", "com.jidesoft.plaf.windows.WindowsEditableTableHeaderUI");
        }
    }

    /**
     * Initializes components defaults.
     *
     * @param table
     */
    public static void initComponentDefaults(UIDefaults table) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        WindowsDesktopProperty defaultTextColor = new WindowsDesktopProperty("win.button.textColor", table.get("controlText"), toolkit);
        WindowsDesktopProperty defaultBackgroundColor = new WindowsDesktopProperty("win.3d.backgroundColor", table.get("control"), toolkit);
        WindowsDesktopProperty defaultHighlightColor = new WindowsDesktopProperty("win.3d.lightColor", table.get("controlHighlight"), toolkit);
        WindowsDesktopProperty defaultLtHighlightColor = new WindowsDesktopProperty("win.3d.highlightColor", table.get("controlLtHighlight"), toolkit);
        WindowsDesktopProperty defaultShadowColor = new WindowsDesktopProperty("win.3d.shadowColor", table.get("controlShadow"), toolkit);
        WindowsDesktopProperty defaultDarkShadowColor = new WindowsDesktopProperty("win.3d.darkShadowColor", table.get("controlDkShadow"), toolkit);
        WindowsDesktopProperty activeTitleTextColor = new WindowsDesktopProperty("win.frame.captionTextColor", table.get("activeCaptionText"), toolkit);
        WindowsDesktopProperty activeTitleBackgroundColor = new WindowsDesktopProperty("win.frame.activeCaptionColor", table.get("activeCaption"), toolkit);
        WindowsDesktopProperty activeTitleBarGradientColor = new WindowsDesktopProperty("win.frame.activeCaptionGradientColor", table.get("activeCaption"), toolkit);
        WindowsDesktopProperty inactiveTitleTextColor = new WindowsDesktopProperty("win.frame.inactiveCaptionTextColor", table.get("controlText"), toolkit);
        WindowsDesktopProperty inactiveTitleBackgroundColor = new WindowsDesktopProperty("win.3d.shadowColor", table.get("controlShadow"), toolkit);
        WindowsDesktopProperty mdiBackgroundColor = new WindowsDesktopProperty("win.mdi.backgroundColor", table.get("controlShadow"), toolkit);

        Object controlFont = JideSwingUtilities.getControlFont(toolkit, table);
        Object toolbarFont = JideSwingUtilities.getMenuFont(toolkit, table);
        Object boldFont = JideSwingUtilities.getBoldFont(toolkit, table);

        Border shadowBorder = BorderFactory.createCompoundBorder(new ShadowBorder(null, null, new Color(171, 168, 165), new Color(143, 141, 138), new Insets(0, 0, 2, 2)),
                BorderFactory.createLineBorder(Color.gray));

        Border documentBorder = shadowBorder; /*BorderFactory.createCompoundBorder(
                shadowBorder,
                BorderFactory.createLineBorder(Color.gray));*/

        Object sunkenBorder = new ExtWindowsDesktopProperty(new String[]{"win.3d.lightColor", "win.3d.highlightColor", "win.3d.shadowColor", "win.3d.darkShadowColor"},
                new Object[]{table.get("control"), table.get("controlLtHighlight"), table.get("controlShadow"), table.get("controlDkShadow")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new SunkenBorder((Color) obj[0], (Color) obj[1], (Color) obj[2], (Color) obj[3],
                        new Insets(1, 1, 2, 1));
            }
        });

        Object inactiveTabForground = new ExtWindowsDesktopProperty(// Not exactly right
                new String[]{"win.3d.shadowColor"}, new Object[]{table.get("controlShadow")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return ((Color) obj[0]).darker();
            }
        });

        Object focusedButtonColor = new ExtWindowsDesktopProperty(new String[]{"win.item.highlightColor"}, new Object[]{table.get("textHighlight")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new ColorUIResource(EclipseUtils.getFocusedButtonColor((Color) obj[0]));
            }
        });

        Object selectedAndFocusedButtonColor = new ExtWindowsDesktopProperty(new String[]{"win.item.highlightColor"}, new Object[]{table.get("textHighlight")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new ColorUIResource(EclipseUtils.getSelectedAndFocusedButtonColor((Color) obj[0]));
            }
        });

        Object selectedButtonColor = new ExtWindowsDesktopProperty(new String[]{"win.item.highlightColor"}, new Object[]{table.get("textHighlight")}, toolkit, new ConvertListener() {
            public Object convert(Object[] obj) {
                return new ColorUIResource(EclipseUtils.getSelectedButtonColor((Color) obj[0]));
            }
        });

        WindowsDesktopProperty selectionBackgroundColor = new WindowsDesktopProperty("win.item.highlightColor", table.get("controlShadow"), toolkit);
        WindowsDesktopProperty selectionTextColor = new WindowsDesktopProperty("win.item.highlightTextColor", table.get("textHighlightText"), toolkit);

        Painter gripperPainter = new Painter() {
            public void paint(JComponent c, Graphics g, Rectangle rect, int orientation, int state) {
                EclipsePainter.getInstance().paintGripper(c, g, rect, orientation, state);
            }
        };

        Object buttonBorder = new BasicBorders.MarginBorder();

        ImageIcon sliderHorizontalImage = IconsFactory.getImageIcon(BasicRangeSliderUI.class, "icons/slider_horizontal.gif");
        ImageIcon sliderVerticalalImage = IconsFactory.getImageIcon(BasicRangeSliderUI.class, "icons/slider_vertical.gif");

        Object uiDefaults[] = {
                "JideButton.selectedAndFocusedBackground", selectedAndFocusedButtonColor,
                "JideButton.focusedBackground", focusedButtonColor,
                "JideButton.selectedBackground", selectedButtonColor,
                "JideButton.borderColor", selectionBackgroundColor,

                "JideButton.font", controlFont,
                "JideButton.background", defaultBackgroundColor,
                "JideButton.foreground", defaultTextColor,
                "JideButton.shadow", defaultShadowColor,
                "JideButton.darkShadow", defaultDarkShadowColor,
                "JideButton.light", defaultHighlightColor,
                "JideButton.highlight", defaultLtHighlightColor,
                "JideButton.border", buttonBorder,
                "JideButton.margin", new InsetsUIResource(3, 3, 3, 3),
                "JideButton.textIconGap", new Integer(4),
                "JideButton.textShiftOffset", new Integer(0),
                "JideButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{
                "SPACE", "pressed",
                "released SPACE", "released",
                "ENTER", "pressed",
                "released ENTER", "released"
        }),

                "JideSplitPane.dividerSize", new Integer(3),
                "JideSplitPaneDivider.border", new BorderUIResource(BorderFactory.createEmptyBorder()),
                "JideSplitPaneDivider.background", defaultBackgroundColor,
                "JideSplitPaneDivider.gripperPainter", gripperPainter,

                "JideTabbedPane.defaultTabShape", new Integer(JideTabbedPane.SHAPE_ECLIPSE),
                "JideTabbedPane.defaultTabColorTheme", new Integer(JideTabbedPane.COLOR_THEME_WIN2K),

                "JideTabbedPane.tabRectPadding", new Integer(2),
                "JideTabbedPane.closeButtonMarginHorizonal", new Integer(3),
                "JideTabbedPane.closeButtonMarginVertical", new Integer(3),
                "JideTabbedPane.textMarginVertical", new Integer(4),
                "JideTabbedPane.noIconMargin", new Integer(2),
                "JideTabbedPane.iconMargin", new Integer(5),
                "JideTabbedPane.textPadding", new Integer(6),
                "JideTabbedPane.buttonSize", new Integer(18),
                "JideTabbedPane.buttonMargin", new Integer(5),
                "JideTabbedPane.fitStyleBoundSize", new Integer(8),
                "JideTabbedPane.fitStyleFirstTabMargin", new Integer(4),
                "JideTabbedPane.fitStyleIconMinWidth", new Integer(24),
                "JideTabbedPane.fitStyleTextMinWidth", new Integer(16),
                "JideTabbedPane.compressedStyleNoIconRectSize", new Integer(24),
                "JideTabbedPane.compressedStyleIconMargin", new Integer(12),
                "JideTabbedPane.compressedStyleCloseButtonMarginHorizontal", new Integer(0),
                "JideTabbedPane.compressedStyleCloseButtonMarginVertical", new Integer(0),
                "JideTabbedPane.fixedStyleRectSize", new Integer(60),
                "JideTabbedPane.closeButtonMargin", new Integer(2),
                "JideTabbedPane.gripLeftMargin", new Integer(4),
                "JideTabbedPane.closeButtonMarginSize", new Integer(6),
                "JideTabbedPane.closeButtonLeftMargin", new Integer(2),
                "JideTabbedPane.closeButtonRightMargin", new Integer(2),

                "JideTabbedPane.defaultTabBorderShadowColor", new ColorUIResource(115, 109, 99),

                "JideTabbedPane.gripperPainter", gripperPainter,
                "JideTabbedPane.border", new BorderUIResource(shadowBorder),
                "JideTabbedPane.background", defaultBackgroundColor,
                "JideTabbedPane.foreground", defaultTextColor,
                "JideTabbedPane.light", defaultHighlightColor,
                "JideTabbedPane.highlight", defaultLtHighlightColor,
                "JideTabbedPane.shadow", defaultShadowColor,
                "JideTabbedPane.tabInsets", new InsetsUIResource(1, 4, 1, 4),
                "JideTabbedPane.contentBorderInsets", new InsetsUIResource(1, 0, 0, 0),
                "JideTabbedPane.ignoreContentBorderInsetsIfNoTabs", Boolean.FALSE,
                "JideTabbedPane.tabAreaInsets", new InsetsUIResource(0, 0, 0, 0),
                "JideTabbedPane.tabAreaBackground", defaultBackgroundColor,
                "JideTabbedPane.tabRunOverlay", new Integer(2),
                "JideTabbedPane.font", controlFont,
                "JideTabbedPane.selectedTabFont", controlFont,
                "JideTabbedPane.darkShadow", defaultTextColor,
                "JideTabbedPane.selectedTabTextForeground", activeTitleTextColor,
                "JideTabbedPane.unselectedTabTextForeground", defaultTextColor,
                "JideTabbedPane.selectedTabBackground", defaultBackgroundColor,
                "JideTabbedPane.textIconGap", new Integer(4),
                "JideTabbedPane.showIconOnTab", Boolean.FALSE,
                "JideTabbedPane.showCloseButtonOnTab", Boolean.TRUE,
                "JideTabbedPane.closeButtonAlignment", new Integer(SwingConstants.TRAILING),
                "JideTabbedPane.focusInputMap",
                new UIDefaults.LazyInputMap(new Object[]{
                        "RIGHT", "navigateRight",
                        "KP_RIGHT", "navigateRight",
                        "LEFT", "navigateLeft",
                        "KP_LEFT", "navigateLeft",
                        "UP", "navigateUp",
                        "KP_UP", "navigateUp",
                        "DOWN", "navigateDown",
                        "KP_DOWN", "navigateDown",
                        "ctrl DOWN", "requestFocusForVisibleComponent",
                        "ctrl KP_DOWN", "requestFocusForVisibleComponent",
                }),
                "JideTabbedPane.ancestorInputMap",
                new UIDefaults.LazyInputMap(new Object[]{
                        "ctrl PAGE_DOWN", "navigatePageDown",
                        "ctrl PAGE_UP", "navigatePageUp",
                        "ctrl UP", "requestFocus",
                        "ctrl KP_UP", "requestFocus",
                }),

                "Gripper.size", new Integer(8),
                "Gripper.painter", gripperPainter,

                "Resizable.resizeBorder", new BorderUIResource(shadowBorder),

                "Button.margin", new InsetsUIResource(2, 11, 2, 11),

                "ButtonPanel.order", "ACO",
                "ButtonPanel.oppositeOrder", "H",
                "ButtonPanel.buttonGap", new Integer(6),
                "ButtonPanel.groupGap", new Integer(6),
                "ButtonPanel.minButtonWidth", new Integer(75),

                "JideSplitButton.font", controlFont,
                "JideSplitButton.margin", new InsetsUIResource(3, 3, 3, 7),
                "JideSplitButton.border", buttonBorder,
                "JideSplitButton.borderPainted", Boolean.FALSE,
                "JideSplitButton.textIconGap", new Integer(3),
                "JideSplitButton.selectionBackground", selectionBackgroundColor,
                "JideSplitButton.selectionForeground", table.get("controlText"),
                "JideSplitButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{
                "SPACE", "pressed",
                "released SPACE", "released",
                "ENTER", "pressed",
                "released ENTER", "released",
                "DOWN", "downPressed",
                "released DOWN", "downReleased"
        }),

                "RangeSlider.lowerIcon", IconsFactory.getIcon(null, sliderHorizontalImage, 0, 0, 9, 8),
                "RangeSlider.upperIcon", IconsFactory.getIcon(null, sliderHorizontalImage, 0, 8, 9, 8),
                "RangeSlider.middleIcon", IconsFactory.getIcon(null, sliderHorizontalImage, 0, 16, 9, 6),
                "RangeSlider.lowerVIcon", IconsFactory.getIcon(null, sliderVerticalalImage, 0, 0, 8, 9),
                "RangeSlider.upperVIcon", IconsFactory.getIcon(null, sliderVerticalalImage, 8, 0, 8, 9),
                "RangeSlider.middleVIcon", IconsFactory.getIcon(null, sliderVerticalalImage, 16, 0, 6, 9),

                "Cursor.hsplit", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.HSPLIT),
                "Cursor.vsplit", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.VSPLIT),

                "Cursor.north", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.NORTH),
                "Cursor.south", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.SOUTH),
                "Cursor.east", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.EAST),
                "Cursor.west", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.WEST),
                "Cursor.tab", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.TAB),
                "Cursor.float", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.FLOAT),
                "Cursor.vertical", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.VERTICAL),
                "Cursor.horizontal", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.HORIZONTAL),
                "Cursor.delete", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.DELETE),
                "Cursor.drag", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.DROP),
                "Cursor.dragStop", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.NODROP),
                "Cursor.dragText", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.DROP_TEXT),
                "Cursor.dragTextStop", JideIconsFactory.getImageIcon(JideIconsFactory.Cursor.NODROP_TEXT),


                "Icon.floating", Boolean.FALSE,

                "JideScrollPane.border", new BorderUIResource(BorderFactory.createEmptyBorder()),
                "MenuBar.border", new BorderUIResource(BorderFactory.createEmptyBorder(2, 2, 2, 2)),

                "TextArea.font", controlFont,
        };
        table.putDefaults(uiDefaults);

        int products = LookAndFeelFactory.getProductsUsed();

        if ((products & PRODUCT_DOCK) != 0) {
            Object slidingEastFrameBorder = new ExtWindowsDesktopProperty(new String[]{"win.3d.lightColor", "win.3d.highlightColor", "win.3d.shadowColor", "win.3d.darkShadowColor"},
                    new Object[]{table.get("control"), table.get("controlLtHighlight"), table.get("controlShadow"), table.get("controlDkShadow")}, toolkit, new ConvertListener() {
                public Object convert(Object[] obj) {
                    return new FrameBorder((Color) obj[0], (Color) obj[1], (Color) obj[2], (Color) obj[3],
                            new Insets(0, 4, 0, 0));
                }
            });

            Object slidingWestFrameBorder = new ExtWindowsDesktopProperty(new String[]{"win.3d.lightColor", "win.3d.highlightColor", "win.3d.shadowColor", "win.3d.darkShadowColor"},
                    new Object[]{table.get("control"), table.get("controlLtHighlight"), table.get("controlShadow"), table.get("controlDkShadow")}, toolkit, new ConvertListener() {
                public Object convert(Object[] obj) {
                    return new FrameBorder((Color) obj[0], (Color) obj[1], (Color) obj[2], (Color) obj[3],
                            new Insets(0, 0, 0, 4));
                }
            });

            Object slidingNorthFrameBorder = new ExtWindowsDesktopProperty(new String[]{"win.3d.lightColor", "win.3d.highlightColor", "win.3d.shadowColor", "win.3d.darkShadowColor"},
                    new Object[]{table.get("control"), table.get("controlLtHighlight"), table.get("controlShadow"), table.get("controlDkShadow")}, toolkit, new ConvertListener() {
                public Object convert(Object[] obj) {
                    return new FrameBorder((Color) obj[0], (Color) obj[1], (Color) obj[2], (Color) obj[3],
                            new Insets(0, 0, 4, 0));
                }
            });

            Object slidingSouthFrameBorder = new ExtWindowsDesktopProperty(new String[]{"win.3d.lightColor", "win.3d.highlightColor", "win.3d.shadowColor", "win.3d.darkShadowColor"},
                    new Object[]{table.get("control"), table.get("controlLtHighlight"), table.get("controlShadow"), table.get("controlDkShadow")}, toolkit, new ConvertListener() {
                public Object convert(Object[] obj) {
                    return new FrameBorder((Color) obj[0], (Color) obj[1], (Color) obj[2], (Color) obj[3],
                            new Insets(4, 0, 0, 0));
                }
            });

            ImageIcon titleButtonImage = IconsFactory.getImageIcon(Eclipse3xWindowsUtils.class, "icons/title_buttons_eclipse.gif"); // 16 x 16 x 8
            final int titleButtonSize = 16;

            uiDefaults = new Object[]{
                    "Workspace.background", mdiBackgroundColor,

                    "SidePane.margin", new InsetsUIResource(1, 1, 1, 1),
                    "SidePane.iconTextGap", new Integer(2),
                    "SidePane.textBorderGap", new Integer(13),
                    "SidePane.itemGap", new Integer(4),
                    "SidePane.groupGap", new Integer(3),
                    "SidePane.foreground", defaultDarkShadowColor,
                    "SidePane.background", defaultBackgroundColor,
                    "SidePane.lineColor", defaultShadowColor,
                    "SidePane.buttonBackground", defaultBackgroundColor,
                    "SidePane.font", controlFont,
                    "SidePane.orientation", new Integer(1),
                    "SidePane.showSelectedTabText", Boolean.FALSE,
                    "SidePane.alwaysShowTabText", Boolean.FALSE,

                    "ContentContainer.background", defaultBackgroundColor,
                    "ContentContainer.vgap", new Integer(1),
                    "ContentContainer.hgap", new Integer(1),
                    "MainContainer.border", new BorderUIResource(BorderFactory.createEmptyBorder(0, 0, 0, 0)),

                    "DockableFrame.defaultIcon", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.BLANK),
                    "DockableFrame.background", defaultBackgroundColor,
                    "DockableFrame.border", new BorderUIResource(BorderFactory.createEmptyBorder(0, 0, 0, 0)),
                    "DockableFrame.floatingBorder", new BorderUIResource(BorderFactory.createEmptyBorder(0, 0, 0, 0)),
                    "DockableFrame.slidingEastBorder", slidingEastFrameBorder,
                    "DockableFrame.slidingWestBorder", slidingWestFrameBorder,
                    "DockableFrame.slidingNorthBorder", slidingNorthFrameBorder,
                    "DockableFrame.slidingSouthBorder", slidingSouthFrameBorder,

                    "DockableFrame.activeTitleBackground", activeTitleBackgroundColor,
                    "DockableFrame.activeTitleBackground2", activeTitleBarGradientColor, //EclipseUtils.getLighterColor((Color)windowTitleBackground),
                    "DockableFrame.activeTitleForeground", activeTitleTextColor,
                    "DockableFrame.inactiveTitleBackground", inactiveTitleBackgroundColor,
                    "DockableFrame.inactiveTitleForeground", defaultTextColor,
                    "DockableFrame.activeTitleBorderColor", activeTitleBackgroundColor,
                    "DockableFrame.inactiveTitleBorderColor", inactiveTitleTextColor,
                    "DockableFrame.titleBorder", new BorderUIResource(BorderFactory.createEmptyBorder(0, 0, 0, 0)),
                    "DockableFrame.font", controlFont,

                    "DockableFrameTitlePane.gripperPainter", gripperPainter,
                    "DockableFrameTitlePane.font", controlFont,
                    "DockableFrameTitlePane.hideIcon", IconsFactory.getIcon(null, titleButtonImage, 0, 0, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.unfloatIcon", IconsFactory.getIcon(null, titleButtonImage, 0, titleButtonSize, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.floatIcon", IconsFactory.getIcon(null, titleButtonImage, 0, 2 * titleButtonSize, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.autohideIcon", IconsFactory.getIcon(null, titleButtonImage, 0, 3 * titleButtonSize, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.stopAutohideIcon", IconsFactory.getIcon(null, titleButtonImage, 0, 4 * titleButtonSize, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.hideAutohideIcon", IconsFactory.getIcon(null, titleButtonImage, 0, 5 * titleButtonSize, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.maximizeIcon", IconsFactory.getIcon(null, titleButtonImage, 0, 6 * titleButtonSize, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.restoreIcon", IconsFactory.getIcon(null, titleButtonImage, 0, 7 * titleButtonSize, titleButtonSize, titleButtonSize),
                    "DockableFrameTitlePane.titleBarComponent", Boolean.TRUE,

                    "DockableFrameTitlePane.alwaysShowAllButtons", Boolean.FALSE, // true if show all three buttons no matter if the buttons is available. false if only show buttons which is available
                    "DockableFrameTitlePane.buttonsAlignment", new Integer(SwingConstants.TRAILING), // trailing or leading
                    "DockableFrameTitlePane.titleAlignment", new Integer(SwingConstants.LEADING), // trailing or leading or center
                    "DockableFrameTitlePane.buttonGap", new Integer(3), // gap between buttons
                    "DockableFrameTitlePane.showIcon", Boolean.TRUE, // show icon or not, the alignment is the same as titleAlignment
                    "DockableFrameTitlePane.margin", new InsetsUIResource(0, 6, 0, 0), // gap

                    "Contour.color", new ColorUIResource(136, 136, 136),
                    "Contour.thickness", new Integer(2),

                    "DockingFramework.changeCursor", Boolean.TRUE,
            };
            table.putDefaults(uiDefaults);
        }

        if ((products & PRODUCT_COMPONENTS) != 0) {
            ImageIcon collapsiblePaneImage = IconsFactory.getImageIcon(Eclipse3xWindowsUtils.class, "icons/collapsible_pane_eclipse.gif"); // 11 x 11 x 2
            final int collapsiblePaneSize = 11;

            uiDefaults = new Object[]{
                    "CollapsiblePanes.border", new BorderUIResource(BorderFactory.createEmptyBorder(12, 12, 0, 12)),
                    "CollapsiblePanes.gap", new Integer(15),

                    "CollapsiblePane.background", defaultBackgroundColor,
                    "CollapsiblePane.contentBackground", defaultLtHighlightColor,
                    "CollapsiblePane.foreground", defaultTextColor,
                    "CollapsiblePane.emphasizedBackground", activeTitleBackgroundColor,
                    "CollapsiblePane.emphasizedForeground", activeTitleTextColor,
                    "CollapsiblePane.border", new BorderUIResource(BorderFactory.createEmptyBorder(1, 1, 1, 1)),
                    "CollapsiblePane.font", controlFont,

                    "CollapsiblePane.contentBorder", new BorderUIResource(BorderFactory.createEmptyBorder(8, 10, 8, 10)),

                    "CollapsiblePane.titleBorder", new BorderUIResource(BorderFactory.createEmptyBorder(3, 3, 3, 3)),
                    "CollapsiblePane.titleFont", boldFont,
                    "CollapsiblePane.downIcon", IconsFactory.getIcon(null, collapsiblePaneImage, 0, 0, collapsiblePaneSize, collapsiblePaneSize),
                    "CollapsiblePane.upIcon", IconsFactory.getIcon(null, collapsiblePaneImage, 0, collapsiblePaneSize, collapsiblePaneSize, collapsiblePaneSize),

                    "StatusBarItem.border", sunkenBorder,

                    "StatusBar.border", new BorderUIResource(BorderFactory.createEmptyBorder(2, 0, 0, 0)),
                    "StatusBar.margin", new Insets(2, 0, 0, 0),
                    "StatusBar.gap", new Integer(3),
                    "StatusBar.background", defaultBackgroundColor,
                    "StatusBar.font", controlFont,

                    "DocumentPane.groupBorder", new BorderUIResource(documentBorder),
                    "DocumentPane.newHorizontalGroupIcon", JideIconsFactory.getImageIcon(JideIconsFactory.WindowMenu.NEW_HORIZONTAL_TAB),
                    "DocumentPane.newVerticalGroupIcon", JideIconsFactory.getImageIcon(JideIconsFactory.WindowMenu.NEW_VERTICAL_TAB),
                    "DocumentPane.boldActiveTab", Boolean.FALSE,

            };
            table.putDefaults(uiDefaults);
        }

        if ((products & PRODUCT_ACTION) != 0) {
            uiDefaults = new Object[]{
                    "CommandBar.font", toolbarFont,
                    "CommandBar.background", defaultBackgroundColor,
                    "CommandBar.foreground", defaultTextColor,
                    "CommandBar.shadow", defaultShadowColor,
                    "CommandBar.darkShadow", defaultDarkShadowColor,
                    "CommandBar.light", defaultHighlightColor,
                    "CommandBar.highlight", defaultLtHighlightColor,
                    "CommandBar.border", new BorderUIResource(BorderFactory.createEmptyBorder(2, 1, 2, 1)),
                    "CommandBar.borderVert", new BorderUIResource(BorderFactory.createEmptyBorder(2, 1, 2, 1)),
                    "CommandBar.borderFloating", new BorderUIResource(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(table.getColor("activeCaption"), 2),
                    BorderFactory.createEmptyBorder(1, 1, 1, 1))),
                    "CommandBar.separatorSize", new Integer(3),
                    "CommandBar.ancestorInputMap",
                    new UIDefaults.LazyInputMap(new Object[]{
                            "UP", "navigateUp",
                            "KP_UP", "navigateUp",
                            "DOWN", "navigateDown",
                            "KP_DOWN", "navigateDown",
                            "LEFT", "navigateLeft",
                            "KP_LEFT", "navigateLeft",
                            "RIGHT", "navigateRight",
                            "KP_RIGHT", "navigateRight"
                    }),
                    "CommandBar.titleBarSize", new Integer(17),
                    "CommandBar.titleBarButtonGap", new Integer(1),
                    "CommandBar.titleBarBackground", table.getColor("activeCaption"),
                    "CommandBar.titleBarForeground", table.getColor("activeCaptionText"),
                    "CommandBar.titleBarFont", boldFont,
                    "CommandBar.minimumSize", new DimensionUIResource(20, 20),

                    "CommandBar.separatorSize", new DimensionUIResource(5, 20),

                    // *** Separator
                    "CommandBarSeparator.background", new Color(219, 216, 209),
                    "CommandBarSeparator.foreground", new Color(166, 166, 166),

                    "Chevron.size", new Integer(11),
                    "Chevron.alwaysVisible", Boolean.FALSE,
            };
            table.putDefaults(uiDefaults);
        }

        if ((products & PRODUCT_GRIDS) != 0) {
            uiDefaults = new Object[]{
                    "AbstractComboBox.useJButton", Boolean.FALSE,
                    "NestedTableHeader.cellBorder", new HeaderCellBorder(),
            };
            table.putDefaults(uiDefaults);
        }

        if (!JideSwingUtilities.shouldUseSystemFont()) {
            Object uiDefaultsFont[] = {
                    "TabbedPane.font", controlFont,
                    "TitledBorder.font", controlFont,
                    "TableHeader.font", controlFont,
                    "Table.font", controlFont,
                    "List.font", controlFont,
                    "Tree.font", controlFont,
                    "ToolTip.font", controlFont,
                    "CheckBox.font", controlFont,
                    "RadioButton.font", controlFont,
                    "Label.font", controlFont,
                    "Panel.font", controlFont,
                    "TextField.font", controlFont,
                    "ComboBox.font", controlFont,
                    "Button.font", controlFont
            };
            table.putDefaults(uiDefaultsFont);
        }
        table.put("Theme.painter", EclipsePainter.getInstance());
    }

    /**
     * Initializes components defaults with menu components UIDefaults.
     *
     * @param table
     */
    public static void initComponentDefaultsWithMenu(UIDefaults table) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        initComponentDefaults(table);

        if (!Beans.isDesignTime()) {
            WindowsDesktopProperty defaultLightColor = new WindowsDesktopProperty("win.3d.lightColor", table.get("controlHighlight"), toolkit);
            WindowsDesktopProperty defaultHighlightColor = new WindowsDesktopProperty("win.3d.highlightColor", table.get("controlLtHighlight"), toolkit);
            WindowsDesktopProperty selectionTextColor = new WindowsDesktopProperty("win.item.highlightTextColor", table.get("textHighlightText"), toolkit);
            WindowsDesktopProperty selectionBackgroundColor = new WindowsDesktopProperty("win.item.highlightColor", table.get("controlShadow"), toolkit);

            WindowsDesktopProperty defaultShadowColor = new WindowsDesktopProperty("win.3d.shadowColor", table.get("controlShadow"), toolkit);

            Object menuBorder = new ExtWindowsDesktopProperty(new String[]{"win.3d.lightColor", "win.3d.highlightColor", "win.3d.shadowColor", "win.3d.darkShadowColor"},
                    new Object[]{table.get("control"), table.get("controlLtHighlight"), table.get("controlShadow"), table.get("controlDkShadow")}, toolkit, new ConvertListener() {
                public Object convert(Object[] obj) {
                    return new RaisedBorder((Color) obj[0], (Color) obj[1], (Color) obj[2], (Color) obj[3],
                            new Insets(2, 2, 2, 2));
                }
            });

            Object menuFont = JideSwingUtilities.getMenuFont(toolkit, table);

            Object uiDefaults[] = {
                    "PopupMenuSeparator.foreground", defaultHighlightColor,
                    "PopupMenuSeparator.background", defaultShadowColor,

                    "CheckBoxMenuItem.checkIcon", JideIconsFactory.getImageIcon(JideIconsFactory.MENU_CHECKBOX_ECLIPSE),
                    "CheckBoxMenuItem.selectionBackground", selectionBackgroundColor,
                    "CheckBoxMenuItem.selectionForeground", selectionTextColor,
                    "CheckBoxMenuItem.acceleratorSelectionForeground", selectionTextColor,
                    "CheckBoxMenuItem.mouseHoverBackground", defaultHighlightColor,
                    "CheckBoxMenuItem.mouseHoverBorder", new BorderUIResource(BorderFactory.createLineBorder(new Color(10, 36, 106))),
                    "CheckBoxMenuItem.margin", new InsetsUIResource(2, 2, 2, 2),
                    "CheckBoxMenuItem.font", menuFont,
                    "CheckBoxMenuItem.acceleratorFont", menuFont,

                    "RadioButtonMenuItem.checkIcon", JideIconsFactory.getImageIcon(JideIconsFactory.MENU_RADIOBUTTON_ECLIPSE),
                    "RadioButtonMenuItem.selectionBackground", selectionBackgroundColor,
                    "RadioButtonMenuItem.selectionForeground", selectionTextColor,
                    "RadioButtonMenuItem.acceleratorSelectionForeground", selectionTextColor,
                    "RadioButtonMenuItem.mouseHoverBackground", defaultHighlightColor,
                    "RadioButtonMenuItem.mouseHoverBorder", new BorderUIResource(BorderFactory.createLineBorder(new Color(10, 36, 106))),
                    "RadioButtonMenuItem.margin", new InsetsUIResource(2, 2, 2, 2),
                    "RadioButtonMenuItem.font", menuFont,
                    "RadioButtonMenuItem.acceleratorFont", menuFont,

                    "MenuBar.border", new BorderUIResource(BorderFactory.createEmptyBorder(2, 2, 4, 2)),

                    "Menu.selectionBackground", selectionBackgroundColor,
                    "Menu.selectionForeground", selectionTextColor,
                    "Menu.mouseHoverBackground", selectionBackgroundColor,
                    "Menu.mouseHoverBorder", new BorderUIResource(BorderFactory.createEmptyBorder()),
                    "Menu.mouseSelectedBorder", new BorderUIResource(BorderFactory.createEmptyBorder()),
                    "Menu.margin", new InsetsUIResource(4, 6, 2, 6),
                    "Menu.textIconGap", new Integer(4),
                    "Menu.checkIcon", JideIconsFactory.getImageIcon(JideIconsFactory.MENU_CHECKBOX_VSNET),
                    "Menu.font", menuFont,
                    "Menu.acceleratorFont", menuFont,

                    "PopupMenu.border", menuBorder,

                    "MenuItem.checkIcon", JideIconsFactory.getImageIcon(JideIconsFactory.MENU_CHECKBOX_VSNET),
                    "MenuItem.selectionBackground", selectionBackgroundColor,
                    "MenuItem.selectionForeground", selectionTextColor,
                    "MenuItem.acceleratorSelectionForeground", selectionTextColor,
//            "MenuItem.background", menuBackground,
                    "MenuItem.selectionBorderColor", selectionBackgroundColor,
                    "MenuItem.shadowWidth", new Integer(24),
                    "MenuItem.shadowColor", defaultLightColor, // TODO: not exactly. The actual one a little bit brighter than it
                    "MenuItem.textIconGap", new Integer(4),
                    "MenuItem.accelEndGap", new Integer(18),
                    "MenuItem.margin", new InsetsUIResource(2, 2, 2, 2),
                    "MenuItem.font", menuFont,
                    "MenuItem.acceleratorFont", menuFont,
            };
            table.putDefaults(uiDefaults);
        }
    }
}
