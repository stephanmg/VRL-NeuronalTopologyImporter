/// package's name
package edu.gcsc.vrl.neti;

/// imports
import eu.mihosoft.vrl.visual.VFilter;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @brief NeuronalTopologyImporter menu filter
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public class NeuronalTopologyImporterVFilter implements VFilter {
	/**
	 * @brief default ctor
	 */
	public NeuronalTopologyImporterVFilter() {

	}

	/**
	 * @brief matcher
	 * @param o
	 * @return
	 */
	@Override
	public boolean matches(Object o) {

		if (!(o instanceof DefaultMutableTreeNode)) {
			return false;
		}

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) o;

		if (node.getUserObject() == null
			|| !(node.getUserObject() instanceof Class<?>)) {
			return false;
		}

		/*
		boolean ugClass = UGObjectUtil.isUGAPIClass(cls);
		boolean wrapperClass = UGObjectUtil.isWrapperClass(cls);
		boolean constClass = UGObjectUtil.isConstClass(cls);
		boolean groupRoot = UGObjectUtil.isGroupRoot(cls);
		boolean groupChild = UGObjectUtil.isGroupChild(cls);
		*/
		
		Class<?> cls = (Class<?>) node.getUserObject();
		return "NeuronalTopologyImporterProvider".equals(cls.getName());
	}

	/**
	 * @brief returns the name of the filter
	 * @return
	 */
	@Override
	public String getName() {
		return "Default NETI component filter";
	}

	/**
	 * @brief returns true if hidden when matching
	 * @return
	 */
	@Override
	public boolean hideWhenMatching() {
		return true;
	}
}
