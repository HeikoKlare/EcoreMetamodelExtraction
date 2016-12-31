package eme.parser;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

import eme.model.ExtractedAttribute;
import eme.model.ExtractedDataType;
import eme.model.ExtractedParameter;

/**
 * Helper class to deal with type signatures.
 * @author Timur Saglam
 */
public abstract class DataTypeParser {

    /**
     * Creates extracted attribute from a field and its type.
     * @param field is the field.
     * @param iType is the type of the field.
     * @return the extracted attribute of the field.
     * @throws JavaModelException if there are problems with the JDT API.
     */
    public static ExtractedAttribute parseField(IField field, IType iType) throws JavaModelException {
        String signature = field.getTypeSignature(); // get return type signature
        int arrayCount = Signature.getArrayCount(signature);
        String name = field.getElementName(); // name of the field
        ExtractedAttribute attribute = new ExtractedAttribute(name, getFullName(signature, iType), arrayCount);
        attribute.setGenericArguments(parseGenericTypes(signature, iType));
        return attribute;
    }

    /**
     * Creates extracted method parameter from a parameter and its method.
     * @param parameter is the parameter.
     * @param iMethod is the method of the parameter.
     * @return the extracted method parameter.
     * @throws JavaModelException if there are problems with the JDT API.
     */
    public static ExtractedParameter parseParameter(ILocalVariable parameter, IMethod iMethod) throws JavaModelException {
        String signature = parameter.getTypeSignature(); // get return type signature
        String name = parameter.getElementName(); // name of the parameter
        IType declaringType = iMethod.getDeclaringType(); // declaring type of the method
        int arrayCount = Signature.getArrayCount(signature); // amount of array dimensions
        ExtractedParameter variable = new ExtractedParameter(name, getFullName(signature, declaringType), arrayCount);
        variable.setGenericArguments(parseGenericTypes(signature, declaringType));
        return variable;
    }

    /**
     * Creates extracted return type from a method.
     * @param iMethod is the method.
     * @return the return type, or null if it is void.
     * @throws JavaModelException if there are problems with the JDT API.
     */
    public static ExtractedDataType parseReturnType(IMethod iMethod) throws JavaModelException {
        String signature = iMethod.getReturnType(); // get return type signature
        if (Signature.SIG_VOID.equals(signature)) {
            return null; // void signature, no return type.
        }
        return parseDataType(signature, iMethod.getDeclaringType());
    }

    /**
     * Returns the full name of a signature and the declaring type, e.g "java.lang.String", "java.util.List" or "char".
     */
    private static String getFullName(String signature, IType declaringType) throws JavaModelException {
        String simpleName = Signature.getSignatureSimpleName(Signature.getElementType(signature));
        String[][] resolvedType = declaringType.resolveType(simpleName); // resolve type
        if (resolvedType != null && resolvedType[0] != null) {
            return Signature.toQualifiedName(resolvedType[0]);
        } else if (simpleName.contains("<")) {
            return simpleName.substring(0, simpleName.indexOf('<'));
        }
        return simpleName;
    }

    /**
     * Creates extracted data type from a signature and a declaring type.
     */
    private static ExtractedDataType parseDataType(String signature, IType declaringType) throws JavaModelException {
        int arrayCount = Signature.getArrayCount(signature);
        ExtractedDataType dataType = new ExtractedDataType(getFullName(signature, declaringType), arrayCount);
        dataType.setGenericArguments(parseGenericTypes(signature, declaringType));
        return dataType;
    }

    /**
     * Parses generic types from type signature and returns them in a list.
     */
    private static List<ExtractedDataType> parseGenericTypes(String signature, IType declaringType) throws JavaModelException {
        List<ExtractedDataType> genericTypes = new LinkedList<ExtractedDataType>();
        for (String genericTypeSignature : Signature.getTypeArguments(signature)) {
            genericTypes.add(parseDataType(genericTypeSignature, declaringType));
        }
        return genericTypes;
    }
}