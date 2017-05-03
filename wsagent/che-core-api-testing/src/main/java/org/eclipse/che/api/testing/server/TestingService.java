/*******************************************************************************
 * Copyright (c) 2012-2017 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.api.testing.server;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.che.api.testing.server.framework.TestFrameworkRegistry;
import org.eclipse.che.api.testing.server.framework.TestRunner;
import org.eclipse.che.api.testing.shared.TestResult;
import org.eclipse.core.resources.ResourcesPlugin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Service for handling Che unit test frameworks.
 *
 * @author Mirage Abeysekara
 */
@Api(value = "/che-testing")
@Path("che/testing")
public class TestingService {

    private final TestFrameworkRegistry frameworkRegistry;

    @Inject
    public TestingService(TestFrameworkRegistry frameworkRegistry) {
        this.frameworkRegistry = frameworkRegistry;
    }

    /**
     * Execute the Java test cases and return the test result.
     *
     * <pre>
     *     Required request parameters.
     *     <em>projectPath</em> : Relative path to the project directory.
     *     <em>testFramework</em> : Name of the test framework where the tests should be run on. This should match with
     *                     the name returned by {@link TestRunner#getName()} implementation.
     * </pre>
     *
     * @param uriInfo JAX-RS implementation of UrlInfo with set of query parameters.
     * @return the test result of test case
     * @throws Exception when the test runner failed to execute test cases.
     */
    @GET
    @Path("run")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Execute Java tests and return results",
        notes = "The GET parameters are passed to the test framework implementation.")
    @ApiResponses({@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Server error")})
    public TestResult run(@Context UriInfo uriInfo) throws Exception {
        try {
            Map<String, String> queryParameters = getMap(uriInfo.getQueryParameters());
            String projectPath = queryParameters.get("projectPath");
            String absoluteProjectPath = ResourcesPlugin.getPathToWorkspace() + projectPath;
            queryParameters.put("absoluteProjectPath", absoluteProjectPath);
            String testFramework = queryParameters.get("testFramework");
            TestRunner runner = frameworkRegistry.getTestRunner(testFramework);
            if (runner == null) {
                throw new WebApplicationException("No test frameworks found: " + testFramework);
            }
            TestResult result = frameworkRegistry.getTestRunner(testFramework).execute(queryParameters);
            return result;
        } catch (Throwable e) {
            throw translateException(e);
        }

    }

    private WebApplicationException translateException(Throwable e) {
        if (e instanceof WebApplicationException) {
            return (WebApplicationException)e;
        }
        Throwable cause = null;
        if (e instanceof InvocationTargetException) {
            cause = e.getCause();
        }
        if (e instanceof RuntimeException) {
            cause = e.getCause();
        }
        if (cause != null && cause != e) {
            return translateException(cause);
        }
        return new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                                   .entity("An unexpected error occured during test execution: " + e.toString()).build());
    }

    private Map<String, String> getMap(MultivaluedMap<String, String> queryParameters) {
        Map<String, String> map = new HashMap<>();
        if (queryParameters == null) {
            return map;
        }
        for (Map.Entry<String, List<String>> entry : queryParameters.entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (String s : entry.getValue()) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(s);
            }
            map.put(entry.getKey(), sb.toString());
        }
        return map;
    }
}
