package edu.cmu.ml.proppr.graph;

import edu.cmu.ml.proppr.graph.LearningGraph.GraphFormatException;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.map.hash.TObjectDoubleHashMap;

public abstract class LearningGraphBuilder {
	public static final String FEATURE_INDEX_DELIM = ":";
	public static final String EDGE_FEATURE_DELIM = ":";
	public static final String SRC_DST_DELIM = "->";
	public static final String FEATURE_DELIM = ",";
	public static final String FEATURE_WEIGHT_DELIM = "@";
	
	public abstract LearningGraph create();
	public LearningGraph deserialize(String string) throws GraphFormatException {
		String[] parts = string.split("\t",4);
		if (parts.length != 4) {
			throw new GraphFormatException("Only "+parts.length+" tsv fields in graph; need 4 distinct parts:"+string);
		}

		String[] featureList;
		if(parts[2].equals("-")) featureList = new String[0];
		else featureList = parts[2].split(FEATURE_INDEX_DELIM);

		LearningGraph graph = create();
		for (String p : parts[3].split("\t")) {
			String[] pair = p.split(EDGE_FEATURE_DELIM);
			String edgeStr = pair[0], featStr = pair[1];

			String[] raw = edgeStr.split(SRC_DST_DELIM);
			int[] nodes = new int[raw.length];
			for (int i=0; i<raw.length; i++) { nodes[i] = Integer.parseInt(raw[i]); }

			TObjectDoubleMap<String> fd = new TObjectDoubleHashMap<String>();
			for (String f : featStr.split(FEATURE_DELIM)) {
				if (featureList.length > 0) {
					String[] fw = f.split(FEATURE_WEIGHT_DELIM);
					fd.put(featureList[Integer.parseInt(fw[0])-1],fw.length>1?Double.parseDouble(fw[1]):1.0);
				} else {
					fd.put(f,1.0);
				}
			}
			if (fd.isEmpty()) {
				throw new GraphFormatException("Can't have no features on an edge for ("+nodes[0]+", "+nodes[1]+")");
			}
			graph.addOutlink(nodes[0], new RWOutlink(fd, nodes[1]));
		}
		graph.freeze(); // might be slow? but saves memory
		return graph;
	}
}