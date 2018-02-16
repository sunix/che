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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import javax.validation.constraints.NotNull;
import org.eclipse.che.ide.api.parts.base.BaseView;
import org.eclipse.che.ide.pairprogramming.PairProgrammingMessages;
import org.eclipse.che.ide.pairprogramming.PairProgrammingResources;

public class PairProgrammingViewImpl extends BaseView<PairProgrammingView.ActionDelegate>
    implements PairProgrammingView {

  @UiField ScrollPanel ppPanel;
  @UiField FlowPanel stubPanel;

  /** The resources for the view. */
  @UiField(provided = true)
  PairProgrammingResources resources;

  @UiField TextBox apiServerBaseUrl;
  @UiField TextBox pusherServiceKey;
  @UiField TextBox pusherClusterName;
  @UiField TextBox portalId;
  @UiField TextBox userToken;
  @UiField Anchor userTokenUrl;
  /** The i18n messages. */
  @UiField(provided = true)
  PairProgrammingMessages messages;

  /** The new contribution button. */
  @UiField Button connectButton;

  @Inject
  public PairProgrammingViewImpl(
      @NotNull final PairProgrammingMessages messages,
      @NotNull final PairProgrammingResources resources,
      @NotNull final PairProgrammingViewUiBinder uiBinder) {
    this.messages = messages;
    this.resources = resources;

    setContentWidget(uiBinder.createAndBindUi(this));

    setTitle(messages.pairprogrammingPartTitle());
    userTokenUrl.setHref("https://teletype.atom.io/login");
    userTokenUrl.setText("https://teletype.atom.io/login");
  }

  @SuppressWarnings("UnusedParameters")
  @UiHandler("connectButton")
  protected void contributeClick(final ClickEvent event) {
    delegate.onButtonClicked();
  }

  @Override
  public String getAPIServerBaseURL() {
    return apiServerBaseUrl.getText();
  }

  @Override
  public String getPusherClusterName() {
    return pusherClusterName.getText();
  }

  @Override
  public String getPusherServiceKey() {
    return pusherServiceKey.getText();
  }

  @Override
  public String getPortalId() {
    return portalId.getText();
  }

  @Override
  public String getUserToken() {
    return userToken.getText();
  }

  public void setApiServerBaseUrl(String apiServerBaseUrl) {
    this.apiServerBaseUrl.setText(apiServerBaseUrl);
  }

  public void setPusherClusterName(String pusherClusterName) {
    this.pusherClusterName.setText(pusherClusterName);
  }

  public void setPusherServiceKey(String pusherServiceKey) {
    this.pusherServiceKey.setText(pusherServiceKey);
  }

  public void setUserToken(String userToken) {
    this.userToken.setText(userToken);
  }

  public void setPortalId(String portalId) {
    this.portalId.setText(portalId);
  }

  @Override
  public void disableJoinButton() {
    connectButton.setEnabled(false);
  }
}
