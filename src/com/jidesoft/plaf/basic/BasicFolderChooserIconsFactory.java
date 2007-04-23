/*
 * @(#)FileSystemIconsFactory.java 9/12/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */
package com.jidesoft.plaf.basic;/*
 * @(#)CiscoIconsFactory.java 6/16/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.icons.IconsFactory;

import javax.swing.*;

/**
 * A helper class to contain icons for demo of JIDE products.
 * Those icons are copyrighted by JIDE Software, Inc.
 */
public class BasicFolderChooserIconsFactory {

    public static class ToolBar {
        public final static String NEW = "icons/new.png";
        public final static String DELETE = "icons/delete.png";
        public final static String HOME = "icons/home.png";
        public final static String MY_DOCUMENT = "icons/myDocument.png";
        public final static String DESKTOP = "icons/desktop.png";
    }

    public static ImageIcon getImageIcon(String name) {
        if (name != null)
            return IconsFactory.getImageIcon(BasicFolderChooserIconsFactory.class, name);
        else
            return null;
    }

    public static void main(String[] argv) {
        IconsFactory.generateHTML(BasicFolderChooserIconsFactory.class);
    }


}
