package org.kframework.kil;

public abstract class DefinitionItem extends ASTNode {

	/** set iff the item was read from a file in the standard libraries */
	protected boolean predefined;

	public DefinitionItem() {
		super();
	}

	public DefinitionItem(DefinitionItem di) {
		super(di);
	}

	public void setPredefined(boolean predefined) {
		this.predefined = predefined;
	}

	public boolean isPredefined() {
		return predefined;
	}
}
