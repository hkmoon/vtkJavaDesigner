package vtk;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

/**
 * Created by moon on 3/16/15.
 */
public class Conv
{
	public static vtkImageCast getVtkImageCastFromCurrentIJImage()
	{
		ImagePlus imp = IJ.getImage();
		if(null == imp ) return null;

		int xsize = imp.getWidth();
		int ysize = imp.getHeight();
		int zsize = imp.getNSlices();

		vtkImageData image = new vtkImageData();
		image.SetDimensions( xsize, ysize, zsize );
		image.SetOrigin( 0, 0, 0 );
		image.SetSpacing( 1.0, 1.0, 1.0 );

		ImageProcessor ip = imp.getChannelProcessor();

		switch ( imp.getType() )
		{

			case ImagePlus.GRAY8:
				// 8bit gray
				vtkUnsignedCharArray charArray = new vtkUnsignedCharArray();
				charArray.SetJavaArray( (byte []) ip.getPixels() );
				image.GetPointData().SetScalars( charArray );
				break;

			case ImagePlus.COLOR_256:
				// 8bit indexed color
				vtkUnsignedCharArray rgbArray = new vtkUnsignedCharArray();
				rgbArray.SetJavaArray( (byte []) ip.getPixels() );
				image.GetPointData().SetScalars( rgbArray );
				break;

			case ImagePlus.GRAY16:
				// 16bit gray
				vtkUnsignedShortArray shortArray = new vtkUnsignedShortArray();
				shortArray.SetJavaArray( (short []) ip.getPixels() );
				image.GetPointData().SetScalars( shortArray );
				break;

			case ImagePlus.COLOR_RGB:
				// 32bit - Color RGB
				vtkUnsignedIntArray intArray = new vtkUnsignedIntArray();
				intArray.SetJavaArray( (int []) ip.getPixels() );
				image.GetPointData().SetScalars( intArray );
				break;

			case ImagePlus.GRAY32:
				// 32bit - Float
				vtkFloatArray floatArray = new vtkFloatArray();
				floatArray.SetJavaArray( (float []) ip.getPixels() );
				image.GetPointData().SetScalars( floatArray );
				break;
		}

		vtkImageCast cast = new vtkImageCast();
		cast.SetInputData( image );
		cast.SetOutputScalarTypeToFloat();
		cast.Update();

		return cast;
	}
}
