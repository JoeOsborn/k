package org.kframework.backend.java.symbolic;

import org.kframework.backend.java.builtins.BoolToken;
import org.kframework.backend.java.builtins.IntToken;
import org.kframework.backend.java.builtins.UninterpretedToken;
import org.kframework.backend.java.kil.BuiltinMap;
import org.kframework.backend.java.kil.BuiltinSet;
import org.kframework.backend.java.kil.Cell;
import org.kframework.backend.java.kil.CellCollection;
import org.kframework.backend.java.kil.Definition;
import org.kframework.backend.java.kil.Hole;
import org.kframework.backend.java.kil.KItem;
import org.kframework.backend.java.kil.KLabelConstant;
import org.kframework.backend.java.kil.KLabelFreezer;
import org.kframework.backend.java.kil.KLabelInjection;
import org.kframework.backend.java.kil.KList;
import org.kframework.backend.java.kil.KSequence;
import org.kframework.backend.java.kil.MapLookup;
import org.kframework.backend.java.kil.MapUpdate;
import org.kframework.backend.java.kil.MetaVariable;
import org.kframework.backend.java.kil.SetLookup;
import org.kframework.backend.java.kil.SetUpdate;
import org.kframework.backend.java.kil.Term;
import org.kframework.backend.java.kil.TermContext;
import org.kframework.backend.java.kil.Variable;
import org.kframework.kil.ASTNode;


/**
 * Transformer class which applies the same transformation on all {@link Term} nodes in the AST.
 *
 * @author AndreiS
 */
public class TermTransformer extends CopyOnWriteTransformer {

    public TermTransformer(TermContext context) {
        super(context);
    }

    public TermTransformer(Definition definition) {
        super(definition);
    }

    public Term transformTerm(Term term) {
        return term;
    }

    @Override
    public ASTNode transform(BoolToken boolToken) {
        return transformTerm((Term) super.transform(boolToken));
    }

    @Override
    public ASTNode transform(BuiltinMap builtinMap) {
        return transformTerm((Term) super.transform(builtinMap));
    }

    @Override
    public ASTNode transform(BuiltinSet builtinSet) {
        return transformTerm((Term) super.transform(builtinSet));
    }

    @Override
    public ASTNode transform(Cell cell) {
        return transformTerm((Term) super.transform(cell));
    }

    @Override
    public ASTNode transform(CellCollection cellCollection) {
        return transformTerm((Term) super.transform(cellCollection));
    }

    @Override
    public ASTNode transform(Hole hole) {
        return transformTerm((Term) super.transform(hole));
    }

    @Override
    public ASTNode transform(IntToken intToken) {
        return transformTerm((Term) super.transform(intToken));
    }

    @Override
    public ASTNode transform(KLabelConstant kLabelConstant) {
        return transformTerm((Term) super.transform(kLabelConstant));
    }

    @Override
    public ASTNode transform(KLabelFreezer kLabelFreezer) {
        return transformTerm((Term) super.transform(kLabelFreezer));
    }

    @Override
    public ASTNode transform(KLabelInjection kLabelInjection) {
        return transformTerm((Term) super.transform(kLabelInjection));
    }

    @Override
    public ASTNode transform(KItem kItem) {
        return transformTerm((Term) super.transform(kItem));
    }

    @Override
    public ASTNode transform(KList kList) {
        return transformTerm((Term) super.transform(kList));
    }

    @Override
    public ASTNode transform(KSequence kSequence) {
        return transformTerm((Term) super.transform(kSequence));
    }

    @Override
    public ASTNode transform(MapLookup mapLookup) {
        return transformTerm((Term) super.transform(mapLookup));
    }

    @Override
    public ASTNode transform(MapUpdate mapUpdate) {
        return transformTerm((Term) super.transform(mapUpdate));
    }

    @Override
    public ASTNode transform(SetLookup setLookup) {
        return transformTerm((Term) super.transform(setLookup));
    }

    @Override
    public ASTNode transform(SetUpdate setUpdate) {
        return transformTerm((Term) super.transform(setUpdate));
    }

    @Override
    public ASTNode transform(MetaVariable metaVariable) {
        return transformTerm((Term) super.transform(metaVariable));
    }

    @Override
    public ASTNode transform(UninterpretedToken uninterpretedToken) {
        return transformTerm((Term) super.transform(uninterpretedToken));
    }

    @Override
    public ASTNode transform(Variable variable) {
        return transformTerm((Term) super.transform(variable));
    }

}
