package com.shuzijun.leetcode.plugin.actions.tree;

import com.intellij.ide.RevealFileInFinder;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.shuzijun.leetcode.plugin.model.CodeTypeEnum;
import com.shuzijun.leetcode.plugin.model.Config;
import com.shuzijun.leetcode.plugin.model.Question;
import com.shuzijun.leetcode.plugin.setting.PersistentConfig;
import com.shuzijun.leetcode.plugin.utils.MessageUtils;
import com.shuzijun.leetcode.plugin.utils.VelocityUtils;

import java.awt.*;
import java.io.File;

/**
 * @author laizhixingxingdeli
 */
public class OpenFolderAction extends AbstractTreeAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config, Question question) {
        Project project = anActionEvent.getProject();
        if (project == null) {
            return;
        }

        CodeTypeEnum codeTypeEnum = config.getCodeTypeEnum(project);
        if (codeTypeEnum == null) {
            return;
        }

        String filePath = PersistentConfig.getInstance().getTempFilePath()
                + VelocityUtils.convert(config.getCustomFileName(), question)
                + codeTypeEnum.getSuffix();

        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && parentDir.exists()) {
            VirtualFile vf = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(parentDir);
            if (vf != null) {
                RevealFileInFinder.getInstance().selectFile(vf);
            } else if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(parentDir);
                } catch (Exception e) {
                    MessageUtils.getInstance(project).showWarnMsg("info", "Failed to open folder");
                }
            }
        } else {
            MessageUtils.getInstance(project).showWarnMsg("info", "File not found, please open the question first");
        }
    }
}
