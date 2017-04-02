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
package org.eclipse.che.ide.factory.json;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.ide.CoreLocalizationConstant;
import org.eclipse.che.ide.api.action.Action;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.factory.FactoryResources;

/**
 * @author Sergii Leschenko
 */
@Singleton
public class ImportFromConfigAction extends Action {

    private final ImportFromConfigPresenter presenter;

    @Inject
    public ImportFromConfigAction(final ImportFromConfigPresenter presenter,
                                  CoreLocalizationConstant locale,
                                  FactoryResources resources) {
        super(locale.importFromConfigurationName(), locale.importFromConfigurationDescription(), null, resources.importConfig());
        this.presenter = presenter;
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        presenter.showDialog();
    }

    /** {@inheritDoc} */
    @Override
    public void update(ActionEvent event) {
    }
}
