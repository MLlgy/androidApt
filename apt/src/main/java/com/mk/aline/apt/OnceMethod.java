package com.mk.aline.apt;

import java.util.List;

/**
 * Creater: liguoying
 * Time: 2018/7/9 0009 14:29
 * 需要讲的一点是，每一个使用了@OnceClick注解的Activity或View，都会为其生成一个代理类，
 * 而一个代理中有可能有很多个@OnceClick修饰的方法，所以我们专门为每个方法有创建了一个javaBean用于保存方法信息:
 */
public class OnceMethod {

    private int id;
    private String methodName;
    private List<String> methodParameters;

    OnceMethod(int id, String methodName, List<String> methodParameters) {
        this.id = id;
        this.methodName = methodName;
        this.methodParameters = methodParameters;
    }

    int getMethodParametersSize() {
        return methodParameters == null ? 0 : methodParameters.size();
    }

    int getId() {
        return id;
    }

    String getMethodName() {
        return methodName;
    }

    List<String> getMethodParameters() {
        return methodParameters;
    }

}
