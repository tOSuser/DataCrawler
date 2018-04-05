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

/**
 *
 * @author Hossein Ebrahimi
 */

package Crawler;


public class PathInfo  implements Comparable<PathInfo> {
   private PageInfo pageinfo;

    public PathInfo() {
    }

    public PathInfo(PageInfo pi) {
        this.pageinfo = pi;
    }

    @Override
    public int compareTo(PathInfo path) {
        return this.getPage().getUrl().compareTo(path.getPage().getUrl());
    }

    @Override
    public boolean equals(Object obj) {
        return this.getPage().equals(((PathInfo)obj).getPage()); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the page
     */
    public PageInfo getPage() {
        return pageinfo;
    }

    /**
     * @param page the page to set
     */
    public void setPage(PageInfo pi) {
        this.pageinfo = pi;
    }
}

