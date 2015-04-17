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

import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.helios.beans.events.TriggerListener;

import java.util.Collection;

/**
 * The selection of a graph canvas
 */
public interface GraphCanvasSelection {

    void addVertex(Vertex vertex);

    void addVertexes(Collection<Vertex> vertexes);

    boolean containsVertex(Vertex vertex);

    void removeVertex(Vertex vertex);

    Collection<Vertex> getVertexes();

    void addLink(Link link);

    void addLinks(Collection<Link> links);

    boolean containsLink(Link link);

    void removeLink(Link link);

    Collection<Link> getLinks();

    void clear();

    void addChangedListener(TriggerListener listener);

    void removeChangedListener(TriggerListener listener);

    void startAtomicSection();

    void stopAtomicSection();

    boolean isEmpty();

    boolean hasOneItem();
}
