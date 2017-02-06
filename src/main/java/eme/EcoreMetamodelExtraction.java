package eme;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import eme.codegen.CodeGenerator;
import eme.codegen.GenModelGenerator;
import eme.extractor.JavaProjectExtractor;
import eme.generator.EcoreMetamodelGenerator;
import eme.generator.saving.SavingInformation;
import eme.model.IntermediateModel;
import eme.properties.ExtractionProperties;

/**
 * Main class for Ecore metamodel extraction.
 * @author Timur Saglam
 */
public class EcoreMetamodelExtraction {
    private static final Logger logger = LogManager.getLogger(EcoreMetamodelExtraction.class.getName());
    private final EcoreMetamodelGenerator generator;
    private final GenModelGenerator genModelGenerator;
    private final JavaProjectExtractor parser;
    private final ExtractionProperties properties;

    /**
     * Basic constructor. Builds {@link JavaProjectExtractor}, {@link EcoreMetamodelGenerator} and
     * {@link GenModelGenerator}.
     */
    public EcoreMetamodelExtraction() {
        logger.info("Started EME...");
        properties = new ExtractionProperties();
        parser = new JavaProjectExtractor();
        generator = new EcoreMetamodelGenerator(properties);
        genModelGenerator = new GenModelGenerator();
    }

    /**
     * Starts the Ecore metamodel extraction for a specific {@link IProject}. The {@link IProject} will be parsed and an
     * Ecore metamodel will be build and saved as an Ecore file. A {@link GenModel} will be build from the metamodel
     * which is then used to generate Ecore model code.
     * @param project is the specific {@link IProject} for the extraction.
     */
    public void extractAndGenerateFrom(IProject project) {
        EPackage metamodel = extractFrom(project); // extract metamodel from project
        SavingInformation information = generator.saveMetamodel(); // save model and store saving information
        GenModel genModel = genModelGenerator.generate(metamodel, information); // generate generator model
        CodeGenerator.generate(genModel);
    }

    /**
     * Starts the Ecore metamodel extraction for a specific {@link IProject}. The {@link IProject} will be parsed and an
     * Ecore metamodel will be build and saved as an Ecore file.
     * @param project is the specific {@link IProject} for the extraction.
     */
    public void extractAndSaveFrom(IProject project) {
        extractFrom(project); // extract metamodel from project
        generator.saveMetamodel(); // save metamodel
    }

    /**
     * Starts the Ecore metamodel extraction for a specific {@link IProject}. The {@link IProject} will be parsed and an
     * Ecore metamodel will be build.
     * @param project is the specific {@link IProject} for the extraction.
     * @return the Ecore metamodel with the default {@link EPackage} as root.
     */
    public EPackage extractFrom(IProject project) {
        logger.info("Started extraction of project " + project.getName());
        check(project); // check if valid.
        IJavaProject javaProject = JavaCore.create(project); // create java project
        IntermediateModel model = parser.buildIntermediateModel(javaProject);
        return generator.generateMetamodelFrom(model);
    }

    /**
     * Grants access to the {@link ExtractionProperties}.
     * @return the {@link ExtractionProperties}.
     */
    public ExtractionProperties getProperties() {
        return properties;
    }

    /**
     * Checks whether a specific {@link IProject} is valid (neither null nor nonexistent)
     * @param project is the specific {@link IProject}.
     */
    private void check(IProject project) {
        if (project == null) {
            throw new IllegalArgumentException("Project can't be null!");
        } else if (!project.exists()) {
            throw new IllegalArgumentException("Project " + project.toString() + "does not exist!");
        }
    }
}
