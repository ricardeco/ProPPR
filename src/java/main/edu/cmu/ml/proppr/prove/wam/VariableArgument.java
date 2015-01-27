package edu.cmu.ml.proppr.prove.wam;

public class VariableArgument extends Argument {
	private int index;
	private String name;

	public VariableArgument(int i) {
		this.index = i;
		this.name = "X"+String.valueOf(this.index);
	}
	
	@Override
	public boolean isConstant() {
		return false;
	}
	
	@Override
	public int getValue() { return this.index; }
	@Override
	public String getName() { return this.name; }
	
//	@Override
//	public Argument getRenamed(int offset) {
//		return new VariableArgument(this.index-offset);
//	}
	public String toString() {
		return this.getName();//"v["+this.name+"]";//String.format("v[%s]",this.index);
	}
}