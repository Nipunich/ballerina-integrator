// Copyright (c) 2019 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.wso2.integration.ballerina;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

class TopLevelScopeGenerator {

    private TopLevelScopeGenerator() {
    }

    static void generateTopLevelScope(List<String> names) throws IOException {
        String scopeBody = "";
        String scopeLine;

        File sourceFile = Paths.get("vscode", "snippets", "ei-snippets", "src", "main", "java", "org",
                                    "wso2", "integration", "ballerina", "autogen", "TopLevelScope.java").toFile();

        try {
            sourceFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    String scopeHeader = "package org.wso2.integration.ballerina.autogen;\n" + "\n" +
                           "import org.antlr.v4.runtime.CommonToken;\n" +
                           "import org.ballerinalang.annotation.JavaSPIService;\n" +
                           "import org.ballerinalang.langserver.common.CommonKeys;\n" +
                           "import org.ballerinalang.langserver.compiler.LSContext;\n" +
                           "import org.ballerinalang.langserver.completions.SymbolInfo;\n" +
                           "import org.ballerinalang.langserver.completions.providers.scopeproviders." +
                                                                                "TopLevelScopeProvider;\n" +
                           "import org.ballerinalang.langserver.completions.spi.LSCompletionProvider;\n" +
                           "import org.eclipse.lsp4j.CompletionItem;\n" +
                           "import org.wso2.ballerinalang.compiler.parser.antlr4.BallerinaParser;\n" +
                           "import org.wso2.ballerinalang.compiler.tree.BLangPackage;\n" +
                           "\n" + "import java.util.ArrayList;\n" + "import java.util.Collection;\n" +
                           "import java.util.List;\n" + "import java.util.Optional;\n" + "\n" +
                           "@JavaSPIService(\"org.ballerinalang.langserver.completions.spi.LSCompletionProvider\")\n" +
                           "public class TopLevelScope extends TopLevelScopeProvider {\n" + "\n" +
                           "    public static Precedence precedence;\n" + "\n" +
                           "    public TopLevelScope() {\n" + "        this.attachmentPoints.add(BLangPackage.class);"
                                                                                                               + "\n" +
                           "        this.precedence = Precedence.HIGH;\n" + "    }\n" + "\n" + "    /**\n" +
                           "     * Get a static completion Item for the given snippet.\n" + "     *\n" +
                           "     * @param snippet Snippet to generate the static completion item\n" +
                           "     * @return {@link CompletionItem} Generated static completion Item\n" +
                           "     */\n" + "\n" +
                           "    protected CompletionItem getStaticItem(LSContext ctx, Snippets snippet) {\n" +
                           "        return snippet.get().build(ctx);\n" + "    }\n" + "\n" +
                           "    public static final LSContext.Key<List<CommonToken>> LHS_DEFAULT_TOKENS_KEY = " +
                                                                                           "new LSContext.Key<>();\n" +
                           "\n" + "\n" + "    //Override the getCompletions method in LSCompletion Provider\n" +
                           "    @Override\n" +
                           "    public List<CompletionItem> getCompletions(LSContext ctx) {\n" +
                           "         ArrayList<CompletionItem> completionItm = new ArrayList<>();\n" +
                           "        Optional<LSCompletionProvider> contextProvdr = this.getContextProvider(ctx);\n" +
                           "        List<CommonToken> lhsDefaultTokens = ctx.get(LHS_DEFAULT_TOKENS_KEY);\n" + "\n" +
                           "        if (contextProvdr.isPresent()) {\n" +
                           "            return contextProvdr.get().getCompletions(ctx);\n" + "        }\n" +
                           "\n" +
                           "        if (!(lhsDefaultTokens != null && lhsDefaultTokens.size() >= 2 && " +
                                                                          "BallerinaParser.LT == lhsDefaultTokens\n" +
                           " .get(lhsDefaultTokens.size() - 1).getType())) {\n" +
                           "            completionItm.addAll(addTopLevelItem(ctx));\n" + "        }\n" +
                           "        List<SymbolInfo> visibleSymbols = new ArrayList<>" +
                           "(ctx.get(CommonKeys.VISIBLE_SYMBOLS_KEY));\n" +
                           "        completionItm.addAll((Collection<? extends CompletionItem>) " +
                           "getBasicTypes(visibleSymbols));\n" +
                           "        completionItm.addAll((Collection<? extends CompletionItem>) " +
                           "this.getPackagesCompletionItems(ctx));\n" +
                           "\n" + "        return completionItm;\n" + "    }\n" + "\n" + "\n" +
                           "    protected List<CompletionItem> addTopLevelItem(LSContext context) {\n" +
                           "        ArrayList<CompletionItem> completionItemsArr = new ArrayList<>(); " + "\t" +
                                                                                           "\n" +"\n";


        for (String name : names) {
            String[] namesSplit = name.split(":");
            scopeLine = "\t" + "\t" + "completionItemsArr.add(getStaticItem(context, Snippets." +
                                                                               namesSplit[1].trim() + ")) ;" + "\n";
            scopeBody = scopeBody + scopeLine;
        }

        String itemResolver = scopeHeader + scopeBody + "\n" + "\n" + "return completionItemsArr;" + "\n" + "\n" + "}";

        FileWriter writer = new FileWriter(sourceFile);
        writer.write(itemResolver);
        writer.close();
    }
}
