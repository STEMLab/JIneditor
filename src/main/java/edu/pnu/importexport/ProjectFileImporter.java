package edu.pnu.importexport;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.pnu.gui.FloorImportDialog;
import edu.pnu.gui.MainFrame;
import edu.pnu.project.ProjectFile;

public class ProjectFileImporter {
	MainFrame parent;
	ProjectFile origin;
	ProjectFile project;
	
	public ProjectFileImporter(MainFrame parent, ProjectFile origin) {
		// TODO Auto-generated constructor stub
		this.parent = parent;
		this.origin = origin;
	}
	
	public void importProject() {
		String filePath;
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("data (*.dat)", "dat");
        fileChooser.setFileFilter(filter);
        
        int returnVal = fileChooser.showOpenDialog(parent);
        if( returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fileChooser.getSelectedFile();
    		FileInputStream fis = null;
    		BufferedInputStream bis = null;
    		ObjectInputStream ois = null;
    		try {
    			fis = new FileInputStream(file);
    			bis = new BufferedInputStream(fis);
    			ois = new ObjectInputStream(bis);
    			
    			project = (ProjectFile)ois.readObject();
    			ois.close();
    		} catch (FileNotFoundException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		} catch (ClassNotFoundException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
        }
        
        if(project == null) return;
        
        FloorImportDialog importDialog = new FloorImportDialog(parent, origin, project);
        importDialog.setModal(true);
		importDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		importDialog.setVisible(true);
	}

}
