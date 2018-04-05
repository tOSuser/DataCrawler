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
package PorterStemAnalyzer;


import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;

import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.util.Version;

/**
 *
 * @author Hossein Ebrahimi
 */

/**
 * PorterStemAnalyzer processes input
 * text by stemming English words to their roots.
 * This Analyzer also converts the input to lower case
 * and removes stop words.  A small set of default stop
 * words is defined in the STOP_WORDS
 * array, but a caller can specify an alternative set
 * of stop words by calling non-default constructor.
 */
public class PorterStemAnalyzer extends StopwordAnalyzerBase
{
    

    
    /**
     * An array containing some common English words
     * that are usually not useful for searching.
     */

        final static List<String> STOP_WORDS = Arrays.asList(
        "a","able","about","across","after","all","almost","also","am","among",
        "an","and","any","are","as","at","be","because","been","but","by","can",
        "cannot","could","dear","did","do","does","either","else","ever",
        "every","for","from","get","got","had","has","have","he","her",
        "hers","him","his","how","however","i","if","in","into","is","it",
        "its","just","least","let","like","likely","may","me","might","most",
        "must","my","neither","no","nor","not","of","off","often","on","only","or",
        "other","our","own","rather","said","say","says","she","should","since","so",
        "some","than","that","the","their","them","then","there","these","they","this","tis",
        "to","too","twas","us","wants","was","we","were","what","when","where","which","while",
        "who","whom","why","will","with","would","yet","you","your");
        final static CharArraySet stopSet = new CharArraySet(Version.LUCENE_47, STOP_WORDS, false);
        static CharArraySet _stopTable = CharArraySet.unmodifiableSet(stopSet);

    /**
     * Builds an analyzer.
     */
    public PorterStemAnalyzer(Version matchVersion)
    {
        super(matchVersion, _stopTable);
    }


    /**
     * Processes the input by first converting it to
     * lower case, then by eliminating stop words, and
     * finally by performing Porter stemming on it.
     *
     * @param reader the Reader that
     *               provides access to the input text
     * @return an instance of TokenStream
     */
    
        
    public final TokenStream tokenStream(Tokenizer tokenizer,Reader reader){
        return new PorterStemFilter(
            new StopFilter(Version.LUCENE_47,tokenizer,_stopTable));
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        final Tokenizer tokenizer = new LowerCaseTokenizer(matchVersion, this.initReader(fieldName, reader));
        
        return new TokenStreamComponents(tokenizer,tokenStream(tokenizer,reader));
    }

}