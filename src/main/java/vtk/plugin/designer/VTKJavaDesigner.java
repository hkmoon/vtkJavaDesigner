package vtk.plugin.designer;

import org.opencv.core.Core;

import javax.swing.*;

/**
 * VTKJavaDesigner generates a user-defined java vtk program
 *
 * @author HongKee Moon
 * @version 0.1beta
 * @since 9/5/13
 */
public class VTKJavaDesigner extends AbstractDesigner {

	// -----------------------------------------------------------------
	// Load VTK library and print which library was not properly loaded
	static {
		if (!vtk.vtkNativeLibrary.LoadAllNativeLibraries()) {
			for (vtk.vtkNativeLibrary lib : vtk.vtkNativeLibrary.values()) {
				if (!lib.IsLoaded()) {
					System.out.println(lib.GetLibraryName() + " not loaded");
				}
			}
		}
		vtk.vtkNativeLibrary.DisableOutputWindow(null);
	}


	public VTKJavaDesigner(String jarInfo)
    {
        super("vtkJava Designer", jarInfo);

        initializeComponents();
    }

    public static void main(String[] args) {
        // Start all Swing applications on the EDT.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new VTKJavaDesigner(null).setVisible(true);
            }
        });
    }
}
