vtkJavaDesigner
===============

Let's run vtk & opencv program by run-time dynamic code compilation.
vtkJavaDesigner makes easy to develop vtk opencv java programs in the small text window.

Requirements
---------------
VTK native libraries and wrappers should be installed on the computer.
OpenCV native libraries should be installed on the computer.

Under the system environment setting, add the vtk library path to LD_LIBRARY_PATH(for linux) or DYLD_LIBRARY_PATH(for Mac OSX).

	(Linux) LD_LIBRARY_PATH=/usr/local/VTK/VTKBuild/lib:/usr/local/opencv/build/lib
	(Mac OSX) DYLD_LIBRARY_PATH=/Users/user/Projects/git-projects/VTK/VTKBuild/lib:/Users/user/Projects/git-projects/opencv/build/lib
	
In order to compile VTK in Mac OSX, refer http://stackoverflow.com/questions/17329258/how-to-install-vtk-6-1-for-osx-10-8-with-cocoa-xcode-support

For java opencv, refer http://docs.opencv.org/doc/tutorials/introduction/desktop_java/java_dev_intro.html

Features
---------------
* Code completion

![CodeCompletionScreenshot](https://github.com/hkmoon/vtkJavaDesigner/blob/master/doc/CodeCompletion.png?raw=true)

Samples
---------------
Currently, there is one sample code in /samples folder. 

1. WarpView.java provides 3D representation of clown.tiff pixel intensity. In order to run this script, please, set the working directory to your cloned vtkJavaDesigner.

![WarpView1Screenshot](https://github.com/hkmoon/vtkJavaDesigner/blob/master/doc/WarpView1.png?raw=true)

![WarpView2Screenshot](https://github.com/hkmoon/vtkJavaDesigner/blob/master/doc/WarpView2.png?raw=true)

Trouble shooting
---------------
* When you use jdk8, fiji can complain with the wrong compiled version. In such a case, please, remove ```javac-1.6.0.24-ubuntu-fiji2.jar``` and ```tools-1.4.2.jar``` from ```Fiji.app/jars```(refer https://github.com/fiji/fiji/issues/119).

Acknowledgements
---------------
1. Robert Futrell's RSyntexTextArea, AutoComplete and RSTALanguageSupport are used.
1. Peter Lawrey's dynamic code injection codes are used.
1. Werner Randelshofer's ExtensionFileFilter codes are used.
