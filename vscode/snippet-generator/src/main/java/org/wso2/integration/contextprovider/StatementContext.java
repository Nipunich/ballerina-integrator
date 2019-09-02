//package org.wso2.integration.contextprovider;
//
//import org.antlr.v4.runtime.CommonToken;
//import org.antlr.v4.runtime.ParserRuleContext;
//import org.ballerinalang.langserver.SnippetBlock;
//import org.ballerinalang.langserver.common.CommonKeys;
//import org.ballerinalang.langserver.common.utils.CommonUtil;
//import org.ballerinalang.langserver.compiler.LSContext;
//import org.ballerinalang.langserver.completions.CompletionKeys;
//import org.ballerinalang.langserver.completions.CompletionSubRuleParser;
//import org.ballerinalang.langserver.completions.SymbolInfo;
//import org.ballerinalang.langserver.completions.providers.contextproviders.InvocationOrFieldAccessContextProvider;
//import org.ballerinalang.langserver.completions.providers.contextproviders.StatementContextProvider;
//import org.ballerinalang.langserver.completions.util.Snippet;
//import org.ballerinalang.langserver.completions.util.filters.StatementTemplateFilter;
//import org.ballerinalang.langserver.completions.util.filters.SymbolFilters;
//import org.eclipse.lsp4j.CompletionItem;
//import org.eclipse.lsp4j.jsonrpc.messages.Either;
//import org.wso2.ballerinalang.compiler.parser.antlr4.BallerinaParser;
//import org.wso2.ballerinalang.compiler.semantics.model.types.BType;
//import org.wso2.ballerinalang.compiler.semantics.model.types.BUnionType;
//import org.wso2.ballerinalang.compiler.util.TypeTags;
//import org.wso2.integration.util.CompletionKey;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//public class StatementContext extends StatementContextProvider  {
//     public static Precedence precedence;
//
//     public StatementContext() {
//        this.attachmentPoints.add(StatementContext.class);
//        this.precedence = Precedence.HIGH;
//    }
//
//    @Override
//    public List<CompletionItem> getCompletions(LSContext context) {
//        List<CommonToken> lhsTokens = context.get(CompletionKey.LHS_TOKENS_KEY);
//        Optional<String> subRule = this.getSubRule(lhsTokens);
//        subRule.ifPresent(rule -> CompletionSubRuleParser.parseWithinFunctionDefinition(rule, context));
//        ParserRuleContext parserRuleContext = context.get(CompletionKey.PARSER_RULE_CONTEXT_KEY);
//        Boolean inWorkerReturn = context.get(CompletionKey.IN_WORKER_RETURN_CONTEXT_KEY);
//        int invocationOrDelimiterTokenType = context.get(CompletionKey.INVOCATION_TOKEN_TYPE_KEY);
//
////        if (this.isAnnotationAccessExpression(context)) {
////            return this.getProvider(AnnotationAccessExpressionContextProvider.class).getCompletions(context);
////        }
//
//        if (parserRuleContext != null && this.getProvider(parserRuleContext.getClass()) != null) {
//            return this.getProvider(parserRuleContext.getClass()).getCompletions(context);
//        }
//
//        if (invocationOrDelimiterTokenType > -1) {
//            /*
//            Action invocation context
//             */
//            return this.getProvider(InvocationOrFieldAccessContextProvider.class).getCompletions(context);
//        }
//        if (inWorkerReturn != null && inWorkerReturn) {
//            return this.getProvider(BallerinaParser.WorkerDeclarationContext.class).getCompletions(context);
//        }
//
//        // Add the visible static completion items
//        ArrayList<CompletionItem> completionItemsArr = new ArrayList<>(getStaticCompletionItems(context));
//        // Add the statement templates
//        Either<List<CompletionItem>, List<SymbolInfo>> itemList = SymbolFilters.get(StatementTemplateFilter.class)
//                                                                               .filterItems(context);
//        List<SymbolInfo> filteredList = new ArrayList<>(context.get(CommonKeys.VISIBLE_SYMBOLS_KEY));
//
//        completionItemsArr.addAll(this.getCompletionItemList(itemList, context));
//        filteredList.removeIf(this.attachedOrSelfKeywordFilter());
//        completionItemsArr.addAll(this.getCompletionItemList(filteredList, context));
//        completionItemsArr.addAll(this.getPackagesCompletionItems(context));
//        completionItemsArr.addAll(this.getTypeguardDestructuredItems(filteredList, context));
//        // Now we need to sort the completion items and populate the completion items specific to the scope owner
//        // as an example, resource, action, function scopes are different from the if-else, while, and etc
//        Class itemSorter = context.get(CompletionKey.BLOCK_OWNER_KEY).getClass();
//        context.put(CompletionKey.ITEM_SORTER_KEY, itemSorter);
//
//        return completionItemsArr;
//    }
//
//    private List<CompletionItem> getStaticCompletionItems(LSContext context) {
//
//        ArrayList<CompletionItem> completionItemsArr  = new ArrayList<>();
//
//        // Add the xmlns snippet
//        completionItemsArr.add(Snippet.STMT_NAMESPACE_DECLARATION.get().build(context));
//        // Add the var keyword
//        completionItemsArr.add(Snippet.KW_VAR.get().build(context));
//        // Add the error snippet
//        completionItemsArr.add(Snippet.DEF_ERROR.get().build(context));
//        // Add the checkpanic keyword
//        //completionItems.add(Snippet.KW_CHECK_PANIC.get().build(context));
//
//        return completionItemsArr;
//    }
//
//    private List<CompletionItem> getTypeguardDestructuredItems(List<SymbolInfo> symbolInfoList, LSContext ctx) {
//        List<String> capturedSymbols = new ArrayList<>();
//        // In the case of type guarded variables multiple symbols with the same symbol name and we ignore those
//        return symbolInfoList.stream()
//                .filter(symbolInfo -> (symbolInfo.getScopeEntry().symbol.type instanceof BUnionType)
//                        && !capturedSymbols.contains(symbolInfo.getScopeEntry().symbol.name.value))
//                .map(symbolInfo -> {
//                    capturedSymbols.add(symbolInfo.getSymbolName());
//                    List<BType> errorTypes = new ArrayList<>();
//                    List<BType> resultTypes = new ArrayList<>();
//                    List<BType> members =
//                            new ArrayList<>(((BUnionType) symbolInfo.getScopeEntry().symbol.type).getMemberTypes());
//                    members.forEach(bType -> {
//                        if (bType.tag == TypeTags.ERROR) {
//                            errorTypes.add(bType);
//                        } else {
//                            resultTypes.add(bType);
//                        }
//                    });
//                    if (errorTypes.size() == 1) {
//                        resultTypes.addAll(errorTypes);
//                    }
//                    String symbolName = symbolInfo.getScopeEntry().symbol.name.getValue();
//                    String label = symbolName + " - typeguard " + symbolName;
//                    String detail = "Destructure the variable " + symbolName + " with typeguard";
//                    StringBuilder snippet = new StringBuilder();
//                    int paramCounter = 1;
//                    if (errorTypes.size() > 1) {
//                        snippet.append("if (").append(symbolName).append(" is ").append("error) {")
//                               .append(CommonUtil.LINE_SEPARATOR).append("\t${1}").append(CommonUtil.LINE_SEPARATOR)
//                               .append("}");
//                        paramCounter++;
//                    } else if (errorTypes.size() == 1) {
//                        snippet.append("if (").append(symbolName).append(" is ")
//                                .append(CommonUtil.getBTypeName(errorTypes.get(0), ctx)).append(") {")
//                                .append(CommonUtil.LINE_SEPARATOR).append("\t${1}").append(CommonUtil.LINE_SEPARATOR)
//                                .append("}");
//                        paramCounter++;
//                    }
//                    int finalParamCounter = paramCounter;
//                    String restSnippet = (!snippet.toString().isEmpty() && resultTypes.size() > 2) ? " else " : "";
//                    restSnippet += IntStream.range(0, resultTypes.size() - paramCounter).mapToObj(value -> {
//                        BType bType = members.get(value);
//                        String placeHolder = "\t${" + (value + finalParamCounter) + "}";
//                        return "if (" + symbolName + " is " + CommonUtil.getBTypeName(bType, ctx) + ") {"
//                                + CommonUtil.LINE_SEPARATOR + placeHolder + CommonUtil.LINE_SEPARATOR + "}";
//                    }).collect(Collectors.joining(" else ")) + " else {" + CommonUtil.LINE_SEPARATOR + "\t${"
//                                   + members.size() + "}" + CommonUtil.LINE_SEPARATOR + "}";
//
//                    snippet.append(restSnippet);
//
//                    return new SnippetBlock(label, snippet.toString(), detail,
//                                            SnippetBlock.SnippetType.SNIPPET).build(ctx);
//                }).collect(Collectors.toList());
//    }
//
//    private boolean isAnnotationAccessExpression(LSContext context) {
//        List<Integer> defaultTokenTypes = context.get(CompletionKeys.LHS_DEFAULT_TOKEN_TYPES_KEY);
//        int annotationAccessIndex = defaultTokenTypes.indexOf(BallerinaParser.ANNOTATION_ACCESS);
//
//        return annotationAccessIndex > -1;
//    }
//}
