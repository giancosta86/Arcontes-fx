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

import info.gianlucacosta.arcontes.fx.canvas.metainfo.DefaultLinkStyleInfo;
import info.gianlucacosta.arcontes.fx.canvas.metainfo.LinkStyleInfo;
import info.gianlucacosta.arcontes.fx.rendering.metainfo.DefaultLinkInternalPointsInfo;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.metainfo.LabelInfo;
import info.gianlucacosta.helios.metainfo.AbstractMetaInfoAgent;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;

/**
 * Metainfo agent writing the basic information required to have a link
 * correctly rendered by DefaultGraphCanvas
 */
public class BasicGraphCanvasLinkMetaInfoAgent extends AbstractMetaInfoAgent<Link> {

    private final LinkStyleInfo linkStyleInfo;
    private final LabelInfo linkLabelInfo;

    public BasicGraphCanvasLinkMetaInfoAgent(LinkStyleInfo linkStyleInfo, LabelInfo linkLabelInfo) {
        super(false);

        this.linkLabelInfo = linkLabelInfo;
        this.linkStyleInfo = new DefaultLinkStyleInfo(linkStyleInfo);
    }

    @Override
    protected boolean doAct(MetaInfoRepository metaInfoRepository, Link link) {
        metaInfoRepository.putMetaInfo(link, linkLabelInfo);

        metaInfoRepository.putMetaInfo(link, linkStyleInfo);
        metaInfoRepository.putMetaInfo(link, new DefaultLinkInternalPointsInfo());

        return true;
    }

}
