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
package org.eclipse.che.plugin.testing.ide;

import org.vectomatic.dom.svg.ui.SVGResource;

import com.google.gwt.resources.client.ClientBundle;

/**
 * Resources for test extension.
 *
 * @author Mirage Abeysekara
 * 
 */
public interface TestResources extends ClientBundle {

    @Source("org/eclipse/che/plugin/testing/ide/svg/test.svg")
    SVGResource testIcon();

    @Source("org/eclipse/che/plugin/testing/ide/svg/test_results_pass.svg")
    SVGResource testResultsPass();

    @Source("org/eclipse/che/plugin/testing/ide/svg/test_results_fail.svg")
    SVGResource testResultsFail();

    @Source("org/eclipse/che/plugin/testing/ide/svg/test_method_fail.svg")
    SVGResource testMethodFail();

    @Source("org/eclipse/che/plugin/testing/ide/svg/test_class_fail.svg")
    SVGResource testClassFail();

    @Source("org/eclipse/che/plugin/testing/ide/svg/show_all_tests_icon.svg")
    SVGResource showAllTestsButtonIcon();

    @Source("org/eclipse/che/plugin/testing/ide/svg/show_failures_only_icon.svg")
    SVGResource showFailuresOnlyButtonIcon();
}
