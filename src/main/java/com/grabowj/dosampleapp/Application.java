package com.grabowj.dosampleapp;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@Theme(themeClass = Material.class, variant = Material.DARK)
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {
}
