package com.adcc;

import com.adcc.view.MainShell;
import org.eclipse.swt.widgets.Display;

/**
 * Created by ZP on 2017/7/11.
 */
public class APP {
    public static void main(String args[]) {
        try {
            Display display = Display.getDefault();
            MainShell shell = new MainShell(display);
            shell.open();
            shell.layout();
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
