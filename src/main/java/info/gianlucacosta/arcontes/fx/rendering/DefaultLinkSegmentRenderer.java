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
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import javafx.geometry.Point2D;

/**
 * Basic implementation of LinkSegmentRenderer
 */
class DefaultLinkSegmentRenderer extends LinkLine implements LinkSegmentRenderer {

    private final MetaInfoRepository metaInfoRepository;
    private final LinkPoint startLinkPoint;
    private final LinkPoint endLinkPoint;

    public DefaultLinkSegmentRenderer(MetaInfoRepository metaInfoRepository, LinkPoint startLinkPoint, LinkPoint endLinkPoint) {
        this.metaInfoRepository = metaInfoRepository;
        this.startLinkPoint = startLinkPoint;
        this.endLinkPoint = endLinkPoint;
    }

    @Override
    public LinkPoint getStartLinkPoint() {
        return startLinkPoint;
    }

    @Override
    public LinkPoint getEndLinkPoint() {
        return endLinkPoint;
    }

    public void render() {
        Point2D startPoint = metaInfoRepository.getMetaInfo(startLinkPoint, LinkPointPositionInfo.class).getPoint();
        Point2D endPoint = metaInfoRepository.getMetaInfo(endLinkPoint, LinkPointPositionInfo.class).getPoint();

        setStartX(startPoint.getX());
        setStartY(startPoint.getY());

        setEndX(endPoint.getX());
        setEndY(endPoint.getY());
    }

}
