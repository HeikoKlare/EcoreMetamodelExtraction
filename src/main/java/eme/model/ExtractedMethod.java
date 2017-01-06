package eme.model;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.IMethod;

import eme.model.datatypes.AccessLevelModifier;
import eme.model.datatypes.ExtractedDataType;
import eme.model.datatypes.ExtractedParameter;

/**
 * Represents a method of a class in the model.
 * @author Timur Saglam
 */
public class ExtractedMethod extends ExtractedElement {
    private boolean abstractMethod;
    private final boolean constructor;
    private final List<ExtractedDataType> exceptions;
    private IMethod iMethod;
    private AccessLevelModifier modifier;
    private final List<ExtractedParameter> parameters;
    private final ExtractedDataType returnType;
    private boolean staticMethod;

    /**
     * Basic constructor. Sets access level modifier to NO_MODIFIER, static and abstract to false;
     * @param fullName is the full name of the method, consisting out of the full class name and the method name.
     * @param returnType is the data type for the return type of the method. null if it is void.
     * @param constructor determines whether this method is an constructor.
     */
    public ExtractedMethod(String fullName, ExtractedDataType returnType, boolean constructor) {
        super(fullName);
        parameters = new LinkedList<ExtractedParameter>();
        exceptions = new LinkedList<ExtractedDataType>();
        this.returnType = returnType;
        this.constructor = constructor;
        modifier = AccessLevelModifier.NO_MODIFIER;
        staticMethod = false;
        abstractMethod = false;
    }

    /**
     * Constructor that also takes the JDT representation. Sets access level modifier to NO_MODIFIER, static and
     * abstract to false;
     * @param fullName is the full name of the method, consisting out of the full class name and the method name.
     * @param returnType is the data type for the return type of the method. null if it is void.
     * @param constructor determines whether this method is an constructor.
     * @param iMethod is the JDT representation.
     */
    public ExtractedMethod(String fullName, ExtractedDataType returnType, boolean constructor, IMethod iMethod) {
        this(fullName, returnType, constructor);
        this.iMethod = iMethod;
    }

    /**
     * Adds a parameter to the method.
     * @param parameter is the new parameter.
     */
    public void addParameter(ExtractedParameter parameter) {
        parameters.add(parameter);
    }

    /**
     * Adds a throws declaration to the method.
     * @param exception is the throws declaration.
     */
    public void addThrowsDeclaration(ExtractedDataType exception) {
        exceptions.add(exception);
    }

    /**
     * Getter for the JDT representation.
     * @return the JDT representation, an IMethod.
     */
    public IMethod getJDTRepresentation() {
        return iMethod;
    }

    /**
     * Getter for the access level modifier.
     * @return the access level modifier of the method.
     */
    public AccessLevelModifier getModifier() {
        return modifier;
    }

    /**
     * getter for the parameters.
     * @return the parameters
     */
    public List<ExtractedParameter> getParameters() {
        return parameters;
    }

    /**
     * getter for the return type;
     * @return the returnType
     */
    public ExtractedDataType getReturnType() {
        return returnType;
    }

    /**
     * getter for the throws declarations.
     * @return the throws declarations
     */
    public List<ExtractedDataType> getThrowsDeclarations() {
        return exceptions;
    }

    /**
     * Checks whether the method is abstract.
     * @return true if it is abstract.
     */
    public boolean isAbstract() {
        return abstractMethod;
    }

    /**
     * Checks whether the method is a constructor.
     * @return true if it is a constructor.
     */
    public boolean isConstructor() {
        return constructor;
    }

    /**
     * Checks whether the method is static.
     * @return true if it is static.
     */
    public boolean isStatic() {
        return staticMethod;
    }

    /**
     * Sets the flags of the method. That means whether the method is static, whether the method is abstract and what
     * access level modifier it has.
     * @param modifier is the access level modifier.
     * @param staticMethod determines whether the method is static or not.
     * @param abstractMethod determines whether the method is abstract or not.
     */
    public void setFlags(AccessLevelModifier modifier, boolean staticMethod, boolean abstractMethod) {
        this.modifier = modifier;
        this.staticMethod = staticMethod;
        this.abstractMethod = abstractMethod;
    }
}