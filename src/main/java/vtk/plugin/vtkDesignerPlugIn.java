package vtk.plugin;

import ij.IJ;
import ij.ImageJ;
import ij.plugin.PlugIn;
import vtk.plugin.compile.CompilerUtils;
import vtk.plugin.designer.VTKJavaDesigner;

import javax.swing.*;

import java.io.File;

/**
 * Created by moon on 3/12/15.
 */
public class vtkDesignerPlugIn implements PlugIn
{
	String jarInfo;
	public vtkDesignerPlugIn()
	{
		try
		{
			String str = IJ.getClassLoader().loadClass( "vtk.plugin.vtkDesignerPlugIn" ).getResource( "" ).toString();
			jarInfo = str.replace( "jar:file:", "" ).replace( "!/vtk/plugin/", "" );

			if(new File(jarInfo).exists())
				CompilerUtils.addClassPath( jarInfo );

			if(new File(jarInfo.replace( "plugins/vtkDesigner.jar", "jars/vtk.jar" )).exists())
				CompilerUtils.addClassPath( jarInfo.replace( "plugins/vtkDesigner.jar", "jars/vtk.jar" ) );
		}
		catch ( ClassNotFoundException e )
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run( String s )
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				new VTKJavaDesigner(jarInfo).setVisible( true );
			}
		} );
	}

	public static void main( final String[] args )
	{
		ImageJ.main( args );
		IJ.open( "http://imagej.nih.gov/ij/images/blobs.gif" );
		new vtkDesignerPlugIn().run( null );
	}
}
