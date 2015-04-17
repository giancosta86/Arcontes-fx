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

import info.gianlucacosta.arcontes.fx.canvas.metainfo.LinkStyleInfo;
import info.gianlucacosta.arcontes.fx.canvas.metainfo.VertexStyleInfo;
import info.gianlucacosta.arcontes.fx.rendering.GraphRenderer;
import info.gianlucacosta.arcontes.fx.rendering.GraphRendererListener;
import info.gianlucacosta.arcontes.fx.rendering.metainfo.*;
import info.gianlucacosta.arcontes.graphs.*;
import info.gianlucacosta.helios.beans.events.TriggerEvent;
import info.gianlucacosta.helios.beans.events.TriggerListener;
import info.gianlucacosta.helios.metainfo.MetaInfoAgent;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;

/**
 * Implementation of GraphCanvas
 */
public class DefaultGraphCanvas extends BorderPane implements GraphCanvas {

    private final GraphCanvasSelection selection = new DefaultGraphCanvasSelection();
    private final GraphRenderer graphRenderer;
    private final TriggerEvent manualModificationEvent = new TriggerEvent();
    private final GraphCanvasPermissions permissions = new DefaultGraphCanvasPermissions();
    private final GraphCanvasAgentsFactory canvasAgentsFactory;
    private final VertexListener vertexRemovedListenerUpdatingSelection;
    private final LinkListener linkRemovedListenerUpdatingSelection;
    private final GraphCanvasInteractionCoordinator interactionCoordinator;

    public DefaultGraphCanvas(final GraphRenderer graphRenderer, GraphCanvasAgentsFactory canvasAgentsFactory) {
        this(graphRenderer, canvasAgentsFactory, new DefaultGraphCanvasInteractionCoordinator());
    }

    public DefaultGraphCanvas(final GraphRenderer graphRenderer, GraphCanvasAgentsFactory canvasAgentsFactory, GraphCanvasInteractionCoordinator interactionCoordinator) {
        this.graphRenderer = graphRenderer;
        this.canvasAgentsFactory = canvasAgentsFactory;
        this.interactionCoordinator = interactionCoordinator;

        selection.addChangedListener(new TriggerListener() {
            @Override
            public void onTriggered() {
                render();
            }

        });

        vertexRemovedListenerUpdatingSelection = new VertexAdapter() {
            @Override
            public void onVertexRemoved(Graph graph, Vertex vertex) {
                selection.removeVertex(vertex);
            }

        };

        linkRemovedListenerUpdatingSelection = new LinkAdapter() {
            @Override
            public void onLinkRemoved(Graph graph, Link link) {
                selection.removeLink(link);
            }

        };

        setCenter(graphRenderer.asNode());

        interactionCoordinator.attachToCanvas(this);
    }

    @Override
    public GraphCanvasSelection getSelection() {
        return selection;
    }

    @Override
    public void selectAll() {
        GraphContext graphContext = getGraphContext();
        Graph graph = graphContext.getGraph();

        selection.startAtomicSection();

        selection.addVertexes(graph.getVertexes());
        selection.addLinks(graph.getLinks());

        selection.stopAtomicSection();
    }

    @Override
    public void selectNothing() {
        selection.clear();
    }

    @Override
    public void removeSelectedItems() {
        GraphContext graphContext = getGraphContext();
        Graph graph = graphContext.getGraph();

        for (Link link : new ArrayList<>(selection.getLinks())) {
            graph.removeLink(link);
        }

        for (Vertex vertex : new ArrayList<>(selection.getVertexes())) {
            graph.removeVertex(vertex);
        }

        render();
    }

    @Override
    public GraphContext getGraphContext() {
        return graphRenderer.getGraphContext();
    }

    @Override
    public void setGraphContext(GraphContext graphContext) {
        if (getGraphContext() != null) {
            Graph graph = graphContext.getGraph();
            graph.removeVertexListener(vertexRemovedListenerUpdatingSelection);
            graph.removeLinkListener(linkRemovedListenerUpdatingSelection);
        }

        Graph graph = graphContext.getGraph();

        graphRenderer.setGraphContext(graphContext);

        graph.addVertexListener(vertexRemovedListenerUpdatingSelection);
        graph.addLinkListener(linkRemovedListenerUpdatingSelection);

        graphContext.getGraph();

        interactionCoordinator.reset();

        selection.clear();
    }

    @Override
    public void render() {
        GraphContext graphContext = getGraphContext();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();
        Graph graph = graphContext.getGraph();

        canvasAgentsFactory.getAgentForGraph(graph).act(metaInfoRepository, graph);

        GraphRenderingInfo graphRenderingInfo = metaInfoRepository.getMetaInfo(graph, GraphRenderingInfo.class);

        double width = graphRenderingInfo.getWidth();
        double height = graphRenderingInfo.getHeight();

        setWidth(width);
        setHeight(height);
        setMaxSize(width, height);

        applyVertexStyleInfo();
        applyLinkStyleInfo();

        graphRenderer.render();
    }

    private void applyVertexStyleInfo() {
        GraphContext graphContext = getGraphContext();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();
        Graph graph = graphContext.getGraph();

        for (Vertex vertex : graph.getVertexes()) {
            VertexStyleInfo vertexStyleInfo = metaInfoRepository.getMetaInfo(vertex, VertexStyleInfo.class);

            VertexRenderingInfo vertexRenderingInfo;

            if (selection.containsVertex(vertex)) {
                vertexRenderingInfo = vertexStyleInfo.getSelectedRenderingInfo();
            } else {
                vertexRenderingInfo = vertexStyleInfo.getNonSelectedRenderingInfo();
            }

            metaInfoRepository.putMetaInfo(vertex, vertexRenderingInfo, VertexRenderingInfo.class);
        }
    }

    private void applyLinkStyleInfo() {
        GraphContext graphContext = getGraphContext();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();
        Graph graph = graphContext.getGraph();

        for (Link link : graph.getLinks()) {

            LinkStyleInfo linkStyleInfo = metaInfoRepository.getMetaInfo(link, LinkStyleInfo.class);

            LinkRenderingInfo linkRenderingInfo;
            LinkLabelRenderingInfo linkLabelRenderingInfo;
            LinkLabelConnectorRenderingInfo linkLabelConnectorRenderingInfo;
            LinkTailRenderingInfo linkTailRenderingInfo;

            if (selection.containsLink(link)) {
                linkRenderingInfo = linkStyleInfo.getSelectedRenderingInfo();
                linkLabelRenderingInfo = linkStyleInfo.getSelectedLabelRenderingInfo();
                linkLabelConnectorRenderingInfo = linkStyleInfo.getSelectedLabelConnectorRenderingInfo();
                linkTailRenderingInfo = linkStyleInfo.getSelectedTailRenderingInfo();
            } else {
                linkRenderingInfo = linkStyleInfo.getNonSelectedRenderingInfo();
                linkLabelRenderingInfo = linkStyleInfo.getNonSelectedLabelRenderingInfo();
                linkLabelConnectorRenderingInfo = linkStyleInfo.getNonSelectedLabelConnectorRenderingInfo();
                linkTailRenderingInfo = linkStyleInfo.getNonSelectedTailRenderingInfo();
            }

            metaInfoRepository.putMetaInfo(link, linkRenderingInfo);
            metaInfoRepository.putMetaInfo(link, linkLabelRenderingInfo);
            metaInfoRepository.putMetaInfo(link, linkLabelConnectorRenderingInfo);
            metaInfoRepository.putMetaInfo(link, linkTailRenderingInfo);
        }
    }

    @Override
    public void addManualModificationListener(TriggerListener listener) {
        manualModificationEvent.addListener(listener);
    }

    @Override
    public void removeManualModificationListener(TriggerListener listener) {
        manualModificationEvent.removeListener(listener);
    }

    @Override
    public void fireManualModificationEvent() {
        manualModificationEvent.fire();
    }

    @Override
    public boolean editVertex(Vertex vertex) {
        MetaInfoAgent<Vertex> agentForVertexEditing = canvasAgentsFactory.getAgentForVertexEditing(getGraphContext(), vertex);

        if (agentForVertexEditing.act(getGraphContext().getMetaInfoRepository(), vertex)) {
            render();
            manualModificationEvent.fire();
            return true;
        }

        return false;
    }

    @Override
    public boolean editLink(Link link) {
        MetaInfoAgent<Link> agentForLinkEditing = canvasAgentsFactory.getAgentForLinkEditing(getGraphContext(), link);

        if (agentForLinkEditing.act(getGraphContext().getMetaInfoRepository(), link)) {
            render();

            manualModificationEvent.fire();

            return true;
        }

        return false;
    }

    @Override
    public Node asNode() {
        return this;
    }

    @Override
    public GraphCanvasPermissions getPermissions() {
        return permissions;
    }

    protected GraphCanvasAgentsFactory getMetaInfoAgentsFactory() {
        return canvasAgentsFactory;
    }

    protected void addGraphRendererListener(GraphRendererListener listener) {
        graphRenderer.addGraphRendererListener(listener);
    }

    protected void removeGraphRendererListener(GraphRendererListener listener) {
        graphRenderer.removeGraphRendererListener(listener);
    }

    GraphRenderer getGraphRenderer() {
        return graphRenderer;
    }

}
