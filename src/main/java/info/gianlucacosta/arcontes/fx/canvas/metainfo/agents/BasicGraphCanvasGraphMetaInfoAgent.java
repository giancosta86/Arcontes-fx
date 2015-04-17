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

package info.gianlucacosta.arcontes.fx.canvas.metainfo.agents;

import info.gianlucacosta.arcontes.fx.canvas.metainfo.SelectionRectangleInfo;
import info.gianlucacosta.arcontes.fx.rendering.metainfo.GraphRenderingInfo;
import info.gianlucacosta.arcontes.graphs.Graph;
import info.gianlucacosta.helios.metainfo.AbstractMetaInfoAgent;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;

/**
 * Metainfo agent writing the basic information required to have a graph (not
 * its components, that should be handled by other, more specific, agents)
 * correctly rendered by DefaultGraphCanvas
 */
public class BasicGraphCanvasGraphMetaInfoAgent extends AbstractMetaInfoAgent<Graph> {

    private final GraphRenderingInfo graphRenderingInfo;
    private final SelectionRectangleInfo selectionRectangleInfo;

    public BasicGraphCanvasGraphMetaInfoAgent(GraphRenderingInfo graphRenderingInfo, SelectionRectangleInfo selectionRectangleInfo) {
        super(false);
        this.graphRenderingInfo = graphRenderingInfo;
        this.selectionRectangleInfo = selectionRectangleInfo;
    }

    @Override
    protected boolean doAct(MetaInfoRepository metaInfoRepository, Graph graph) {
        metaInfoRepository.putMetaInfo(graph, graphRenderingInfo);
        metaInfoRepository.putMetaInfo(graph, selectionRectangleInfo);
        return true;
    }

}
