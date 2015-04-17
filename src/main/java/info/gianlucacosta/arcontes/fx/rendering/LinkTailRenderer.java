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
import info.gianlucacosta.arcontes.fx.rendering.metainfo.LinkRenderingInfo;
import info.gianlucacosta.arcontes.fx.rendering.metainfo.LinkTailRenderingInfo;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 * Renders the tail of a link, as an arrow with 2 segments
 */
class LinkTailRenderer extends Group {

    private final MetaInfoRepository metaInfoRepository;
    private final Link link;
    private final LinkSegmentRenderer targetSegmentRenderer;
    private final LinkLine pSegment = new LinkLine();
    private final LinkLine qSegment = new LinkLine();

    public LinkTailRenderer(MetaInfoRepository metaInfoRepository, Link link, LinkSegmentRenderer targetSegmentRenderer) {
        this.metaInfoRepository = metaInfoRepository;
        this.link = link;
        this.targetSegmentRenderer = targetSegmentRenderer;

        ObservableList<Node> childNodes = getChildren();

        childNodes.add(pSegment);
        childNodes.add(qSegment);
    }

    public void render() {
        LinkTailRenderingInfo tailRenderingInfo = metaInfoRepository.getMetaInfo(link, LinkTailRenderingInfo.class);

        pSegment.setVisible(tailRenderingInfo.isVisible());
        qSegment.setVisible(tailRenderingInfo.isVisible());

        if (!isVisible()) {
            return;
        }

        Point2D start = metaInfoRepository.getMetaInfo(targetSegmentRenderer.getStartLinkPoint(), LinkPointPositionInfo.class).getPoint();
        double startX = start.getX();
        double startY = start.getY();

        Point2D stop = metaInfoRepository.getMetaInfo(targetSegmentRenderer.getEndLinkPoint(), LinkPointPositionInfo.class).getPoint();
        double stopX = stop.getX();
        double stopY = stop.getY();

        double anchorX = startX + tailRenderingInfo.getRelativePositionX() * (stopX - startX);
        double anchorY = startY + tailRenderingInfo.getRelativePositionY() * (stopY - startY);

        double gamma = Math.atan2(anchorY - startY, anchorX - startX);
        double dPx = tailRenderingInfo.getSize() * Math.cos(gamma - tailRenderingInfo.getAngle());
        double dPy = tailRenderingInfo.getSize() * Math.sin(gamma - tailRenderingInfo.getAngle());

        double epsilon = Math.PI - gamma - tailRenderingInfo.getAngle();
        double dQx = tailRenderingInfo.getSize() * Math.cos(epsilon);
        double dQy = tailRenderingInfo.getSize() * Math.sin(epsilon);

        double pX;
        double pY;
        double qX;
        double qY;

        if (anchorY <= startY) {
            pX = anchorX - dPx;
            pY = anchorY - dPy;

            qX = anchorX + dQx;
            qY = anchorY - dQy;
        } else {
            pX = anchorX + dQx;
            pY = anchorY - dQy;

            qX = anchorX - dPx;
            qY = anchorY - dPy;
        }

        pSegment.setStartX(anchorX);
        pSegment.setStartY(anchorY);
        pSegment.setEndX(pX);
        pSegment.setEndY(pY);

        qSegment.setStartX(anchorX);
        qSegment.setStartY(anchorY);
        qSegment.setEndX(qX);
        qSegment.setEndY(qY);

        LinkRenderingInfo linkRenderingInfo = metaInfoRepository.getMetaInfo(link, LinkRenderingInfo.class);
        pSegment.applyRenderingInfo(linkRenderingInfo);
        qSegment.applyRenderingInfo(linkRenderingInfo);
    }

}
