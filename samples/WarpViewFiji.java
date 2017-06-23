package moon;
import vtk.*;
import vtk.rendering.jogl.*;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by moon on 22/6/17.
 */
public class WarpView extends JPanel implements ActionListener {

	int currentT = 0;

	//// 1) Create the standard renderer, render window and int
	private vtkJoglPanelComponent renWin;
	private vtkCamera camera;
	private vtkActor actor;

	private JButton exitButton;

	/** An ActionListener that listens to the button. */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(exitButton)) {
			JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
			frame.dispose();
		}
	}

	public WarpView()
	{
		super(new BorderLayout(10, 10));
		renWin = new vtkJoglPanelComponent();
		renWin.getComponent().setPreferredSize(new Dimension(320, 200));
		renWin.setInteractorStyle(new vtkInteractorStyleTrackballCamera());

		double w = 256d * 256d;
		double h = 254d * 254d;

		double re = Math.sqrt(w + h);

		double expHeight = re / 7d;

		double factor = expHeight / 256;

		show(factor);

		// Add Java UI components
		exitButton = new JButton("Exit");
		exitButton.addActionListener(this);

		add(renWin.getComponent());
		add(exitButton, BorderLayout.SOUTH);
	}


	public void show(double scaleFactor)
	{
		if(null != actor)
		{
			removeActor(actor);
		}

		// Image data
		//vtkTIFFReader reader = new vtkTIFFReader();
		//reader.SetFileName("clown.tif");
		//reader.SetOrientationType(2);
		//reader.SetDataScalarTypeToFloat();
		//reader.Update();

		//vtkAlgorithmOutput imageData = reader.GetOutputPort();

		//IJ.open( "http://imagej.nih.gov/ij/images/blobs.gif" );
		ImagePlus imp = IJ.getImage();
		if( null == imp ) return;

		//System.out.println(imp.getWidth());
		//System.out.println(imp.getHeight());
		//System.out.println(imp.getNSlices());

		int xsize = imp.getWidth();
		int ysize = imp.getHeight();
		int zsize = imp.getNSlices();

		ImageProcessor ip = imp.getChannelProcessor();

		vtkUnsignedCharArray array = new vtkUnsignedCharArray();
		array.SetJavaArray((byte []) ip.getPixels());
		array.SetName( "scalars" );

		vtkImageData image = new vtkImageData();
		image.SetDimensions( xsize, ysize, zsize );
		image.SetOrigin( 0, 0, 0 );
		image.SetSpacing( 1.0, 1.0, 1.0 );
		image.GetPointData().SetScalars( array );

		vtkImageCast cast = new vtkImageCast();
		cast.SetInputData(image);
		cast.SetOutputScalarTypeToFloat();
		cast.Update();

		vtkAlgorithmOutput imageData = cast.GetOutputPort();

		vtkLookupTable colorTable = Jet.getLookuptable();
		colorTable.SetTableRange(0, 255);
		colorTable.Build();

		vtkImageDataGeometryFilter geometry = new vtkImageDataGeometryFilter();
		geometry.SetInputConnection(imageData);

		vtkWarpScalar warp = new vtkWarpScalar();
		warp.SetInputConnection(geometry.GetOutputPort());
		warp.SetScaleFactor(scaleFactor);

		vtkMergeFilter merge = new vtkMergeFilter();
		merge.SetGeometryConnection(warp.GetOutputPort());
		merge.SetScalarsData(cast.GetOutput());

		vtkDataSetMapper mapper = new vtkDataSetMapper();
		mapper.SetLookupTable(colorTable);
		mapper.SetInputConnection(merge.GetOutputPort());
		mapper.SetScalarRange(0d, 256d);

		actor = new vtkActor();
		actor.SetMapper(mapper);

		vtkRenderer renderer = new vtkRenderer();
		renderer.AddActor( actor );
		renderer.ResetCameraClippingRange();

		renWin.getRenderWindow().AddRenderer( renderer );

		vtkBorderWidget borderWidget = new vtkBorderWidget(  );
		borderWidget.SetDefaultRenderer( renWin.getRenderer() );
		borderWidget.SetInteractor( renWin.getRenderWindowInteractor() );
		borderWidget.EnabledOn();

		renWin.resetCamera();
	}

	private void addActor(vtkActor actor)
	{
		renWin.getRenderer().AddActor(actor);
	}

	private void removeActor(vtkActor actor)
	{
		renWin.getRenderer().RemoveActor(actor);
	}

	public static void main(String s[]) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame("WarpView");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.getContentPane().add(new WarpView());
				//frame.setSize(400, 300);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.pack();
			}
		});
	}
}

class Jet {
	public static double[][] jet = {
			{0,0,5.200000e-01},
			{0,0,5.400000e-01},
			{0,0,5.600000e-01},
			{0,0,5.800000e-01},
			{0,0,6.000000e-01},
			{0,0,6.200000e-01},
			{0,0,6.400000e-01},
			{0,0,6.600000e-01},
			{0,0,6.800000e-01},
			{0,0,7.000000e-01},
			{0,0,7.200000e-01},
			{0,0,7.400000e-01},
			{0,0,7.600000e-01},
			{0,0,7.800000e-01},
			{0,0,8.000000e-01},
			{0,0,8.200000e-01},
			{0,0,8.400000e-01},
			{0,0,8.600000e-01},
			{0,0,8.800000e-01},
			{0,0,9.000000e-01},
			{0,0,9.200000e-01},
			{0,0,9.400000e-01},
			{0,0,9.600000e-01},
			{0,0,9.800000e-01},
			{0,0,1},
			{0,2.000000e-02,1},
			{0,4.000000e-02,1},
			{0,6.000000e-02,1},
			{0,8.000000e-02,1},
			{0,1.000000e-01,1},
			{0,1.200000e-01,1},
			{0,1.400000e-01,1},
			{0,1.600000e-01,1},
			{0,1.800000e-01,1},
			{0,2.000000e-01,1},
			{0,2.200000e-01,1},
			{0,2.400000e-01,1},
			{0,2.600000e-01,1},
			{0,2.800000e-01,1},
			{0,3.000000e-01,1},
			{0,3.200000e-01,1},
			{0,3.400000e-01,1},
			{0,3.600000e-01,1},
			{0,3.800000e-01,1},
			{0,4.000000e-01,1},
			{0,4.200000e-01,1},
			{0,4.400000e-01,1},
			{0,4.600000e-01,1},
			{0,4.800000e-01,1},
			{0,5.000000e-01,1},
			{0,5.200000e-01,1},
			{0,5.400000e-01,1},
			{0,5.600000e-01,1},
			{0,5.800000e-01,1},
			{0,6.000000e-01,1},
			{0,6.200000e-01,1},
			{0,6.400000e-01,1},
			{0,6.600000e-01,1},
			{0,6.800000e-01,1},
			{0,7.000000e-01,1},
			{0,7.200000e-01,1},
			{0,7.400000e-01,1},
			{0,7.600000e-01,1},
			{0,7.800000e-01,1},
			{0,8.000000e-01,1},
			{0,8.200000e-01,1},
			{0,8.400000e-01,1},
			{0,8.600000e-01,1},
			{0,8.800000e-01,1},
			{0,9.000000e-01,1},
			{0,9.200000e-01,1},
			{0,9.400000e-01,1},
			{0,9.600000e-01,1},
			{0,9.800000e-01,1},
			{0,1,1},
			{2.000000e-02,1,9.800000e-01},
			{4.000000e-02,1,9.600000e-01},
			{6.000000e-02,1,9.400000e-01},
			{8.000000e-02,1,9.200000e-01},
			{1.000000e-01,1,9.000000e-01},
			{1.200000e-01,1,8.800000e-01},
			{1.400000e-01,1,8.600000e-01},
			{1.600000e-01,1,8.400000e-01},
			{1.800000e-01,1,8.200000e-01},
			{2.000000e-01,1,8.000000e-01},
			{2.200000e-01,1,7.800000e-01},
			{2.400000e-01,1,7.600000e-01},
			{2.600000e-01,1,7.400000e-01},
			{2.800000e-01,1,7.200000e-01},
			{3.000000e-01,1,7.000000e-01},
			{3.200000e-01,1,6.800000e-01},
			{3.400000e-01,1,6.600000e-01},
			{3.600000e-01,1,6.400000e-01},
			{3.800000e-01,1,6.200000e-01},
			{4.000000e-01,1,6.000000e-01},
			{4.200000e-01,1,5.800000e-01},
			{4.400000e-01,1,5.600000e-01},
			{4.600000e-01,1,5.400000e-01},
			{4.800000e-01,1,5.200000e-01},
			{5.000000e-01,1,5.000000e-01},
			{5.200000e-01,1,4.800000e-01},
			{5.400000e-01,1,4.600000e-01},
			{5.600000e-01,1,4.400000e-01},
			{5.800000e-01,1,4.200000e-01},
			{6.000000e-01,1,4.000000e-01},
			{6.200000e-01,1,3.800000e-01},
			{6.400000e-01,1,3.600000e-01},
			{6.600000e-01,1,3.400000e-01},
			{6.800000e-01,1,3.200000e-01},
			{7.000000e-01,1,3.000000e-01},
			{7.200000e-01,1,2.800000e-01},
			{7.400000e-01,1,2.600000e-01},
			{7.600000e-01,1,2.400000e-01},
			{7.800000e-01,1,2.200000e-01},
			{8.000000e-01,1,2.000000e-01},
			{8.200000e-01,1,1.800000e-01},
			{8.400000e-01,1,1.600000e-01},
			{8.600000e-01,1,1.400000e-01},
			{8.800000e-01,1,1.200000e-01},
			{9.000000e-01,1,1.000000e-01},
			{9.200000e-01,1,8.000000e-02},
			{9.400000e-01,1,6.000000e-02},
			{9.600000e-01,1,4.000000e-02},
			{9.800000e-01,1,2.000000e-02},
			{1,1,0},
			{1,9.800000e-01,0},
			{1,9.600000e-01,0},
			{1,9.400000e-01,0},
			{1,9.200000e-01,0},
			{1,9.000000e-01,0},
			{1,8.800000e-01,0},
			{1,8.600000e-01,0},
			{1,8.400000e-01,0},
			{1,8.200000e-01,0},
			{1,8.000000e-01,0},
			{1,7.800000e-01,0},
			{1,7.600000e-01,0},
			{1,7.400000e-01,0},
			{1,7.200000e-01,0},
			{1,7.000000e-01,0},
			{1,6.800000e-01,0},
			{1,6.600000e-01,0},
			{1,6.400000e-01,0},
			{1,6.200000e-01,0},
			{1,6.000000e-01,0},
			{1,5.800000e-01,0},
			{1,5.600000e-01,0},
			{1,5.400000e-01,0},
			{1,5.200000e-01,0},
			{1,5.000000e-01,0},
			{1,4.800000e-01,0},
			{1,4.600000e-01,0},
			{1,4.400000e-01,0},
			{1,4.200000e-01,0},
			{1,4.000000e-01,0},
			{1,3.800000e-01,0},
			{1,3.600000e-01,0},
			{1,3.400000e-01,0},
			{1,3.200000e-01,0},
			{1,3.000000e-01,0},
			{1,2.800000e-01,0},
			{1,2.600000e-01,0},
			{1,2.400000e-01,0},
			{1,2.200000e-01,0},
			{1,2.000000e-01,0},
			{1,1.800000e-01,0},
			{1,1.600000e-01,0},
			{1,1.400000e-01,0},
			{1,1.200000e-01,0},
			{1,1.000000e-01,0},
			{1,8.000000e-02,0},
			{1,6.000000e-02,0},
			{1,4.000000e-02,0},
			{1,2.000000e-02,0},
			{1,0,0},
			{9.800000e-01,0,0},
			{9.600000e-01,0,0},
			{9.400000e-01,0,0},
			{9.200000e-01,0,0},
			{9.000000e-01,0,0},
			{8.800000e-01,0,0},
			{8.600000e-01,0,0},
			{8.400000e-01,0,0},
			{8.200000e-01,0,0},
			{8.000000e-01,0,0},
			{7.800000e-01,0,0},
			{7.600000e-01,0,0},
			{7.400000e-01,0,0},
			{7.200000e-01,0,0},
			{7.000000e-01,0,0},
			{6.800000e-01,0,0},
			{6.600000e-01,0,0},
			{6.400000e-01,0,0},
			{6.200000e-01,0,0},
			{6.000000e-01,0,0},
			{5.800000e-01,0,0},
			{5.600000e-01,0,0},
			{5.400000e-01,0,0},
			{5.200000e-01,0,0},
			{5.000000e-01,0,0}
	};

	static public vtkLookupTable getLookuptable()
	{
		vtkLookupTable t = new vtkLookupTable();
		t.SetNumberOfColors(200);
		for (int i = 0; i < 200; i++)
			t.SetTableValue(i, jet[i][0], jet[i][1], jet[i][2], 1.0d);

		return t;
	}

	static public vtkColorTransferFunction getColorTransferFunction(double min, double max)
	{
		vtkColorTransferFunction t = new vtkColorTransferFunction();
		t.SetRange(min, max);
		double d = (max - min) / (200 - 1);
		for (int i = 0; i < 200; i++)
			t.AddRGBPoint(min + i * d, jet[i][0], jet[i][1], jet[i][2]);
		return t;
	}
}

