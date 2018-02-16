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

import com.google.gwt.i18n.client.Messages;

/** Internationalizable messages for the pairProgramming plugin. */
public interface PairProgrammingMessages extends Messages {
  /*
   * PairProgramming part
   */
  @Key("pairprogramming.part.title")
  String pairprogrammingPartTitle();

  @Key("pairprogramming.apiServerBaseUrl")
  String apiServerBaseUrl();

  @Key("pairprogramming.pusherServiceKey")
  String pusherServiceKey();

  @Key("pairprogramming.pusherClusterName")
  String pusherClusterName();

  @Key("pairprogramming.portalId")
  String portalId();

  @Key("pairprogramming.userToken")
  String userToken();

  @Key("pairprogramming.connect")
  String connect();
}
