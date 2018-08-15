package james;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.text.Chunk;
import org.cogroo.text.Document;
import org.cogroo.text.Sentence;
import org.cogroo.text.SyntacticChunk;
import org.cogroo.text.Token;
import org.cogroo.text.impl.DocumentImpl;

/**
 * CoGrOO 4.0.0-SNAPSHOT usage example
 */
public class Example {

  
  private Analyzer cogroo;

  public Example() {
	  ComponentFactory factory = ComponentFactory.create(new Locale("pt", "BR"));
	  this.cogroo = factory.createPipe();
	  
  }

  public void analyzeAndPrintDocument(String documentText) {
	Document document = new DocumentImpl();
    document.setText(documentText);
    long start = System.nanoTime();
    cogroo.analyze(document);
    System.out.println("Document processed in " 
        + ((System.nanoTime() - start) / 1000000) + "ms");
    print(document);
  }

  private void print(Document document) {
    StringBuilder output = new StringBuilder();
    for (Sentence sentence : document.getSentences()) {
      output.append("Sentence: ").append(sentence.getText()).append("\n");
      output.append("  Tokens: \n");
      for (Token token : sentence.getTokens()) {
        String lexeme = token.getLexeme();
        String lemmas = Arrays.toString(token.getLemmas());
        String pos = token.getPOSTag();
        String feat = token.getFeatures();        
        output.append(String.format("    %-10s %-12s %-6s %-10s\n", lexeme,
            lemmas, pos, feat));
      }

      output.append("  Chunks: ");
      for (Chunk chunk : sentence.getChunks()) {
        output.append("[").append(chunk.getTag()).append(": ");
        for (Token innerToken : chunk.getTokens()) {
          output.append(innerToken.getLexeme()).append(" ");
        }
        output.append("] ");
      }
      output.append("\n");

      output.append("  Shallow Structure: ");
      for (SyntacticChunk structure : sentence.getSyntacticChunks()) {
        output.append("[").append(structure.getTag()).append(": ");
        for (Token innerToken : structure.getTokens()) {
          output.append(innerToken.getLexeme()).append(" ");
        }
        output.append("] ");
      }
      output.append("\n");      
      output.append("   Syntax tree: \n   ");
      output.append(sentence.asTree().toSyntaxTree());
    }
    
    System.out.println(output.toString());
  }

  
  public static void main(String[] args) {
	Example ex = new Example();
	Scanner kb = new Scanner(System.in);
	System.out.print("Enter the sentence or 'q' to quit: ");
	String input = "tentei ser um cara legal, todavia falhei miseravelmente."; //kb.nextLine();
	ex.analyzeAndPrintDocument(input);
	//while (!input.equals("q")) {
	  //System.out.print("Enter the sentence or 'q' to quit: ");
	  //input = kb.nextLine();
	//}
  }
}