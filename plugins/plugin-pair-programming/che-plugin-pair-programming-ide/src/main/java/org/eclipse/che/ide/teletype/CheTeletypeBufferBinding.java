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
import java.util.function.Supplier;

public class CheTeletypeBufferBinding extends JavaScriptObject {
  protected CheTeletypeBufferBinding() {}

  public static final native CheTeletypeBufferBinding create() /*-{
          return $wnd.CheTeletype.createBufferBinding();
      }-*/;

  public final native void onGetText(Supplier<String> handler) /*-{
                this.onGetText = function(param) {
                         handler.@java.util.function.Supplier::get(*)(param);
                     }
      }-*/;

  public final native void onSetText(Consumer<String> handler) /*-{
                this.onSetText = function(param) {
                         handler.@java.util.function.Consumer::accept(*)(param);
                     }
      }-*/;

  public final native void onUpdateText(Consumer<TeletypeTextUpdate> handler) /*-{
                this.onUpdateText = function(param) {
                         handler.@java.util.function.Consumer::accept(*)(param);
                     }
      }-*/;
}
