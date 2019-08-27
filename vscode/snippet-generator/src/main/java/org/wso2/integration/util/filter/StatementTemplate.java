package org.wso2.integration.util.filter;

import org.ballerinalang.langserver.compiler.LSContext;
import org.ballerinalang.langserver.completions.CompletionKeys;
import org.ballerinalang.langserver.completions.SymbolInfo;
import org.ballerinalang.langserver.completions.util.Snippet;
import org.ballerinalang.langserver.completions.util.filters.AbstractSymbolFilter;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.InsertTextFormat;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.wso2.ballerinalang.compiler.tree.BLangFunction;
import org.wso2.ballerinalang.compiler.tree.BLangNode;
import org.wso2.ballerinalang.compiler.tree.statements.BLangBlockStmt;
import org.wso2.ballerinalang.compiler.tree.statements.BLangForkJoin;
import org.wso2.ballerinalang.compiler.tree.statements.BLangIf;
import org.wso2.integration.util.Snippets;
import org.wso2.integration.util.CompletionKey;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StatementTemplate extends AbstractSymbolFilter {
    @Override
    public Either<List<CompletionItem>, List<SymbolInfo>> filterItems(LSContext context) {
        ArrayList<CompletionItem> completionItemsArr = new ArrayList<>();
        BLangNode bLangNode = context.get(CompletionKey.SCOPE_NODE_KEY);

        // Populate If Statement template
        completionItemsArr.add(Snippets.STMT_IF.get().build(context));

                if (context.get(CompletionKey.PREVIOUS_NODE_KEY) instanceof BLangIf) {
            // Populate Else If Statement template
            completionItemsArr.add(Snippets.STMT_ELSE_IF.get().build(context));
            // Populate Else Statement template
            completionItemsArr.add(Snippets.STMT_ELSE.get().build(context));
        }

//        if (context.get(CompletionKeys.PREVIOUS_NODE_KEY) instanceof BLangIf) {
//            // Populate Else If Statement template
//            completionItems.add(Snippet.STMT_ELSE_IF.get().build(context));
//            // Populate Else Statement template
//            completionItems.add(Snippet.STMT_ELSE.get().build(context));
//        }

        // Populate While Statement template
       // completionItems.add(Snippet.STMT_WHILE.get().build(context));
        // Populate Lock Statement template
       // completionItems.add(Snippet.STMT_LOCK.get().build(context));
        // Populate Foreach Statement template
       // completionItems.add(Snippet.STMT_FOREACH.get().build(context));
        // Populate Fork Statement template
      //  completionItems.add(Snippet.STMT_FORK.get().build(context));
        // Populate Transaction Statement template
        completionItemsArr.add(Snippets.STMT_TRANSACTION.get().build(context));
        completionItemsArr.add(Snippets.DEF_RESOURCE_HTTP.get().build(context));
        // Populate Match statement template
      //  completionItems.add(Snippet.STMT_MATCH.get().build(context));
//        if (bLangNode instanceof BLangBlockStmt
//                && (bLangNode.parent instanceof BLangFunction || bLangNode.parent instanceof BLangForkJoin)) {
//            // Populate Worker Declaration statement template
//            completionItemsArr.add(Snippet.DEF_WORKER.get().build(context));
//        }
//
//        if (context.get(CompletionKeys.LOOP_COUNT_KEY) > 0
//                && !context.get(CompletionKeys.CURRENT_NODE_TRANSACTION_KEY)) {
//            /*
//            Populate Continue Statement template only if enclosed within a looping construct
//            and not in immediate transaction construct
//             */
//            completionItems.add(Snippet.STMT_CONTINUE.get().build(context));
//        }
//
//        if (context.get(CompletionKeys.LOOP_COUNT_KEY) > 0) {
//            // Populate Break Statement template only if there is an enclosing looping construct such as while/ foreach
//            completionItems.add(Snippet.STMT_BREAK.get().build(context));
//        }
//        // Populate Return Statement template
//        completionItems.add(Snippet.STMT_RETURN.get().build(context));
//
//        if (context.get(CompletionKeys.TRANSACTION_COUNT_KEY) > 0) {
//            completionItems.add(Snippet.STMT_ABORT.get().build(context));
//            completionItems.add(Snippet.STMT_RETRY.get().build(context));
//        }
//        // Populate Throw Statement template
//        completionItems.add(Snippet.STMT_PANIC.get().build(context));
//
        completionItemsArr.sort(Comparator.comparing(CompletionItem::getLabel));
//
//        // Set the insert text format to be snippet supported format
//        completionItems.forEach(completionItem -> completionItem.setInsertTextFormat(InsertTextFormat.Snippet));

        return Either.forLeft(completionItemsArr);
    }
}
