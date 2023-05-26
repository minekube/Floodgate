package com.minekube.connect.module;

import com.google.inject.AbstractModule;
import com.minekube.connect.util.AutoBind;
import com.minekube.connect.util.Utils;

public class AutoBindModule extends AbstractModule {
    @Override
    protected void configure() {
        for (Class<?> clazz : Utils.getGeneratedClassesForAnnotation(AutoBind.class)) {
            bind(clazz).asEagerSingleton();
        }
    }
}
