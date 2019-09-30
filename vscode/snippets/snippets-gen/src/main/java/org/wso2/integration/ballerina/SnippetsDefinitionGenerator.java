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

public class SnippetsDefinitionGenerator {
    private SnippetsDefinitionGenerator() {
    }

   static void generateSnippetDefinition(List<String> names) throws IOException {

        String snippetDefBody = "";
        String snippetDefLine;

        File sourceFile = Paths.get("vscode", "snippets", "ei-snippets", "src", "main", "java", "org",
                                    "wso2", "integration", "ballerina", "autogen", "Snippets.java").toFile();

        try {
            sourceFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String snippetDefHeader = "package org.wso2.integration.ballerina.autogen;\n" + "\n" + "\n"  +
                                  "import org.wso2.integration.ballerina.util.SnippetsBlock;" + "\n" +
                                  "    /**\n" + "     " +
                                  "* Snippets for the Ballerina Integrator.\n" + "     */\n" + "\n" +
                                  "public enum Snippets {\n ";

        String snippetDefFooter = "private String snippetName;\n" + "   " +
                                      " private SnippetsBlock snippetBlock;\n" + "\n" +
                                      "    Snippets(SnippetsBlock snippetBlock) {\n" + "       " +
                                      " this.snippetName = null;\n" + "        " +
                                      "this.snippetBlock = snippetBlock;\n" + "    }\n" + "\n" + " " +
                                      "  Snippets(String snippetName, SnippetsBlock snippetBlock) {\n" + "      " +
                                      "  this.snippetName = snippetName;\n" + "     " +
                                      "   this.snippetBlock = snippetBlock;\n" +
                                      "    }\n" + "\n" + "  " + "    /**\n" + "   " +
                                      "  * Get the Snippet Name.\n" + "     *\n" +
                                      "     * @return {@link String} snippet name\n" + "   " + "  */\n" +
                                      "    public String snippetName() {\n" +
                                      "        return this.snippetName;\n" + "  " + "  }\n"
                                      + "\n" + "    /**\n" + "     * Get the SnippetBlock.\n" + "     *\n" +
                                      "     * @return {@link SnippetsBlock} SnippetBlock\n" + "     */\n" +
                              "    public SnippetsBlock get() {\n" + "        return this.snippetBlock;\n" +
                              "    }\n" + "}";


       for (int i = 0; i < names.size(); i++) {
           String[] namesSplit = names.get(i).trim().split(":");
           String name = namesSplit[1].trim();
           String snippetDefPart = "DEF_" + name + "(SnippetsGenerator.get" +
                                   name.replaceAll("_", "").toLowerCase() + "()),";

           if (i < (names.size() - 1)) {
               // snippetDefLine =;
               snippetDefBody = snippetDefBody + snippetDefPart + "," + "\n";

           } else {
               //snippetDefLine = snippetDefPart + ";";
               snippetDefBody = snippetDefBody + snippetDefPart + ";" + "\n";
           }
       }

       String snippetDefinition = snippetDefHeader + snippetDefBody + "}" + snippetDefFooter;


       FileWriter writer = new FileWriter(sourceFile);
       writer.write(snippetDefinition);
       writer.close();
   }
}
