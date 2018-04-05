/*
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License, version 3,
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package Crawler;

import PorterStemAnalyzer.PorterStemAnalyzer;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import weka.filters.unsupervised.attribute.PotentialClassIgnorer;

/**
 *
 * @author Hossein Ebrahimi
 */

public class Indexer {
    
    private IndexWriter writer;
    public PorterStemAnalyzer analyzer;

    public Indexer() {
    }

    public Indexer(String indexPath, boolean create) throws IOException {
        this.init(indexPath, create);
    }
    
    public void init(String indexPath, boolean create) throws IOException {
        File file = new File(indexPath);
        Directory dir = FSDirectory.open(file);
        analyzer = new PorterStemAnalyzer(Version.LUCENE_47);
        //analyzer = new StandardAnalyzer(Version.LUCENE_47);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
        if (create) {
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        } else {
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }
        writer = new IndexWriter(dir, iwc);
        System.out.println("Writing the index file : " + file.getAbsolutePath());
    }
    
    public void addDoc(Document doc, String path) {
        try {
            if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
                //System.out.println("adding " + path);
                writer.addDocument(doc);
            } else {
                System.out.println("updating " + path);
                writer.updateDocument(new Term("path", path), doc);
            }
        } catch (IOException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        try {
            this.writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

