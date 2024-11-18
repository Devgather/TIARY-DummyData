package me.tiary.dummydata.config;

import me.tiary.dummydata.config.property.RangePropertyEditor;
import me.tiary.dummydata.data.Range;
import net.datafaker.Faker;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {
    @Bean
    public CustomEditorConfigurer customEditorConfigurer() {
        final CustomEditorConfigurer editorConfigurer = new CustomEditorConfigurer();
        final Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap<>();

        customEditors.put(Range.class, RangePropertyEditor.class);
        editorConfigurer.setCustomEditors(customEditors);

        return editorConfigurer;
    }

    @Bean
    public Faker faker() {
        return new Faker();
    }
}