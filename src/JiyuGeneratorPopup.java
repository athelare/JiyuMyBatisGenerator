import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.ljy.GeneratorController;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class JiyuGeneratorPopup extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        VirtualFile file = PlatformDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        if(!file.getName().equals("generatorConfig.xml")){
            System.out.println("不是generatorConfig.xml文件");
            return;
        }
        System.out.println(file.getPath());
        GeneratorController.GenerateFromFile(file.getPath());
        System.out.println("Finished.");
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        //super.update(e);
        VirtualFile file = PlatformDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        String fileName = file.getName();

        if(!fileName.equals("generatorConfig.xml")){
            e.getPresentation().setEnabled(false);
        }
    }


}
