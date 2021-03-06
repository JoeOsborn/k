package org.kframework.krun;

import org.apache.commons.cli.*;
import org.kframework.utils.ActualPosixParser;

import java.util.ArrayList;
import java.util.Comparator;

public class CommandlineOptions {

	private Options options;
	private HelpFormatter help;
	private CommandLine cl;
    private ArrayList<Option> optionList = new ArrayList<Option>();
    
    //Comparator class needed to display the help options in the order they were created  
    static class OptionComparator implements Comparator<Object> {
		public int compare(Object obj1, Object obj2) {
			Option opt1 = (Option) obj1;
			Option opt2 = (Option) obj2;
			int index1 = new CommandlineOptions().getOptionList().indexOf(opt1);
			int index2 = new CommandlineOptions().getOptionList().indexOf(opt2);

			if (index1 > index2)
				return 1;
			else if (index1 < index2)
				return -1;
			else
				return 0;
		}
	}
    
    public CommandlineOptions() {
    	options = new Options();
		help = new HelpFormatter();
		
		if (K.debug || K.guidebug) {
			initializeDebugOptions();
		}
		else {
			initializeKRunOptions();
		}
    }
	
    //create options displayed in the krun help
	@SuppressWarnings("static-access")
	public void initializeKRunOptions() {
		//krun options
		
		// General options
		Option help1 = new Option("h", "help", false, "Display the detailed help message and quit");
		Option help2 = new Option("?", false, "Display the detailed help message and quit");
		Option version = new Option("v", "version", false, "Display the version number and quit");
		Option verbose = new Option("verbose", false, "Display the time each step takes");
		Option fastKast = new Option("fastKast", false, "Display the time each step takes");
		
		options.addOption(help1); getOptionList().add(help1);
		options.addOption(help2); getOptionList().add(help2);
		options.addOption(version); getOptionList().add(version);
		options.addOption(verbose); getOptionList().add(verbose);
		options.addOption(fastKast); getOptionList().add(fastKast);
		
		// Common K options
		Option pgm = OptionBuilder.hasArg(true).withArgName("FILE").withLongOpt("pgm").withDescription("Name of the program to execute").create();
		Option k_definition = OptionBuilder.hasArg(true).withArgName("FILE").withLongOpt("k-definition").withDescription("Path to the K definition").create();
		Option main_module = OptionBuilder.hasArg(true).withArgName("STRING").withLongOpt("main-module").withDescription("Module the program should execute in").create();
		Option syntax_module = OptionBuilder.hasArg(true).withArgName("STRING").withLongOpt("syntax-module").withDescription("Name of the syntax module").create();
		Option parser = OptionBuilder.hasArg(true).withArgName("STRING").withLongOpt("parser").withDescription("Command used to parse programs (default: kast)." + K.lineSeparator + "You need to specify the path where you parser" + K.lineSeparator + "is located on the disk").create();
		Option cfg_parser = OptionBuilder.hasArg(true).withArgName("STRING").withLongOpt("cfg-parser").withDescription("Command used to parse configuration variables (default: kast --def -e)." + K.lineSeparator + "See --parser above.  Applies to subsequent -c options until another parser is specified with this option").create();
		Option io = OptionBuilder.hasArg(false).withLongOpt("io").withDescription("Use real IO when running the definition").create();
		Option no_io = OptionBuilder.hasArg(false).withLongOpt("no-io").create();
		Option statistics = OptionBuilder.hasArg(false).withLongOpt("statistics").withDescription("Print Maude's rewrite statistics").create();
		Option no_statistics = OptionBuilder.hasArg(false).withLongOpt("no-statistics").create();
		Option color = OptionBuilder.hasArg(false).withLongOpt("color").withDescription("Use colors in output").create();
		Option no_color = OptionBuilder.hasArg(false).withLongOpt("no-color").create();
		Option parens = OptionBuilder.hasArg(false).withLongOpt("parens").withDescription("Show parentheses in output").create();
		Option no_parens = OptionBuilder.hasArg(false).withLongOpt("no-parens").create();
		Option term = OptionBuilder.hasArg(true).withArgName("STRING").withLongOpt("term").withDescription("Input argument will be parsed with the specified parser and used as the sole input to krun").create();


		options.addOption(pgm); getOptionList().add(pgm);
		options.addOption(k_definition); getOptionList().add(k_definition);
		options.addOption(main_module); getOptionList().add(main_module);
		options.addOption(syntax_module); getOptionList().add(syntax_module);
		options.addOption(parser); getOptionList().add(parser);
		options.addOption(cfg_parser); getOptionList().add(cfg_parser);
		options.addOption(io); getOptionList().add(io);
		options.addOption(no_io); getOptionList().add(no_io);
		options.addOption(statistics); getOptionList().add(statistics);
		options.addOption(no_statistics); getOptionList().add(no_statistics);
		options.addOption(color); getOptionList().add(color);
		options.addOption(no_color); getOptionList().add(no_color);
		options.addOption(parens); getOptionList().add(parens);
		options.addOption(no_parens); getOptionList().add(no_parens);
		options.addOption(term); getOptionList().add(term);


		// Advanced K options
		Option compiled_def = OptionBuilder.hasArg(true).withArgName("FILE").withLongOpt("compiled-def").withDescription("Path to the compiled K definition").create();
		Option do_search = OptionBuilder.hasArg(false).withLongOpt("do-search").withDescription("Search for all possible results").create();
		Option no_do_search = OptionBuilder.hasArg(false).withLongOpt("no-do-search").create();
		Option maude_cmd = OptionBuilder.hasArg(true).withArgName("STRING").withLongOpt("maude-cmd").withDescription("Maude command used to execute the definition").create();
		/*Option xsearch_pattern = OptionBuilder.withLongOpt("xsearch-pattern")
				.withDescription("Search pattern. In conjunction with it you can specify 2 options that are optional: bound (the number of desired solutions) and depth (the maximum depth of the search)")
				.hasArg().withArgName("STRING").create();*/   
		Option pattern = OptionBuilder.withLongOpt("pattern").withDescription("The pattern used for search. In conjunction" + K.lineSeparator + "with it you can specify other 2 options that" + K.lineSeparator + "are optional: bound (the number of desired" + K.lineSeparator + "solutions) and depth (the maximum depth of the" + K.lineSeparator + "search)")
				.hasArg().withArgName("STRING").create();
		Option bound = OptionBuilder.hasArg(true).withArgName("STRING").withLongOpt("bound").withDescription("The number of desired solutions for search").create();
		Option depth = OptionBuilder.hasArg(true).withArgName("STRING").withLongOpt("depth").withDescription("The maximum depth of the search").create();
        Option graph = OptionBuilder.hasArg(false).withLongOpt("graph").withDescription("Displays the search graph generated by the last search").create();
		Option output_mode = OptionBuilder.hasArg(true).withArgName("STRING").withLongOpt("output-mode").withDescription("How to display Maude results (none, raw," + K.lineSeparator + "pretty, binary)").create();
		Option log_io = OptionBuilder.hasArg(false).withLongOpt("log-io").withDescription("Tell the IO server to create logs").create();
		Option no_log_io = OptionBuilder.hasArg(false).withLongOpt("no-log-io").create();
		Option output = OptionBuilder.hasArg(true).withArgName("FILE").withLongOpt("output").withDescription("Save the output of krun in the file given as argument").create();

		options.addOption(compiled_def); getOptionList().add(compiled_def);
		options.addOption(do_search); getOptionList().add(do_search);
		options.addOption(no_do_search); getOptionList().add(no_do_search);
		options.addOption(maude_cmd); getOptionList().add(maude_cmd);
		//options.addOption(xsearch_pattern); getOptionList().add(xsearch_pattern);
		options.addOption(pattern); getOptionList().add(pattern);
		options.addOption(bound); getOptionList().add(bound);
		options.addOption(depth); getOptionList().add(depth);
		options.addOption(graph); getOptionList().add(graph);
		options.addOption(output_mode); getOptionList().add(output_mode);
		options.addOption(log_io); getOptionList().add(log_io);
		options.addOption(no_log_io); getOptionList().add(no_log_io);
		options.addOption(output); getOptionList().add(output);
		
		//for group options
		Option search = OptionBuilder.hasArg(false).withLongOpt("search").withDescription("In conjunction with it you can specify 3" + K.lineSeparator + "options that are optional: pattern (the pattern" + K.lineSeparator + "used for search), bound (the number of desired" + K.lineSeparator + "solutions) and depth (the maximum depth of the" + K.lineSeparator + "search)").create();
		Option search_final = OptionBuilder.hasArg(false).withLongOpt("search-final").withDescription("Same as --search but only return final states, even if --depth is provided").create();
		Option search_all = OptionBuilder.hasArg(false).withLongOpt("search-all").withDescription("Same as --search but return all matching states, even if --depth is not provided").create();
		Option search_one_step = OptionBuilder.hasArg(false).withLongOpt("search-one-step").withDescription("Same as --search but search only transition step").create();
		Option search_one_or_more_steps = OptionBuilder.hasArg(false).withLongOpt("search-one-or-more-steps").withDescription("Same as --search-all but exclude initial state, even if it matches").create();
		Option config = OptionBuilder.hasArg(false).withLongOpt("config").create();
		Option no_config = OptionBuilder.hasArg(false).withLongOpt("no-config").create();
		
		options.addOption(search); getOptionList().add(search);
		options.addOption(search_final); getOptionList().add(search_final);
		options.addOption(search_all); getOptionList().add(search_all);
		options.addOption(search_one_step); getOptionList().add(search_one_step);
		options.addOption(search_one_or_more_steps); getOptionList().add(search_one_or_more_steps);
		options.addOption(config); getOptionList().add(config);
		options.addOption(no_config); getOptionList().add(no_config);
		
		//for deleting temporary folders created by krun at previous executions that couldn't be renamed into "krun" because some errors occurred
		Option deleteTempDir = OptionBuilder.hasArg(false).withLongOpt("deleteTempDir").withDescription("Delete temporary folders created by krun at" + K.lineSeparator + "previous executions").create();
		Option no_deleteTempDir = OptionBuilder.hasArg(false).withLongOpt("no-deleteTempDir").withDescription("Do not delete temporary folders created by krun at previous executions").create();
		options.addOption(deleteTempDir); getOptionList().add(deleteTempDir);
		options.addOption(no_deleteTempDir); getOptionList().add(no_deleteTempDir);
		
		// for debugger
	    Option debug = OptionBuilder.hasArg(false).withLongOpt("debug").withDescription("Run an execution in debug mode").create();
	    Option guiDebug = OptionBuilder.hasArg(false).withLongOpt("debug-gui").withDescription("Run an execution in debug mode with graphical interface").create();
		Option trace = OptionBuilder.hasArg(false).withLongOpt("trace").withDescription("Turn on maude trace").create();
		Option profile = OptionBuilder.hasArg(false).withLongOpt("profile").withDescription("Turn on maude profiler").create();
		options.addOption(trace); getOptionList().add(trace);
		options.addOption(profile); getOptionList().add(profile);

		options.addOption(debug); getOptionList().add(debug);
		options.addOption(guiDebug); getOptionList().add(guiDebug);
		//options.addOption(trace); getOptionList().add(trace);
		
		//for LTL model-checking
		Option model_checking = OptionBuilder.hasArg(true).withArgName("FILE/STRING").withLongOpt("ltlmc").withDescription("Specify the formula for model checking through" + K.lineSeparator + "a file or at commandline").create();
		options.addOption(model_checking); getOptionList().add(model_checking);

		//for configuration variables
		Option configuration_variables = OptionBuilder.withArgName("name=value").hasArgs(2).withValueSeparator().withDescription("Specify values for variables in the configuration").create("c");
		options.addOption(configuration_variables); getOptionList().add(configuration_variables);

		Option backend = OptionBuilder.hasArg(true).withArgName("STRING").withLongOpt("backend").withDescription("Specify the krun backend to execute with").create();
		options.addOption(backend); getOptionList().add(backend);

        Option prove = OptionBuilder.hasArg(true).withArgName("FILE").withLongOpt("prove").withDescription("Prove a set of reachability rules").create();
        options.addOption(prove); getOptionList().add(prove);

        // java rewrite engine specific options
        Option no_smt = OptionBuilder.hasArg(false).withLongOpt("no-smt").withDescription("do not use SMT solvers for checking constraints").create();
        options.addOption(no_smt); getOptionList().add(no_smt);

        //test generation options
        Option testgen = OptionBuilder.hasArg(false).withLongOpt("generate-tests").withDescription("Test programs will be generated along with normal search").create();
        options.addOption(testgen); getOptionList().add(testgen);
	}
	
	//create options displayed in the krun debugger help
	@SuppressWarnings("static-access")
	public void initializeDebugOptions() {
		//stepper options
		Option step = OptionBuilder.hasArg(false).withLongOpt("step").withDescription("Execute one step or multiple steps at one time if you specify a positive integer argument").create();
		Option stepAll = OptionBuilder.hasArg(false).withLongOpt("step-all").withDescription("Computes all successors in one or more transitions (if you specify a positive integer argument) of the current configuration").create();
		Option select = OptionBuilder.hasArg(true).withArgName("number").withLongOpt("select").withDescription("Selects the current configuration after a search result using step-all command").create();
		Option showSearchGraph = OptionBuilder.hasArg(false).withLongOpt("show-search-graph").withDescription("Displays the search graph generated by the last search").create();
		Option showNode = OptionBuilder.hasArg(true).withArgName("NODE").withLongOpt("show-node").withDescription("Displays info about the specified node in the search graph." + K.lineSeparator + "The node is specified by its id indicated as argument of this option").create();
		Option showEdge = OptionBuilder.hasArgs(2).withArgName("NODE1 NODE2").withLongOpt("show-edge").withDescription("Displays info about the specifiede edge in the search graph." + K.lineSeparator + "The edge is specified by the ids of the endpoints indicated as argument of this option").create();
		Option resume = OptionBuilder.hasArg(false).withLongOpt("resume").withDescription("Resume the execution and exit from the debug mode").create();
		Option abort = OptionBuilder.hasArg(false).withLongOpt("abort").withDescription("Abort the execution and exit from the debug mode").create();
		Option save = OptionBuilder.hasArg(true).withArgName("STRING").withLongOpt("save").withDescription("Save the debug session to file").create();
		Option load = OptionBuilder.hasArg(true).withArgName("STRING").withLongOpt("load").withDescription("Load the debug session from file").create();
		Option help = OptionBuilder.hasArg(false).withLongOpt("help").withDescription("Display the available commands").create();
		Option read = OptionBuilder.hasArg(true).withArgName("STRING").withLongOpt("read")
			.withDescription("Read a string from stdin").create();
		options.addOption(step); getOptionList().add(step);
		options.addOption(stepAll); getOptionList().add(stepAll);
		options.addOption(select); getOptionList().add(select);
		options.addOption(showSearchGraph); getOptionList().add(showSearchGraph);
		options.addOption(showNode); getOptionList().add(showNode);
		options.addOption(showEdge); getOptionList().add(showEdge);
		options.addOption(resume); getOptionList().add(resume);
		options.addOption(abort); getOptionList().add(abort);
		options.addOption(save); getOptionList().add(save);
		options.addOption(load); getOptionList().add(load);
		options.addOption(read); getOptionList().add(read);
		options.addOption(help); getOptionList().add(help);
	}

	public CommandLine parse(String[] cmd) {
		CommandLineParser parser = new ActualPosixParser();
		try {
			setCommandLine(parser.parse(options, cmd));
			return getCommandLine();
		} catch (ParseException e) {
			System.out.println("Error while parsing commandline:" + e.getMessage());
		}

		return null;
	}

	public Options getOptions() {
		return options;
	}

	public HelpFormatter getHelp() {
		return help;
	}

	CommandLine getCommandLine() {
		return cl;
	}

	void setCommandLine(CommandLine cl) {
		this.cl = cl;
	}
	
	public ArrayList<Option> getOptionList() {
		return optionList;
	}

	public void setOptionList(ArrayList<Option> optionList) {
		this.optionList = optionList;
	}
	
}
