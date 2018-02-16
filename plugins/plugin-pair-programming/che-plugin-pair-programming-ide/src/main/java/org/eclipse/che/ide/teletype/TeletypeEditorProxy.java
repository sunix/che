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

public class TeletypeEditorProxy extends JavaScriptObject {
  protected TeletypeEditorProxy() {}

  public final native TeletypeBufferProxy getBufferProxy() /*-{
          return this.bufferProxy;
      }-*/;

  public final native void setDelegate(CheTeletypeEditorBinding delegate) /*-{
          this.setDelegate(delegate);
      }-*/;

  public final native void updateSelections(int row1, int column1, int row2, int column2) /*-{
            this.updateSelections({
        1: {range: {start: {row: row1, column: column1}, end: {row: row2, column: column2}}}
      });
  }-*/;
}
