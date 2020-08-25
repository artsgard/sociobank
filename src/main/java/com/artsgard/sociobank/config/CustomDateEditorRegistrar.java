package com.artsgard.sociobank.config;

import java.text.SimpleDateFormat;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;

@Component
public class CustomDateEditorRegistrar implements PropertyEditorRegistrar {
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        registry.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyyy"),true));
    }
}
