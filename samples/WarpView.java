package moon;
import vtk.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * WarpView provides 3D visualization of grey image intensity
 * Created by moon on 8/7/14.
 */
public class WarpView extends JPanel implements ActionListener {

	int currentT = 0;

	//// 1) Create the standard renderer, render window and int
	private vtkRenderWindowPanel renWin;
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
		renWin = new vtkRenderWindowPanel();
		renWin.setPreferredSize(new Dimension(320, 200));
		renWin.setInteractorStyle(new vtkInteractorStyleTrackballCamera());

		double w = 320d * 320d;
		double h = 200d * 200d;

		double re = Math.sqrt(w + h);

		double expHeight = re / 7d;

		double factor = expHeight / 248;

		show(factor);

		// Add Java UI components
		exitButton = new JButton("Exit");
		exitButton.addActionListener(this);

		add(renWin);
		add(exitButton, BorderLayout.SOUTH);
	}


	public void show(double scaleFactor)
	{
		if(null != actor)
		{
			removeActor(actor);
		}

		// Image data
		vtkTIFFReader reader = new vtkTIFFReader();
		reader.SetFileName("samples/clown.tif");
		reader.SetOrientationType(2);
		reader.SetDataScalarTypeToFloat();
		reader.Update();

		vtkAlgorithmOutput imageData = reader.GetOutputPort();
		
		vtkLookupTable colorTable = Jet.getLookuptable();
		colorTable.Build();

		vtkImageDataGeometryFilter geometry = new vtkImageDataGeometryFilter();
		geometry.SetInputConnection(imageData);

		vtkWarpScalar warp = new vtkWarpScalar();
		warp.SetInputConnection(geometry.GetOutputPort());
		warp.SetScaleFactor(scaleFactor);

		vtkMergeFilter merge = new vtkMergeFilter();
		merge.SetGeometryConnection(warp.GetOutputPort());
		merge.SetScalarsData(reader.GetOutput());

		vtkDataSetMapper mapper = new vtkDataSetMapper();
		mapper.SetLookupTable(colorTable);
		mapper.SetInputConnection(merge.GetOutputPort());
		mapper.SetScalarRange(0d, 248d);

		actor = new vtkActor();
		actor.SetMapper(mapper);

		addActor(actor);

		vtkScalarBarActor scalarBar = new vtkScalarBarActor();
		scalarBar.SetTitle("");
		scalarBar.SetOrientationToHorizontal();
		scalarBar.SetLookupTable(colorTable);

		vtkScalarBarWidget scalarBarWidget = new vtkScalarBarWidget();
		scalarBarWidget.SetInteractor(renWin.getRenderWindowInteractor());
		scalarBarWidget.SetScalarBarActor(scalarBar);
		scalarBarWidget.On();

		if(null == camera)
		{
			renWin.GetRenderer().ResetCamera();;
//			renWin.GetRenderer().GetActiveCamera().Roll(180);
//			renWin.GetRenderer().GetActiveCamera().Azimuth(180);
			renWin.GetRenderer().GetActiveCamera().Dolly(1.4);
			renWin.GetRenderer().ResetCameraClippingRange();
			camera = renWin.GetRenderer().GetActiveCamera();
		}
		else
		{
			renWin.GetRenderer().SetActiveCamera(camera);
		}
	}

	private void addActor(vtkActor actor)
	{
		renWin.GetRenderer().AddActor(actor);
	}

	private void removeActor(vtkActor actor)
	{
		renWin.GetRenderer().RemoveActor(actor);
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

