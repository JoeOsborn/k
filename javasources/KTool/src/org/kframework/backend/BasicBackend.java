package org.kframework.backend;

import org.kframework.compile.FlattenModules;
import org.kframework.compile.ResolveConfigurationAbstraction;
import org.kframework.compile.checks.CheckConfigurationCells;
import org.kframework.compile.checks.CheckRewrite;
import org.kframework.compile.checks.CheckVariables;
import org.kframework.compile.sharing.DeclareCellLabels;
import org.kframework.compile.tags.AddDefaultComputational;
import org.kframework.compile.tags.AddOptionalTags;
import org.kframework.compile.tags.AddStrictStar;
import org.kframework.compile.transformers.*;
import org.kframework.compile.utils.*;
import org.kframework.kil.Definition;
import org.kframework.kil.loader.Context;
import org.kframework.main.FirstStep;
import org.kframework.main.LastStep;
import org.kframework.utils.Stopwatch;
import org.kframework.utils.general.GlobalSettings;

/**
 * Initially created by: Traian Florin Serbanuta
 * <p/>
 * Date: 12/17/12 Time: 11:18 PM
 */
public abstract class BasicBackend implements Backend {
	protected Stopwatch sw;
	protected Context context;

	public BasicBackend(Stopwatch sw, Context context) {
		this.sw = sw;
		this.context = context;
	}

	@Override
	public Definition lastStep(Definition def) {
		return def;
	}

	@Override
	public Definition firstStep(Definition def) {
		return def;
	}

	public boolean autoinclude() {
		return true;
	}

	public CompilerSteps<Definition> getCompilationSteps() {
		CompilerSteps<Definition> steps = new CompilerSteps<Definition>(context);
		steps.add(new FirstStep(this, context));
		steps.add(new CheckVisitorStep<Definition>(new CheckConfigurationCells(context), context));
		steps.add(new RemoveBrackets(context));
		steps.add(new AddEmptyLists(context));
		steps.add(new RemoveSyntacticCasts(context));
		steps.add(new CheckVisitorStep<Definition>(new CheckVariables(context), context));
		steps.add(new CheckVisitorStep<Definition>(new CheckRewrite(context), context));
		steps.add(new FlattenModules(context));
		steps.add(new StrictnessToContexts(context));
		steps.add(new FreezeUserFreezers(context));
		steps.add(new ContextsToHeating(context));
		steps.add(new AddSupercoolDefinition(context));
		steps.add(new AddHeatingConditions(context));
		steps.add(new AddSuperheatRules(context));
		steps.add(new DesugarStreams(context, false));
		steps.add(new ResolveFunctions(context));
		steps.add(new AddKCell(context));
        steps.add(new AddStreamCells(context));
		steps.add(new AddSymbolicK(context));
		steps.add(new AddSemanticEquality(context));
		// steps.add(new ResolveFresh());
		steps.add(new FreshCondToFreshVar(context));
		steps.add(new ResolveFreshVarMOS(context));
		steps.add(new AddTopCellConfig(context));
		if (GlobalSettings.addTopCell) {
		steps.add(new AddTopCellRules(context));
		}
		steps.add(new ResolveBinder(context));
		steps.add(new ResolveAnonymousVariables(context));
		steps.add(new ResolveBlockingInput(context, false));
		steps.add(new AddK2SMTLib(context));
		steps.add(new AddPredicates(context));
		steps.add(new ResolveSyntaxPredicates(context));
		steps.add(new ResolveBuiltins(context));
		steps.add(new ResolveListOfK(context));
		steps.add(new FlattenSyntax(context));
        steps.add(new InitializeConfigurationStructure(context));
		steps.add(new AddKStringConversion(context));
		steps.add(new AddKLabelConstant(context));
		steps.add(new ResolveHybrid(context));
		steps.add(new ResolveConfigurationAbstraction (context));
		steps.add(new ResolveOpenCells(context));
		steps.add(new ResolveRewrite(context));
        steps.add(new CompileDataStructures(context));

		if (GlobalSettings.sortedCells) {
			steps.add(new SortCells(context));
		}
		steps.add(new ResolveSupercool(context));
		steps.add(new AddStrictStar(context));
		steps.add(new AddDefaultComputational(context));
		steps.add(new AddOptionalTags(context));
		steps.add(new DeclareCellLabels(context));
		steps.add(new LastStep(this, context));
		return steps;
	}
}
