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

public class TeletypeTextUpdate extends JavaScriptObject {
  protected TeletypeTextUpdate() {}

  public final native int getOldEndRow() /*-{
      return this.oldEnd.row;
    }-*/;

  public final native int getOldEndColumn() /*-{
      return this.oldEnd.column;
    }-*/;

  public final native int getOldStartRow() /*-{
      return this.oldStart.row;
    }-*/;

  public final native int getOldStartColumn() /*-{
      return this.oldStart.column;
    }-*/;

  public final native String getNewText() /*-{
      return this.newText;
    }-*/;

  public final String nonFinalToString() {
    return "Start r:"
        + getOldStartRow()
        + "-c:"
        + getOldStartColumn()
        + " End r:"
        + getOldEndRow()
        + "-c:"
        + getOldEndColumn()
        + " new text: "
        + getNewText();
  }
}
