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

import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.helios.beans.events.TriggerListener;
import info.gianlucacosta.helios.fx.events.EventHandlersTarget;
import javafx.scene.Node;

/**
 * Renders a graph and enables users to visually interact with it
 * <p>
 * Most methods do not automatically trigger the canvas rendering, which must be
 * programmatically performed by calling the render() method.
 */
public interface GraphCanvas extends EventHandlersTarget {

    /**
     * Returns the graph context of the graph canvas
     *
     * @return an instance of GraphContext
     */
    GraphContext getGraphContext();

    /**
     * Sets the graph context on which the graph canvas operates
     *
     * @param graphContext the graph context
     */
    void setGraphContext(GraphContext graphContext);

    /**
     * Forces the graph canvas to redraw its underlying graph context
     */
    void render();

    /**
     * Returns an object letting you set the canvas-related user permissions
     *
     * @return the instance of the permissions object
     */
    GraphCanvasPermissions getPermissions();

    /**
     * Edits the given vertex, as if the user requested it via direct
     * interaction. This method, being requested programmatically, works even if
     * the user doesn't have permissions on vertex editing
     *
     * @param vertex a vertex
     * @return true if the user confirmed their changes; false otherwise
     */
    boolean editVertex(Vertex vertex);

    /**
     * Edits the given link, as if the user requested it via direct interaction.
     * This method, being requested programmatically, works even if the user
     * doesn't have permissions on link editing
     *
     * @param link a vertex
     * @return true if the user confirmed their changes; false otherwise
     */
    boolean editLink(Link link);

    /**
     * Enables you to retrieve and change the permissions granted to the user on
     * the graph canvas
     *
     * @return the object encapsulating the permissions
     */
    GraphCanvasSelection getSelection();

    /**
     * Selects all the items owned by the underlying graph
     */
    void selectAll();

    /**
     * Empties the current selection. Shortcut for getSelection().clear()
     */
    void selectNothing();

    /**
     * Deletes the selected items from the graph
     */
    void removeSelectedItems();

    void addManualModificationListener(TriggerListener listener);

    void removeManualModificationListener(TriggerListener listener);

    /**
     * Fires a "manual modification" event, simulating user intervention on the
     * graph canvas
     */
    void fireManualModificationEvent();

    /**
     * Method to integrate GraphCanvas in the JavaFX scene
     *
     * @return the graph canvas as a JavaFX node
     */
    Node asNode();

}
