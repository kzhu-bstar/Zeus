package pig.dream.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * @author zhukun on 2017/4/15.
 */

public class AnnotatedClass {

    private static final String ROOT_PACKAGE_NAME = "pig.dream.zeuslibs.inject";

    public TypeElement mClassElement;
    public Elements mElementUtils;

    private List<BindViewField> bindViewFields;
    private List<BindIntentField> bindIntentFields;

    public AnnotatedClass(TypeElement classElement, Elements mElementUtils) {
        this.mClassElement = classElement;
        this.mElementUtils = mElementUtils;
    }

    // omit some easy methods
    public JavaFile generateActivityCoreClass() {

        // method inject(final T host, Object source, Provider provider)
        MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mClassElement.asType()), "host", Modifier.FINAL)
                .addParameter(ClassName.get("android.view", "View"), "source");

        if (bindViewFields != null && bindViewFields.size() != 0) {
            for (BindViewField field : bindViewFields) {
                // find views
                injectMethodBuilder.addStatement("host.$N = ($T)(source.findViewById($L))", field.getFieldName(),
                        ClassName.get(field.getFieldType()), field.getResId());
            }
        }
        // method  void parseIntent(T host, Bundle savedInstanceState, Intent intent);
        MethodSpec.Builder parseIntentBuilder = MethodSpec.methodBuilder("parseIntent")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mClassElement.asType()), "host", Modifier.FINAL)
                .addParameter(ClassName.get("android.os", "Bundle"), "savedInstanceState")
                .addParameter(ClassName.get("android.content", "Intent"), "intent");

        if (bindIntentFields != null && bindIntentFields.size() != 0) {
            parseIntentBuilder.beginControlFlow("if (savedInstanceState == null)");
            for (BindIntentField field : bindIntentFields) {
                // parse Intent
                parseIntentBuilder.addStatement(parseIntentType(field), field.getFieldName(), field.getValue());
            }
            parseIntentBuilder.endControlFlow();
            parseIntentBuilder.beginControlFlow("else");
            for (BindIntentField field : bindIntentFields) {
                parseIntentBuilder.addStatement(parseBundleType(field), field.getFieldName(), field.getValue());
            }
            parseIntentBuilder.endControlFlow();
        }

        // void saveInstanceState(T host, Bundle outState);
        MethodSpec.Builder saveInstanceStateBuilder = MethodSpec.methodBuilder("saveInstanceState")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mClassElement.asType()), "host", Modifier.FINAL)
                .addParameter(ClassName.get("android.os", "Bundle"), "outState");

        if (bindIntentFields != null && bindIntentFields.size() != 0) {
            for (BindIntentField field : bindIntentFields) {
                // save InstanceState
                saveInstanceStateBuilder.addStatement(saveInstanceState(field), field.getValue(), field.getFieldName());
            }
        }

        // generate whole class
        TypeSpec finderClass = TypeSpec.classBuilder(mClassElement.getSimpleName() + "$$Core")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ROOT_PACKAGE_NAME, "Core"), TypeName.get(mClassElement.asType())))
                .addMethod(injectMethodBuilder.build())
                .addMethod(parseIntentBuilder.build())
                .addMethod(saveInstanceStateBuilder.build())
                .build();

        String packageName = mElementUtils.getPackageOf(mClassElement).getQualifiedName().toString();
        // generate file
        return JavaFile.builder(packageName, finderClass).build();
    }

    private String parseIntentType(BindIntentField bindIntentField) {
        String result = "host.$N = intent.getIntExtra($S, 0)";
        VariableElement element = bindIntentField.getFieldElement();
        TypeMirror typeMirror = bindIntentField.getFieldType();
        TypeKind typeKind = typeMirror.getKind();
        switch (typeKind) {
            case INT:
                break;
            case BOOLEAN:
                result = "host.$N = intent.getBooleanExtra($S, false)";
                break;
            case DECLARED:
                String typeMirrorName = typeMirror.toString();
                if ("java.lang.String".equals(typeMirrorName)){
                    result = "host.$N = intent.getStringExtra($S)";
                } else if ("java.lang.Integer".equals(typeMirrorName)) {
                    result = "host.$N = intent.getIntExtra($S, 0)";
                } else {
                    result = "host.$N = intent.getSerializableExtra($S)";
                }
                break;
            default:
                break;

        }
        return result;
    }

    private String parseBundleType(BindIntentField bindIntentField) {
        String result = "host.$N = savedInstanceState.getInt($S, 0)";
        VariableElement element = bindIntentField.getFieldElement();
        TypeMirror typeMirror = bindIntentField.getFieldType();
        TypeKind typeKind = typeMirror.getKind();
        switch (typeKind) {
            case INT:
                break;
            case BOOLEAN:
                result = "host.$N = savedInstanceState.getBoolean($S, false)";
                break;
            case DECLARED:
                String typeMirrorName = typeMirror.toString();
                if ("java.lang.String".equals(typeMirrorName)){
                    result = "host.$N = savedInstanceState.getString($S)";
                } else if ("java.lang.Integer".equals(typeMirrorName)) {
                    result = "host.$N = savedInstanceState.getInt($S, 0)";
                } else {
                    result = "host.$N = savedInstanceState.getSerializable($S)";
                }
                break;
            default:
                break;

        }
        return result;
    }

    private String saveInstanceState(BindIntentField bindIntentField) {
        String result = "outState.putInt($S, host.$N);";
        VariableElement element = bindIntentField.getFieldElement();
        TypeMirror typeMirror = bindIntentField.getFieldType();
        TypeKind typeKind = typeMirror.getKind();
        switch (typeKind) {
            case INT:
                break;
            case BOOLEAN:
                result = "outState.putBoolean($S, host.$N);";
                break;
            case DECLARED:
                String typeMirrorName = typeMirror.toString();
                if ("java.lang.String".equals(typeMirrorName)){
                    result = "outState.putString($S, host.$N);";
                } else if ("java.lang.Integer".equals(typeMirrorName)) {
                    result = "outState.putInt($S, host.$N);";
                } else {
                    result = "outState.putSerializable($S, host.$N);";
                }
                break;
            default:
                break;

        }
        return  result;
    }

    public void addBindViewField(BindViewField bindViewField) {
        if (bindViewFields == null) {
            bindViewFields = new ArrayList<>();
        }
        bindViewFields.add(bindViewField);
    }

    public void addBindIntentField(BindIntentField bindIntentField) {
        if (bindIntentFields == null) {
            bindIntentFields = new ArrayList<>();
        }

        bindIntentFields.add(bindIntentField);
    }

}
