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
 *
 *    baseed on :
 *    TextDirectoryToArff.java
 *    Copyright (C) 2002 Richard Kirkby
*/
package arffwriter;

/**
 *
 * @author Hossein
 */
import PorterStemAnalyzer.PorterStemAnalyzer;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;


public class ARFFCreator {
    Analyzer analyser = new PorterStemAnalyzer(Version.LUCENE_47);
    Set<String> attrs = new HashSet<String>();
    Set<String> class_label = new HashSet<String>();
    List<Doc> data = new ArrayList<Doc>();
    String relation = null;
    
    public static void main(String[] args) {

        //String docsPath = "data/news/";
        String docsPath = "http://apache.org/";        


        ARFFCreator arffcreator = new ARFFCreator();
        try {
            arffcreator.initialize(docsPath);
            arffcreator.toFile("mini_newsgroups.arff");
            
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
        }
    }

    private boolean initialize(String docpath) {
        File file = new File(docpath);
        this.relation = file.getName();
        return createdata(file);
    }

    private boolean createdata(File file) {
        if (!file.canRead()) 
            return false;
        if (file.isDirectory()) {
            String[] list = file.list();
            for (String f : list) {
                createdata(new File(file, f));
            }
        } else {
            this.parser(file);
        }
        return true;
    }

    private void parser(File file) {
        try {
            Doc doc = new Doc();
            doc.label = file.getParentFile().getName();
            this.class_label.add(doc.label);
            Reader reader = new FileReader(file);
            TokenStream tokenStream = this.analyser.tokenStream("contents", reader);
            CharTermAttribute attribute = tokenStream.getAttribute(CharTermAttribute.class);
            
            try {
                tokenStream.reset();
                while (tokenStream.incrementToken()) {
                    String word = attribute.toString();
                    this.attrs.add(word);
                    Integer get = doc.map.get(word);
                    get = get == null ? 0 : get;
                    doc.map.put(word, get + 1);
                }
                tokenStream.end();
            } finally {
                tokenStream.close();
            }
            this.data.add(doc);
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(ARFFCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void toFile(String indexarff, String relation) throws IOException {
        this.relation = relation;
        this.toFile(indexarff);
    }
    
    private void toFile(String indexarff) throws IOException {
        File file = new File(indexarff);
        if (!file.exists()) file.createNewFile();
        if (!file.canWrite()) {
            System.err.println("Not able to edit the file");
            return ;
        }
        try {
            BufferedWriter writer;
            writer = new BufferedWriter(new FileWriter(file));
            /**
             * Adding the relation
             */
            writer.write("@RELATION " + relation);
            writer.newLine();
            for (String string : attrs) {
                writer.write("@ATTRIBUTE " + string + " integer");
                writer.newLine();
            }
            /**
             * Writing every Class_label
             */
            StringBuilder sb = new StringBuilder();
            sb.append("@ATTRIBUTE CLASS_LABEL {");
            for (String cl : this.class_label) {
                if (sb.indexOf("{") < sb.length() - 1)
                    sb.append(',');
                sb.append(cl);
            }
            sb.append('}');
            writer.write(sb.toString());
            writer.newLine();
            /**
             * Adding the data to the ARFF
             */
            writer.write("% Here are the data");
            writer.newLine();
            writer.write("@DATA ");
            writer.newLine();
            for (Doc doc : data) {
                StringBuilder strb = new StringBuilder();
                int i = 0;
                strb.append('{');
                for (Iterator<String> it1 = attrs.iterator(); it1.hasNext();) {
                    String attr = it1.next();
                    if (doc.map.containsKey(attr)) {
                        strb.append(i).append(' ').append(doc.map.get(attr)).append(',');
                    }
                    i++;
                }
                strb.append(i).append(' ').append(doc.label).append('}');
                String towrite = strb.toString();
                writer.write(towrite);
                writer.newLine();
            }
            writer.close();
            System.out.println("File generated in : " + file.getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(ARFFCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class Doc {
        public Map<String, Integer> map = new HashMap<String, Integer>();
        public String label;

        public Doc() {
        }
    }
}
