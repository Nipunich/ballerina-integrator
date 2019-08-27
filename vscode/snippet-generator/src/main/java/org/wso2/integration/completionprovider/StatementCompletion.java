package org.wso2.integration.completionprovider;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.ballerinalang.langserver.common.CommonKeys;
import org.ballerinalang.langserver.compiler.LSContext;
import org.ballerinalang.langserver.completions.CompletionKeys;
import org.ballerinalang.langserver.completions.CompletionSubRuleParser;
import org.ballerinalang.langserver.completions.SymbolInfo;
import org.ballerinalang.langserver.completions.providers.contextproviders.InvocationOrFieldAccessContextProvider;
import org.ballerinalang.langserver.completions.providers.contextproviders.StatementContextProvider;
import org.ballerinalang.langserver.completions.util.Snippet;
import org.ballerinalang.langserver.completions.util.filters.StatementTemplateFilter;
import org.ballerinalang.langserver.completions.util.filters.SymbolFilters;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.wso2.ballerinalang.compiler.parser.antlr4.BallerinaParser;
//import org.ballerinalang.langserver.completions.providers.contextproviders.AnnotationAccessExpressionContextProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class StatementCompletion extends StatementContextProvider {

      private List<CompletionItem> getStaticcompletionItemsArr(LSContext context) {

        ArrayList<CompletionItem> completionItemsArr = new ArrayList<>();

        // Add the xmlns snippet
        completionItemsArr.add(Snippet.STMT_NAMESPACE_DECLARATION.get().build(context));
        // Add the var keyword
        completionItemsArr.add(Snippet.KW_VAR.get().build(context));
        // Add the error snippet
        completionItemsArr.add(Snippet.DEF_ERROR.get().build(context));
        // Add the checkpanic keyword
        //completionItemsArr.add(Snippet.KW_CHECK_PANIC.get().build(context));

        return completionItemsArr;
    }

 public List<CompletionItem> getCompletions(LSContext context) {
        List<CommonToken> lhsTokens = context.get(CompletionKeys.LHS_TOKENS_KEY);
        Optional<String> subRule = this.getSubRule(lhsTokens);
        subRule.ifPresent(rule -> CompletionSubRuleParser.parseWithinFunctionDefinition(rule, context));
        ParserRuleContext parserRuleContext = context.get(CompletionKeys.PARSER_RULE_CONTEXT_KEY);
        Boolean inWorkerReturn = context.get(CompletionKeys.IN_WORKER_RETURN_CONTEXT_KEY);
        int invocationOrDelimiterTokenType = context.get(CompletionKeys.INVOCATION_TOKEN_TYPE_KEY);

//        if (this.isAnnotationAccessExpression(context)) {
//            return this.getProvider(AnnotationAccessExpressionContextProvider.class).getCompletions(context);
//        }

        if (parserRuleContext != null && this.getProvider(parserRuleContext.getClass()) != null) {
            return this.getProvider(parserRuleContext.getClass()).getCompletions(context);
        }

        if (invocationOrDelimiterTokenType > -1) {
            /*
            Action invocation context
             */
            return this.getProvider(InvocationOrFieldAccessContextProvider.class).getCompletions(context);
        }
        if (inWorkerReturn != null && inWorkerReturn) {
            return this.getProvider(BallerinaParser.WorkerDeclarationContext.class).getCompletions(context);
        }

        // Add the visible static completion items
        ArrayList<CompletionItem> completionItemsArr = new ArrayList<>(getStaticcompletionItemsArr(context));
        // Add the statement templates
        Either<List<CompletionItem>, List<SymbolInfo>> itemList = SymbolFilters.get(StatementTemplateFilter.class)
                .filterItems(context);
        List<SymbolInfo> filteredList = new ArrayList<>(context.get(CommonKeys.VISIBLE_SYMBOLS_KEY));

        completionItemsArr.addAll(this.getCompletionItemList(itemList, context));
        filteredList.removeIf(this.attachedOrSelfKeywordFilter());
        completionItemsArr.addAll(this.getCompletionItemList(filteredList, context));
        completionItemsArr.addAll(this.getPackagesCompletionItems(context));
       // completionItemsArr.addAll(this.getTypeguardDestructuredItems(filteredList, context));
        // Now we need to sort the completion items and populate the completion items specific to the scope owner
        // as an example, resource, action, function scopes are different from the if-else, while, and etc
        Class itemSorter = context.get(CompletionKeys.BLOCK_OWNER_KEY).getClass();
        context.put(CompletionKeys.ITEM_SORTER_KEY, itemSorter);

        return completionItemsArr;
    }
   private boolean isAnnotationAccessExpression(LSContext context) {
        List<Integer> defaultTokenTypes = context.get(CompletionKeys.LHS_DEFAULT_TOKEN_TYPES_KEY);
        int annotationAccessIndex = defaultTokenTypes.indexOf(BallerinaParser.ANNOTATION_ACCESS);

        return annotationAccessIndex > -1;
    }
}
