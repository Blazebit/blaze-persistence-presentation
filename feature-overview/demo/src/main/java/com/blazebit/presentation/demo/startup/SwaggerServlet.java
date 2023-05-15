/*
 * Copyright 2014 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blazebit.presentation.demo.startup;

import io.swagger.config.ScannerFactory;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.DefaultJaxrsConfig;
import io.swagger.jaxrs.config.DefaultJaxrsScanner;
import io.swagger.jaxrs.config.SwaggerContextService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * @author Moritz Becker (moritz.becker@gmx.at)
 * @since 1.2
 */
@WebServlet(loadOnStartup = 2, initParams = @WebInitParam(name = "scan.all.resources", value = "true"))
public class SwaggerServlet extends DefaultJaxrsConfig {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        BeanConfig test = new BeanConfig();
        test.setVersion("1.0.0");
        test.setSchemes(new String[]{"http"});
        test.setHost("localhost:8080");
        test.setBasePath("/api");
        test.setResourcePackage("com.blazebit.presentation.demo.rest");
        test.setScan(true);

        servletConfig.getServletContext().setAttribute(SwaggerContextService.SCANNER_ID_DEFAULT, test);

//        new SwaggerContextService().withServletConfig(servletConfig).withScanner(test).initConfig().initScanner();
//        ScannerFactory.setScanner(test);
    }
}
