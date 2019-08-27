package org.wso2.integration.util;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.ballerinalang.langserver.AnnotationNodeKind;
import org.ballerinalang.langserver.compiler.LSContext;
import org.ballerinalang.langserver.compiler.workspace.WorkspaceDocumentManager;
import org.ballerinalang.model.tree.Node;
import org.eclipse.lsp4j.CompletionCapabilities;
import org.wso2.ballerinalang.compiler.tree.BLangNode;

import java.util.List;

public class CompletionKey {
     private CompletionKey() {
    }
    public static final LSContext.Key<BLangNode> SCOPE_NODE_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<Node> BLOCK_OWNER_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<BLangNode> PREVIOUS_NODE_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<AnnotationNodeKind> NEXT_NODE_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<Integer> LOOP_COUNT_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<Boolean> CURRENT_NODE_TRANSACTION_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<Integer> TRANSACTION_COUNT_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<WorkspaceDocumentManager> DOC_MANAGER_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<ParserRuleContext> PARSER_RULE_CONTEXT_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<CompletionCapabilities> CLIENT_CAPABILITIES_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<List<CommonToken>> LHS_TOKENS_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<List<CommonToken>> LHS_DEFAULT_TOKENS_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<List<Integer>> LHS_DEFAULT_TOKEN_TYPES_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<List<CommonToken>> RHS_TOKENS_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<Integer> INVOCATION_TOKEN_TYPE_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<Boolean> IN_WORKER_RETURN_CONTEXT_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<Boolean> IN_INVOCATION_PARAM_CONTEXT_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<Class> ITEM_SORTER_KEY
            = new LSContext.Key<>();

    // Following key is used for the completion within the if else/ while condition context
    public static final LSContext.Key<Boolean> IN_CONDITION_CONTEXT_KEY
            = new LSContext.Key<>();
}
