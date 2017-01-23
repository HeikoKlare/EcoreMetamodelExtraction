package eme.codegen;

import org.eclipse.emf.codegen.ecore.generator.Generator;
import org.eclipse.emf.codegen.ecore.generator.GeneratorAdapterFactory;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelPackage;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenBaseGeneratorAdapter;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenModelGeneratorAdapterFactory;
import org.eclipse.emf.common.util.BasicMonitor;

/**
 * Class for code generation (e.g generating Java code from Ecore GenModels).
 */
public abstract class CodeGenerator {

    /**
     * Uses a specific GenModel to generate Java Code.
     * @param genModel is the specific GenModel.
     */
    public static void generate(GenModel genModel) {
        // Globally register the default generator adapter factory for GenModel elements (only needed in stand-alone):
        GeneratorAdapterFactory.Descriptor.Registry.INSTANCE.addDescriptor(GenModelPackage.eNS_URI, GenModelGeneratorAdapterFactory.DESCRIPTOR);
        // Create the generator and set the model-level input object:
        Generator generator = new Generator();
        generator.setInput(genModel);
        // Generator model code:
        generator.generate(genModel, GenBaseGeneratorAdapter.MODEL_PROJECT_TYPE, new BasicMonitor.Printing(System.out));
    }
}