package com.mk.aline.apt;


import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Creater: liguoying
 * Time: 2018/7/9 0009 11:36
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class OnceClickProcess extends AbstractProcessor {

    private Messager messager;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(OnceClick.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> mSet, RoundEnvironment mRoundEnvironment) {
//        使用参数roundEnv，遍历所有@OnceClick注解，并生成代理类ProxyInfo的Map
        Map<String, ProxyInfo> mProxyInfoMap = getProxyMap(mRoundEnvironment);
        //遍历 Map，并生成代码
        for (String key : mProxyInfoMap.keySet()) {
            ProxyInfo mProxyInfo = mProxyInfoMap.get(key);
            writeCode(mProxyInfo);
        }
        return true;
    }


    //使用参数roundEnv，遍历所有@OnceClick注解，并生成代理类ProxyInfo的Map
    private Map<String, ProxyInfo> getProxyMap(RoundEnvironment mRoundEnvironment) {

        Map<String, ProxyInfo> proxyMap = new HashMap<>();
        for (Element element : mRoundEnvironment.getElementsAnnotatedWith(OnceClick.class)) {
            //target相同只能强转。不同使用getEnclosingElement
            ExecutableElement executableElement = (ExecutableElement) element;
            TypeElement classElement = (TypeElement) element
                    .getEnclosingElement();

            PackageElement packageElement = elementUtils.getPackageOf(classElement);


            String fullClassName = classElement.getQualifiedName().toString();
            String className = classElement.getSimpleName().toString();
            String packageName = packageElement.getQualifiedName().toString();
            String methodName = executableElement.getSimpleName().toString();
            int viewId = executableElement.getAnnotation(OnceClick.class).value();

            print("fullClassName: " + fullClassName +
                    ",  methodName: " + methodName +
                    ",  viewId: " + viewId);

            OnceMethod onceMethod = new OnceMethod(viewId, methodName, getMethodParameterTypes(executableElement));

            ProxyInfo proxyInfo = proxyMap.get(fullClassName);
            if (proxyInfo != null) {
                proxyInfo.addMethod(onceMethod);
            } else {
                proxyInfo = new ProxyInfo(packageName, className);
                proxyInfo.setTypeElement(classElement);
                proxyInfo.addMethod(onceMethod);
                proxyMap.put(fullClassName, proxyInfo);
            }
        }
        return proxyMap;
    }

    /**
     * 取得方法参数类型列表
     */
    private List<String> getMethodParameterTypes(ExecutableElement mExecutableElement) {
        List<? extends VariableElement> methodParameters = mExecutableElement.getParameters();
        if (methodParameters.size() == 0) {
            return null;
        }
        List<String> types = new ArrayList<>();
        for (VariableElement variableElement : methodParameters) {
            TypeMirror methodParameterType = variableElement.asType();
            if (methodParameterType instanceof TypeVariable) {
                TypeVariable typeVariable = (TypeVariable) methodParameterType;
                methodParameterType = typeVariable.getUpperBound();
            }
            types.add(methodParameterType.toString());
        }
        return types;
    }

    private void writeCode(ProxyInfo mProxyInfo) {
        try {
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                    mProxyInfo.getProxyClassFullName(),
                    mProxyInfo.getTypeElement());
            Writer writer = jfo.openWriter();
            writer.write(mProxyInfo.generateJavaCode());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            error(mProxyInfo.getTypeElement(),
                    "Unable to write injector for type %s: %s",
                    mProxyInfo.getTypeElement(), e.getMessage());
        } catch (OnceClickException mE) {
            mE.printStackTrace();
        }
    }

    //两个日志输出方法。
    private void print(String message) {
        System.out.print("pppp"+ message);
        messager.printMessage(Diagnostic.Kind.ERROR, message);
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }

}
