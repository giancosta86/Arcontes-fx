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
import info.gianlucacosta.helios.beans.events.AtomicTriggerEvent;
import info.gianlucacosta.helios.beans.events.TriggerListener;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of GraphCanvasSelection
 */
class DefaultGraphCanvasSelection implements GraphCanvasSelection {

    private final Set<Vertex> vertexes = new HashSet<>();
    private final Set<Link> links = new HashSet<>();
    private final AtomicTriggerEvent changedEvent = new AtomicTriggerEvent();

    @Override
    public void addVertex(Vertex vertex) {
        vertexes.add(vertex);

        changedEvent.fire();
    }

    @Override
    public void addVertexes(Collection<Vertex> vertexes) {
        this.vertexes.addAll(vertexes);

        changedEvent.fire();
    }

    @Override
    public boolean containsVertex(Vertex vertex) {
        return vertexes.contains(vertex);
    }

    @Override
    public void removeVertex(Vertex vertex) {
        vertexes.remove(vertex);

        changedEvent.fire();
    }

    @Override
    public Collection<Vertex> getVertexes() {
        return Collections.unmodifiableCollection(vertexes);
    }

    @Override
    public void addLink(Link link) {
        links.add(link);

        changedEvent.fire();
    }

    @Override
    public void addLinks(Collection<Link> links) {
        this.links.addAll(links);

        changedEvent.fire();
    }

    @Override
    public boolean containsLink(Link link) {
        return links.contains(link);
    }

    @Override
    public void removeLink(Link link) {
        links.remove(link);

        changedEvent.fire();
    }

    @Override
    public Collection<Link> getLinks() {
        return Collections.unmodifiableCollection(links);
    }

    @Override
    public void clear() {
        vertexes.clear();
        links.clear();

        changedEvent.fire();
    }

    @Override
    public boolean isEmpty() {
        return vertexes.size() + links.size() == 0;
    }

    @Override
    public void addChangedListener(TriggerListener listener) {
        changedEvent.addListener(listener);
    }

    @Override
    public void removeChangedListener(TriggerListener listener) {
        changedEvent.removeListener(listener);
    }

    @Override
    public void startAtomicSection() {
        changedEvent.startAtomicSection();
    }

    @Override
    public void stopAtomicSection() {
        changedEvent.stopAtomicSection();
    }

    @Override
    public boolean hasOneItem() {
        return vertexes.size() + links.size() == 1;
    }
}
