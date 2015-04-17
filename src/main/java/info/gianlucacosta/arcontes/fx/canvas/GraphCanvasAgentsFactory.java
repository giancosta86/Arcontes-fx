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

import info.gianlucacosta.arcontes.graphs.Graph;
import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.helios.metainfo.MetaInfoAgent;

/**
 * Service used by a graph canvas to create metainfo agents needed while
 * interacting with users
 */
public interface GraphCanvasAgentsFactory {

    MetaInfoAgent<Graph> getAgentForGraph(Graph graph);

    MetaInfoAgent<Vertex> getAgentForVertexCreation(GraphContext graphContext, Vertex vertex);

    MetaInfoAgent<Vertex> getAgentForVertexEditing(GraphContext graphContext, Vertex vertex);

    MetaInfoAgent<Link> getAgentForLinkCreation(GraphContext graphContext, Link link);

    MetaInfoAgent<Link> getAgentForLinkEditing(GraphContext graphContext, Link link);

}
