package JaccardSimilarity;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.util.BytesRef;


public class JaccardSimilarity extends DefaultSimilarity {
    
    int numberOfDocumentTerms = 1; 
    
     @Override 
     public float idf(long docFreq, long numDocs) {
     return 1.0f; 
   } 
     @Override 
     public float tf(float freq){
     return 1.0f; 
   } 

     public int getNumberOfDocumentTerms() { 
         return numberOfDocumentTerms; 
     } 

     public void setNumberOfDocumentTerms(int numberOfDocumentTerms) { 
         this.numberOfDocumentTerms = numberOfDocumentTerms; 
     } 

     @Override 
     public float queryNorm(float i) {
     return 1.0f; 
   } 

     @Override 
     public float lengthNorm(FieldInvertState state){
         // just avilables on indexing time
         numberOfDocumentTerms=state.getLength();//for each field we get the number of terms 
         //setNumberOfDocumentTerms(numberOfDocumentTerms); 
     return 1.0f; 
   } 

     @Override 
     public float scorePayload(int doc, int start, int end, BytesRef payload){
        System.err.println("doc="+doc+", start="+start+", end="+end+", payload.length="+payload.length);
        return 1.0f; 
   } 

    
     @Override 
     public float coord(int overlap, int maxOverlap) { 
         //System.err.println("overlap="+overlap+", maxOverlap="+maxOverlap);
     return (overlap/(numberOfDocumentTerms+(maxOverlap-overlap)));
   } 
}