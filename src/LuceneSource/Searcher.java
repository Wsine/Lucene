package LuceneSource;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * <h1>主类之一</h1>
 * 用来搜索特定的关键词
 */
public class Searcher {
    /**
     * 索引搜索器
     */
    IndexSearcher indexSearcher;
    /**
     * 关键词解析器
     */
    QueryParser queryParser;
    /**
     * 实际搜索内容
     */
    Query query;

    /**
     * 搜索器构造函数-带索引路径
     * @param indexDir 索引存放路径
     * @throws IOException
     */
    public Searcher(@NotNull String indexDir) throws IOException {
        /* 声明磁盘路径 */
        Directory dir = FSDirectory.open(new File(indexDir).toPath());
        /* 声明路径的读入器 */
        DirectoryReader dirReader = DirectoryReader.open(dir);
        /* 构造索引搜索器 */
        indexSearcher = new IndexSearcher(dirReader);
        /* 构造关键词解析器，根据文本内容解析，使用标准分析器（仅支持英文） */
        queryParser = new QueryParser(LuceneConstants.CONTENTS, new StandardAnalyzer());
    }

    /**
     * 安全关闭（暂时无用）
     * @throws IOException
     */
    public void close() throws IOException {
        // none
    }

    /**
     * 搜索函数
     * @param searchQuery 搜索关键词
     * @return 最大搜索数量的搜索结果
     * @throws IOException
     * @throws ParseException
     */
    public TopDocs search(@NotNull String searchQuery) throws IOException, ParseException {
        query = queryParser.parse(searchQuery);
        return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
    }

    /**
     * 获取描述文档
     * @param scoreDoc 搜索结果的其中一条记录
     * @return 对应的描述文档
     * @throws IOException
     */
    public Document getDocument(@NotNull ScoreDoc scoreDoc) throws IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }
}
