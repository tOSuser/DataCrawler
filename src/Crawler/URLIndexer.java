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

import Crawler.Indexer;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 *
 * @author Hossein Ebrahimi
 */
public class URLIndexer {


    public Indexer indexer = null;
    private static URLIndexer myinstance = null;

    public SortedSet<PageInfo>  pageinfoset = new TreeSet<>();
    public SortedSet<PathInfo>     pathinfoset = new TreeSet<>();    

    protected URLIndexer() {
    }
    
    public static URLIndexer getInstance() {
        if (myinstance == null)
            myinstance = new URLIndexer();
        return myinstance;
    }
    
    public PageInfo getPageFromPath(String path) {
        for (PageInfo pageinfo : pageinfoset) {
            if (pageinfo.getUrl().compareTo(path) == 0)
                return pageinfo;
        }
        return null;
    }
    
    public PageInfo getPage(String docid) {
        PageInfo pi = new PageInfo(docid);
        
        if (pageinfoset.contains(pi) == true) {
            for (PageInfo myinfopage : pageinfoset) {
                if (myinfopage.compareTo(pi) == 0) {
                    return myinfopage;
                }
            }
        } else {
            pageinfoset.add(pi);
        }
        
        return pi;
    }
    
    public void close() {
        this.indexer.close();
    }
    
    public void addPage(Page page) {
        
        String domain = page.getWebURL().getDomain();
        String path = page.getWebURL().getURL().substring(page.getWebURL().getURL().indexOf(domain) + domain.length());
        List<WebURL> links = ((HtmlParseData) page.getParseData()).getOutgoingUrls();
        
        System.out.println(">>Analyze page: " + path);
        
        String hash = DigestUtils.md5Hex(((HtmlParseData) page.getParseData()).getText());
        PageInfo pi = URLIndexer.getInstance().getPage(hash);
        pi.setUrl(path);
        pi.setProbability((links.isEmpty()) ? 0 : 1.f / links.size());
        String data = ((HtmlParseData) page.getParseData()).getText();
        pi.setData(data);
        try (TokenStream tokenStream = URLIndexer.getInstance().indexer.analyzer.tokenStream("text", data)) {
            tokenStream.reset();
            pi.nbWord = 0;
            while (tokenStream.incrementToken()) { pi.nbWord++; }
            tokenStream.end();
        } catch (IOException ex) {
            Logger.getLogger(URLIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }

        String parentUrl = page.getWebURL().getParentUrl();
        if (!parentUrl.isEmpty()) {
            String parentPath = parentUrl.substring(parentUrl.indexOf(domain) + domain.length());
            PageInfo pagepath = URLIndexer.getInstance().getPageFromPath(parentPath);
            if (pagepath != null) {
                pi.addParent(pagepath);
                if (pi.getUrl().startsWith(pagepath.getUrl())) {
                    PathInfo pathinfo = new PathInfo(pagepath);
                    URLIndexer.getInstance().pathinfoset.add(pathinfo);
                }
            }
        }
    }

    private void addDocToIndexer(PageInfo pageinfo) {
        if (this.indexer != null) {
            Document doc = new Document();
            String data = pageinfo.getData();
            doc.add(new StringField("path", pageinfo.getUrl(), Field.Store.YES));
            doc.add(new TextField("contents", (data == null) ? "" : data, Field.Store.YES));
            doc.add(new DoubleField("pageRank", pageinfo.getPagerank(), Field.Store.YES));
            //System.out.println(">>Add to index : " + pageinfo.getUrl());
            this.indexer.addDoc(doc, pageinfo.getUrl());
            
        }
    }

    public void generateIndex() {
        for (PageInfo pageinfo : pageinfoset) {
            pageinfo.calculPageRank();
            this.addDocToIndexer(pageinfo);
        }
    }
}

