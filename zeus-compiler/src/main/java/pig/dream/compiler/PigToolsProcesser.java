package pig.dream.compiler;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import pig.dream.annotations.BindIntent;
import pig.dream.annotations.BindView;
import pig.dream.annotations.RouteActivity;

import static sun.management.Agent.error;

/**
 * Created by zhukun on 2017/4/15.
 */

@AutoService(Processor.class)
public class PigToolsProcesser extends AbstractProcessor {

    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类
    private Messager messager;

    private Map<String, AnnotatedClass> mAnnotatedClassMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
    }

    /**
     * @return 指定哪些注解应该被注解处理器注册
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(BindView.class.getCanonicalName());
        types.add(RouteActivity.class.getCanonicalName());
        types.add(BindIntent.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // process() will be called several times
        mAnnotatedClassMap.clear();

        messager.printMessage(Diagnostic.Kind.NOTE, "Printing: 111");
        for (TypeElement te : annotations) {
            messager.printMessage(Diagnostic.Kind.NOTE, "Printing type: " + te.toString());
            for (Element e : roundEnv.getElementsAnnotatedWith(te)) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Printing: " + e.toString() + e.asType());
            }
        }
        try {
            processBindView(roundEnv);
            processBindIntent(roundEnv);
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
            return true; // stop process
        }

        for (AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {
            try {
//                info("Generating file for %s", annotatedClass.getFullClassName());

                annotatedClass.generateFinder().writeTo(mFiler);
            } catch (IOException e) {
                error("Generate file failed, reason: %s", e.getMessage());
                return true;
            }
        }
        return true;
    }

    private void processBindView(RoundEnvironment roundEnv) throws IllegalArgumentException {
        for (Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            BindViewField field = new BindViewField(element);
            annotatedClass.addBindViewField(field);
        }
    }

    private void processBindIntent(RoundEnvironment roundEnv) throws IllegalArgumentException {
//        Set<? extends Element> sets = roundEnv.getElementsAnnotatedWith(BindIntent.class);
//        if (sets == null) {
//            return;
//        }
        for (Element element : roundEnv.getElementsAnnotatedWith(BindIntent.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            BindIntentField field = new BindIntentField(element);
            annotatedClass.addBindIntentField(field);
        }
    }

    private AnnotatedClass getAnnotatedClass(Element element) {
        TypeElement classElement = (TypeElement) element.getEnclosingElement();
        String fullClassName = classElement.getQualifiedName().toString();
        messager.printMessage(Diagnostic.Kind.NOTE, "Printing fullClassName: " + fullClassName + " type " + classElement.toString());
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullClassName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(classElement, mElementUtils);
            mAnnotatedClassMap.put(fullClassName, annotatedClass);
        }
        return annotatedClass;
    }
}
