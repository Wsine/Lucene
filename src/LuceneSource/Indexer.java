package LuceneSource;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
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

    protected Document getDocument(File f) throws IOException {
        Document doc = new Document();
        doc.add(new Field(LuceneConstants.CONTENTS, new FileReader(f), TextField.TYPE_STORED));
        doc.add(new Field(LuceneConstants.FILE_NAME, f.getName(), StringField.TYPE_STORED));
        doc.add(new Field(LuceneConstants.FILE_PATH, f.getPath(), StringField.TYPE_STORED));
        return doc;
    }

    protected void indexFile(File file) throws IOException {
        System.out.println("Indexing " + file.getCanonicalPath());
        Document document = getDocument(file);
        writer.addDocument(document);
    }

    public int createIndex(String dataDir, FileFilter filter) throws IOException {
        File[] files = new File(dataDir).listFiles();
        for (File f : files) {
            if (!f.isDirectory() && !f.isHidden() && f.exists() && f.canRead()
                    && (filter == null || filter.accept(f))) {
                indexFile(f);
            }
        }
        return writer.numDocs();
    }
}
