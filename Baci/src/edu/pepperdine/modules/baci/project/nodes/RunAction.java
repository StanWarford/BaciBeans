/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pepperdine.modules.baci.project.nodes;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.apache.tools.ant.module.api.support.ActionUtils;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.modules.InstalledFileLocator;

public class RunAction {

    public static void run(String filename, String pathname) {
        String classString = pathname + "/" + filename;
        File emulator = InstalledFileLocator.getDefault().locate(
                "JavaBACIClasses",
                "edu.pepperdine.modules.baci",
                false);

        String baccPath = emulator.getAbsolutePath();
        baccPath += ":" + baccPath + "/javabaci/bin";

        String script = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<project name=\"Bacibeans\" default=\"build\" basedir=\".\">"
                + "<target name=\"build\">"
                + "<java classpath=\"" + baccPath + "\" classname=\"bacc\" args=\"" + classString + "\">"
                + "</java>"
                + "<java classpath=\"" + baccPath + "\" classname=\"bainterp\" args=\"" + classString + ".pco" + "\">"
                + "</java>"
                + "</target>"
                + "</project>";

        try {
            File zf = File.createTempFile("ant-build", "xml");
            BufferedWriter out = new BufferedWriter(new FileWriter(zf.getAbsoluteFile()));
            out.write(script);
            out.close();
            FileObject zfo = FileUtil.toFileObject(FileUtil.normalizeFile(zf));
            ActionUtils.runTarget(zfo, new String[]{"build"}, null);
            FileUtil.refreshAll();
            zf.deleteOnExit();
        } catch (IOException i) {
            System.out.println("IO error: " + i);
        }
    }
}
