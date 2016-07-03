package LuceneSource;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

/**
 * <h1>主类之一</h1>
 * 将富文本转化为索引文件存储在磁盘中
 */
public class Indexer {
    /**
     * 索引写入器
     */
    private IndexWriter writer;

    /**
     * 索引构造器-路径
     * @param indexDir 存储索引文件的路径
     * @throws IOException
     */
    public Indexer(@NotNull String indexDir) throws IOException {
        /* 声明磁盘路径 */
        Directory dir = FSDirectory.open(new File(indexDir).toPath());
        /* 使用标准文本解析器（仅支持英文） */
        Analyzer luceneAnalzer = new StandardAnalyzer();
        /* 使用分析器构造索引写入器的配置文件 */
        IndexWriterConfig config = new IndexWriterConfig(luceneAnalzer);
        /* 构造索引写入器 */
        writer = new IndexWriter(dir, config);
    }

    /**
     * 安全关闭索引写入器
     * @throws IOException
     */
    public void close() throws IOException {
        writer.close();
    }

    /**
     * 将文件转化成用来描述文档的{@link Document}类
     * @param f 待转化的输入文件
     * @return 描述文档
     * @throws IOException
     */
    protected Document getDocument(@NotNull File f) throws IOException {
        Document doc = new Document();
        /* 添加描述文档的特定属性 */
        doc.add(new Field(LuceneConstants.CONTENTS, new FileReader(f), TextField.TYPE_NOT_STORED));
        doc.add(new Field(LuceneConstants.FILE_NAME, f.getName(), StringField.TYPE_STORED));
        doc.add(new Field(LuceneConstants.FILE_PATH, f.getPath(), StringField.TYPE_STORED));
        return doc;
    }

    /**
     * 对文件进行转化为索引记录下来
     * @param file 输入文件
     * @throws IOException
     */
    protected void indexFile(@NotNull File file) throws IOException {
        System.out.println("Indexing " + file.getCanonicalPath());
        Document document = getDocument(file);
        writer.addDocument(document);
    }

    /**
     * 创建索引
     * @param dataDir 数据源的路径
     * @param filter 文件过滤器，筛选特定的文件输入
     * @return 索引的文件的数量
     * @throws IOException
     */
    public int createIndex(@NotNull String dataDir, FileFilter filter) throws IOException {
        File[] files = new File(dataDir).listFiles();
        /* 获取全部的文件创建索引 */
        for (File f : files) {
            if (!f.isDirectory() && !f.isHidden() && f.exists() && f.canRead()
                    && (filter == null || filter.accept(f))) {
                indexFile(f);
            }
        }
        return writer.numDocs();
    }
}
