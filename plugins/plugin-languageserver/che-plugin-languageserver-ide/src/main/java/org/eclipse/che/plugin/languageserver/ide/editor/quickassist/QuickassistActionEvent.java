/*******************************************************************************
 * Copyright (c) 2017 Red Hat.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat - Initial Contribution
 *******************************************************************************/package org.eclipse.che.plugin.languageserver.ide.editor.quickassist;

import java.util.List;

import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.action.ActionManager;
import org.eclipse.che.ide.api.action.Presentation;
import org.eclipse.che.ide.api.parts.PerspectiveManager;

/**
 * An extension of {@link ActionEvent} to pass command paramenters as a list of Objects to the action.
 * @author Thomas Mäder
 *
 */
public class QuickassistActionEvent extends ActionEvent {

	private List<Object> arguments;

	public QuickassistActionEvent(Presentation presentation, ActionManager actionManager, PerspectiveManager perspectiveManager, List<Object> arguments) {
		super(presentation, actionManager, perspectiveManager);
		this.arguments= arguments;
	}

	public List<Object> getArguments() {
		return arguments;
	}
}
