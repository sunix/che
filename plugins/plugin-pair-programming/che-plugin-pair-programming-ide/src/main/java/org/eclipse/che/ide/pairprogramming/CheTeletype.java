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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.api.editor.document.Document;
import org.eclipse.che.ide.api.editor.document.DocumentHandle;
import org.eclipse.che.ide.api.editor.events.CursorActivityEvent;
import org.eclipse.che.ide.api.editor.events.CursorActivityHandler;
import org.eclipse.che.ide.api.editor.events.DocumentChangingEvent;
import org.eclipse.che.ide.api.editor.events.DocumentChangingHandler;
import org.eclipse.che.ide.api.editor.events.DocumentReadyEvent;
import org.eclipse.che.ide.api.editor.events.DocumentReadyHandler;
import org.eclipse.che.ide.api.editor.text.TextPosition;
import org.eclipse.che.ide.api.editor.text.TextRange;
import org.eclipse.che.ide.api.editor.texteditor.HasTextMarkers.MarkerRegistration;
import org.eclipse.che.ide.api.editor.texteditor.TextEditor;
import org.eclipse.che.ide.resource.Path;
import org.eclipse.che.ide.teletype.CheTeletypeBufferBinding;
import org.eclipse.che.ide.teletype.CheTeletypeEditorBinding;
import org.eclipse.che.ide.teletype.CheTeletypePortalBinding;
import org.eclipse.che.ide.teletype.TeletypeActivePosition;
import org.eclipse.che.ide.teletype.TeletypeActivePositions;
import org.eclipse.che.ide.teletype.TeletypeBufferProxy;
import org.eclipse.che.ide.teletype.TeletypeEditorProxy;
import org.eclipse.che.ide.teletype.TeletypeTextUpdate;
import org.eclipse.che.ide.util.loging.Log;

@Singleton
public class CheTeletype {

  private Map<Double, MarkerRegistration> markers = new HashMap<Double, MarkerRegistration>();
  private EventBus eventBus;
  private EditorAgent editorAgent;
  private boolean isUpdatingModel = false;

  @Inject
  public CheTeletype(final EventBus eventBus, final EditorAgent editorAgent) {
    this.eventBus = eventBus;
    this.editorAgent = editorAgent;
  }

  public void connectAlreadyOpennedEditor(CheTeletypePortalBinding guestPortalBinding) {
    List<EditorPartPresenter> editors = editorAgent.getOpenedEditors();
    for (EditorPartPresenter editorPartPresenter : editors) {
      if (editorPartPresenter instanceof TextEditor) {
        TextEditor textEditor = (TextEditor) editorPartPresenter;
        Document document = textEditor.getDocument();
        synchronizeDocument(document, guestPortalBinding);
      }
    }
  }

  public void registerUpdateActivePosition(CheTeletypePortalBinding guestPortalBinding) {

    guestPortalBinding.onUpdateActivePositions(
        new java.util.function.Consumer<TeletypeActivePositions>() {
          @Override
          public void accept(TeletypeActivePositions t) {
            for (int i = 0; i < t.getKeys().length(); i++) {
              try {
                double siteId = t.getKeys().get(i);
                TeletypeActivePosition activePosition = t.getActivePosition(siteId);

                TextEditor textEditor;
                EditorPartPresenter openedEditor =
                    editorAgent.getOpenedEditor(
                        new Path("/" + activePosition.getEditorProxy().getBufferProxy().getURI()));
                Log.info(
                    getClass(),
                    "trying get the opened editor:"
                        + activePosition.getEditorProxy().getBufferProxy().getURI()
                        + " instance: "
                        + openedEditor);

                if (openedEditor instanceof TextEditor) {
                  Log.info(
                      getClass(),
                      "is text editor" + activePosition.getEditorProxy().getBufferProxy().getURI());
                  textEditor = (TextEditor) openedEditor;
                  TextPosition pos =
                      new TextPosition(
                          activePosition.getPosition().getRow(),
                          activePosition.getPosition().getColumn());
                  TextRange textRange = new TextRange(pos, pos);

                  if (markers.containsKey(siteId)) {
                    markers.get(siteId).clearMark();
                  }

                  markers.put(
                      siteId,
                      textEditor
                          .getEditorWidget()
                          .addMarker(textRange, "pairProgramminigUser" + (1 + (siteId % 14))));
                }
              } catch (Exception e) {
                Log.error(getClass(), e);
              }
            }
          }
        });
  };

  public void onEditorsDocumentModelChanged(CheTeletypePortalBinding guestPortalBinding) {
    eventBus.addHandler(
        DocumentReadyEvent.TYPE,
        new DocumentReadyHandler() {
          @Override
          public void onDocumentReady(DocumentReadyEvent event) {
            try {
              synchronizeDocument(event.getDocument(), guestPortalBinding);
            } catch (Exception e) {
              Log.error(getClass(), e);
            }
          }
        });
  }

  private void synchronizeDocument(
      Document documentMain, CheTeletypePortalBinding guestPortalBinding) {
    final DocumentHandle documentHandle = documentMain.getDocumentHandle();
    String fullPath = documentMain.getFile().getLocation().toString().substring(1);

    if (guestPortalBinding == null) {
      Log.info(getClass(), "guestPortalBinding null byebye");
      return;
    }

    TeletypeEditorProxy guestEditorProxy = guestPortalBinding.editorProxyForURI(fullPath);
    if (guestEditorProxy == null) {
      Log.info(getClass(), "guesteditor proxy null for " + fullPath + "byebye");
      return;
    }
    TeletypeBufferProxy guestBufferProxy = guestEditorProxy.getBufferProxy();

    CheTeletypeEditorBinding guestEditorDelegate = CheTeletypeEditorBinding.create();
    guestEditorProxy.setDelegate(guestEditorDelegate);

    CheTeletypeBufferBinding guestBufferDelegate = CheTeletypeBufferBinding.create();

    guestBufferDelegate.onGetText(
        new Supplier<String>() {
          @Override
          public String get() {
            return documentMain.getContents();
          }
        });
    guestBufferDelegate.onSetText(
        new java.util.function.Consumer<String>() {

          @Override
          public void accept(String arg0) {
            Log.info(getClass(), "set text from remote : " + arg0);
            documentMain.replace(0, documentMain.getContents().length(), arg0);
          }
        });

    guestBufferDelegate.onUpdateText(
        new java.util.function.Consumer<TeletypeTextUpdate>() {

          @Override
          public void accept(TeletypeTextUpdate updateText) {
            isUpdatingModel = true;
            Log.info(getClass(), updateText.nonFinalToString());
            TextPosition cursorPosition = documentMain.getCursorPosition();
            documentMain.replace(
                updateText.getOldStartRow(), //
                updateText.getOldStartColumn(), //
                updateText.getOldEndRow(), //
                updateText.getOldEndColumn(), //
                updateText.getNewText());
            documentMain.setCursorPosition(cursorPosition);
            isUpdatingModel = false;
          }
        });
    guestBufferProxy.setDelegate(guestBufferDelegate);
    documentHandle
        .getDocEventBus()
        .addHandler(
            DocumentChangingEvent.TYPE,
            new DocumentChangingHandler() {

              @Override
              public void onDocumentChanging(DocumentChangingEvent event) {
                Log.info(getClass(), "Changing event:" + event.toDebugString());
                if (isUpdatingModel) {
                  return;
                }

                TextPosition startPosition =
                    event.getDocument().getDocument().getPositionFromIndex(event.getOffset());
                TextPosition endPosition =
                    event
                        .getDocument()
                        .getDocument()
                        .getPositionFromIndex(event.getOffset() + event.getRemoveCharCount());
                guestBufferProxy.setTextInRange(
                    startPosition.getLine(),
                    startPosition.getCharacter(),
                    endPosition.getLine(),
                    endPosition.getCharacter(),
                    event.getText());
              }
            });

    documentMain.addCursorHandler(
        new CursorActivityHandler() {
          @Override
          public void onCursorActivity(CursorActivityEvent event) {
            TextPosition cursorPos = documentMain.getCursorPosition();
            guestEditorProxy.updateSelections(
                cursorPos.getLine(),
                cursorPos.getCharacter(),
                cursorPos.getLine(),
                cursorPos.getCharacter());
          }
        });
  }
}
