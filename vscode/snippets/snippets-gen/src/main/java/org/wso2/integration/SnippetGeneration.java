package org.wso2.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.nio.file.Paths.get;

public class SnippetGeneration {
    private static final Logger logger = LoggerFactory.getLogger(SnippetGeneration.class);
    private static final String PATH = "\"vscode\",\"snippets\",\"ei-snippet\",\"src\",\"main\",\"java\",\"org\",\"wso2\",\"integration\",\"autogen\"";
    private static final String PACKAGE = "\"package org.wso2.integration.autogen;" ;


    private static void readFile()  {
        File file = get("vscode", "snippets","snippets-gen","src","main","resources","Snippets").toFile();

        ArrayList<File> fileArr = new ArrayList<>(Arrays.asList(Objects.requireNonNull(file.listFiles())));
        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < fileArr.size(); i++) {
            String line;

            try {
                try (BufferedReader reader = Files.newBufferedReader(get(String.valueOf(fileArr.get(i))))) {

                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("-", 2);

                        if (parts.length >= 2) {

                            String key = parts[0] + i;
                            String value = parts[1];
                            map.put(key, value);
                        } else if (parts.length < 2) {
                            String part = "" + ("\n" + parts[0]);

                            String key = "SnippetGen" + i;

                            map.put(key, part);
                        }
                    }
                }
                generateSnippet(map, fileArr.size());

            } catch (IOException  e ) {
                e.printStackTrace();
            }
        }
    }


    private static void generateSnippet(Map<String, String> map, int size)  {

        ArrayList<String> nameArr = new ArrayList<>();
        ArrayList<String> importsArr = new ArrayList<>();
        ArrayList<String> triggerArr = new ArrayList<>();
        ArrayList<String> snippetsArr = new ArrayList<>();
        ArrayList<String> generatedSnippetsArr = new ArrayList<>();
        HashMap<String, ArrayList> snippetsMap = new HashMap<String, ArrayList>();

        for (int i = 0; i < size; i++) {
            String name ;
            String imports ;
            String trigger ;
            String code ;

            name = map.get("Name " + i);
            name = name.toUpperCase();
            nameArr.add(name);

            imports = map.get("Imports " + i);
            importsArr.add(imports);

            trigger = map.get("Trigger " + i);
            triggerArr.add(trigger);

            String snippet = map.get("Snippet " + i);
            snippetsArr.add(snippet);

            code = map.get("SnippetGen" + i);
            generatedSnippetsArr.add(snippet + code);

            Snippet snp = new Snippet(name, imports, trigger, code);

            snp.setName(name);
            snp.setImports(imports);
            snp.setTrigger(trigger);
            snp.setCode(snippet + code);
        }

        snippetsMap.put("Name", nameArr);
        snippetsMap.put("Imports", importsArr);
        snippetsMap.put("Trigger", triggerArr);
        snippetsMap.put("Code", generatedSnippetsArr);

        generateItemResolver(snippetsMap);
        generateSnippetContent(snippetsMap);
        generateSnippetName(snippetsMap);
        generateTopLevelScope(snippetsMap);
    }


    private static void generateItemResolver(Map hs) {

        File sourceFile = get(PATH, "ItemResolverConstants.java").toFile();

        ArrayList triggerArr ;
        ArrayList nameArr ;

        triggerArr = (ArrayList) hs.get("Trigger");
        nameArr = (ArrayList) hs.get("Name");

        StringBuilder content = null;

        if (sourceFile.exists()) {
            sourceFile.delete();
        }

        try {
            if(sourceFile.createNewFile()){
             logger.info("ItemResolverConstants.java file created successfully");
            }
        } catch (IOException e) {
            logger.error("Error in creating ItemResolverConstants.java file",e);
        }


        String content1 = PACKAGE + "\n" + "\n" + "public class ItemResolverConstants" + "{ " + "\n" +
                          "\n" + "  // Symbol Types Constants\n" +
                          "    public static final String SNIPPET_TYPE = \"Snippet\";\n" +
                          "    public static final String RESOURCE = \"resource\";\n" +
                          "    public static final String RECORD_TYPE = \"type <RecordName> record\";\n" +
                          "    // End Symbol Types Constants";

        for (int i = 0; i < triggerArr.size(); i++) {
            StringBuilder co = (StringBuilder) triggerArr.get(i);
            String na = (String) nameArr.get(i);
            String[] ind = na.split("\\(");
           // na = ind[0];
            co = new StringBuilder().append(co).append("=").append(co);
            content = new StringBuilder().append(content).append("\n").append("public static final String").append(co).append(";");
        }

        content1 = new StringBuilder().append(content1).append("\n").append(content).append("\n").append("\n").append("}").toString();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        try (FileWriter writer = new FileWriter(sourceFile)) {
            writer.write(content1);
             fileManager.close();
        } catch (IOException e) {
           logger.error("Error in writing ItemResolverConstants.java file ", e);
        }
    }


    private static void generateSnippetName(HashMap hs)  {

        File sourceFile2;
        sourceFile2 = get(PATH, "Snippets.java").toFile();

        ArrayList snippetArr ;
        snippetArr = (ArrayList) hs.get("Name");

        StringBuilder content = null;

        if (sourceFile2.exists()) {
            sourceFile2.delete();
        }

        try {
            if(sourceFile2.createNewFile()){
             logger.info("Snippets.java file created successfully");
            }
        } catch (IOException e) {
            logger.error("Error in cretaing Snippets.java file",e);
        }

        String content1 = PACKAGE + "\n" + "\n" + "\n" +
                          "import org.wso2.integration.utils.SnippetsBlock;" + "\n" + "    /**\n" + "     " +
                          "* Snippets for the Ballerina Integrator.\n" + "   */\n" + "\n" +
                          "public enum Snippets {\n ";

        String content2 = "private String snippetName;\n" + "   " +
                          " private SnippetsBlock snippetBlock;\n" + "\n" +
                          "    Snippets(SnippetsBlock snippetBlock) {\n" + "       " +
                          " this.snippetName = null;\n" + "        " +
                          "this.snippetBlock = snippetBlock;\n" + "    }\n" + "\n" + " " +
                          "  Snippets(String snippetName, SnippetsBlock snippetBlock) {\n" + "      " +
                          "  this.snippetName = snippetName;\n" + "     " +
                          "   this.snippetBlock = snippetBlock;\n" + "    }\n" + "\n" + "  " + "    /**\n" + "   " +
                          "  * Get the Snippet Name.\n" + "     *\n" + "     * @return {@link String} snippet name\n" +
                          "   " + "  */\n" + "    public String snippetName() {\n" +
                          "        return this.snippetName;\n" + "  " + "  }\n" + "\n" + "    /**\n" +
                          "     * Get the SnippetBlock.\n" + "     *\n" +
                          "     * @return {@link SnippetsBlock} SnippetBlock\n" + "     */\n" +
                          "    public SnippetsBlock get() {\n" + "        return this.snippetBlock;\n" + "    }\n" +
                          "}";

        int i = 0;
        while (i < snippetArr.size()) {
            String co = (String) snippetArr.get(i);
            String na;
            String nam = (String) snippetArr.get(i);
            String[] ind = nam.split("\\(");
            na = ind[0];
            co = new StringBuilder().append(na).append("(").append("SnippetsContent.").append(co.trim()).append(")").toString();

            if (i < (snippetArr.size() - 1)) {
                content = new StringBuilder().append(content).append("\n").append(co).append(",");
            } else {
                content.append("\n").append(co).append(";").append("\n");
            }
            i++;
        }

        content1 = content1 + "\n" + content + "\n" + content2;

        FileWriter writer;

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {

            try {
                writer = new FileWriter(sourceFile2);
                writer.write(content1);
                writer.close();
                fileManager.close();
            } catch (IOException e) {
                logger.error("Error in writing Snippets.java file", e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Generate Snippet Content
    private static void generateSnippetContent(Map hs) {
        File sourceFile3 = get(PATH, "SnippetsContent.java").toFile();

        ArrayList codeArr ;
        ArrayList nameArr ;
        ArrayList importArr ;
        ArrayList<ArrayList> splitImports = new ArrayList<>();

        codeArr = (ArrayList) hs.get("Code");
        nameArr = (ArrayList) hs.get("Name");
        importArr = (ArrayList) hs.get("Imports");

        if (sourceFile3.exists()) {
            sourceFile3.delete();
        }

        try {
            if(sourceFile3.createNewFile()){
            logger.info("SnippetsContent.java file created successfully");
           }
        } catch (IOException e) {
            logger.error("Error in creating SnippetsContent.java file", e);
        }

        String content2 =  PACKAGE + "\n" + "\n" +
                          "import org.apache.commons.lang3.tuple.ImmutablePair;\n" +
                          "import org.wso2.integration.utils.SnippetsBlock;\n" +
                          "import generated.ItemResolverConstants;\n"  +
                          "import org.ballerinalang.langserver.common.utils.CommonUtil;\n" + "\n" + "\n" + "\n" +
                          "public class SnippetsContent {\n" + "\n" + "    private SnippetsContent() {\n" + "    } ";


        String finalSnippet = "";
        String content1 ;
        String content3 ;

        for (int i = 0; i < codeArr.size(); i++) {
            String snippetPart1;
            String snippetPart2 = "";
            String snippetPart3;
            String snippetPart4;
            String snippetPart5;
            String snippetPart6 = "";
            String concatSnippetPart;

            String co = codeArr.get(i).toString();
            String na = nameArr.get(i).toString();
            String im = importArr.get(i).toString();

            snippetPart1 = new StringBuilder().append("\n").append("\n").append("public static SnippetsBlock ").
                                                                                  append(na).append(" {").toString();
            String[] imArr = im.split(",");

            for (String anImArr : imArr) {
                String[] x = ((anImArr).split("/"));
                splitImports.add(x[1]);
            }

            for (int k = 0; k < splitImports.size(); k++) {
                snippetPart2 += "\n" + "\t" + "ImmutablePair<String, String> imports" + k +
                                " = new ImmutablePair<> (\"ballerina\"" + "," + "\"" + splitImports.get(k) + ")" + ";";

                snippetPart6 += "," + "imports" + k;

            }
            snippetPart3 = "\n" + "\n" + "\t" + "String snippet =  " + co;

           snippetPart4 = new StringBuilder().append("\n").append("\n").
                   append("return new SnippetsBlock(ItemResolverConstants.RESOURCEDEFINITION, snippet,").
                                                                      append("ItemResolverConstants.SNIPPET_TYPE,\n").
                   append(" SnippetsBlock.SnippetType.SNIPPET ").toString();

            snippetPart5 = ");  ";

            concatSnippetPart = snippetPart4 + snippetPart6 + snippetPart5;

            content1 = snippetPart1 + snippetPart2 + snippetPart3 + concatSnippetPart + "}";

            finalSnippet += content1;
            splitImports.clear();
        }

        content3 = content2 + "\n" + finalSnippet + "\n" + "}";

        FileWriter writer = null;
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        try {
            writer = new FileWriter(sourceFile3);
            writer.write(content3);
            writer.close();
            fileManager.close();
        } catch (IOException e) {
           logger.error("Error in writing SnippetsContent.java file",e);
        }
    }


    public static void generateTopLevelScope(Map<String, String> hs) {

        File sourceFile5 = get(PATH,"TopLevelScope.java").toFile();

        List<String> snippetArr ;
       // snippetArr.add("fdfd");
        snippetArr = (ArrayList) hs.get("Name");

        String content ;
        String content2 ;
        String tmp = "";

        if (sourceFile5.exists()) {
            sourceFile5.delete();
        }

        try {
            if(sourceFile5.createNewFile()){
             logger.info("TopLevelScope.java file created successfully");
            }
        } catch (IOException e) {
            logger.error("Error in creating TopLevelScope.java file ",e);
        }

        String content1 = PACKAGE+ "\n" + "\n" +
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
                           "        ArrayList<CompletionItem> completionItemsArr = new ArrayList<>(); " +"\t";


        for (String aSnippetArr : snippetArr) {
            String na;
            String[] ind = aSnippetArr.split("\\(");
            na = ind[0];

            String co = "\t" + "\t" + "completionItemsArr.add(getStaticItem(context, Snippets." + na + "));" + "\n";

            tmp += co;
        }

        content2 = "\n" + " return completionItemsArr;\n" + "    }\n" + "}\n";

        content = content1 + "\n" + tmp + "\n" + content2;
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        FileWriter writer = null;
        try {
            writer = new FileWriter(sourceFile5);
            writer.write(content);
            fileManager.close();
            writer.close();
        } catch (IOException e) {
            logger.error("Error in writing TopLevelScope.java file",e);
        }
    }

    public static void main(String[] args)  {
          readFile();
    }
}

