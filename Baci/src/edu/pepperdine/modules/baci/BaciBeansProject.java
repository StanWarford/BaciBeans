package edu.pepperdine.modules.baci;

import edu.pepperdine.modules.baci.project.nodes.BuildAction;
import edu.pepperdine.modules.baci.project.nodes.DebugAction;
import edu.pepperdine.modules.baci.project.nodes.RunAction;
import java.awt.Image;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

public class BaciBeansProject implements Project {

    private final FileObject projectDir;
    private final ProjectState state;
    private Lookup lkp;

    public BaciBeansProject(FileObject dir, ProjectState state) {
        this.projectDir = dir;
        this.state = state;
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{ // register your features here
                this,
                state,
                new Info(),
                new BaciBeansProjectLogicalView(this),
                new BaciBeansCustomizerProvider(this),
                //                        new BuildAction(this),
                //                        new RunAction(this),
                //this provides standard actions like build and clean
                new ActionProviderImpl()
            });
        }
        return lkp;
    }

    private final class ActionProviderImpl implements ActionProvider {

        private final String[] supported = new String[]{
            ActionProvider.COMMAND_BUILD,
            ActionProvider.COMMAND_RUN,
            ActionProvider.COMMAND_DEBUG};

        @Override
        public String[] getSupportedActions() {
            return supported;
        }

        @Override
        public void invokeAction(String string, Lookup lookup) throws IllegalArgumentException {
            String filename = projectDir.getName();
            String pathname = projectDir.getPath();
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_BUILD)) {
                BuildAction.build(filename, pathname);
            }
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_RUN)) {
                RunAction.run(filename, pathname);
            }
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_DEBUG)) {
                DebugAction.debug(filename, pathname);
            }
        }

        @Override
        public boolean isActionEnabled(String command, Lookup lookup) throws IllegalArgumentException {
            if ((command.equals(ActionProvider.COMMAND_BUILD))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_RUN))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_DEBUG))) {
                return true;
            } else {
                throw new IllegalArgumentException(command);
            }
        }
    }

    private final class Info implements ProjectInformation {

        @StaticResource()
        public static final String BACIBEANS_ICON = "edu/pepperdine/modules/baci/CMMProject.png";

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(BACIBEANS_ICON));
        }

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public Project getProject() {
            return BaciBeansProject.this;
        }
    }

    public class BaciBeansProjectLogicalView implements LogicalViewProvider {

        @StaticResource()
        public static final String BACIBEANS_ICON = "edu/pepperdine/modules/baci/CMMProject.png";
        private final BaciBeansProject project;

        public BaciBeansProjectLogicalView(BaciBeansProject project) {
            this.project = project;
        }

        @Override
        public Node createLogicalView() {
            try {
                //Obtain the project directory's node:
                FileObject projectDirectory = project.getProjectDirectory();
                DataFolder projectFolder = DataFolder.findFolder(projectDirectory);
                Node nodeOfProjectFolder = projectFolder.getNodeDelegate();
                //Decorate the project directory's node:
                return new ProjectNode(nodeOfProjectFolder, project);
            } catch (DataObjectNotFoundException donfe) {
                Exceptions.printStackTrace(donfe);
                //Fallback-the directory couldn't be created -
                //read-only filesystem or something evil happened
                return new AbstractNode(Children.LEAF);
            }
        }

        private final class ProjectNode extends FilterNode {

            final BaciBeansProject project;

            public ProjectNode(Node node, BaciBeansProject project)
                    throws DataObjectNotFoundException {
                super(node,
                        NodeFactorySupport.createCompositeChildren(
                        project,
                        "Projects/edu-pepperdine-modules-baci/Nodes"),
                        //new FilterNode.Children(node),
                        new ProxyLookup(
                        new Lookup[]{
                    Lookups.singleton(project),
                    node.getLookup()
                }));
                this.project = project;
            }

            @Override
            public Action[] getActions(boolean arg0) {
                return new Action[]{
                    CommonProjectActions.newFileAction(),
                    CommonProjectActions.copyProjectAction(),
                    CommonProjectActions.deleteProjectAction(),
                    CommonProjectActions.closeProjectAction(),
                    CommonProjectActions.customizeProjectAction(),
                    //Added on top of tuitorial 
                    CommonProjectActions.newProjectAction(),
                    CommonProjectActions.setAsMainProjectAction()
                };
            }

            @Override
            public Image getIcon(int type) {
                return ImageUtilities.loadImage(BACIBEANS_ICON);
            }

            @Override
            public Image getOpenedIcon(int type) {
                return getIcon(type);
            }

            @Override
            public String getDisplayName() {
                return project.getProjectDirectory().getName();
            }
        }

        @Override
        public Node findPath(Node root, Object target) {
            //leave unimplemented for now
            return null;
        }
    }
}