package edu.pepperdine.modules.baci.panels;

import javax.swing.JComponent;
import javax.swing.JPanel;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

public class GeneralBaciBeansProperties 
    implements ProjectCustomizer.CompositeCategoryProvider {

    private static final String GENERAL = "General";

    @ProjectCustomizer.CompositeCategoryProvider.Registration(
            projectType = "edu-pepperdine-modules-baci", position = 10)
    public static GeneralBaciBeansProperties createGeneral() {
        return new GeneralBaciBeansProperties();
    }

    @NbBundle.Messages("LBL_Config_General=General")
    @Override
    public Category createCategory(Lookup lkp) {
        return ProjectCustomizer.Category.create(
                GENERAL,
                Bundle.LBL_Config_General(),
                null);
    }

    @Override
    public JComponent createComponent(Category category, Lookup lkp) {
        return new JPanel();
    }

}