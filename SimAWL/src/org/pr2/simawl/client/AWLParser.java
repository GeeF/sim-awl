package org.pr2.simawl.client;

import java.util.HashMap;

import org.pr2.simawl.client.Conjunction;

import com.google.gwt.core.client.GWT;

public class AWLParser {
	private boolean VKE = false;
	private boolean input[];
	private boolean output[];
	private boolean error = false;

	public AWLParser(int i, int o) {
		input = new boolean[i];
		output = new boolean[o + 2]; // add 2 for error and VKE return
	}

	public void conjunction(HashMap<Integer, Conjunction> map) {
		conjunctionRecursive(map, 0);
	}

	private void conjunctionRecursive(HashMap<Integer, Conjunction> map, int run) {
		Conjunction con = map.get(run);
		// just load
		if (run == 0) {
			switch (con.op) {
			// load
			case U:
			case O:
			case X:
				VKE = con.value;
				break;
			// neg load
			case UN:
			case ON:
			case XN:
				VKE = !con.value;
				break;
			}
		}
		// concatenate
		else {
			switch (con.op) {
			case U:
				VKE = VKE & con.value;
				break;
			case UN:
				VKE = VKE & !con.value;
				break;
			case O:
				VKE = VKE | con.value;
				break;
			case ON:
				VKE = VKE | !con.value;
				break;
			case X:
				VKE = VKE ^ con.value;
				break;
			case XN:
				VKE = VKE ^ !con.value;
				break;
			default:
				break;
			}
		}
		if (run < map.size() - 1) {
			conjunctionRecursive(map, run + 1);
		}
	}

	private boolean readValue(String io, String num) {
		if (io.equals("E")) {
			GWT.log("Value read at E "
					+ io
					+ num
					+ ":  "
					+ this.input[Integer.parseInt(Character.toString(num
							.charAt(2)))]);
			return this.input[Integer
					.parseInt(Character.toString(num.charAt(2)))];
		} else {
			this.error = true;
			return true;
		}
	}

	private void writeValue(String io, String num, boolean value) {
		if (io.equals("A")) {
			this.output[Integer.parseInt(Character.toString(num.charAt(2)))] = value;
		}
	}

	public boolean[] parse(String s, boolean[] input) {
		// set input
		this.input = input;
		this.error = false;

		String log = new String();
		for (int i = 0; i < input.length; i++) {
			log += i + ":" + String.valueOf(input[i]) + "-";
		}
		GWT.log("Input: " + log);

		HashMap<Integer, Conjunction> map = new HashMap<Integer, Conjunction>();
		int i = 0;

		BufferedReader reader = new BufferedReader(new StringReader(s));

		try {
			String line;
			while ((line = reader.readLine()) != null) {
				String tokens[] = line.split(" ");
				for (int t = 0; t < tokens.length; t++) {
					String operator = tokens[t];
					t++;
					if (operator.equals("=")) {
						conjunction(map);
						writeValue(tokens[t++], tokens[t++], VKE);
					} else if (operator.equals("R")) {
						writeValue(tokens[t++], tokens[t++], false);
					} else if (operator.equals("S")) {
						writeValue(tokens[t++], tokens[t++], true);
					} else {
						map.put(i,
								new Conjunction(readValue(tokens[t++],
										tokens[t++]), Operation
										.valueOf(operator)));
						i++;
					}
				}
			}

		} catch (Exception e) {
			this.error = true;
		}

		// second to last field is error flag
		this.output[this.output.length - 2] = this.error;
		// last field in output is VKE
		this.output[this.output.length - 1] = this.VKE;
		return this.output;
	}
}
