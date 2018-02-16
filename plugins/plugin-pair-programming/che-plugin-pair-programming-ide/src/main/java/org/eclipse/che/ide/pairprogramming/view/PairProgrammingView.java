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

import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.api.parts.base.BaseActionDelegate;

public interface PairProgrammingView extends View<PairProgrammingView.ActionDelegate> {

  void setVisible(boolean visible);

  interface ActionDelegate extends BaseActionDelegate {
    void onButtonClicked();
  }

  String getAPIServerBaseURL();

  String getPusherClusterName();

  String getPusherServiceKey();

  String getUserToken();

  String getPortalId();

  void setApiServerBaseUrl(String apiServerBaseUrl);

  void setPusherClusterName(String pusherClusterName);

  void setPusherServiceKey(String pusherServiceKey);

  void setUserToken(String userToken);

  void setPortalId(String portalId);

  void disableJoinButton();
}
