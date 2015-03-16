package vtk.plugin;

import ij.IJ;
import ij.ImageJ;
import ij.plugin.PlugIn;
import vtk.plugin.compile.CompilerUtils;
import vtk.plugin.designer.VTKJavaDesigner;

import javax.swing.*;

/**
 * Created by moon on 3/12/15.
 */
public class vtkDesignerPlugIn implements PlugIn
{
	public vtkDesignerPlugIn()
	{
		//CompilerUtils.addClassPath( IJ.cl );
		try
		{
			String str = IJ.getClassLoader().loadClass( "vtk.plugin.vtkDesignerPlugIn" ).getResource( "" ).toString();
			str = str.replace( "jar:file:", "" ).replace( "!/vtk/plugin/", "" );
			CompilerUtils.addClassPath( str );
			//System.out.println( IJ.getClassLoader().loadClass( "vtk.plugin.vtkDesignerPlugIn" ).getResource( "" ).toString() );
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
				new VTKJavaDesigner().setVisible( true );
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
