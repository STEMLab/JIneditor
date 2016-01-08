package edu.pnu.visitor;

public interface IndoorGMLElement {
	void accept(IndoorGMLElementVisitor visitor);
}
