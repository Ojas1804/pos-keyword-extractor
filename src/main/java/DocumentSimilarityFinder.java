
import java.util.*;

public class DocumentSimilarityFinder {

    private Map<String, Set<String>> documentKeywords;

    public DocumentSimilarityFinder() {
        this.documentKeywords = new HashMap<>();
    }

    // Add document to the index
    public void addDocument(String documentId, String documentText) {
        Set<String> keywords = extractKeywords(documentText);
        documentKeywords.put(documentId, keywords);
    }

    // Extract keywords from document text
    private Set<String> extractKeywords(String text) {
        // Implement keyword extraction logic (e.g., using TF-IDF, TextRank, etc.)
        // For simplicity, let's split text into words and consider each word as a keyword
        String[] words = text.toLowerCase().split("\\s+");
        return new HashSet<>(Arrays.asList(words));
    }

    // Find documents similar to the given text
    public List<String> findSimilarDocuments(String queryText, int topN) {
        Set<String> queryKeywords = extractKeywords(queryText);
        final Map<String, Integer> documentScores = new HashMap<>();

        // Calculate similarity scores between the query and each document
        for (Map.Entry<String, Set<String>> entry : documentKeywords.entrySet()) {
            String documentId = entry.getKey();
            Set<String> documentKeywords = entry.getValue();
            int score = calculateKeywordOverlap(queryKeywords, documentKeywords);
            documentScores.put(documentId, score);
        }

        // Sort documents by similarity score
        List<String> similarDocuments = new ArrayList<>(documentScores.keySet());
        similarDocuments.sort((doc1, doc2) -> documentScores.get(doc2) - documentScores.get(doc1));

        // Return top N similar documents
        return similarDocuments.subList(0, Math.min(topN, similarDocuments.size()));
    }

    // Calculate keyword overlap between two sets
    private int calculateKeywordOverlap(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        return intersection.size();
    }

    public static void main(String[] args) {
        DocumentSimilarityFinder finder = new DocumentSimilarityFinder();

        // Add initial documents to the index
        finder.addDocument("doc1", "This is a sample document about programming.");
        finder.addDocument("doc2", "Java programming language is widely used.");
        finder.addDocument("doc3", "Python is another popular programming language.");

        // Find similar documents for a query
        String query = "Java is a programming language.";
        List<String> similarDocuments = finder.findSimilarDocuments(query, 2);

        // Print similar documents
        System.out.println("Similar Documents:");
        for (String document : similarDocuments) {
            System.out.println(document);
        }
    }
}
