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

import info.gianlucacosta.arcontes.fx.rendering.metainfo.LinkPointPositionInfo;
import info.gianlucacosta.arcontes.fx.rendering.metainfo.LinkPointRenderingInfo;
import info.gianlucacosta.arcontes.fx.rendering.metainfo.LinkRenderingInfo;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Basic implementation of LinkPointRenderer
 */
class DefaultLinkPointRenderer extends Circle implements LinkPointRenderer {

    private final MetaInfoRepository metaInfoRepository;
    private final Link link;
    private final LinkPoint linkPoint;

    public DefaultLinkPointRenderer(MetaInfoRepository metaInfoRepository, Link link, LinkPoint linkPoint) {
        this.metaInfoRepository = metaInfoRepository;
        this.link = link;
        this.linkPoint = linkPoint;
    }

    @Override
    public LinkPoint getLinkPoint() {
        return linkPoint;
    }

    @Override
    public void render(LinkPointRenderingInfo linkPointRenderingInfo) {
        setOpacity(linkPointRenderingInfo.getOpacity());

        Point2D centerPoint = metaInfoRepository.getMetaInfo(linkPoint, LinkPointPositionInfo.class).getPoint();

        setCenterX(centerPoint.getX());
        setCenterY(centerPoint.getY());

        LinkRenderingInfo linkRenderingInfo = metaInfoRepository.getMetaInfo(link, LinkRenderingInfo.class);
        double radius = linkRenderingInfo.getLineSize() * linkPointRenderingInfo.getRadiusFactor();

        setRadius(radius);

        Color fillColor = linkPointRenderingInfo.getColor();
        if (fillColor.equals(Color.TRANSPARENT)) {
            fillColor = linkRenderingInfo.getColor();
        }

        setFill(fillColor);
    }

}
