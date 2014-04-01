
package edu.pepperdine.modules.baci.project.nodes;

import edu.pepperdine.modules.baci.BaciBeansProject;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

@NodeFactory.Registration(projectType = "edu-pepperdine-modules-baci", position = 10)
public class TextsNodeFactory implements NodeFactory {

    @Override
    public NodeList<?> createNodes(Project project) {
        BaciBeansProject p = project.getLookup().lookup(BaciBeansProject.class);
        assert p != null;
        return new TextsNodeList(p);
    }

    public class TextsNodeList implements NodeList<Node> {

        BaciBeansProject project;

        public TextsNodeList(BaciBeansProject project) {
            this.project = project;
        }

        @Override
        public List<Node> keys() {
            String folderName = project.getProjectDirectory().getName();
            FileObject textsFolder = 
                  project.getProjectDirectory();
//                project.getProjectDirectory().getFileObject("Source Files");
            List<Node> result = new ArrayList<Node>();
            if (textsFolder != null) {
                for (FileObject textsFolderFile : textsFolder.getChildren()) {
                    try {
                        System.out.println(textsFolderFile.getExt());
                        if((
                                textsFolderFile.hasExt("lst") || 
                                textsFolderFile.hasExt("pco") || 
                                textsFolderFile.hasExt("cm"))
                                && textsFolderFile.getName().equals(folderName) 
                                || textsFolderFile.getName().equals("C--File.cm")){
                            result.add(DataObject.find(textsFolderFile).getNodeDelegate());
                        }
                    } catch (DataObjectNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
            return result;
        }

        @Override
        public Node node(Node node) {
            return new FilterNode(node);
        }

        @Override
        public void addNotify() {
        }

        @Override
        public void removeNotify() {
        }

        @Override
        public void addChangeListener(ChangeListener cl) {
        }

        @Override
        public void removeChangeListener(ChangeListener cl) {
        }
        
    }
    
}