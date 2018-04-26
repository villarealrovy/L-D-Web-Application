/*
 * Copyright (C) 2017 FUJITSU All rights reserved.
 */
package com.fujitsu.itLogs.online;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * @author r.monte@ph.fujitsu.com
 *
 * @version 1.0.0
 * 
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/itLogs").setViewName("redirect:/itLogs/search");
		registry.addViewController("/itLogs/").setViewName("redirect:/itLogs/search");
		
		registry.addViewController("/itLog").setViewName("redirect:/itLogs/search");
		registry.addViewController("/itLog/").setViewName("redirect:/itLogs/search");
		
		registry.addViewController("/itlog").setViewName("redirect:/itLogs/search");
		registry.addViewController("/itlog/").setViewName("redirect:/itLogs/search");
		
		registry.addViewController("/itlogs").setViewName("redirect:/itLogs/search");
		registry.addViewController("/itlogs/").setViewName("redirect:/itLogs/search");		
		
		registry.addViewController("/home").setViewName("redirect:/itLogs/search");
		registry.addViewController("/home/").setViewName("redirect:/itLogs/search");
		
		registry.addViewController("/").setViewName("redirect:/itLogs/search");
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.US);
		return slr;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

}
