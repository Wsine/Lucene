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

import java.io.File;
import java.io.IOException;

public class Searcher {
    IndexSearcher indexSearcher;
    QueryParser queryParser;
    Query query;

    public Searcher(String indexDir) throws IOException {
        Directory dir = FSDirectory.open(new File(indexDir).toPath());
        DirectoryReader dirReader = DirectoryReader.open(dir);
        indexSearcher = new IndexSearcher(dirReader);
        queryParser = new QueryParser(LuceneConstants.CONTENTS, new StandardAnalyzer());
    }

    public void close() throws IOException {
        // none
    }

    public TopDocs search(String searchQuery) throws IOException, ParseException {
        query = queryParser.parse(searchQuery);
        return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
    }

    public Document getDocument(ScoreDoc scoreDoc) throws IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }
}
