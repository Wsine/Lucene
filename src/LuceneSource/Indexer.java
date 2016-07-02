package LuceneSource;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

public class Indexer {
    private IndexWriter writer;

    public Indexer(String indexDir) throws IOException {
        Directory dir = FSDirectory.open(new File(indexDir).toPath());
        Analyzer luceneAnalzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(luceneAnalzer);
        writer = new IndexWriter(dir, config);
    }

    public void close() throws IOException {
        writer.close();
    }

    protected Document getDocument(File f) throws Exception {
        Document doc = new Document();
        FieldType defaultType = new FieldType();
        doc.add(new Field(LuceneConstants.CONTENTS, new FileReader(f), defaultType));
        FieldType storeNotAnalyzerType = new FieldType();
        storeNotAnalyzerType.setStored(true);
        storeNotAnalyzerType.setTokenized(false);
        doc.add(new Field(LuceneConstants.FILE_NAME, f.getName(), storeNotAnalyzerType));
        doc.add(new Field(LuceneConstants.FILE_PATH, f.getPath(), storeNotAnalyzerType));
        return doc;
    }

    protected void indexFile(File file) throws Exception {
        System.out.println("Indexing " + file.getCanonicalPath());
        Document document = getDocument(file);
        writer.addDocument(document);
    }

    public int createIndex(String dataDir, FileFilter filter) throws Exception {
        File[] files = new File(dataDir).listFiles();
        for (File f : files) {
            if (!f.isDirectory() && !f.isHidden() && f.exists() && f.canRead()
                    && (filter == null || filter.accept(f))) {

            }
        }
        return writer.numDocs();
    }
}
