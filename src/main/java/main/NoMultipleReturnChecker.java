package main;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.puppycrawl.tools.checkstyle.api.TokenTypes.*;

/**
 * Checkout method declaration:
 * https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#METHOD_DEF
 * <p>
 * Check also TokenUtil.class
 * <p>
 * TODO Replace raw string error message to constant
 */
public class NoMultipleReturnChecker extends AbstractCheck {
    // returns a set of TokenTypes which are processed in visitToken() method by default.
    @Override
    public int[] getDefaultTokens() {
        return new int[]{
                METHOD_DEF
        };
    }

    // Returns a set, which contains all the TokenTypes that can be processed by the check.
    // Both DefaultTokens and
    // RequiredTokens and any custom set of TokenTypes are subsets of AcceptableTokens.
    // TODO not really getting what's this
    @Override
    public int[] getAcceptableTokens() {
//        return getDefaultTokens();
        return new int[0];
    }

    //  returns a set of TokenTypes which Check must be subscribed to for a valid execution.
    //  If the user wants to specify a custom set of TokenTypes then this set must contain
    //  all the TokenTypes from RequiredTokens.
    // TODO not really getting what's this
    @Override
    public int[] getRequiredTokens() {
        return new int[0];
    }

    @Override
    public void visitToken(DetailAST detailAST) {
        DetailAST rootAst = detailAST.findFirstToken(TokenTypes.SLIST);

        if (Objects.nonNull(rootAst)) {
            final List<DetailAST> res = new ArrayList<>();

            for (DetailAST ast = rootAst.getFirstChild(); Objects.nonNull(ast); ast = ast.getNextSibling()) {
                // Iterating through blocks like if (1 < 2) { ... }
                DetailAST leftCurly = findLeftCurly(ast);
                if (Objects.nonNull(leftCurly)) {
                    res.addAll(findBlockCodeTokenCount(leftCurly, LITERAL_RETURN));
                } else if (Objects.equals(ast.getType(), LITERAL_RETURN)) {
                    res.add(ast);
                }
            }

            if (res.size() > 1) {
                for (DetailAST ast : res) {
                    log(ast, "Method has to have only one `return` statement!");
                }
            }
        }
    }

    // TODO don't really know about another names of `{` left brace
    private DetailAST findLeftCurly(DetailAST root) {
        DetailAST result;
        DetailAST lCurly = root.findFirstToken(LCURLY);
        if (Objects.nonNull(lCurly)) { // switch
            result = lCurly;
        } else { // if
            result = root.findFirstToken(SLIST);
        }
        return result;
    }

    // Recursively iterating through code blocks
    // If we're talking about code block like 'if(1 < 2) { ... }' then root it's '{' - left curly
    private List<DetailAST> findBlockCodeTokenCount(DetailAST root, int searchableToken) {
        // In case of nested blocks
        final List<DetailAST> result = new ArrayList<>();
        // TODO don't really know isn't that bad, but in my experience with LCURLE must use getNextSibling()
        //  and with SLIST must use getFirstChild()
        final DetailAST initial = Objects.equals(root.getType(), LCURLY) ? root.getNextSibling() : root.getFirstChild();
        // Iterating via lines of one block level, f.e. every line in 'if' block or every line in method
        for (DetailAST ast = initial; Objects.nonNull(ast); ast = ast.getNextSibling()) {
            // If we already in 'if' statement and current line is nested 'if'
            DetailAST leftCurly = findLeftCurly(ast);
            if (Objects.nonNull(leftCurly)) {
                result.addAll(findBlockCodeTokenCount(leftCurly, searchableToken));
            } else if (Objects.equals(ast.getType(), searchableToken)) {
                result.add(ast);
            }
        }
        return result;
    }
}
