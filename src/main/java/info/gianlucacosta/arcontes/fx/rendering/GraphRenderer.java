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

import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.helios.fx.events.EventHandlersTarget;
import javafx.scene.Node;

import java.util.Collection;

/**
 * Renders a graph and its components.
 * <p>
 * Most methods do not automatically trigger the rendering, which must be
 * programmatically performed by calling the render() method.
 */
public interface GraphRenderer extends EventHandlersTarget {

    /**
     * Returns the graph context of the graph renderer
     *
     * @return an instance of GraphContext
     */
    GraphContext getGraphContext();

    /**
     * Sets the graph context shown by the graph renderer
     *
     * @param graphContext a graph context
     */
    void setGraphContext(GraphContext graphContext);

    /**
     * Forces the graph renderer to redraw its underlying graph context
     */
    void render();

    /**
     * Returns the collection of vertex renderers used by the graph renderer
     *
     * @return an immutable collection
     */
    Collection<VertexRenderer> getVertexRenderers();

    /**
     * Returns the vertex renderer associated with the given vertex
     *
     * @param vertex a vertex
     * @return the related vertex renderer
     */
    VertexRenderer getVertexRenderer(Vertex vertex);

    /**
     * Returns the collection of link renderers used by the graph renderer
     *
     * @return an immutable collection
     */
    Collection<LinkRenderer> getLinkRenderers();

    /**
     * Returns the link renderer associated with the given link
     *
     * @param link a link
     * @return the related link renderer
     */
    LinkRenderer getLinkRenderer(Link link);

    void addGraphRendererListener(GraphRendererListener listener);

    void removeGraphRendererListener(GraphRendererListener listener);

    /**
     * Method to integrate GraphRenderer in the JavaFX scene
     *
     * @return the graph renderer as a JavaFX node
     */
    Node asNode();

}
