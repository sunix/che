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
package org.eclipse.che.plugin.testing.ide.view.navigation.nodes;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.ide.api.data.tree.AbstractTreeNode;
import org.eclipse.che.ide.api.data.tree.HasAction;
import org.eclipse.che.ide.api.data.tree.Node;
import org.eclipse.che.ide.ext.java.client.JavaResources;
import org.eclipse.che.ide.ui.smartTree.presentation.HasPresentation;
import org.eclipse.che.ide.ui.smartTree.presentation.NodePresentation;
import org.eclipse.che.plugin.testing.ide.TestResources;
import org.eclipse.che.plugin.testing.ide.view.navigation.TestClassNavigation;
import org.vectomatic.dom.svg.ui.SVGResource;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Tree node for display the failing methods.
 * 
 * @author Mirage Abeysekara
 */
public class TestResultMethodNode extends AbstractTreeNode implements HasAction, HasPresentation {

    private String              methodName;
    private boolean             success;
    private String              stackTrace;
    private String              message;
    private int                 lineNumber;
    private TestClassNavigation navigationHandler;
    private final TestResources testResources;
    private final JavaResources javaResources;
    private NodePresentation    nodePresentation;


    @Inject
    public TestResultMethodNode(TestResources testResources,
                                JavaResources javaResources,
                                @Assisted boolean success,
                                @Assisted("methodName") String methodName,
                                @Assisted("stackTrace") String stackTrace,
                                @Assisted("message") String message,
                                @Assisted int lineNumber,
                                @Assisted TestClassNavigation navigationHandler) {
        this.methodName = methodName;
        this.success = success;
        this.stackTrace = stackTrace;
        this.message = message;
        this.lineNumber = lineNumber;
        this.navigationHandler = navigationHandler;
        this.testResources = testResources;
        this.javaResources = javaResources;
    }

    @Override
    protected Promise<List<Node>> getChildrenImpl() {
        return null;
    }

    @Override
    public String getName() {
        if (message != null && !message.isEmpty()) {
            return "Method: " + methodName + " (" + message + ")";
        }
        return "Method: " + methodName;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public void actionPerformed() {
        if (getParent() instanceof TestResultClassNode) {
            String packagePath = ((TestResultClassNode)getParent()).getClassName().replace(".", "/") + ".java";
            String className = ((TestResultClassNode)getParent()).getClassName();
            navigationHandler.gotoClass(packagePath, className, methodName, lineNumber);
        }
    }

    @Override
    public void updatePresentation(@NotNull NodePresentation presentation) {
        if (message != null && !message.isEmpty()) {
            presentation.setInfoText("(" + message + ")");
        }
        presentation.setPresentableText(methodName);
        presentation.setPresentableIcon(javaResources.publicMethod());
        SVGResource svg = success ? javaResources.publicMethod() : testResources.testMethodFail();
        presentation.setPresentableIcon(svg);
    }

    @Override
    public NodePresentation getPresentation(boolean update) {
        if (nodePresentation == null) {
            nodePresentation = new NodePresentation();
            updatePresentation(nodePresentation);
        }

        if (update) {
            updatePresentation(nodePresentation);
        }
        return nodePresentation;
    }
}
