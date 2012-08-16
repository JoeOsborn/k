package ro.uaic.info.fmse.hkcd;

import ro.uaic.info.fmse.k.*;
import ro.uaic.info.fmse.loader.DefinitionHelper;
import ro.uaic.info.fmse.visitors.BasicVisitor;
import ro.uaic.info.fmse.transitions.maude.MaudeHelper;

/**
 * Translate AST into Haskell data constructors.
 */
public class HaskellFilter extends BasicVisitor {
	String endl = System.getProperty("line.separator");
	protected String result = "";

	public void visit(Constant cst) {
		String s = cst.getSort();
		String v = cst.getValue();

		result += "KApp (";

		// Perhaps we'd want to simply serialize some set of
		// classes into Haskell. However, this cannot be done
		// with existing set of Java classes for K since in
		// Haskell we have different constructors for various
		// types of constants, while in Java this information
		// is stored in the field of Constant class.
		// Implementation for such classes would basically be
		// the same as this code.
		if (s.equals("#Int")) {
			result += "KInt (" + v + ")";
		} else if (s.equals("#String")) {
			result += "KString " + v;
		} else if (s.equals("#Id")) {
			result += "KId \"" + v + "\"";
		} else if (s.equals("#Bool")) {
			result += "KBool " + HaskellUtil.capitalizeFirst(v);
		}

		// We take a shortcut here instead of processing a new
		// Empty of sort "List{K}".
		result += ") []";
	}

	public void visit(Empty e) {
		String s = e.getSort();
		
		if (!MaudeHelper.basicSorts.contains(s)) {
			result += "KApp (KLabel \"'." + s + "\") []";
		}
		else
			result += "[]";
	}

	public String getResult() {
		return result;
	}
}