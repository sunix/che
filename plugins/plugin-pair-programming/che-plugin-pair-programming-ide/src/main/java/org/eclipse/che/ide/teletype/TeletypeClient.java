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
package org.eclipse.che.ide.teletype;

import com.google.gwt.core.client.JavaScriptObject;
import java.util.function.Consumer;

public class TeletypeClient extends JavaScriptObject {
  protected TeletypeClient() {}

  public static final native TeletypeClient create(
      String pusherKeyValue, String pusherCluster, String baseURL) /*-{
          console.log("run teletype start " + pusherKeyValue + " - " + pusherCluster + " - " + baseURL);
          var cheTeletypeLib = new $wnd.CheTeletype({
            pusherKey: pusherKeyValue,
            pusherOptions: {
              cluster: pusherCluster,
            },
            baseURL: baseURL
          })
          return cheTeletypeLib.createClient();
      }-*/;

  public final native void signIn(String token) /*-{
      console.log("signin " + token);
      this.signIn(token);
    }-*/;

  public final native void initialize() /*-{
      console.log("initialize");
      this.initialize();
    }-*/;

  public final native void joinPortal(
      String portalIdString, Consumer<TeletypePortal> handler) /*-{
      this.joinPortal(portalIdString).then(
      function(param) {
                         handler.@java.util.function.Consumer::accept(*)(param);
                     });
    }-*/;
}
