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

public class TeletypeBufferProxy extends JavaScriptObject {
  protected TeletypeBufferProxy() {}

  public final native void setDelegate(CheTeletypeBufferBinding bufferDelegate) /*-{
          this.setDelegate(bufferDelegate);
      }-*/;

  public final native String getURI() /*-{
          return this.uri;
      }-*/;

  public final native void setTextInRange(
      int startLine, int startChar, int endLine, int endChar, String text) /*-{
          this.setTextInRange({row: startLine, column: startChar}, {row: endLine, column: endChar}, text);
      }-*/;
}
