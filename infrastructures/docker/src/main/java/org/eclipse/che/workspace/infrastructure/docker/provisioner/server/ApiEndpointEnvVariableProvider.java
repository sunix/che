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
package org.eclipse.che.workspace.infrastructure.docker.provisioner.server;

import com.google.common.base.Strings;

import org.eclipse.che.api.core.model.workspace.runtime.RuntimeIdentity;
import org.eclipse.che.commons.lang.Pair;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static org.eclipse.che.workspace.infrastructure.docker.DockerMachine.API_ENDPOINT_URL_VARIABLE;

/**
 * Add env variable to docker machine with url of Che API
 *
 * @author Alexander Garagatyi
 */
@Singleton
public class ApiEndpointEnvVariableProvider implements ServerEnvironmentVariableProvider{
    private Pair<String, String> apiEnvVar;

    @Inject
    public ApiEndpointEnvVariableProvider(@Named("che.workspace.che_server_endpoint") String apiEndpoint) {
        String apiEndpointEnvVar = System.getenv(API_ENDPOINT_URL_VARIABLE);
        if (Strings.isNullOrEmpty(apiEndpoint) &&
            !Strings.isNullOrEmpty(apiEndpointEnvVar)) {
            apiEndpoint = apiEndpointEnvVar;
        }
        apiEnvVar = Pair.of(API_ENDPOINT_URL_VARIABLE, apiEndpoint);
    }

    @Override
    public Pair<String, String> get(RuntimeIdentity runtimeIdentity) {
        return new Pair<>(apiEnvVar.first, apiEnvVar.second);
    }
}