/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.ide.pairprogramming;

import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.extension.Extension;
import org.eclipse.che.ide.api.parts.PartStackType;
import org.eclipse.che.ide.api.parts.WorkspaceAgent;
import org.eclipse.che.ide.pairprogramming.view.PairProgrammingPresenter;
import org.eclipse.che.ide.teletype.TeletypeResources;

/**
 * Che product information constant.
 *
 * @author Sun Seng David TAN
 * @author Randika Navagamuwa
 */
@Extension(title = "Che Pair programing extension with Teletype", version = "1.0.0")
public class CheTeletypeExtension {

  private WorkspaceAgent workspaceAgent;

  private PairProgrammingPresenter ppPresenter;

  @Inject
  public CheTeletypeExtension(
      final EventBus eventBus,
      final EditorAgent editorAgent,
      final WorkspaceAgent workspaceAgent,
      final PairProgrammingPresenter ppPresenter) {
    this.workspaceAgent = workspaceAgent;
    this.ppPresenter = ppPresenter;

    displayPairProgramingPanel();

    injectTeletype();

    injectCssStyles();
  }

  private void displayPairProgramingPanel() {
    workspaceAgent.openPart(ppPresenter, PartStackType.TOOLING);
    workspaceAgent.setActivePart(ppPresenter);
  }

  private void injectCssStyles() {

    Map<Integer, String> colorMap = new HashMap<Integer, String>();
    colorMap.put(1, "#FFFF00");
    colorMap.put(2, "#00FF00");
    colorMap.put(3, "#00FFFF");
    colorMap.put(4, "#FF00FF");
    colorMap.put(5, "#FFFFFF");
    colorMap.put(6, "#C0C0C0");
    colorMap.put(7, "#808080");
    colorMap.put(8, "#FF0000");
    colorMap.put(9, "#800000");
    colorMap.put(10, "#808000");
    colorMap.put(11, "#008000");
    colorMap.put(12, "#008080");
    colorMap.put(13, "#0000FF");
    colorMap.put(14, "#800080");

    String css = null;
    for (int i = 1; i < colorMap.size() + 1; i++) {
      css =
          ".pairProgramminigUser"
              + i
              + " { outline: 1px solid "
              + colorMap.get(i)
              + "; animation: blinker 1s linear infinite;} @keyframes blinker { 50% { opacity: 0.0; }}";
      StyleInjector.inject(css);
    }
  }

  private void injectTeletype() {
    TeletypeResources teletypeResources = GWT.create(TeletypeResources.class);
    ScriptInjector.fromString(teletypeResources.teletype().getText())
        .setWindow(ScriptInjector.TOP_WINDOW)
        .inject();
  }
}
