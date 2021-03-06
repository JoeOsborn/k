package org.kframework.compile.transformers;

import org.kframework.compile.utils.MaudeHelper;
import org.kframework.compile.utils.MetaK;
import org.kframework.kil.*;
import org.kframework.kil.Collection;
import org.kframework.kil.loader.Context;
import org.kframework.kil.visitors.CopyOnWriteTransformer;
import org.kframework.kil.visitors.exceptions.TransformerException;
import org.kframework.krun.K;
import org.kframework.utils.general.GlobalSettings;

import java.util.*;
import java.util.List;
import java.util.Set;


public class FlattenSyntax extends CopyOnWriteTransformer {
	FlattenKSyntax kTrans;
	Set<String> listSeparators = new HashSet<String>();
	boolean isComputation = false;

	public FlattenSyntax(Context context) {
		super("Syntax K to Abstract K", context);
		kTrans = new FlattenKSyntax(this, context);
	}

	@Override
	public ASTNode transform(Module node) throws TransformerException {
		listSeparators.clear();
		node = (Module) super.transform(node);
		if (listSeparators.isEmpty())
			return node;

		// List<PriorityBlock> pbs = new ArrayList<PriorityBlock>();
		// PriorityBlock pb = new PriorityBlock();
		// pbs.add(pb);
		// Syntax syn = new Syntax(new Sort(KSorts.KLABEL), pbs);
		// node.getItems().add(syn);
		// for (String separator : listSeparators) {
		// List<ProductionItem> pis = new ArrayList<ProductionItem>();
		// pis.add(new Terminal(MetaK.getListUnitLabel(separator)));
		// pb.getProductions().add(new Production(new Sort(KSorts.KLABEL), pis));
		// }
		for (String sep : listSeparators) {
			node.addConstant(KSorts.KLABEL, MetaK.getListUnitLabel(sep));
		}
		return node;
	}

	@Override
	public ASTNode transform(Syntax node) throws TransformerException {
		if (!MetaK.isComputationSort(node.getSort().getName())) {
			isComputation = false;
			return super.transform(node);
		}
		isComputation = true;
		node = (Syntax) super.transform(node);
		node.setSort(new Sort(KSorts.KLABEL));
		return node;
	}

	@Override
	public ASTNode transform(Production node) throws TransformerException {
		if (node.containsAttribute("KLabelWrapper"))
			return node;
		if (!isComputation)
			return super.transform(node);
		if (node.isSubsort() && !node.containsAttribute("klabel"))
			return null;
		String arity = String.valueOf(node.getArity());
		Attributes attrs = node.getAttributes().shallowCopy();
		if (node.isListDecl()) {
			listSeparators.add(((UserList) node.getItems().get(0)).getSeparator());
			attrs.set("hybrid", "");
		}
		node = node.shallowCopy();
		List<ProductionItem> pis = new ArrayList<ProductionItem>();
		pis.add(new Terminal(node.getKLabel()));
		node.setItems(pis);
		attrs.set("arity", arity);
		node.setAttributes(attrs);
		node.setSort(KSorts.KLABEL);
		return node;
	}

	@Override
	public ASTNode transform(Sort node) throws TransformerException {
		if (!MetaK.isComputationSort(node.getName()))
			return node;
		return new Sort("K");
	}

	@Override
	public ASTNode transform(KApp node) throws TransformerException {
		return node.accept(kTrans);
	}

	@Override
	public ASTNode transform(KSequence node) throws TransformerException {
		return node.accept(kTrans);
	}

	@Override
	public ASTNode transform(Variable node) throws TransformerException {
		if (MetaK.isComputationSort(node.getSort()))
			return node.accept(kTrans);
		return node;
	}

	@Override
	public ASTNode transform(Empty node) throws TransformerException {
		if (MetaK.isComputationSort(node.getSort()))
			return node.accept(kTrans);
		return node;
	}

	@Override
	public ASTNode transform(Constant node) throws TransformerException {
        assert false : "dead code";
		if (MetaK.isComputationSort(node.getSort()))
			return node.accept(kTrans);
		return node;
	}

	@Override
	public ASTNode transform(TermCons tc) throws TransformerException {
		if (MetaK.isComputationSort(tc.getSort()))
			return tc.accept(kTrans);
		return super.transform(tc);
	}

	class FlattenKSyntax extends CopyOnWriteTransformer {
		FlattenSyntax trans;

		public FlattenKSyntax(FlattenSyntax t, Context context) {
			super("Flatten K Syntax", context);
			trans = t;
		}

		@Override
		public ASTNode transform(KApp node) throws TransformerException {
			Term label = (Term) node.getLabel().accept(trans);
			Term child = (Term) node.getChild().accept(trans);
			if (child != node.getChild() || label != node.getLabel()) {
				node = node.shallowCopy();
				node.setChild(child);
				node.setLabel(label);
			}
			return node;
		}

		@Override
		public ASTNode transform(Freezer node) throws TransformerException {
			return KApp.of(new FreezerLabel((Term) node.getTerm().accept(this)));
		}

		@Override
		public ASTNode transform(TermCons tc) throws TransformerException {
			if (!MetaK.isComputationSort(tc.getSort())) {
				return KApp.of(new KInjectedLabel((Term) tc.accept(trans)));
			}

			String l = tc.getLocation();
			String f = tc.getFilename();
			Production ppp = context.conses.get(tc.getCons());
			KList lok = new KList(l, f);
			for (Term t : tc.getContents()) {
				lok.getContents().add((Term) t.accept(this));
			}
			return new KApp(l, f, KLabelConstant.of(ppp.getKLabel(), context), lok);
		}

		@Override
		public ASTNode transform(KLabel kLabel) throws TransformerException {
			return new KApp(
                    kLabel.getLocation(),
                    kLabel.getFilename(),
                    new KInjectedLabel(kLabel),
                    KList.EMPTY);
		}

		@Override
		public ASTNode transform(Constant cst) throws TransformerException {
            assert false : "deprecated class";
            return null;
/*			String l = cst.getLocation();
			String f = cst.getFilename();

			if (!MetaK.isBuiltinSort(cst.getSort(context))) {
				KList list = new KList();
				list.add(StringBuiltin.of(cst.getSort(context)));
				list.add(StringBuiltin.of(StringUtil.escape(cst.getValue())));
				return new KApp(KLabelConstant.of("#token", context), list).accept(this);
			} else {
				return new KApp(l, f, new KInjectedLabel(cst), KList.EMPTY);
			} */
		}

		@Override
		public ASTNode transform(Empty emp) {
			String l = emp.getLocation();
			String f = emp.getFilename();
			if (!MetaK.isComputationSort(emp.getSort())) {
				return KApp.of(new KInjectedLabel(emp));
			}
			// if this is a list sort
			if (!MaudeHelper.basicSorts.contains(emp.getSort())) {
				Production listProd = context.listConses.get(emp.getSort());
				String separator = ((UserList) listProd.getItems().get(0)).getSeparator();
				return new KApp(l, f, KLabelConstant.of(MetaK.getListUnitLabel(separator), context), KList.EMPTY);
				// Constant cst = new Constant(l, f, KSorts.KLABEL, "'." + emp.getSort() + "");
				// return new KApp(l, f, cst, new Empty(l, f, MetaK.Constants.KList));
			}
			return emp;
		}

		@Override
		public ASTNode transform(Collection node) throws TransformerException {
			if (node instanceof KSequence)
				return super.transform(node);
			return KApp.of(new KInjectedLabel((Term) node.accept(trans)));
		}

		@Override
		public ASTNode transform(CollectionItem node) throws TransformerException {
			return KApp.of(new KInjectedLabel((Term) node.accept(trans)));
		}

		@Override
		public ASTNode transform(MapItem node) throws TransformerException {
			return KApp.of(new KInjectedLabel((Term) node.accept(trans)));
		}

        @Override
        public ASTNode transform(CollectionBuiltin node) throws TransformerException {
            /* just for LHS for now */
            assert  (node.isLHSView() || node.isElementCollection());

            LinkedHashSet<Term> elements = new LinkedHashSet<Term>(node.elements().size());
            for (Term term : node.elements()) {
                Term transformedTerm = (Term) term.accept(trans);
                elements.add(transformedTerm);
            }

            ArrayList<Term> terms = new ArrayList<Term>(node.baseTerms().size());
            if (node.isLHSView()) {
                Variable frame = node.viewBase();
                frame.setSort(node.sort().type());
                terms.add(frame);
            }

            return KApp.of(new KInjectedLabel(CollectionBuiltin.of(
                    node.sort(),
                    elements,
                    terms)));
        }

        @Override
        public ASTNode transform(MapBuiltin node) throws TransformerException {
            /* just for LHS for now */
            assert  (node.isLHSView() || node.isElementCollection());

            LinkedHashMap<Term, Term> elements = new LinkedHashMap<Term, Term>(node.elements().size());
            for (java.util.Map.Entry<Term, Term> entry : node.elements().entrySet()) {
                Term transformedKey = (Term) entry.getKey().accept(trans);
                Term transformedValue = (Term) entry.getValue().accept(trans);
                elements.put(transformedKey, transformedValue);
            }

            ArrayList<Term> terms = new ArrayList<Term>(node.baseTerms().size());
            if (node.isLHSView()) {
                Variable frame = node.viewBase();
                frame.setSort(node.sort().type());
                terms.add(frame);
            }

            return KApp.of(new KInjectedLabel(new MapBuiltin(
                    node.sort(),
                    elements,
                    terms)));
        }

		@Override
		public ASTNode transform(Cell node) throws TransformerException {
			return KApp.of(new KInjectedLabel((Term) node.accept(trans)));
		}

		@Override
		public ASTNode transform(Variable node) throws TransformerException {
			if (node.getSort().equals(KSorts.KITEM) || node.getSort().equals(KSorts.K)) {
				return node;
            }
			if (MetaK.isKSort(node.getSort())) {
				return KApp.of(new KInjectedLabel(node));
            }

            if (node.getSort().equals(BoolBuiltin.SORT_NAME)
                    || node.getSort().equals(IntBuiltin.SORT_NAME)
                    || node.getSort().equals(FloatBuiltin.SORT_NAME)
                    || node.getSort().equals(StringBuiltin.SORT_NAME)) {
                return node;
            }

            if (context.getDataStructureSorts().containsKey(node.getSort())) {
                //node = node.shallowCopy();
                //node.setSort(context.dataStructureSorts.get(node.getSort()).type());
                //return KApp.of(new KInjectedLabel(node));
                return node;
            }

			node = node.shallowCopy();
            if (GlobalSettings.matchingLogic || K.backend.equals("java-symbolic")) {
                /* the Java Rewrite Engine preserves sort information for variables */
            } else {
			    node.setSort(KSorts.KITEM);
            }
			return node;
		}
	}
}
