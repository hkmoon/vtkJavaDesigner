vtkJavaDesigner
===============

Run vtk program by run-time dynamic code injection.
vtkJavaDesigner makes easy to develop vtk java programs in the small text window.

Requirements
---------------
VTK native libraries and wrappers should be installed on the computer.

Under the system environment setting, add the vtk library path to LD_LIBRARY_PATH or DYLD_LIBRARY_PATH(for Mac OSX).

For example,
	DYLD_LIBRARY_PATH=/Users/user/Projects/git-projects/VTK/VTKBuild/lib
	
In order to compile VTK in Mac OSX, refer http://stackoverflow.com/questions/17329258/how-to-install-vtk-6-1-for-osx-10-8-with-cocoa-xcode-support.


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


Acknowledgements
---------------
1. Robert Futrell's RSyntexTextArea, AutoComplete and RSTALanguageSupport are used.
1. Peter Lawrey's dynamic code injection codes are used.
1. Werner Randelshofer's ExtensionFileFilter codes are used.
