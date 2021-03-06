package org.kframework.backend.hkcd;

import org.kframework.kil.Rewrite;
import org.kframework.kil.Rule;
import org.kframework.kil.Term;
import org.kframework.kil.Variable;
import org.kframework.kil.loader.Context;

/**
 * Transform AST of language definition into set of Haskell
 * constructors.
 *
 * @see ProgramLoader.loadPgmAst
 */
public class HaskellDefFilter extends HaskellFilter {
	public HaskellDefFilter(Context context) {
		super(context);
	}

	public void visit(Rule rl) {
		result += "KRule [";
		Term b = rl.getBody();

		// Rule has no bag or cell, just a single rewrite of
		// KList sort
		if (b instanceof Rewrite) {
			result += "(KRuleCell \"PGM\" ";
			b.accept(this);
			result += ")";
		}
		result += "]";
		result += endl;
	}

	/**
	 * A rewrite is entered from cell, thus we yield two
	 * KRuleSide's.
	 */
	public void visit(Rewrite rw) {
		result += "(KRewriteSide (";
		rw.getLeft().accept(this);
		result += ")) (KRewriteSide (";
		rw.getRight().accept(this);
		result += "))";
	}

	/**
	 * When on LHS, produce a pattern to match constructor for
	 * sort of variable. Lower-cased name of variable will be used
	 * as name of pattern.
	 */
	public void visit(Variable var) {
		result += "KVar \"" + var.getName() + "\" \"" + var.getSort() + "\"";
	}
}
