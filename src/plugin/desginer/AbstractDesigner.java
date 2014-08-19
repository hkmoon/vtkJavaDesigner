package plugin.desginer;

import org.fife.rsta.ac.LanguageSupport;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.rsta.ac.java.JavaLanguageSupport;
import org.fife.rsta.ac.java.buildpath.*;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.*;
import plugin.PluginRuntime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AbstractDesigner provides runtime compilation.
 *
 * @author HongKee Moon
 * @version 0.1beta
 * @since 9/5/13
 */
public abstract class AbstractDesigner extends JFrame {
    protected final String pluginType;
    protected RSyntaxTextArea textArea;
    protected Class plugin;
    protected HashMap<String, ActionListener> buttons = new HashMap<String, ActionListener>();

    protected void load(Class clazz)
    {
		Method method = null;
		try {
			method = clazz.getMethod("main", String[].class);
			method.invoke(null, new String[1]);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    }

    protected void unload()
    {
        System.out.println("Unloaded");
    }

    protected AbstractDesigner(String pluginType) {
        this.pluginType = pluginType;
        initializeComponents();
    }


    protected void initializeComponents()
    {
        JPanel cp = new JPanel(new BorderLayout());

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton loadBtn = new JButton("Load");
        loadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = getJavaFileChooser();

                int returnVal = chooser.showOpenDialog(getParent());
                if(returnVal == JFileChooser.APPROVE_OPTION)
                {
                    String filename = chooser.getSelectedFile().getAbsolutePath();

                    try {
                        FileInputStream fis = new FileInputStream(filename);
                        InputStreamReader in = new InputStreamReader(fis, "UTF-8");

                        textArea.read(in, null);

                        in.close();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IOException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        });
        bp.add(loadBtn);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = getJavaFileChooser();

                int returnVal = chooser.showSaveDialog(getParent());
                if(returnVal == JFileChooser.APPROVE_OPTION)
                {
                    String filename = chooser.getSelectedFile().getAbsolutePath();
                    if(!filename.endsWith(".java"))
                    {
                        filename += ".java";
                    }

                    try {
                        FileOutputStream fos = new FileOutputStream(filename);
                        OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");

                        textArea.write(out);

                        out.close();

                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IOException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        });
        bp.add(saveBtn);

        JButton compileBtn = new JButton("Compile/Run");
        compileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compile();
            }
        });
        bp.add(compileBtn);

        for(Map.Entry<String, ActionListener> item : buttons.entrySet())
        {
            JButton btn = new JButton(item.getKey());
            btn.addActionListener(item.getValue());
            bp.add(btn);
        }

		LanguageSupportFactory lsf = LanguageSupportFactory.get();
		LanguageSupport support = lsf.getSupportFor(SyntaxConstants.SYNTAX_STYLE_JAVA);
		JavaLanguageSupport jls = (JavaLanguageSupport)support;
		jls.setAutoCompleteEnabled(true);
		jls.setParameterAssistanceEnabled(true);
		jls.setShowDescWindow(false);
		jls.setAutoActivationDelay(300);
		//jls.setAutoActivationEnabled(true);

//		DirLibraryInfo dirInfo = new DirLibraryInfo("bin/", new DirSourceLocation("src/"));
		JarLibraryInfo jarInfo = new JarLibraryInfo("lib/vtk.jar");
		try {
			jls.getJarManager().addCurrentJreClassFileSource();
			jls.getJarManager().addClassFileSource(jarInfo);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

        textArea = new RSyntaxTextArea(20, 60);
		lsf.register(textArea);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        textArea.setAntiAliasingEnabled(true);
		textArea.setAutoIndentEnabled(true);
		textArea.setCloseCurlyBraces(true);
		textArea.setMarkOccurrences(true);
		textArea.setPaintMarkOccurrencesBorder(true);
		textArea.setPaintMatchedBracketPair(true);
		textArea.setPaintTabLines(true);
		textArea.setTabsEmulated(false);
		//setTheme("eclipse");
		setTheme("dark");


        RTextScrollPane sp = new RTextScrollPane(textArea);
        sp.setFoldIndicatorEnabled(true);
        cp.add(bp, BorderLayout.NORTH);
        cp.add(sp, BorderLayout.CENTER);

        setContentPane(cp);
        setTitle(pluginType);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

	public void setTheme(String s)
	{
		try
		{
			Theme t = Theme.load(this.getClass().getClassLoader().getResourceAsStream("themes/" + s + ".xml"));
			t.apply(textArea);
		} catch (IOException e)
		{
			System.out.println("Couldn't load theme");
		}
	}

    private void compile() {
        if(plugin != null)
        {
            unload();
        }

        StringWriter writer = new StringWriter();

        try {
            textArea.write(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PluginRuntime runtime = new PluginRuntime();

        String code = writer.toString();

		if(code.trim().isEmpty())
		{
			System.out.println("No code is provided.");
			return;
		}

        // Remove package declaration
		Pattern pkg = Pattern.compile("[\\s]*package (.*?);");
		Matcher pkgMatcher = pkg.matcher(code);
		pkgMatcher.find();
		String pkgName = pkgMatcher.group(1);

        // Find a plugin class name
        Pattern pattern = Pattern.compile("[\\s]*public class (.*?) ");
        Matcher m = pattern.matcher(code);

        m.find();
        String className = pkgName + "." + m.group(1);

        if(runtime.compile(className, code))
        {
            try {
                plugin = runtime.instanciate(className, writer.toString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InstantiationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            load(plugin);
        }
    }

    private JFileChooser getJavaFileChooser() {
        JFileChooser c = new JFileChooser();
        ExtensionFileFilter defaultFilter = new ExtensionFileFilter("Java","java");
        c.addChoosableFileFilter(defaultFilter);

        c.setFileFilter(defaultFilter);
        return c;
    }
}
