package org.wso2.integration.snippetgenerator;

import org.ballerinalang.langserver.common.utils.CommonUtil;

public class Snippet {
    private String name;
    private String imports;
    private String trigger;
    private String code;

    public Snippet(String name, String imports, String trigger, String code) {
        this.name = name;
        this.imports = imports;
        this.trigger = trigger;
        this.code = code;
    }

    public Snippet() {

    }

    public Snippet(String trigger) {
        this.trigger = trigger;
    }

    SnippetGenerator sg = new SnippetGenerator();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        //sg.generateSnippetName()
        this.name = name;
    }

    public String getImports() {
        return imports;
    }

    public void setImports(String imports) {

        this.imports = imports;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;

        this.trigger = "public static final String " + name + "=" + this.trigger ;

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;

        String resourceConfig = "";
        String resource = "";
        //this.code = resourceConfig + CommonUtil.LINE_SEPARATOR + resource ;

        //resourceConfig = "@http:ResourceConfig {" + CommonUtil.LINE_SEPARATOR  ;




//    public static SnippetsBlock getHttpResourceDefinitionSnippet() {
//        ImmutablePair<String, String> httpImport = new ImmutablePair<>("ballerina", "http");
//        String snippet = "resource function ${1:newResource}(http:Caller ${2:caller}, ${3:http:Request request}) {"
//                + CommonUtil.LINE_SEPARATOR + "\t${4}" + CommonUtil.LINE_SEPARATOR + "}";
//        return new SnippetsBlock(ItemResolverConstants.RESOURCE, snippet, ItemResolverConstants.SNIPPET_TYPE,
//                                SnippetsBlock.SnippetType.SNIPPET, httpImport);
//    }

    }
}
