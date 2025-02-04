package io.quarkus.runtime;

import java.io.InputStream;
import java.lang.reflect.Method;

import io.quarkus.runtime.util.ClassPathUtils;

/**
 * Helper method that is invoked from generated bytecode during image processing
 */
public class ResourceHelper {

    public static void registerResources(String resource) {
        try {
            Class<?> resourcesClass = Class.forName("com.oracle.svm.core.jdk.Resources");
            Method register = resourcesClass.getDeclaredMethod("registerResource", String.class, InputStream.class);
            ClassPathUtils.consumeAsStreams(ResourceHelper.class.getClassLoader(), resource, in -> {
                try {
                    register.invoke(null, resource, in);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to register resource " + resource, e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to load resource " + resource, e);
        }
    }

}
