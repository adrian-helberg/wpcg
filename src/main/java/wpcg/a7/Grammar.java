/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a7;

import java.io.*;
import java.util.*;

/**
 * Representation of the building grammar.
 */
public class Grammar {

    /**
     * Mapping between the symbols and the matching rules.
     */
    private Map<String, List<Rule>> rules;

    public Grammar() {
        rules = new HashMap<>();
        Locale.setDefault(Locale.US);
    }

    /**
     * Returns true if the rule is deterministic (only one rule for the symbol).
     */
    protected boolean isDeterministicSymbolRule(String symbol) {
        return rules.get(symbol) != null && rules.get(symbol).size() == 1;
    }

    /**
     * Returns trie if the symbol is a terminal symbol (no rule available).
     */
    protected boolean isTerminal(String symbol) {
        return rules.get(symbol) == null;
    }

    /**
     * Parse the create file and create the corresponding rules.
     */
    public void parse(String grammarFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(grammarFile)));
            String line;
            while ((line = reader.readLine()) != null) {
                addRule(new Rule(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a rule to the grammar.
     */
    private void addRule(Rule rule) {
        if (rules.get(rule.symbol) == null) {
            List<Rule> rules4pred = new ArrayList<>();
            rules.put(rule.symbol, rules4pred);
        }
        rules.get(rule.symbol).add(rule);
    }

    // +++ GETTER/SETTER ++++++++++++++++++

    protected Map<String, List<Rule>> getRules() {
        return rules;
    }
}
