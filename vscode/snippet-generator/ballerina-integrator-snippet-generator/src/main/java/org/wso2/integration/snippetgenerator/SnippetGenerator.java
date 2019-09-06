package org.wso2.integration.snippetgenerator;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SnippetGenerator {
   public static int readFile(String inputDir) throws Exception {

        File f = new File(inputDir);
        ArrayList<File> filesArr = new ArrayList<File>(Arrays.asList(f.listFiles()));
        HashMap map = new HashMap();

        for (int i = 0; i < filesArr.size(); i++) {

            String line;
            BufferedReader reader = Files.newBufferedReader(Paths.get(String.valueOf(filesArr.get(i))));
            String pr = "";
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("-", 2);

                if (parts.length >= 2) {

                    String key = parts[0] + i;
                    String value = parts[1];

                    map.put(key, value);
                } else if (parts.length < 2) {

                    pr = pr + "\n" + parts[0];
                    String k = "SnippetGen" + i;

                    map.put(k, pr);
                }
            }
            reader.close();
        }
        for (Object key : map.keySet()) {
            System.out.println(key + ">>>>" + map.get(key));
        }
        generateSnippet(map, filesArr.size());

        return filesArr.size();
    }

    public static void generateSnippet(HashMap mp, int size) throws Exception {
        String sName = "";
        String sImports = "";
        String sTrigger = "";
        String sCode = "";

        ArrayList<String> snippetsNameArr = new ArrayList<>();
        ArrayList<String> snippetsImportsArr = new ArrayList<>();
        ArrayList<String> snippetsTriggerArr = new ArrayList<>();
        ArrayList<String> snippetsArr = new ArrayList<>();
        HashMap<String, ArrayList> snipMap = new HashMap<String, ArrayList>();

        for (int i = 0; i < size; i++) {
            sName = (String) mp.get("SnippetName " + i);
            sName = sName.toUpperCase();
            snippetsNameArr.add(sName);

            sImports = (String) mp.get("Imports " + i);
            snippetsImportsArr.add(sImports);

            sTrigger = (String) mp.get("Trigger " + i);
            snippetsTriggerArr.add(sTrigger);

            sCode = (String) mp.get("SnippetGen" + i);
            snippetsArr.add(sCode);

            sCode = (String) mp.get("Snippet" + i);
            snippetsArr.add(sCode);

            System.out.println("Name :" + sName);
            System.out.println("Imports :" + sImports);
            System.out.println("Trigger :" + sTrigger);
            System.out.println("Code :" + sCode);
           // System.out.println("Service" + );

            Snippet snp = new Snippet(sName, sImports, sTrigger, sCode);

            snp.setName(sName);
            snp.setImports(sImports);
            snp.setTrigger(sTrigger);
            snp.setCode(sCode);
        }

        snipMap.put("Name", snippetsNameArr);
        snipMap.put("Imports", snippetsImportsArr);
        snipMap.put("Trigger", snippetsTriggerArr);
        snipMap.put("Code", snippetsArr);

        generateItemResolver(snipMap, sTrigger);
        //generateSnippetName(snipMap, sTrigger);
        // generateSnippetContent(snipMap, sTrigger);
    }

    public static void generateItemResolver(HashMap hs, String trigg) throws Exception {
        File sourceFile = new File("vscode/snippet-generator/ballerina-integrator-snippet-generator/OutFolder/ItemResolverConstants.java");
        // String classname = sourceFile.getName().split("\\.")[0];

        ArrayList triggerArr = new ArrayList();
        ArrayList nameArr = new ArrayList();

        triggerArr = (ArrayList) hs.get("Trigger");
        nameArr = (ArrayList) hs.get("Name");

        String content = "";

        if (sourceFile.exists()) {
            sourceFile.delete();
        }

        sourceFile.createNewFile();

        String content1 = "package org.wso2.integration.ballerinalangserver;" + "\n" + "\n" + "public class  ItemResolverConstants" + "{ ";

        for (int i = 0; i < triggerArr.size(); i++) {
            String co = (String) triggerArr.get(i);
            String na = (String) nameArr.get(i);
            String[] ind = na.split("\\(");
            na = ind[0];
            co = na + "" + " =" + co;
            content = content + "\n" + "public static final String" + co + ";";
        }

        content1 = content1 + "\n" + content + "\n" + "\n" + "}";

        System.out.println("File is created!");

        FileWriter writer = new FileWriter(sourceFile);
        writer.write(content1);
        writer.close();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(new File("vscode/snippet-generator/ballerina-integrator-snippet-generator/OutFolder/")));
        // Compile the file
        boolean success = compiler.getTask(null, fileManager, null, null, null, fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile))).call();
        fileManager.close();

       generateSnippetName(hs);
       generateSnippetContent(hs);
    }


    public static void generateSnippetName(HashMap hs) throws Exception {
       File sourceFile2 = new File("vscode/snippet-generator/ballerina-integrator-snippet-generator/OutFolder1/Snippets.java");

        ArrayList snippetArr = new ArrayList();
        snippetArr = (ArrayList) hs.get("Name");

        String content = "";

        if (sourceFile2.exists()) {
            sourceFile2.delete();
        }

        sourceFile2.createNewFile();

        String content1 = "package org.wso2.integration.util;\n" + "\n" + "import org.wso2.integration.snippetgenerator.SnippetsBlock;\n" + " import org.wso2.integration.snippetgenerator.SnippetsGen;\n" + "\n" + "    /**\n" + "     * Snippets for the Ballerina Integrator.\n" + "     */\n" + "\n" + "public enum Snippets {\n ";

        String content2 = "private String snippetName;\n" + "    private SnippetsBlock snippetBlock;\n" + "\n" + "    Snippets(SnippetsBlock snippetBlock) {\n" + "        this.snippetName = null;\n" + "        this.snippetBlock = snippetBlock;\n" + "    }\n" + "\n" + "    Snippets(String snippetName, SnippetsBlock snippetBlock) {\n" + "        this.snippetName = snippetName;\n" + "        this.snippetBlock = snippetBlock;\n" + "    }\n" + "\n" + "    /**\n" + "     * Get the Snippet Name.\n" + "     *\n" + "     * @return {@link String} snippet name\n" + "     */\n" + "    public String snippetName() {\n" + "        return this.snippetName;\n" + "    }\n" + "\n" + "    /**\n" + "     * Get the SnippetBlock.\n" + "     *\n" + "     * @return {@link SnippetsBlock} SnippetBlock\n" + "     */\n" + "    public SnippetsBlock get() {\n" + "        return this.snippetBlock;\n" + "    }\n" + "}";


        // DEF_RECORD(SnippetsGenerator.getRecordDefinitionSnippet()),


        for (int i = 0; i < snippetArr.size(); i++) {
            String co = (String) snippetArr.get(i);
            String na = "";
            String nam = (String) snippetArr.get(i);
            String[] ind = nam.split("\\(");
            na = ind[0];
            co = na + "(" + "SnippetsGen." + co.trim() + ")";

            if (i < (snippetArr.size() - 1)) {
                content = content + "\n" + co + ",";
            } else {
                content = content + "\n" + co + ";" + "\n";
            }
        }

        content1 = content1 + "\n" + content + "\n" + content2;

        FileWriter writer = new FileWriter(sourceFile2);
        writer.write(content1);
        writer.close();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(new File("vscode/snippet-generator/ballerina-integrator-snippet-generator/OutFolder1/")));
        // Compile the file
        boolean success = compiler.getTask(null, fileManager, null, null, null, fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile2))).call();
        fileManager.close();
    }


    // Generate Snippet Content

        public static void generateSnippetContent(HashMap hs) throws Exception {
        File file = new File("vscode/snippet-generator/ballerina-integrator-snippet-generator/OutFolder2/SnippetsCont.java");

        ArrayList codeArr = new ArrayList();
        ArrayList nameArr = new ArrayList();
        ArrayList importArr = new ArrayList();


        codeArr = (ArrayList) hs.get("Code");
        nameArr = (ArrayList) hs.get("Name");
        importArr = (ArrayList) hs.get("Imports");

        String content = "";

        if (file.exists()) {
            file.delete();
        }

        file.createNewFile();

        String content1 = "package org.wso2.integration.ballerinalangserver;\n" + "\n" + "//import integration.ballerinalang.langserver.SnippetsBlock.SnippetType;\n" + "import org.apache.commons.lang3.tuple.ImmutablePair;\n" + "import org.ballerinalang.langserver.common.utils.CommonUtil;\n" + "\n" + "public class SnippetsGenerator {\n" + "\n" + "    private SnippetsGenerator() {\n" + "    } ";

        String snipPart1 = "";
        String snipPart2 = "";
        String snipPart3 = "";
        String snipPart4 = "";

        for (int i = 0; i < codeArr.size(); i++) {
            String co = (String) codeArr.get(i);
            String na = (String) nameArr.get(i);
            String im = (String) importArr.get(i);

            String[] imArr = im.split(",");
            String val1 = imArr[0];
            String val2 = imArr[1];
            String[] splitArr = val2.split("/");
            String spPart1 = splitArr[1];

            snipPart1 = "\n" + "\n" + "public static SnippetsBlock get" + na.toLowerCase() + "{" + "\n" + "\t\t" + "ImmutablePair<String, String> httpImport" + "= new ImmutablePair<>(" + "\"" + "ballerina" + "\"" + "," + "\"" + spPart1 + ");";

            snipPart3 = "    public static SnippetsBlock getHttpResourceDefinitionSnippet() {\n" + "        ImmutablePair<String, String> httpImport = new ImmutablePair<>(\"ballerina\", \"http\");\n" + "        String snippet = \"resource function ${1:newResource}(http:Caller ${2:caller}, ${3:http:Request request}) {\"\n" + "                + CommonUtil.LINE_SEPARATOR + \"\\t${4}\" + CommonUtil.LINE_SEPARATOR + \"}\";\n" + "        return new SnippetsBlock(ItemResolverConstants.RESOURCE, snippet, ItemResolverConstants.SNIPPET_TYPE,\n" + "                                SnippetsBlock.SnippetType.SNIPPET, httpImport);\n" + "    }\n" + "\n" + "    resource function ${newResource}(grpc:Caller caller, string request) {\n" + "\n" + "\n" + "    }\n" + "}\"";

            //snipPart2 = ;
            String[] ind = na.split("\\(");
            na = ind[0];
            co = na + "" + " =" + co;


            content = content + "\n" + co + ";";
        }

        // content1 = content1 + "\n" + content + "\n" + "\n" + "}";

        content1 = content1 + snipPart1 + snipPart3;

        System.out.println("File is created!");

        try {
            FileWriter fw = new FileWriter(file, true);
            fw.write(content1);
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }




    public static void main(String[] args) throws Exception {
        int i = readFile("vscode/snippet-generator/ballerina-integrator-snippet-generator/Snippets");
    }
}
