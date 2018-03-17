package com.voter.info.app;

import java.util.Comparator;

/**
 * A <code>Comparator<T></code> which is used to sort either
 * district names or assembly constituency names.
 * 
 * @author Shyam | @sbaitmangalkar | catch.shyambaitmangalkar@gmail.com
 *
 */
public class DistrictAndAssemblyConstituencySorter implements Comparator<String> {

	@Override
	public int compare(String assemblyConstituency1, String assemblyConstituency2) {
		if (assemblyConstituency1.compareTo(assemblyConstituency2) > 0)
			return 1;
		else if (assemblyConstituency1.compareTo(assemblyConstituency2) < 0)
			return -1;
		else
			return 0;
	}

}
