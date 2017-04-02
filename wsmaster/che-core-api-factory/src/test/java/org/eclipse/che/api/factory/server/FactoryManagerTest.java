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
package org.eclipse.che.api.factory.server;

import org.eclipse.che.api.factory.server.model.impl.FactoryImpl;
import org.eclipse.che.api.factory.server.spi.FactoryDao;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertFalse;

/**
 * @author Max Shaposhnik (mshaposhnik@codenvy.com) on 3/20/17.
 */
@Listeners(value = {MockitoTestNGListener.class})
public class FactoryManagerTest {

    @Mock
    private FactoryDao factoryDao;

    @InjectMocks
    private FactoryManager factoryManager;

    @Captor
    private ArgumentCaptor<FactoryImpl> factoryCaptor;

    @Test
    public void shouldGenerateNameOnFactoryCreation() throws Exception {
        final FactoryImpl factory = FactoryImpl.builder().generateId().build();
        factoryManager.saveFactory(factory);
        verify(factoryDao).create(factoryCaptor.capture());
        assertFalse(isNullOrEmpty(factoryCaptor.getValue().getName()));
    }
}
