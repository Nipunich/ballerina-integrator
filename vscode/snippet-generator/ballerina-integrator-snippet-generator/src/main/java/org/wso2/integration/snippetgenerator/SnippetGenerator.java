package org.wso2.integration.snippetgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SnippetGenerator {
    private static final List<File> snippets = new ArrayList<>();
    //public Object generateSnippetName;

//    public SnippetGenerator(String sName, String imp, String trig, String snip) {
//
//    }


    public static int readFile(String inputDir, String outputDirPath) throws Exception {

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
        generateSnippetName(map, filesArr.size());
        return filesArr.size();
    }

    public static Snippet generateSnippetName(HashMap mp, int size) throws Exception {

        String sName = "";
        String sImports = "";
        String sTrigger = "";
        String sCode = "";


        ArrayList<String> snippetsNameArr = new ArrayList<>();
        ArrayList<String> snippetsImportsArr = new ArrayList<>();
        ArrayList<String> snippetsTriggerArr = new ArrayList<>();
        ArrayList<String> snippetsArr = new ArrayList<>();

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

            System.out.println("Name :" + sName);
            System.out.println("Imports :" + sImports);
            System.out.println("Trigger :" + sTrigger);
            System.out.println("Code :" + sCode);


            Snippet snp = new Snippet(sName,sImports,sTrigger,sCode);

            snp.setName(sName);
            snp.setImports(sImports);
            snp.setTrigger(sTrigger);
            snp.setCode(sTrigger);


        }
         return new Snippet(sName, sImports, sTrigger, sCode);
    }

//    public static Snippet generateSnippetTemplate(Snippet s){
//
//
//
//
//
//    }

//    public static Snippet generateSniipetTemplate(){
////        ImmutablePair<String, String> httpImport = new ImmutablePair<>("ballerina", "http");
////        String snippet = "resource function ${1:newResource}(http:Caller ${2:caller}, ${3:http:Request request}) {"
////                + CommonUtil.LINE_SEPARATOR + "\t${4}" + CommonUtil.LINE_SEPARATOR + "}";
////        return new SnippetsBlock(ItemResolverConstants.RESOURCE, snippet, ItemResolverConstants.SNIPPET_TYPE,
////                                SnippetsBlock.SnippetType.SNIPPET, httpImport);
//
//
//
//        return new Snippet();


//        public static SnippetsBlock getServiceDefSnippet() {
//        ImmutablePair<String, String> httpImport = new ImmutablePair<>("ballerina", "http");
//        String snippet = "service ${1:serviceName} on new http:Listener(8080) {"
//                + CommonUtil.LINE_SEPARATOR + "\tresource function ${2:newResource}(http:Caller ${3:caller}, "
//                + "http:Request ${5:request}) {" + CommonUtil.LINE_SEPARATOR + "\t\t" + CommonUtil.LINE_SEPARATOR +
//                "\t}" + CommonUtil.LINE_SEPARATOR + "}";
//        return new SnippetsBlock(ItemResolverConstants.SERVICE, snippet, ItemResolverConstants.SNIPPET_TYPE,
//                                SnippetsBlock.SnippetType.SNIPPET, httpImport);
//    }

//    }

//       public static SnippetsBlock getHttpResourceDefinitionSnippet() {
//        ImmutablePair<String, String> httpImport = new ImmutablePair<>("ballerina", "http");
//        String snippet = "resource function ${1:newResource}(http:Caller ${2:caller}, ${3:http:Request request}) {"
//                + CommonUtil.LINE_SEPARATOR + "\t${4}" + CommonUtil.LINE_SEPARATOR + "}";
//        return new SnippetsBlock(ItemResolverConstants.RESOURCE, snippet, ItemResolverConstants.SNIPPET_TYPE,
//                                SnippetsBlock.SnippetType.SNIPPET, httpImport);
//    }


    public static void generateSnippetImports(HashMap mp) throws Exception {
        HashMap hs = mp;

        String sName = "";
        String[] sImports;
        String sTrigger;
        String sCode;

        ArrayList ar = null;
        ArrayList<String> snippetsArr = new ArrayList<>();


        if (mp.get("Imports") != null) {
            sName = (String) mp.get("Imports");
            snippetsArr.add(sName);
            //ar.add(sName);
            System.out.println("Name :" + sName);
        }
    }


    public static void main(String[] args) throws Exception {
        int i = readFile("/Users/nipuni/Documents/BallerinaIntegrator22/ballerina-integrator/vscode/snippet-generator/ballerina-integrator-snippet-generator/Snippets", "vscode/snippet-generator/ballerina-integrator-snippet-generator/Out");

        //generateSnippet(hsmp);
    }
}
