package org.pr2.simawl.client;
import org.pr2.simawl.client.Operation;

public class Conjunction {
	public boolean value;
	public Operation op;
	public Conjunction(boolean value, Operation op) {
		this.value = value;
		this.op = op;
	}
}
