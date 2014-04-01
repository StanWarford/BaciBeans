package edu.pepperdine.modules.baci;

import java.io.IOException;
import org.netbeans.api.project.Project;
//import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;
//import org.openide.windows.TopComponent;
//import edu.pepperdine.bacibeans.cmfiletype.CmDataObject;
//import org.openide.filesystems.MIMEResolver;
//import org.openide.loaders.DataObject;


@ServiceProvider(service=ProjectFactory.class)
public class BaciBeansFactory implements ProjectFactory {

    //Specifies when a project is a project, i.e.,   
    @Override
    public boolean isProject(FileObject projectDirectory) {     
       return projectDirectory.getFileObject(projectDirectory.getName(), "cm") != null; 
    }
    //Specifies when the project will be opened, i.e., if the project exists:
    @Override
    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
        return isProject(dir) ? new BaciBeansProject(dir, state) : null;
    }

    @Override
    public void saveProject(final Project project) throws IOException, ClassCastException {
        // leave unimplemented for the moment
    }

}