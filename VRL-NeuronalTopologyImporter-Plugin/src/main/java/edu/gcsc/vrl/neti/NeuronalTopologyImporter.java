/// package's name
package edu.gcsc.vrl.neti;

/// imports
import edu.gcsc.vrl.ug.api.I_NeuronalTopologyImporter;
import edu.gcsc.vrl.ug.api.NeuronalTopologyImporterProvider;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import java.io.File;
import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.system.VMessage;
import java.io.Serializable;
import org.apache.commons.io.FilenameUtils;

/**
 * @brief NeuronalTopologyImporter component for VRL-Studio
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
@ComponentInfo(name="NeuronalTopologyImporter", category="/UG4/VRL-Plugins/Neuro/NeuronalTopologyImporter")
public class NeuronalTopologyImporter implements Serializable {
	/// private members
	private String selection = "";
	private boolean correctExp2Syn;
	private boolean correctAlphaSyn;
	private I_NeuronalTopologyImporter importer;
	
	/// private static members
	private static final long serialVersionUID = 1L;
	
	/**
	 * @brief optionally prepare the importer (to be deprecated)
	 * Preselect a file type and enable synapse corrections
	 * 
	 * @param selection 
	 * @param correctExp2Syn
	 * @param correctAlphaSyn
	 */
	@MethodInfo(name="Advanced options", hide=true)
	public void advanced_options(
		@ParamInfo(name  = "File type", typeName = "Filetype", 
			   style = "selection", options = "value=[\"NGX\", \"HOC\", \"SWC\"]")
		String selection,
		
		@ParamInfo(name    = "Correct Exp2Syn", typeName = "Corrects the Exp2Syns")
		boolean correctExp2Syn,
		
		@ParamInfo(name    = "Correct AlphaSyn", typeName = "Corrects the AlphaSyns")
		boolean correctAlphaSyn

	)  {
		this.selection = selection;
		this.correctExp2Syn = correctExp2Syn;
		this.correctAlphaSyn = correctAlphaSyn;
	}
	
	/**
	 * @brief import the geometry (either .hoc or .swc. or .txt. or .ngx)
	 * File is a path on local filesystem which is schedule for import
	 * @param file 
	 */
	@MethodInfo(name="Import Geometry", hide=false)
	public void import_geometry(
	@ParamInfo(name  = "Input file", typeName = "Location of the input file", 
		   style = "load-dialog", options = "endings=[\"ngx\", "
			   + "\"hoc\", \"swc\", \"txt\"]; description=\"NGX (NeuGen XML),"
			   + "HOC (NEURON), SWC (NeuroMorpho) or TXT (NeuGen TXT) files\"")
	File file
	) {
		if(file.exists() && !file.isDirectory()) {
			importer = new NeuronalTopologyImporterProvider().getDefaultNeuronalTopologyImporter();
		
			importer.correct_alpha_synapses(this.correctAlphaSyn);
			importer.correct_exp2_synapses(this.correctExp2Syn);
			String extension = FilenameUtils.getExtension(file.toString());
			System.err.println("extension");
			if (selection.isEmpty()) {
				System.err.println("ngx!");
				if ("ngx".equalsIgnoreCase(extension)) {
					importer.import_ngx(file.toString());
				} else if ("hoc".equalsIgnoreCase(extension)) {
					VMessage.warning("NeuronalTopologyImporter", "NEURON file type (.hoc) currently not supported.");
				} else if ("swc".equalsIgnoreCase(extension)) {
					System.err.println("swc!");
					importer.import_geometry(file.toString(), extension.toLowerCase());
				} else if ("txt".equalsIgnoreCase(extension)) {
					System.err.println("txt!");
					VMessage.warning("NeuronalTopologyImporter", "NeuGen TXT file type (.txt) currently not supported.");
					/// importer.import_txt(file.toString(), selection.toLowerCase());
				}
			} else {
				if ("ngx".equalsIgnoreCase(selection)) {
					importer.import_ngx(file.toString());
				} else if ("hoc".equalsIgnoreCase(selection)) {
					VMessage.warning("NeuronalTopologyImporter", "NEURON file type (.hoc) currently not supported.");
				} else if ("swc".equalsIgnoreCase(selection)) {
					importer.import_geometry(file.toString(), selection.toLowerCase());
				}  else if ("txt".equalsIgnoreCase(selection)) {
					System.err.println("txt!");
					VMessage.warning("NeuronalTopologyImporter", "NeuGen TXT file type (.txt) currently not supported.");
					importer.import_txt(file.toString(), selection.toLowerCase());
				}
			}
		} else {
			VMessage.warning("NeuronalTopologyImporter", "Input file not found.");
		}
	}
	
	/**
	 * @brief generated and writes the ug4 compatible computational grid
	 * Note that the grid is stored in the same place where the input
	 * geometry is stored and written with the same basename but with
	 * the ug4 extension for grids .ugx
	 * 
	 */
	@MethodInfo(name="Import Geometry", hide=false)
	public void generate_grid() {
		importer.generate_grid();
	}
}
