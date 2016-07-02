package LuceneTest;

import LuceneSource.Indexer;
import LuceneSource.TextFileFilter;

import java.io.IOException;

public class IndexerTest {
    private Indexer indexer;

    public static void main(String[] args) {
        IndexerTest test;
        try {
            test = new IndexerTest();
            test.createIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createIndex() throws IOException {
        indexer = new Indexer(LuceneConstants.indexDir);
        long startTime = System.currentTimeMillis();
        int numIndexed = indexer.createIndex(LuceneConstants.dataDir, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(numIndexed + " files indexed, time taken: " + (endTime - startTime) + "ms.");
    }
}
