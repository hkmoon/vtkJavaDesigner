package plugin.desginer;

import javax.swing.*;

/**
 * VTKJavaDesigner generates a user-defined java vtk program
 *
 * @author HongKee Moon
 * @version 0.1beta
 * @since 9/5/13
 */
public class VTKJavaDesigner extends AbstractDesigner {

    public VTKJavaDesigner()
    {
        super("vtkJava Designer");

        initializeComponents();

    }

    public static void main(String[] args) {
        // Start all Swing applications on the EDT.

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new VTKJavaDesigner().setVisible(true);
            }
        });
    }
}
