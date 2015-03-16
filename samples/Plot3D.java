package moon;
import vtk.*;
import vtk.vtkActor2D;
import vtk.vtkLabeledDataMapper;
import vtk.vtkOutlineFilter;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Plot3D provides 3D visualization of 3D points
 * Created by moon on 8/22/14.
 */
public class Plot3D extends JPanel implements ActionListener {

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

	public Plot3D()
	{
		super(new BorderLayout(10, 10));
		renWin = new vtkRenderWindowPanel();
		renWin.setPreferredSize(new Dimension(320, 200));
		renWin.setInteractorStyle(new vtkInteractorStyleTrackballCamera());

		show();

		// Add Java UI components
		exitButton = new JButton("Exit");
		exitButton.addActionListener(this);

		add(renWin);
		add(exitButton, BorderLayout.SOUTH);
	}


	public void show()
	{
		if(null != actor)
		{
			removeActor(actor);
		}

		// Read the file
		vtkSimplePointsReader reader = new vtkSimplePointsReader();
		reader.SetFileName("samples/simple.xyz");
		reader.Update();

		vtkPolyData inputPolyData = new vtkPolyData();
		inputPolyData.CopyStructure(reader.GetOutput());

		vtkUnsignedCharArray colors = new vtkUnsignedCharArray();
		colors.SetNumberOfComponents(1);
		colors.SetNumberOfTuples(inputPolyData.GetNumberOfPoints());

		vtkStringArray labels = new vtkStringArray();
		labels.SetNumberOfComponents(1);
		labels.SetNumberOfTuples(inputPolyData.GetNumberOfPoints());
		labels.SetName("Name");

		int i = 0;

		try {
			BufferedReader br = new BufferedReader(new FileReader("samples/simple.label"));
			try {

				String line = br.readLine();

				while (line != null) {
					String[] tokens = line.split("_");
					labels.InsertValue(i, line);

					int color = 0;
					if("AP".equals(tokens[1])) color = 1;
					else if("BP".equals(tokens[1])) color = 2;
					else if("N".equals(tokens[1])) color = 3;
					colors.InsertTuple1(i, color);
					i++;

					line = br.readLine();
				}
			} catch(Exception e) {


			} finally {
				br.close();
			}

		} catch(Exception ex) {

		}

		inputPolyData.GetPointData().SetScalars(colors);
		inputPolyData.GetPointData().AddArray(labels);

		vtkSphereSource glyphSource = new vtkSphereSource();
		glyphSource.SetRadius(0.09d);
		glyphSource.Update();

		vtkGlyph3D glyph3D = new vtkGlyph3D();
		glyph3D.GeneratePointIdsOn();
		glyph3D.SetSourceConnection(glyphSource.GetOutputPort());
		glyph3D.SetInputData(inputPolyData);
		glyph3D.ScalingOff();
		glyph3D.Update();

		vtkLookupTable colorTable = Jet.getLookuptable();
		colorTable.Build();

		vtkLabeledDataMapper labelMapper = new vtkLabeledDataMapper();
		labelMapper.SetFieldDataName("Name");
		labelMapper.SetLabelModeToLabelFieldData();
		labelMapper.SetInputData(inputPolyData);
		labelMapper.SetLabelFormat("%s");

		vtkActor2D labelActor = new vtkActor2D();
		labelActor.SetMapper(labelMapper);

		// Bounding box
		vtkOutlineFilter outline = new vtkOutlineFilter();
		outline.SetInputData(inputPolyData);

		vtkPolyDataMapper outlineMapper = new vtkPolyDataMapper();
		outlineMapper.SetInputConnection(outline.GetOutputPort());
		vtkActor outlineActor = new vtkActor();
		outlineActor.SetMapper(outlineMapper);
		outlineActor.GetProperty().SetColor(0, 0, 0);

		// Visualize
		vtkDataSetMapper mapper = new vtkDataSetMapper();
		mapper.SetLookupTable(colorTable);
		mapper.SetScalarRange(0d, 4d);
		mapper.SetInputConnection(glyph3D.GetOutputPort());

		vtkActor actor = new vtkActor();
		actor.GetProperty().SetPointSize(4);
		actor.SetMapper(mapper);

		addActor(actor);
		addActor(outlineActor);
		addActor(labelActor);

		vtkTextProperty tprop = new vtkTextProperty();
		tprop.SetColor(1, 1, 1);
		tprop.ShadowOn();

		// add & render CubeAxes
		vtkCubeAxesActor2D axes = new vtkCubeAxesActor2D();
		axes.SetInputData(glyph3D.GetOutput());
		axes.SetFontFactor(0.8);
		axes.SetFlyModeToOuterEdges();
		axes.SetCamera(renWin.GetRenderer().GetActiveCamera());
		axes.SetXLabel("PC1");
		axes.SetYLabel("PC2");
		axes.SetZLabel("PC3");
		axes.SetAxisTitleTextProperty(tprop);
		axes.SetAxisLabelTextProperty(tprop);

		renWin.GetRenderer().AddViewProp(axes);

		if(null == camera)
		{
			renWin.GetRenderer().ResetCamera();
			renWin.GetRenderer().SetBackground(0.3, 0.4, 0.4);
			renWin.GetRenderer().ResetCameraClippingRange();
			camera = renWin.GetRenderer().GetActiveCamera();
		}
		else
		{
			renWin.GetRenderer().SetActiveCamera(camera);
		}
	}

	private void addActor(vtkProp actor)
	{
		renWin.GetRenderer().AddActor(actor);
	}

	private void removeActor(vtkProp actor)
	{
		renWin.GetRenderer().RemoveActor(actor);
	}

	public static void main(String s[]) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame("Plot3D");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.getContentPane().add(new Plot3D());
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

