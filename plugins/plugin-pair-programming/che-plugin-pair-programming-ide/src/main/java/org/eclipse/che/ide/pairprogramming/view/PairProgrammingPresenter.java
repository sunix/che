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
package org.eclipse.che.ide.pairprogramming.view;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.function.Consumer;

import org.eclipse.che.ide.api.parts.base.BasePresenter;
import org.eclipse.che.ide.api.preferences.PreferencesManager;
import org.eclipse.che.ide.pairprogramming.CheTeletype;
import org.eclipse.che.ide.teletype.CheTeletypePortalBinding;
import org.eclipse.che.ide.teletype.TeletypeClient;
import org.eclipse.che.ide.teletype.TeletypePortal;
import org.eclipse.che.ide.util.loging.Log;

@Singleton
public class PairProgrammingPresenter extends BasePresenter
    implements PairProgrammingView.ActionDelegate {

  private PairProgrammingView view;
  private CheTeletype cheTeletype;
  private PreferencesManager preferenceManager;

  @Inject
  public PairProgrammingPresenter(PairProgrammingView view, CheTeletype cheTeletype, PreferencesManager preferenceManager) {
    this.view = view;
    this.cheTeletype = cheTeletype;
    this.preferenceManager = preferenceManager;
    this.view.setDelegate(this);
  }

  @Override
  public String getTitle() {
    return "Teletype";
  }

  @Override
  public IsWidget getView() {
    return view;
  }

  @Override
  public String getTitleToolTip() {
    return "Teletype";
  }

  @Override
  public void go(AcceptsOneWidget container) {
    container.setWidget(view);
    view.setApiServerBaseUrl("https://api.teletype.atom.io");
    view.setPusherServiceKey("f119821248b7429bece3");
    view.setPusherClusterName("mt1");
    view.setUserToken("");
    view.setPortalId("");
    String previouslyUsedPortalId = preferenceManager.getValue("teletypePortalId");
    if(previouslyUsedPortalId != null){
        view.setPortalId(previouslyUsedPortalId);
    }
  }

  @Override
  public void onButtonClicked() {
    Log.info(getClass(), "button clicked, " + view.getAPIServerBaseURL());
    TeletypeClient client =
        TeletypeClient.create(
            view.getPusherServiceKey(), view.getPusherClusterName(), view.getAPIServerBaseURL());
    client.signIn(view.getUserToken());
    client.initialize();
    Log.info(getClass(), "calling join portal");
    client.joinPortal(
        view.getPortalId(),
        new Consumer<TeletypePortal>() {
          @Override
          public void accept(TeletypePortal guestPortal) {
            CheTeletypePortalBinding guestPortalBinding = CheTeletypePortalBinding.create();
            guestPortal.setDelegate(guestPortalBinding);
            cheTeletype.registerUpdateActivePosition(guestPortalBinding);
            cheTeletype.connectAlreadyOpennedEditor(guestPortalBinding);
            cheTeletype.onEditorsDocumentModelChanged(guestPortalBinding);
            view.disableJoinButton();
            preferenceManager.setValue("teletypePortalId", view.getPortalId());
            preferenceManager.flushPreferences();
          }
        });
  }
}
