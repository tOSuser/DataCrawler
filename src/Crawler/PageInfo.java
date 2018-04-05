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

import edu.uci.ics.crawler4j.url.WebURL;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Hossein
 */
public class PageInfo  implements Comparable<PageInfo>, Comparator<PageInfo> {
    private String url;
    public Integer nbWord = 0;
    private Set<PageInfo> paths = new HashSet<>();
    public String hash;
    private float pagerank;
    private float probability; 
    private List<WebURL> urls;
    private String data;

    public PageInfo() {
    }
    
    public PageInfo(String docid) {
        this.setDocid(docid);
    }
    
    public String getDocid() {
        return this.hash;
    }
    public void setDocid(String id) {
        this.hash = id;
    }
    
    @Override
    public int compareTo(PageInfo pi) {
        return this.getDocid().compareTo(pi.getDocid());
    }

    @Override
    public int compare(PageInfo pi1, PageInfo pi2) {
        return pi1.compareTo(pi2);
    }

    @Override
    public boolean equals(Object obj) {
        PageInfo o = (PageInfo)obj;
        return this.compareTo(o) == 0; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url.toLowerCase();
    }

    /**
     * @return the urls
     */
    public List<WebURL> getUrls() {
        return urls;
    }

    /**
     * @param urls the urls to set
     */
    public void setUrls(List<WebURL> urls) {
        this.urls = urls;
        this.probability = 1.f / ((float) urls.size());
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }


    void addParent(PageInfo page) {
        this.getPath().add(page);
    }

    /**
     * @return the paths
     */
    public Set<PageInfo> getPath() {
        return paths;
    }

    /**
     * @param paths the paths to set
     */
    public void setPath(Set<PageInfo> paths) {
        this.paths = paths;
    }

    /**
     * @return the pagerank
     */
    public float getPagerank() {
        return pagerank;
    }

    /**
     * @param pagerank the pagerank to set
     */
    public void setPagerank(float pagerank) {
        this.pagerank = pagerank;
    }

    /**
     * @return the probability
     */
    public float getProbability() {
        return probability;
    }

    /**
     * @param probability the probability to set
     */
    public void setProbability(float probability) {
        this.probability = probability;
    }

    void calculPageRank() {
        float pg = 0;
        for (PageInfo pageinfo : paths) {
            pg += pageinfo.getProbability();
        }
        this.pagerank = pg;
    }
    
}
