package main;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.gui.Main;

public class TestChecker extends AbstractCheck {
    @Override
    public int[] getDefaultTokens() {
        return new int[]{
                TokenTypes.LITERAL_WHILE,
                TokenTypes.LITERAL_TRY,
                TokenTypes.LITERAL_FINALLY,
                TokenTypes.LITERAL_DO,
                TokenTypes.LITERAL_IF,
                TokenTypes.LITERAL_ELSE,
                TokenTypes.LITERAL_FOR,
                TokenTypes.INSTANCE_INIT,
                TokenTypes.STATIC_INIT,
                TokenTypes.LITERAL_SWITCH,
                TokenTypes.LITERAL_SYNCHRONIZED,
        };
    }

    @Override
    public int[] getAcceptableTokens() {
        return new int[]{
                TokenTypes.LITERAL_WHILE,
                TokenTypes.LITERAL_TRY,
                TokenTypes.LITERAL_CATCH,
                TokenTypes.LITERAL_FINALLY,
                TokenTypes.LITERAL_DO,
                TokenTypes.LITERAL_IF,
                TokenTypes.LITERAL_ELSE,
                TokenTypes.LITERAL_FOR,
                TokenTypes.INSTANCE_INIT,
                TokenTypes.STATIC_INIT,
                TokenTypes.LITERAL_SWITCH,
                TokenTypes.LITERAL_SYNCHRONIZED,
                TokenTypes.LITERAL_CASE,
                TokenTypes.LITERAL_DEFAULT,
                TokenTypes.ARRAY_INIT,
        };
    }

    @Override
    public int[] getRequiredTokens() {
        return new int[0];
    }

    @Override
    public void visitToken(DetailAST ast) {
        final DetailAST leftCurly = findLeftCurly(ast);
        if (leftCurly != null) {
            final boolean emptyBlock;
            if (leftCurly.getType() == TokenTypes.LCURLY) {
                final DetailAST nextSibling = leftCurly.getNextSibling();
                emptyBlock = nextSibling.getType() != TokenTypes.CASE_GROUP
                        && nextSibling.getType() != TokenTypes.SWITCH_RULE;
            } else {
                emptyBlock = leftCurly.getChildCount() <= 1;
            }
            if (emptyBlock) {
                log(leftCurly,
                        "test",
                        ast.getText());
            }

        }
    }

    /**
     * Calculates the left curly corresponding to the block to be checked.
     *
     * @param ast a {@code DetailAST} value
     * @return the left curly corresponding to the block to be checked
     */
    private static DetailAST findLeftCurly(DetailAST ast) {
        final DetailAST leftCurly;
        final DetailAST slistAST = ast.findFirstToken(TokenTypes.SLIST);
        if ((ast.getType() == TokenTypes.LITERAL_CASE
                || ast.getType() == TokenTypes.LITERAL_DEFAULT)
                && ast.getNextSibling() != null
                && ast.getNextSibling().getFirstChild() != null
                && ast.getNextSibling().getFirstChild().getType() == TokenTypes.SLIST) {
            leftCurly = ast.getNextSibling().getFirstChild();
        } else if (slistAST == null) {
            leftCurly = ast.findFirstToken(TokenTypes.LCURLY);
        } else {
            leftCurly = slistAST;
        }
        return leftCurly;
    }
}
