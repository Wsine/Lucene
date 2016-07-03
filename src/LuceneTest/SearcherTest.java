package LuceneTest;

import LuceneSource.Searcher;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

public class SearcherTest {
    private Searcher searcher;

    @Test
    public void testSearch() throws Exception {
        SearcherTest test = new SearcherTest();
        assertTrue(4 == test.search("Lorem"));
        assertTrue(0 == test.search("Wsine"));
    }

    private int search(String searchQuery) throws IOException, ParseException {
        searcher = new Searcher(LuceneConstants.indexDir);
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(searchQuery);
        long endTime = System.currentTimeMillis();

        System.out.println(hits.totalHits + " documents found. Time: " + (endTime - startTime));
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc);
            System.out.println("File: " + doc.get(LuceneSource.LuceneConstants.FILE_PATH));
        }
        return hits.totalHits;
    }
}
