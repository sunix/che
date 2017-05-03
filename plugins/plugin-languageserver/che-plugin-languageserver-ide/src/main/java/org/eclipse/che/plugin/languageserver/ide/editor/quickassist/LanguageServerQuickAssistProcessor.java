/*******************************************************************************
 * Copyright (c) 2017 Red Hat.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat - Initial Contribution
 *******************************************************************************/
package org.eclipse.che.plugin.languageserver.ide.editor.quickassist;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.che.api.languageserver.shared.dto.DtoClientImpls.CodeActionContextDTOImpl;
import org.eclipse.che.api.languageserver.shared.dto.DtoClientImpls.CodeActionParamsDTOImpl;
import org.eclipse.che.api.languageserver.shared.dto.DtoClientImpls.PositionDTOImpl;
import org.eclipse.che.api.languageserver.shared.dto.DtoClientImpls.RangeDTOImpl;
import org.eclipse.che.api.languageserver.shared.dto.DtoClientImpls.TextDocumentIdentifierDTOImpl;
import org.eclipse.che.api.languageserver.shared.lsapi.CodeActionContextDTO;
import org.eclipse.che.api.languageserver.shared.lsapi.CodeActionParamsDTO;
import org.eclipse.che.api.languageserver.shared.lsapi.CommandDTO;
import org.eclipse.che.api.languageserver.shared.lsapi.DiagnosticDTO;
import org.eclipse.che.api.languageserver.shared.lsapi.PositionDTO;
import org.eclipse.che.api.languageserver.shared.lsapi.RangeDTO;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.ide.api.action.Action;
import org.eclipse.che.ide.api.action.ActionManager;
import org.eclipse.che.ide.api.action.Presentation;
import org.eclipse.che.ide.api.editor.annotation.QueryAnnotationsEvent;
import org.eclipse.che.ide.api.editor.codeassist.CodeAssistCallback;
import org.eclipse.che.ide.api.editor.codeassist.Completion;
import org.eclipse.che.ide.api.editor.codeassist.CompletionProposal;
import org.eclipse.che.ide.api.editor.document.Document;
import org.eclipse.che.ide.api.editor.quickfix.QuickAssistInvocationContext;
import org.eclipse.che.ide.api.editor.quickfix.QuickAssistProcessor;
import org.eclipse.che.ide.api.editor.text.LinearRange;
import org.eclipse.che.ide.api.editor.text.Position;
import org.eclipse.che.ide.api.editor.text.TextPosition;
import org.eclipse.che.ide.api.editor.text.annotation.Annotation;
import org.eclipse.che.ide.api.icon.Icon;
import org.eclipse.che.ide.api.parts.PerspectiveManager;
import org.eclipse.che.plugin.languageserver.ide.editor.DiagnosticAnnotation;
import org.eclipse.che.plugin.languageserver.ide.service.TextDocumentServiceClient;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link QuickAssistProcessor} that implements LSP code actions as quick
 * assists.
 * 
 * @author Thomas Mäder
 *
 */
public class LanguageServerQuickAssistProcessor implements QuickAssistProcessor {
    private TextDocumentServiceClient textDocumentService;
    private ActionManager             actionManager;
    private PerspectiveManager        perspectiveManager;

    @Inject
    public LanguageServerQuickAssistProcessor(TextDocumentServiceClient textDocumentService, ActionManager actionManager,
                                              PerspectiveManager perspectiveManager) {
        this.textDocumentService = textDocumentService;
        this.actionManager = actionManager;
        this.perspectiveManager = perspectiveManager;
    }

    @Override
    public void computeQuickAssistProposals(QuickAssistInvocationContext invocationContext, CodeAssistCallback callback) {
        LinearRange range = invocationContext.getTextEditor().getSelectedLinearRange();
        Document document = invocationContext.getTextEditor().getDocument();
        QueryAnnotationsEvent.AnnotationFilter filter = new QueryAnnotationsEvent.AnnotationFilter() {

            @Override
            public boolean accept(Annotation annotation) {
                return annotation instanceof DiagnosticAnnotation;
            }

        };
        QueryAnnotationsEvent.QueryCallback annotationCallback = new QueryAnnotationsEvent.QueryCallback() {

            @Override
            public void respond(Map<Annotation, Position> annotations) {
                List<DiagnosticDTO> diagnostics = new ArrayList<>();
                // iteration with range never returns anything; need to filter
                // ourselves.
                // https://github.com/eclipse/che/issues/4338
                annotations.entrySet().stream().filter((e) -> e.getValue().overlapsWith(range.getStartOffset(), range.getLength()))
                                .map(Entry::getKey).map(a -> (DiagnosticAnnotation) a).map(DiagnosticAnnotation::getDiagnostic)
                                .collect(Collectors.toList());

                CodeActionContextDTO context = CodeActionContextDTOImpl.make().withDiagnostics(diagnostics);
                CodeActionParamsDTO params = CodeActionParamsDTOImpl.make().withContext(context);

                params.setTextDocument(TextDocumentIdentifierDTOImpl.make().withUri(document.getFile().getLocation().toString()));

                TextPosition start = document.getPositionFromIndex(range.getStartOffset());
                TextPosition end = document.getPositionFromIndex(range.getStartOffset() + range.getLength());
                PositionDTO rangeStart = PositionDTOImpl.make().withLine(start.getLine());
                rangeStart.setCharacter(start.getCharacter());
                PositionDTO rangeEnd = PositionDTOImpl.make().withLine(end.getLine());
                rangeEnd.setCharacter(end.getCharacter());
                RangeDTO rangeParam = RangeDTOImpl.make().withStart(rangeStart);
                rangeParam.setEnd(rangeEnd);
                params.setRange(rangeParam);

                Promise<List<CommandDTO>> codeAction = textDocumentService.codeAction(params);
                List<CompletionProposal> proposals = new ArrayList<>();
                codeAction.then((commands) -> {
                    for (CommandDTO command : commands) {
                        Action action = actionManager.getAction(command.getCommand());
                        if (action != null) {

                            proposals.add(new CompletionProposal() {

                                @Override
                                public void getAdditionalProposalInfo(AsyncCallback<Widget> callback) {
                                }

                                @Override
                                public String getDisplayString() {
                                    return command.getTitle();
                                }

                                @Override
                                public Icon getIcon() {
                                    return null;
                                }

                                @Override
                                public void getCompletion(CompletionCallback callback) {
                                    callback.onCompletion(new Completion() {

                                        @Override
                                        public LinearRange getSelection(Document document) {
                                            return null;
                                        }

                                        @Override
                                        public void apply(Document document) {
                                            QuickassistActionEvent evt = new QuickassistActionEvent(new Presentation(), actionManager,
                                                            perspectiveManager, command.getArguments());
                                            action.actionPerformed(evt);
                                        }
                                    });
                                }
                            });
                        }
                    };
                    callback.proposalComputed(proposals);
                });
            }
        };
        QueryAnnotationsEvent event = new QueryAnnotationsEvent.Builder().withFilter(filter).withCallback(annotationCallback).build();
        document.getDocumentHandle().getDocEventBus().fireEvent(event);
    }

}
