package LuceneSource;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileFilter;

/**
 * Txt文本文件过滤器
 */
public class TextFileFilter implements FileFilter {
    /**
     * 仅接受以txt为后缀的文件
     * @param f 输入文件
     * @return 是否通过过滤
     */
    @Override
    public boolean accept(@NotNull File f) {
        return f.getName().toLowerCase().endsWith(".txt");
    }
}
