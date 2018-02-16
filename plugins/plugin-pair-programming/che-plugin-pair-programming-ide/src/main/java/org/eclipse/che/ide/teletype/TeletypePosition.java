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

public class TeletypePosition extends JavaScriptObject {

  protected TeletypePosition() {}

  public final native int getColumn() /*-{
                                        return this.column;
                                        }-*/;

  public final native int getRow() /*-{
                                     return this.row;
                                     }-*/;
}
