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

package info.gianlucacosta.arcontes.fx.rendering;

import info.gianlucacosta.arcontes.fx.rendering.metainfo.GraphRenderingInfo;
import info.gianlucacosta.arcontes.graphs.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.*;

/**
 * Implementation of GraphRenderer
 */
public class DefaultGraphRenderer extends Pane implements GraphRenderer {

    private final Rectangle clippingRectangle = new Rectangle(0, 0);
    private GraphContext graphContext;
    private final Map<Vertex, VertexRenderer> vertexRenderers = new HashMap<>();
    private final Map<Link, LinkRenderer> linkRenderers = new HashMap<>();
    private final VertexListener graphVertexListener;
    private final LinkListener graphLinkListener;
    private final Rectangle backgroundRectangle;
    private transient List<GraphRendererListener> graphRendererListeners = new ArrayList<>();

    public DefaultGraphRenderer() {
        clippingRectangle.widthProperty().bind(this.widthProperty());
        clippingRectangle.heightProperty().bind(this.heightProperty());
        setClip(clippingRectangle);

        backgroundRectangle = new Rectangle();
        backgroundRectangle.widthProperty().bind(this.widthProperty());
        backgroundRectangle.heightProperty().bind(this.heightProperty());

        getChildren().add(backgroundRectangle);

        graphVertexListener = new VertexAdapter() {
            @Override
            public void onVertexAdded(Graph graph, Vertex vertex) {
                DefaultVertexRenderer vertexRenderer = new DefaultVertexRenderer(graphContext.getMetaInfoRepository(), vertex);

                getChildren().add(vertexRenderer);

                vertexRenderers.put(vertex, vertexRenderer);

                for (GraphRendererListener graphRendererListener : getGraphRendererListeners()) {
                    graphRendererListener.onVertexRendererAdded(vertexRenderer);
                }
            }

            @Override
            public void onVertexRemoved(Graph graph, Vertex vertex) {
                DefaultVertexRenderer vertexRenderer = (DefaultVertexRenderer) vertexRenderers.remove(vertex);
                getChildren().remove(vertexRenderer);

                for (GraphRendererListener graphRendererListener : getGraphRendererListeners()) {
                    graphRendererListener.onVertexRendererRemoved(vertexRenderer);
                }
            }

        };

        graphLinkListener = new LinkAdapter() {
            @Override
            public void onLinkAdded(Graph graph, Link link) {
                DefaultLinkRenderer linkRenderer = new DefaultLinkRenderer(graphContext.getMetaInfoRepository(), link);

                //The link must be added at the end of the sequence of links, before the vertexes. The "1" term in the addition is due to the backgound rectangle
                getChildren().add(1 + linkRenderers.size(), linkRenderer);

                linkRenderers.put(link, linkRenderer);

                for (GraphRendererListener graphRendererListener : getGraphRendererListeners()) {
                    graphRendererListener.onLinkRendererAdded(linkRenderer);
                }
            }

            @Override
            public void onLinkRemoved(Graph graph, Link link) {
                DefaultLinkRenderer linkRenderer = (DefaultLinkRenderer) linkRenderers.remove(link);
                getChildren().remove(linkRenderer);

                for (GraphRendererListener graphRendererListener : getGraphRendererListeners()) {
                    graphRendererListener.onLinkRendererRemoved(linkRenderer);
                }
            }

        };
    }

    @Override
    public GraphContext getGraphContext() {
        return graphContext;
    }

    @Override
    public void setGraphContext(GraphContext graphContext) {
        if (this.graphContext != null) {
            Graph oldGraph = this.graphContext.getGraph();
            oldGraph.removeVertexListener(graphVertexListener);
            oldGraph.removeLinkListener(graphLinkListener);
        }

        vertexRenderers.clear();
        linkRenderers.clear();

        this.graphContext = graphContext;

        Graph newGraph = graphContext.getGraph();
        newGraph.addVertexListener(graphVertexListener);
        newGraph.addLinkListener(graphLinkListener);

        getChildren().clear();
        getChildren().add(backgroundRectangle);

        for (Vertex vertex : newGraph.getVertexes()) {
            graphVertexListener.onVertexAdded(newGraph, vertex);
        }

        for (Link link : newGraph.getLinks()) {
            graphLinkListener.onLinkAdded(newGraph, link);
        }
    }

    @Override
    public void render() {
        GraphRenderingInfo renderingInfo = graphContext.getMetaInfoRepository().getMetaInfo(graphContext.getGraph(), GraphRenderingInfo.class);

        double width = renderingInfo.getWidth();
        double height = renderingInfo.getHeight();

        setWidth(width);
        setHeight(height);
        setMaxSize(width, height);

        backgroundRectangle.setFill(renderingInfo.getBackgroundColor());

        for (VertexRenderer vertexRenderer : vertexRenderers.values()) {
            vertexRenderer.render();
        }

        for (LinkRenderer linkRenderer : linkRenderers.values()) {
            linkRenderer.render();
        }
    }

    @Override
    public Collection<VertexRenderer> getVertexRenderers() {
        return Collections.unmodifiableCollection(vertexRenderers.values());
    }

    @Override
    public VertexRenderer getVertexRenderer(Vertex vertex) {
        return vertexRenderers.get(vertex);
    }

    @Override
    public Collection<LinkRenderer> getLinkRenderers() {
        return Collections.unmodifiableCollection(linkRenderers.values());
    }

    @Override
    public LinkRenderer getLinkRenderer(Link link) {
        return linkRenderers.get(link);
    }

    @Override
    public void addGraphRendererListener(GraphRendererListener listener) {
        getGraphRendererListeners().add(listener);
    }

    @Override
    public void removeGraphRendererListener(GraphRendererListener listener) {
        getGraphRendererListeners().remove(listener);
    }

    @Override
    public Node asNode() {
        return this;
    }

    private Collection<GraphRendererListener> getGraphRendererListeners() {
        if (graphRendererListeners == null) {
            graphRendererListeners = new ArrayList<>();
        }

        return graphRendererListeners;
    }

}
