//package org.wso2.integration.scopeprovider;
//
//import org.ballerinalang.langserver.common.utils.CommonUtil;
//import org.ballerinalang.langserver.compiler.DocumentServiceKeys;
//import org.ballerinalang.langserver.compiler.LSContext;
//import org.ballerinalang.langserver.completions.CompletionKeys;
//import org.ballerinalang.langserver.completions.providers.contextproviders.AnnotationAttachmentContextProvider;
//import org.ballerinalang.langserver.completions.providers.scopeproviders.ServiceScopeProvider;
//import org.ballerinalang.langserver.completions.util.Snippet;
//import org.eclipse.lsp4j.CompletionItem;
//import org.eclipse.lsp4j.Position;
//import org.wso2.ballerinalang.compiler.tree.BLangNode;
//import org.wso2.ballerinalang.compiler.tree.BLangService;
//import org.wso2.ballerinalang.compiler.tree.expressions.BLangExpression;
//import org.wso2.ballerinalang.compiler.util.diagnotic.DiagnosticPos;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ServiceScope extends ServiceScopeProvider {
//    public static Precedence precedence;
//     public ServiceScope() {
//        this.attachmentPoints.add(BLangService.class);
//         this.precedence = Precedence.HIGH;
//    }
//
//    @Override
//    public List<CompletionItem> getCompletions(LSContext ctx) {
//        ArrayList<CompletionItem> completionItems = new ArrayList<>();
//        if (this.isWithinAttachedExpressions(ctx)) {
//            // suggest all the visible, defined listeners
//            return this.getCompletionItemsAfterOnKeyword(ctx);
//        }
//        if (this.isAnnotationAttachmentContext(ctx)) {
//            return this.getProvider(AnnotationAttachmentContextProvider.class).getCompletions(ctx);
//        }
//
//        completionItems.add(Snippet.KW_PUBLIC.get().build(ctx));
//        completionItems.addAll(this.getResourceSnippets(ctx));
//        completionItems.add(Snippet.DEF_FUNCTION.get().build(ctx));
//
//        ctx.put(CompletionKeys.ITEM_SORTER_KEY, BLangService.class);
//
//        return completionItems;
//    }
//
//    private boolean isWithinAttachedExpressions(LSContext lsContext) {
//        BLangNode bLangNode = lsContext.get(CompletionKeys.SCOPE_NODE_KEY);
//        if (!(bLangNode instanceof BLangService)) {
//            return false;
//        }
//        BLangService service = (BLangService) bLangNode;
//        Position cursorPos = lsContext.get(DocumentServiceKeys.POSITION_KEY).getPosition();
//        int line = cursorPos.getLine();
//        int col = cursorPos.getCharacter();
//        List<BLangExpression> attachedExprs = service.attachedExprs;
//        if (attachedExprs.isEmpty()) {
//            return false;
//        }
//        BLangExpression firstExpr = attachedExprs.get(0);
//        BLangExpression lastExpr = CommonUtil.getLastItem(attachedExprs);
//        DiagnosticPos firstExprPos = CommonUtil.toZeroBasedPosition(firstExpr.pos);
//        int fSLine = firstExprPos.sLine;
//        int fSCol = firstExprPos.sCol;
//        DiagnosticPos lastExprPos = CommonUtil.toZeroBasedPosition(lastExpr.pos);
//        int lSLine = lastExprPos.sLine;
//        int lECol = lastExprPos.eCol;
//
//        return fSLine <= line && lSLine >= line && (fSCol <= col && lECol >= col);
//    }
//}
