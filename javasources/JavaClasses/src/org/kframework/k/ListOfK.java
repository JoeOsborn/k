package org.kframework.k;

import org.kframework.exceptions.TransformerException;
import org.kframework.visitors.Transformer;
import org.kframework.visitors.Visitor;
import org.w3c.dom.Element;


public class ListOfK extends Collection {
	public ListOfK() {
		super("List{K}");
	}
	
	public ListOfK(String location, String filename) {
		super(location, filename, "List{K}");
	}

	public ListOfK(Element element) {
		super(element);
	}

	public ListOfK(ListOfK node) {
		super(node);
	}

	@Override
	public String toString() {
		String content = "";
		for (int i = 0; i < contents.size(); i++) {
			if (i == contents.size() - 1)
				content += contents.get(i);
			else
				content += contents.get(i) + ",, ";
		}
		return content;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	@Override
	public ASTNode accept(Transformer visitor) throws TransformerException {
		return visitor.transform(this);
	}
	
	@Override
	public ListOfK shallowCopy() {
		return new ListOfK(this);
	}
}