package LuceneTest;

import LuceneSource.Indexer;
import LuceneSource.TextFileFilter;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

public class IndexerTest {
    private Indexer indexer;

    @Test
    public void testCreate() throws Exception {
        IndexerTest test = new IndexerTest();
        assertTrue(test.createIndex() < 100);
    }

    private long createIndex() throws IOException {
        indexer = new Indexer(LuceneConstants.indexDir);
        long startTime = System.currentTimeMillis();
        int numIndexed = indexer.createIndex(LuceneConstants.dataDir, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(numIndexed + " files indexed, time taken: " + (endTime - startTime) + "ms.");
        return (endTime - startTime);
    }
}
