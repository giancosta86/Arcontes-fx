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

import info.gianlucacosta.arcontes.fx.canvas.metainfo.DefaultVertexStyleInfo;
import info.gianlucacosta.arcontes.fx.canvas.metainfo.VertexStyleInfo;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.arcontes.graphs.metainfo.LabelInfo;
import info.gianlucacosta.helios.metainfo.AbstractMetaInfoAgent;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;

/**
 * Metainfo agent writing the basic information required to have a vertex
 * correctly rendered by DefaultGraphCanvas
 */
public class BasicGraphCanvasVertexMetaInfoAgent extends AbstractMetaInfoAgent<Vertex> {

    private final VertexStyleInfo vertexStyleInfo;
    private final LabelInfo labelInfo;

    public BasicGraphCanvasVertexMetaInfoAgent(VertexStyleInfo vertexStyleInfo, LabelInfo labelInfo) {
        super(false);

        this.labelInfo = labelInfo;
        this.vertexStyleInfo = new DefaultVertexStyleInfo(vertexStyleInfo);
    }

    @Override
    protected boolean doAct(MetaInfoRepository metaInfoRepository, Vertex vertex) {
        metaInfoRepository.putMetaInfo(vertex, labelInfo);
        metaInfoRepository.putMetaInfo(vertex, vertexStyleInfo);

        return true;
    }

}
