/*§
  ===========================================================================
  Arcontes - FX
  ===========================================================================
  Copyright (C) 2013-2015 Gianluca Costa
  ===========================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ===========================================================================
*/

package info.gianlucacosta.arcontes.fx.canvas;

import info.gianlucacosta.arcontes.fx.canvas.metainfo.DefaultLinkStyleInfo;
import info.gianlucacosta.arcontes.fx.canvas.metainfo.LinkStyleInfo;
import info.gianlucacosta.arcontes.fx.canvas.metainfo.SelectionRectangleInfo;
import info.gianlucacosta.arcontes.fx.rendering.*;
import info.gianlucacosta.arcontes.fx.rendering.metainfo.*;
import info.gianlucacosta.arcontes.graphs.*;
import info.gianlucacosta.helios.collections.general.CollectionItems;
import info.gianlucacosta.helios.fx.geometry.RelativeScenePoint;
import info.gianlucacosta.helios.metainfo.MetaInfoAgent;
import info.gianlucacosta.helios.metainfo.MetaInfoException;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Registers and coordinates the event listeners related to the direct user
 * interaction with DefaultGraphCanvas
 */
public class DefaultGraphCanvasInteractionCoordinator implements GraphCanvasInteractionCoordinator {

    private static final Logger logger = LoggerFactory.getLogger(DefaultGraphCanvas.class);
    private final double linkUnderCursorOpacity = 0.5;
    private final Rectangle selectionRectangle;
    private boolean dragging;
    private boolean inLinkPointRenderer;
    private Point2D latestDeltaSourcePoint;
    private DefaultGraphCanvas graphCanvas;

    public DefaultGraphCanvasInteractionCoordinator() {
        selectionRectangle = new Rectangle();
    }

    @Override
    public void attachToCanvas(GraphCanvas graphCanvas) {
        if (this.graphCanvas != null) {
            throw new IllegalStateException();
        }

        this.graphCanvas = (DefaultGraphCanvas) graphCanvas;

        attachEventHandlersToGraphRenderer();
    }

    private void attachEventHandlersToGraphRenderer() {
        graphCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if ((event.getButton() != MouseButton.PRIMARY) || (event.getClickCount() != 1)) {
                    return;
                }

                event.consume();

                latestDeltaSourcePoint = new Point2D(event.getX(), event.getY());

                selectionRectangle.setX(event.getX());
                selectionRectangle.setY(event.getY());
                selectionRectangle.setWidth(0);
                selectionRectangle.setHeight(0);

                GraphContext graphContext = graphCanvas.getGraphContext();
                MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();
                Graph graph = graphContext.getGraph();

                SelectionRectangleInfo selectionRectangleInfo = metaInfoRepository.getMetaInfo(graph, SelectionRectangleInfo.class);
                selectionRectangle.setStrokeWidth(selectionRectangleInfo.getBorderSize());
                selectionRectangle.setStroke(selectionRectangleInfo.getBorderColor());
                selectionRectangle.setFill(selectionRectangleInfo.getBackgroundColor());
                selectionRectangle.setOpacity(selectionRectangleInfo.getOpacity());

                graphCanvas.getChildren().add(selectionRectangle);
            }

        });

        graphCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if ((event.getButton() != MouseButton.PRIMARY) || (event.getClickCount() != 1)) {
                    return;
                }

                event.consume();

                //This condition might occur under some circumstances
                if (latestDeltaSourcePoint == null) {
                    return;
                }

                dragging = true;

                double currentX = Math.max(0, Math.min(
                        event.getX(),
                        graphCanvas.getWidth()));

                double currentY = Math.max(0, Math.min(
                        event.getY(),
                        graphCanvas.getHeight()));

                selectionRectangle.setX(Math.min(currentX, latestDeltaSourcePoint.getX()));
                selectionRectangle.setY(Math.min(currentY, latestDeltaSourcePoint.getY()));

                selectionRectangle.setWidth(Math.abs(currentX - latestDeltaSourcePoint.getX()));
                selectionRectangle.setHeight(Math.abs(currentY - latestDeltaSourcePoint.getY()));
            }

        });

        graphCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();

                graphCanvas.getChildren().remove(selectionRectangle);

                if ((event.getButton() != MouseButton.PRIMARY) || (event.getClickCount() != 1)) {
                    graphCanvas.getSelection().clear();
                    return;
                }

                Point2D releasePoint = new Point2D(event.getX(), event.getY());

                try {
                    if (graphCanvas.getPermissions().isCanCreateVertexes() && releasePoint.distance(latestDeltaSourcePoint) < 3) {
                        graphCanvas.getSelection().clear();

                        Vertex vertex = createVertex();

                        GraphContext graphContext = graphCanvas.getGraphContext();
                        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();
                        Graph graph = graphContext.getGraph();

                        metaInfoRepository.putMetaInfo(vertex, new DefaultVertexCenterInfo(releasePoint));

                        MetaInfoAgent<Vertex> agentForVertexCreation = graphCanvas.getMetaInfoAgentsFactory().getAgentForVertexCreation(graphContext, vertex);
                        if (!agentForVertexCreation.act(metaInfoRepository, vertex)) {
                            return;
                        }

                        graph.addVertex(vertex);

                        graphCanvas.render();

                        graphCanvas.fireManualModificationEvent();

                        return;
                    }
                } finally {
                    dragging = false;
                }

                GraphCanvasSelection selection = graphCanvas.getSelection();
                selection.startAtomicSection();
                try {
                    if (!event.isControlDown()) {
                        selection.clear();
                    }

                    GraphRenderer graphRenderer = graphCanvas.getGraphRenderer();

                    for (VertexRenderer vertexRenderer : graphRenderer.getVertexRenderers()) {
                        if (vertexRenderer.isInRectangle(selectionRectangle)) {
                            Vertex vertex = vertexRenderer.getVertex();

                            if (event.isControlDown() && selection.containsVertex(vertex)) {
                                selection.removeVertex(vertex);
                            } else {
                                selection.addVertex(vertex);
                            }
                        }
                    }

                    for (LinkRenderer linkRenderer : graphRenderer.getLinkRenderers()) {
                        if (linkRenderer.isInRectangle(selectionRectangle)) {
                            Link link = linkRenderer.getLink();

                            if (event.isControlDown() && selection.containsLink(link)) {
                                selection.removeLink(link);
                            } else {
                                selection.addLink(link);
                            }
                        }
                    }

                } finally {
                    selection.stopAtomicSection();
                }
            }

        });

        graphCanvas.addEventHandler(MouseDragEvent.MOUSE_DRAG_OVER, new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(final MouseDragEvent event) {
                if ((event.getButton() != MouseButton.PRIMARY) || (event.getClickCount() != 1)) {
                    return;
                }

                event.consume();

                if (graphCanvas.getPermissions().isCanDragVertexes()) {
                    dragSelectedVertexes(event.getSceneX(), event.getSceneY());
                }
            }

        });

        graphCanvas.addGraphRendererListener(new GraphRendererAdapter() {
            @Override
            public void onVertexRendererAdded(final VertexRenderer vertexRenderer) {
                attachEventHandlersToVertexRenderer(vertexRenderer);
            }

            @Override
            public void onVertexRendererRemoved(VertexRenderer vertexRenderer) {
                GraphContext graphContext = graphCanvas.getGraphContext();
                MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();
                metaInfoRepository.removeMetaInfo(vertexRenderer.getVertex());
            }

            @Override
            public void onLinkRendererAdded(final LinkRenderer linkRenderer) {
                attachEventHandlersToLinkRenderer(linkRenderer);
                attachEventHandlersToLinkLabel(linkRenderer);
                attachEventHandlersToLinkSegments(linkRenderer);
                attachEventHandlersToLinkInternalPointRenderers(linkRenderer);
            }

            @Override
            public void onLinkRendererRemoved(LinkRenderer linkRenderer) {
                GraphContext graphContext = graphCanvas.getGraphContext();
                MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

                metaInfoRepository.removeMetaInfo(linkRenderer.getFirstCenterLinkPoint());
                metaInfoRepository.removeMetaInfo(linkRenderer.getSecondCenterLinkPoint());

                LinkInternalPointsInfo linkInternalPointsInfo = metaInfoRepository.getMetaInfo(linkRenderer.getLink(), LinkInternalPointsInfo.class);

                for (LinkPoint linkInternalPoint : linkInternalPointsInfo.getInternalPoints()) {
                    metaInfoRepository.removeMetaInfo(linkInternalPoint);
                }

                metaInfoRepository.removeMetaInfo(linkRenderer.getLink());
            }

        });
    }

    private void attachEventHandlersToVertexRenderer(VertexRenderer vertexRenderer) {
        final Vertex vertex = vertexRenderer.getVertex();

        vertexRenderer.addEventHandler(MouseDragEvent.MOUSE_DRAG_OVER, new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(final MouseDragEvent event) {
                if ((event.getButton() != MouseButton.PRIMARY) || (event.getClickCount() != 1)) {
                    return;
                }

                event.consume();

                if (graphCanvas.getPermissions().isCanDragVertexes()) {
                    dragSelectedVertexes(event.getSceneX(), event.getSceneY());
                }
            }

        });

        vertexRenderer.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();

                GraphCanvasSelection selection = graphCanvas.getSelection();

                switch (event.getButton()) {
                    case PRIMARY: {
                        selection.startAtomicSection();
                        try {
                            if (!event.isControlDown()) {
                                if (!selection.containsVertex(vertex)) {
                                    selection.clear();
                                    selection.addVertex(vertex);
                                }
                            } else {
                                if (!selection.containsVertex(vertex)) {
                                    selection.addVertex(vertex);
                                } else {
                                    selection.removeVertex(vertex);
                                }
                            }
                        } finally {
                            selection.stopAtomicSection();
                        }

                        switch (event.getClickCount()) {
                            case 1: {
                                latestDeltaSourcePoint = new Point2D(event.getSceneX(), event.getSceneY());
                                break;
                            }

                            case 2: {
                                if (graphCanvas.getPermissions().isCanEditVertexes()) {
                                    graphCanvas.editVertex(vertex);
                                }

                                break;
                            }
                        }
                        break;
                    }

                    case SECONDARY: {
                        if (!graphCanvas.getPermissions().isCanCreateLinks() || (selection.getVertexes().size() != 1)) {
                            return;
                        }

                        Vertex originVertex = CollectionItems.getSingle(selection.getVertexes());
                        Vertex targetVertex = vertex;

                        if (Objects.equals(originVertex, targetVertex)) {
                            return;
                        }

                        Link link = createLink(originVertex, targetVertex);

                        GraphContext graphContext = graphCanvas.getGraphContext();
                        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();
                        Graph graph = graphContext.getGraph();

                        MetaInfoAgent<Link> agentForLinkCreation = graphCanvas.getMetaInfoAgentsFactory().getAgentForLinkCreation(graphContext, link);
                        if (!agentForLinkCreation.act(metaInfoRepository, link)) {
                            return;
                        }

                        graph.addLink(link);

                        graphCanvas.render();

                        graphCanvas.fireManualModificationEvent();
                    }
                }
            }

        });

        vertexRenderer.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (!graphCanvas.getPermissions().isCanDragVertexes() || (event.getButton() != MouseButton.PRIMARY) || (event.getClickCount() != 1)) {
                            return;
                        }

                        event.consume();

                        if (!dragging) {
                            return;
                        }

                        dragging = false;

                        graphCanvas.fireManualModificationEvent();
                    }

                });
    }

    private void attachEventHandlersToLinkRenderer(final LinkRenderer linkRenderer) {
        final Link link = linkRenderer.getLink();

        linkRenderer.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();

                GraphContext graphContext = graphCanvas.getGraphContext();
                MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

                DefaultLinkRenderingInfo linkRenderingInfo = new DefaultLinkRenderingInfo(
                        metaInfoRepository.getMetaInfo(link, LinkRenderingInfo.class));

                linkRenderingInfo.setOpacity(linkUnderCursorOpacity);
                metaInfoRepository.putMetaInfo(link, linkRenderingInfo);

                DefaultLinkLabelConnectorRenderingInfo linkLabelConnectorRenderingInfo = new DefaultLinkLabelConnectorRenderingInfo(
                        metaInfoRepository.getMetaInfo(link, LinkLabelConnectorRenderingInfo.class));
                linkLabelConnectorRenderingInfo.setOpacity(1);
                metaInfoRepository.putMetaInfo(link, linkLabelConnectorRenderingInfo);

                DefaultLinkStyleInfo linkStyleInfo = new DefaultLinkStyleInfo(
                        metaInfoRepository.getMetaInfo(link, LinkStyleInfo.class));

                DefaultLinkRenderingInfo nonSelectedLinkRenderingInfo = linkStyleInfo.getNonSelectedRenderingInfo();
                nonSelectedLinkRenderingInfo.setOpacity(linkUnderCursorOpacity);
                linkStyleInfo.getNonSelectedLabelConnectorRenderingInfo().setOpacity(1);

                DefaultLinkRenderingInfo selectedLinkRenderingInfo = linkStyleInfo.getSelectedRenderingInfo();
                selectedLinkRenderingInfo.setOpacity(linkUnderCursorOpacity);
                linkStyleInfo.getSelectedLabelConnectorRenderingInfo().setOpacity(1);

                metaInfoRepository.putMetaInfo(link, linkStyleInfo);

                linkRenderer.render();
            }

        });

        linkRenderer.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();

                GraphContext graphContext = graphCanvas.getGraphContext();
                MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

                DefaultLinkRenderingInfo linkRenderingInfo = new DefaultLinkRenderingInfo(
                        metaInfoRepository.getMetaInfo(link, LinkRenderingInfo.class));
                linkRenderingInfo.setOpacity(1);
                metaInfoRepository.putMetaInfo(link, linkRenderingInfo);

                DefaultLinkLabelConnectorRenderingInfo labelConnectorRenderingInfo = new DefaultLinkLabelConnectorRenderingInfo(
                        metaInfoRepository.getMetaInfo(link, LinkLabelConnectorRenderingInfo.class));

                labelConnectorRenderingInfo.setOpacity(0);
                metaInfoRepository.putMetaInfo(link, labelConnectorRenderingInfo);

                DefaultLinkStyleInfo linkStyleInfo = new DefaultLinkStyleInfo(
                        metaInfoRepository.getMetaInfo(link, LinkStyleInfo.class));

                DefaultLinkRenderingInfo nonSelectedLinkRenderingInfo = linkStyleInfo.getNonSelectedRenderingInfo();
                nonSelectedLinkRenderingInfo.setOpacity(1);
                linkStyleInfo.getNonSelectedLabelConnectorRenderingInfo().setOpacity(0);

                DefaultLinkRenderingInfo selectedLinkRenderingInfo = linkStyleInfo.getSelectedRenderingInfo();
                selectedLinkRenderingInfo.setOpacity(1);
                linkStyleInfo.getSelectedLabelConnectorRenderingInfo().setOpacity(0);

                metaInfoRepository.putMetaInfo(link, linkStyleInfo);

                linkRenderer.render();
            }

        });

        linkRenderer.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() != MouseButton.PRIMARY) {
                    return;
                }

                event.consume();

                GraphCanvasSelection selection = graphCanvas.getSelection();

                selection.startAtomicSection();

                try {
                    if (!event.isControlDown()) {
                        selection.clear();
                        selection.addLink(link);
                    } else {
                        if (!selection.containsLink(link)) {
                            selection.addLink(link);
                        } else {
                            selection.removeLink(link);
                        }
                    }
                } finally {
                    selection.stopAtomicSection();
                }

                if (graphCanvas.getPermissions().isCanEditLinks() && event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                    graphCanvas.editLink(link);
                }
            }

        });
    }

    private void attachEventHandlersToLinkLabel(final LinkRenderer linkRenderer) {
        final Link link = linkRenderer.getLink();

        linkRenderer.addEventHandlerForLabel(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() != MouseButton.PRIMARY || event.getClickCount() != 1) {
                    return;
                }

                event.consume();

                latestDeltaSourcePoint = new RelativeScenePoint(event.getSceneX(), event.getSceneY(), graphCanvas);
            }

        });

        linkRenderer.addEventHandlerForLabel(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!graphCanvas.getPermissions().isCanDragLinkLabels() || event.getButton() != MouseButton.PRIMARY || event.getClickCount() != 1) {
                    return;
                }

                event.consume();

                //This condition might occur under some circumstances
                if (latestDeltaSourcePoint == null) {
                    return;
                }

                dragging = true;

                Point2D relativeEventPoint = new RelativeScenePoint(event.getSceneX(), event.getSceneY(), graphCanvas);

                double deltaX = relativeEventPoint.getX() - latestDeltaSourcePoint.getX();
                double deltaY = relativeEventPoint.getY() - latestDeltaSourcePoint.getY();

                GraphContext graphContext = graphCanvas.getGraphContext();
                MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

                Point2D currentCenter;
                try {
                    currentCenter = metaInfoRepository.getMetaInfo(link, LinkLabelCenterInfo.class).getPoint();
                } catch (MetaInfoException ex) {
                    logger.trace("The link label appears to have no manually set center. Assuming the default value...");
                    currentCenter = linkRenderer.getDefaultLabelCenter();
                }

                double newCenterX = Math.min(Math.max(currentCenter.getX() + deltaX, 0), graphCanvas.getWidth());
                double newCenterY = Math.min(Math.max(currentCenter.getY() + deltaY, 0), graphCanvas.getHeight());

                Point2D newCenter = new Point2D(newCenterX, newCenterY);

                metaInfoRepository.putMetaInfo(link, new DefaultLinkLabelCenterInfo(newCenter));

                latestDeltaSourcePoint = relativeEventPoint;

                linkRenderer.render();
            }

        });

        linkRenderer.addEventHandlerForLabel(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() != MouseButton.PRIMARY || event.getClickCount() != 1) {
                    return;
                }
                event.consume();

                if (!graphCanvas.getPermissions().isCanDragLinkLabels() || !dragging) {
                    return;
                }

                dragging = false;

                graphCanvas.fireManualModificationEvent();
            }

        });

    }

    private void attachEventHandlersToLinkSegments(final LinkRenderer linkRenderer) {
        final Link link = linkRenderer.getLink();

        linkRenderer.addEventHandlerForLinkSegments(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!graphCanvas.getPermissions().isCanCreateLinkPoints() || event.getButton() != MouseButton.SECONDARY || event.getClickCount() != 1) {
                    return;
                }

                event.consume();

                GraphContext graphContext = graphCanvas.getGraphContext();
                MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

                LinkSegmentRenderer linkSegment = (LinkSegmentRenderer) event.getTarget();

                LinkInternalPointsInfo linkInternalPointsInfo = metaInfoRepository.getMetaInfo(link, LinkInternalPointsInfo.class);

                LinkPoint linkPoint = new DefaultLinkPoint();
                metaInfoRepository.putMetaInfo(linkPoint, new DefaultLinkPointPositionInfo(event.getX(), event.getY()));

                List<LinkPoint> internalPoints = new ArrayList<>(linkInternalPointsInfo.getInternalPoints());

                int insertionPoint = internalPoints.indexOf(linkSegment.getEndLinkPoint());
                if (insertionPoint < 0) { //This happens then the user clicks on the last segment, whose final point is the end vertex and, therefore, NOT an internal point
                    insertionPoint = internalPoints.size();
                }

                internalPoints.add(insertionPoint, linkPoint);
                metaInfoRepository.putMetaInfo(link, new DefaultLinkInternalPointsInfo(internalPoints));
                metaInfoRepository.putMetaInfo(linkPoint, new DefaultLinkPointRenderingInfo());

                linkRenderer.render();

                graphCanvas.fireManualModificationEvent();
            }

        });
    }

    private void attachEventHandlersToLinkInternalPointRenderers(final LinkRenderer linkRenderer) {
        final Link link = linkRenderer.getLink();

        linkRenderer.addEventHandlerForInternalPointRenderers(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();

                switch (event.getButton()) {
                    case PRIMARY: {
                        if (event.getClickCount() != 1) {
                            return;
                        }

                        latestDeltaSourcePoint = new RelativeScenePoint(event.getSceneX(), event.getSceneY(), graphCanvas);
                        break;
                    }

                    case SECONDARY: {
                        if (!graphCanvas.getPermissions().isCanRemoveLinkPoints() || event.getClickCount() != 1) {
                            return;
                        }
                        LinkPointRenderer internalPointRenderer = (LinkPointRenderer) event.getTarget();
                        LinkPoint linkPoint = internalPointRenderer.getLinkPoint();

                        GraphContext graphContext = graphCanvas.getGraphContext();
                        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

                        LinkInternalPointsInfo linkInternalPointsInfo = metaInfoRepository.getMetaInfo(link, LinkInternalPointsInfo.class);

                        List<LinkPoint> internalPoints = new ArrayList<>(linkInternalPointsInfo.getInternalPoints());
                        internalPoints.remove(linkPoint);

                        metaInfoRepository.putMetaInfo(link, new DefaultLinkInternalPointsInfo(internalPoints));
                        metaInfoRepository.removeMetaInfo(linkPoint);

                        linkRenderer.render();

                        graphCanvas.fireManualModificationEvent();

                        break;
                    }
                }
            }

        });

        linkRenderer.addEventHandlerForInternalPointRenderers(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();

                inLinkPointRenderer = true;

                LinkPointRenderer internalPointRenderer = (LinkPointRenderer) event.getTarget();
                LinkPoint linkPoint = internalPointRenderer.getLinkPoint();

                GraphContext graphContext = graphCanvas.getGraphContext();
                MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

                DefaultLinkPointRenderingInfo linkPointRenderingInfo
                        = new DefaultLinkPointRenderingInfo(metaInfoRepository.getMetaInfo(linkPoint, LinkPointRenderingInfo.class));

                linkPointRenderingInfo.setOpacity(1);

                metaInfoRepository.putMetaInfo(linkPoint, linkPointRenderingInfo);

                linkRenderer.render();
            }

        });

        linkRenderer.addEventHandlerForInternalPointRenderers(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();

                inLinkPointRenderer = false;

                if (dragging) {
                    return;
                }

                LinkPointRenderer internalPointRenderer = (LinkPointRenderer) event.getTarget();

                hideInternalPointRenderer(internalPointRenderer);

                linkRenderer.render();
            }

        });

        linkRenderer.addEventHandlerForInternalPointRenderers(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!graphCanvas.getPermissions().isCanDragLinkPoints() || event.getButton() != MouseButton.PRIMARY || event.getClickCount() != 1) {
                    return;
                }

                event.consume();

                //This condition might occur under some circumstances
                if (latestDeltaSourcePoint == null) {
                    return;
                }

                dragging = true;

                Point2D relativeEventPoint = new RelativeScenePoint(event.getSceneX(), event.getSceneY(), graphCanvas);

                double deltaX = relativeEventPoint.getX() - latestDeltaSourcePoint.getX();
                double deltaY = relativeEventPoint.getY() - latestDeltaSourcePoint.getY();

                LinkPointRenderer internalPointRenderer = (LinkPointRenderer) event.getTarget();
                LinkPoint linkPoint = internalPointRenderer.getLinkPoint();

                GraphContext graphContext = graphCanvas.getGraphContext();
                MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

                Point2D currentPoint = metaInfoRepository.getMetaInfo(linkPoint, LinkPointPositionInfo.class).getPoint();

                double newCenterX = Math.min(Math.max(currentPoint.getX() + deltaX, 0), graphCanvas.getWidth());
                double newCenterY = Math.min(Math.max(currentPoint.getY() + deltaY, 0), graphCanvas.getHeight());

                Point2D newPoint = new Point2D(newCenterX, newCenterY);
                metaInfoRepository.putMetaInfo(linkPoint, new DefaultLinkPointPositionInfo(newPoint));

                latestDeltaSourcePoint = relativeEventPoint;

                linkRenderer.render();
            }

        });

        linkRenderer.addEventHandlerForInternalPointRenderers(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    if (graphCanvas.getPermissions().isCanEditLinks()) {
                        graphCanvas.editLink(linkRenderer.getLink());
                    }
                }

                event.consume();
            }
        });

        linkRenderer.addEventHandlerForInternalPointRenderers(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!graphCanvas.getPermissions().isCanDragLinkPoints() || event.getButton() != MouseButton.PRIMARY || event.getClickCount() != 1) {
                    return;
                }

                event.consume();

                dragging = false;

                if (inLinkPointRenderer) {
                    graphCanvas.fireManualModificationEvent();
                    return;
                }

                LinkPointRenderer internalPointRenderer = (LinkPointRenderer) event.getTarget();
                hideInternalPointRenderer(internalPointRenderer);
                linkRenderer.render();

                graphCanvas.fireManualModificationEvent();
            }

        });
    }

    private void hideInternalPointRenderer(LinkPointRenderer internalPointRenderer) {
        LinkPoint linkPoint = internalPointRenderer.getLinkPoint();

        GraphContext graphContext = graphCanvas.getGraphContext();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        DefaultLinkPointRenderingInfo linkPointRenderingInfo;
        try {
            linkPointRenderingInfo = new DefaultLinkPointRenderingInfo(
                    metaInfoRepository.getMetaInfo(linkPoint, LinkPointRenderingInfo.class));
        } catch (MetaInfoException ex) {
            logger.trace("Cannot change the attributes of the link point, because it has no rendering info");
            return;
        }

        linkPointRenderingInfo.setOpacity(0);

        metaInfoRepository.putMetaInfo(linkPoint, linkPointRenderingInfo);
    }

    private void dragSelectedVertexes(double eventSceneX, double eventSceneY) {
        GraphCanvasSelection selection = graphCanvas.getSelection();

        GraphContext graphContext = graphCanvas.getGraphContext();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        if (selection.getVertexes().isEmpty()) {
            return;
        }

        dragging = true;

        double deltaX = eventSceneX - latestDeltaSourcePoint.getX();
        double deltaY = eventSceneY - latestDeltaSourcePoint.getY();

        for (Vertex vertex : selection.getVertexes()) {
            Point2D vertexCenter = metaInfoRepository.getMetaInfo(vertex, VertexCenterInfo.class).getPoint();

            double newCenterX = Math.min(Math.max(vertexCenter.getX() + deltaX, 0), graphCanvas.getWidth());
            double newCenterY = Math.min(Math.max(vertexCenter.getY() + deltaY, 0), graphCanvas.getHeight());

            Point2D newVertexCenter = new Point2D(newCenterX, newCenterY);

            metaInfoRepository.putMetaInfo(vertex, new DefaultVertexCenterInfo(newVertexCenter));
        }

        latestDeltaSourcePoint = new Point2D(eventSceneX, eventSceneY);

        graphCanvas.render();
    }

    @Override
    public void reset() {
        dragging = false;
        inLinkPointRenderer = false;
    }

    protected Vertex createVertex() {
        return new DefaultVertex();
    }

    protected Link createLink(Vertex originVertex, Vertex targetVertex) {
        return new DefaultLink(originVertex, targetVertex);
    }

}
