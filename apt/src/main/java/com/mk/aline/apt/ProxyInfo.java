package com.mk.aline.apt;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;

/**
 * Creater: liguoying
 * Time: 2018/7/9 0009 11:39
 */
public class ProxyInfo {
    public static final String PROXY = "PROXY";
    private String mPackName;
    private String mTargetClassName;
    private String mProxyClassName;

    private TypeElement typeElement;

    private List<OnceMethod> mOnceClickMethods;

    ProxyInfo(String mPackName, String mTargetClassName) {
        this.mTargetClassName = mTargetClassName;
        this.mPackName = mPackName;
        this.mProxyClassName = mTargetClassName + "$$" + PROXY;
    }

    String getProxyClassFullName() {
        return mPackName + "." + mProxyClassName;
    }

    String generateJavaCode() throws OnceClickException {
        StringBuilder mBuilder = new StringBuilder();
        mBuilder.append("// Generate code from APT.Do not modify  ! \n");
        mBuilder.append("package ").append(mPackName).append(";\n\n");

        mBuilder.append("import android.view.View;\n");
        mBuilder.append("import com.mk.aline.aptlib.Finder;\n");
        mBuilder.append("import com.mk.aline.aptlib.AbstractInjector;\n");
        mBuilder.append('\n');

        mBuilder.append("public class ").append(mProxyClassName);
        mBuilder.append("<T extends ").append(getTargetClassName()).append(">");
        mBuilder.append(" implements AbstractInjector<T>");
        mBuilder.append(" {\n");

        generateInjectMethod(mBuilder);
        mBuilder.append('\n');

        mBuilder.append("}\n");
        return mBuilder.toString();
    }

    private String getTargetClassName() {
        return mTargetClassName.replace("$", ".");
    }

    private void generateInjectMethod(StringBuilder builder) throws OnceClickException {
        builder.append("public long intervalTime; \n");

        builder.append("  @Override ")
                .append("public void setIntervalTime(long time) {\n")
                .append("intervalTime = time;\n     } \n");
        builder.append("  @Override ")
                .append("public void inject(final Finder finder, final T target, Object source) {\n");
        builder.append("View view;\n");

        for (OnceMethod method : getMethods()) {
            builder.append("    view = ")
                    .append("finder.findViewById(source, ")
                    .append(method.getId())
                    .append(");\n");
            builder.append("if(view != null){")
                    .append("view.setOnClickListener(new View.OnClickListener() {\n")
                    .append("long time = 0L;");
            builder.append("@Override\n")
                    .append("public void onClick(View v) {");
            builder.append("long temp = System.currentTimeMillis();\n")
                    .append("if (temp - time >= intervalTime) {\n" +
                            "time = temp;\n");
            if (method.getMethodParametersSize() == 1) {
                if (method.getMethodParameters().get(0).equals("android.view.View")) {
                    builder.append("target.").append(method.getMethodName()).append("(v);");
                } else {
                    throw new OnceClickException("Parameters must be android.view.View");
                }
            } else if (method.getMethodParametersSize() == 0) {
                builder.append("target.").append(method.getMethodName()).append("();");
            } else {
                throw new OnceClickException("Does not support more than one parameter");
            }
            builder.append("\n}")
                    .append("    }\n")
                    .append("        });\n}");
        }
        builder.append("  }\n");

    }

    List<OnceMethod> getMethods() {
        return mOnceClickMethods == null ? new ArrayList<OnceMethod>() : mOnceClickMethods;
    }

    TypeElement getTypeElement() {
        return typeElement;
    }

    void setTypeElement(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    void addMethod(OnceMethod onceMethod) {
        if (mOnceClickMethods == null) {
            mOnceClickMethods = new ArrayList<>();
        }
        mOnceClickMethods.add(onceMethod);
    }

}