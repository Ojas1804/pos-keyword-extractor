
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;

public class KeywordExtractorUsingPOS {

    public static List<String> extractKeywordPhrases(String text) {
    	BasicConfigurator.configure();
        // Set up Stanford CoreNLP pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // Perform POS tagging on the input text
        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        List<String> keywordPhrases = new ArrayList<String>();

        // Iterate over sentences in the document
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            // Extract phrases based on POS patterns (e.g., noun phrases)
            List<String> phraseTokens = new ArrayList<String>();
            for (CoreMap token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // Consider words with specific POS tags (e.g., nouns) as potential keywords
                if (pos.startsWith("N")) { // Consider any word starting with "N" as a noun
                    phraseTokens.add(word);
                } else {
                    if (!phraseTokens.isEmpty()) {
                        keywordPhrases.add(String.join(" ", phraseTokens));
                        phraseTokens.clear();
                    }
                }
            }
            // Add the last phrase if it's not empty
            if (!phraseTokens.isEmpty()) {
                keywordPhrases.add(String.join(" ", phraseTokens));
            }
        }

        return keywordPhrases;
    }

    public static void main(String[] args) {
        String text = "TextRank is an algorithm used for keyword extraction and text summarization. "
                + "It's based on PageRank, which is the algorithm Google uses for ranking web pages.";
        List<String> keywordPhrases = extractKeywordPhrases(text);
        System.out.println("Keyword Phrases:");
        for (String phrase : keywordPhrases) {
            System.out.println(phrase);
        }
    }
}
