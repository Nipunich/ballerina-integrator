package org.wso2.integration.util;

import org.wso2.integration.snippetgenerator.SnippetsBlock;
 import org.wso2.integration.snippetgenerator.SnippetsGen;

    /**
     * Snippets for the Ballerina Integrator.
     */

public enum Snippets {
 

  HTTPRESOURCEDEFINITIONSNIPPET(SnippetsGen.HTTPRESOURCEDEFINITIONSNIPPET()),
 SAMPLESNIPPET(SnippetsGen.SAMPLESNIPPET()),
  RESOURCEDEFINITIONSNIPPET(SnippetsGen.RESOURCEDEFINITIONSNIPPET());

private String snippetName;
    private SnippetsBlock snippetBlock;

    Snippets(SnippetsBlock snippetBlock) {
        this.snippetName = null;
        this.snippetBlock = snippetBlock;
    }

    Snippets(String snippetName, SnippetsBlock snippetBlock) {
        this.snippetName = snippetName;
        this.snippetBlock = snippetBlock;
    }

    /**
     * Get the Snippet Name.
     *
     * @return {@link String} snippet name
     */
    public String snippetName() {
        return this.snippetName;
    }

    /**
     * Get the SnippetBlock.
     *
     * @return {@link SnippetsBlock} SnippetBlock
     */
    public SnippetsBlock get() {
        return this.snippetBlock;
    }
}