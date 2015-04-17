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

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.operation.predicate.RectangleIntersects;
import info.gianlucacosta.arcontes.fx.rendering.metainfo.*;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.arcontes.graphs.metainfo.LabelInfo;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Basic implementation of LinkRenderer
 */
class DefaultLinkRenderer extends Group implements LinkRenderer {

    private final MetaInfoRepository metaInfoRepository;
    private final Link link;
    private final List<DefaultLinkSegmentRenderer> linkSegments = new ArrayList<>();
    private final List<DefaultLinkPointRenderer> internalPointRenderers = new ArrayList<>();
    private final Text linkLabel;
    private final Line labelConnector;
    private final List<Pair<? extends EventType<?>, ? extends EventHandler<?>>> linkSegmentsEventHandlers = new ArrayList<>();
    private final List<Pair<? extends EventType<?>, ? extends EventHandler<?>>> internalPointRenderersEventHandlers = new ArrayList<>();
    private final LinkPoint firstCenterLinkPoint = new DefaultLinkPoint();
    private final LinkPoint secondCenterLinkPoint = new DefaultLinkPoint();
    private LinkInternalPointsInfo previousLinkInternalPointsInfo;
    private LinkTailRenderer tailRenderer;

    public DefaultLinkRenderer(MetaInfoRepository metaInfoRepository, Link link) {
        this.metaInfoRepository = metaInfoRepository;
        this.link = link;

        ObservableList<Node> childNodes = getChildren();

        labelConnector = new Line();
        labelConnector.setStrokeLineCap(StrokeLineCap.ROUND);

        childNodes.add(labelConnector);

        linkLabel = new Text();
        linkLabel.setTextOrigin(VPos.TOP);
        childNodes.add(linkLabel);

        addEventHandler(Event.ANY, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                event.consume();
            }

        });
    }

    @Override
    public Link getLink() {
        return link;
    }

    @Override
    public void render() {
        ObservableList<Node> childNodes = getChildren();

        LinkInternalPointsInfo currentLinkInternalPointsInfo = metaInfoRepository.getMetaInfo(link, LinkInternalPointsInfo.class);

        boolean internalPointsListChanged = !Objects.equals(previousLinkInternalPointsInfo, currentLinkInternalPointsInfo);
        if (internalPointsListChanged) {
            int insertionIndex = 1;

            resetLinkSegments();

            for (DefaultLinkSegmentRenderer linkSegment : linkSegments) {
                childNodes.add(insertionIndex, linkSegment);
                insertionIndex++;
            }

            resetInternalPointRenderers(currentLinkInternalPointsInfo);

            for (DefaultLinkPointRenderer internalPointRenderer : internalPointRenderers) {
                childNodes.add(insertionIndex, internalPointRenderer);
                insertionIndex++;
            }

            childNodes.remove(tailRenderer);
            tailRenderer = new LinkTailRenderer(metaInfoRepository, link, linkSegments.get(linkSegments.size() - 1));
            childNodes.add(tailRenderer);
        }

        DefaultLinkSegmentRenderer firstSegment = linkSegments.get(0);
        metaInfoRepository.putMetaInfo(firstSegment.getStartLinkPoint(), new DefaultLinkPointPositionInfo(getFirstCenter()));

        DefaultLinkSegmentRenderer lastSegment = linkSegments.get(linkSegments.size() - 1);
        metaInfoRepository.putMetaInfo(lastSegment.getEndLinkPoint(), new DefaultLinkPointPositionInfo(getSecondCenter()));

        for (DefaultLinkSegmentRenderer linkSegment : linkSegments) {
            linkSegment.render();
        }

        previousLinkInternalPointsInfo = currentLinkInternalPointsInfo;

        for (LinkPointRenderer internalPointRenderer : internalPointRenderers) {
            LinkPointRenderingInfo linkPointRenderingInfo = metaInfoRepository.getMetaInfo(internalPointRenderer.getLinkPoint(), LinkPointRenderingInfo.class);
            internalPointRenderer.render(linkPointRenderingInfo);
        }

        LinkRenderingInfo linkRenderingInfo = metaInfoRepository.getMetaInfo(link, LinkRenderingInfo.class);
        for (LinkLine linkSegment : linkSegments) {
            linkSegment.applyRenderingInfo(linkRenderingInfo);
        }

        if (!internalPointRenderers.isEmpty()) {
            for (DefaultLinkPointRenderer internalPointRenderer : internalPointRenderers) {
                LinkPointRenderingInfo linkPointRenderingInfo = metaInfoRepository.getMetaInfo(internalPointRenderer.getLinkPoint(), LinkPointRenderingInfo.class);
                internalPointRenderer.render(linkPointRenderingInfo);
            }
        }

        LinkLabelRenderingInfo linkLabelRenderingInfo = metaInfoRepository.getMetaInfo(link, LinkLabelRenderingInfo.class);
        linkLabel.setFont(linkLabelRenderingInfo.getFont());
        linkLabel.setFill(linkLabelRenderingInfo.getFontColor());

        String labelText = metaInfoRepository.getMetaInfo(link, LabelInfo.class).getLabel();
        linkLabel.setText(labelText);

        Point2D labelConnectionPoint = getLabelConnectorJoiningPoint();

        Point2D labelCenter;
        if (metaInfoRepository.hasMetaInfo(link, LinkLabelCenterInfo.class)) {
            labelCenter = metaInfoRepository.getMetaInfo(link, LinkLabelCenterInfo.class).getPoint();
        } else {
            labelCenter = getDefaultLabelCenter();
        }

        Bounds labelBounds = linkLabel.getBoundsInParent();
        linkLabel.setX(labelCenter.getX() - labelBounds.getWidth() / 2);
        linkLabel.setY(labelCenter.getY() - labelBounds.getHeight() / 2);

        labelConnector.setStartX(labelCenter.getX());
        labelConnector.setStartY(labelCenter.getY());

        labelConnector.setEndX(labelConnectionPoint.getX());
        labelConnector.setEndY(labelConnectionPoint.getY());

        LinkLabelConnectorRenderingInfo labelConnectorRenderingInfo = metaInfoRepository.getMetaInfo(link, LinkLabelConnectorRenderingInfo.class);
        labelConnector.setStrokeWidth(labelConnectorRenderingInfo.getLineSize());
        labelConnector.setStroke(labelConnectorRenderingInfo.getColor());

        labelConnector.getStrokeDashArray().clear();
        labelConnector.getStrokeDashArray().addAll(labelConnectorRenderingInfo.getLineDashItems());
        labelConnector.setOpacity(labelConnectorRenderingInfo.getOpacity());

        tailRenderer.render();
    }

    private List<LinkPoint> getAllLinkPoints() {
        List<LinkPoint> result = new ArrayList<>();

        result.add(firstCenterLinkPoint);

        List<LinkPoint> internalPoints = metaInfoRepository.getMetaInfo(link, LinkInternalPointsInfo.class).getInternalPoints();
        for (LinkPoint internalPoint : internalPoints) {
            result.add(internalPoint);
        }

        result.add(secondCenterLinkPoint);

        return result;
    }

    private void resetLinkSegments() {
        for (DefaultLinkSegmentRenderer linkSegment : linkSegments) {
            getChildren().remove(linkSegment);
        }

        linkSegments.clear();

        List<LinkPoint> totalLinkPoints = getAllLinkPoints();

        for (int i = 0; i < totalLinkPoints.size() - 1; i++) {
            LinkPoint segmentStartPoint = totalLinkPoints.get(i);
            LinkPoint segmentEndPoint = totalLinkPoints.get(i + 1);

            DefaultLinkSegmentRenderer linkSegment = new DefaultLinkSegmentRenderer(metaInfoRepository, segmentStartPoint, segmentEndPoint);

            for (Pair<? extends EventType<?>, ? extends EventHandler<?>> eventHandlerPair : linkSegmentsEventHandlers) {
                EventType eventType = eventHandlerPair.getKey();
                EventHandler eventHandler = eventHandlerPair.getValue();

                linkSegment.addEventHandler(eventType, eventHandler);
            }

            linkSegments.add(linkSegment);
        }
    }

    private Point2D getFirstCenter() {
        Vertex firstVertex = link.getAnchors().getLeft();
        return metaInfoRepository.getMetaInfo(firstVertex, VertexCenterInfo.class).getPoint();
    }

    private Point2D getSecondCenter() {
        Vertex secondVertex = link.getAnchors().getRight();
        return metaInfoRepository.getMetaInfo(secondVertex, VertexCenterInfo.class).getPoint();
    }

    private Point2D getLabelConnectorJoiningPoint() {
        List<LinkPoint> internalPoints = metaInfoRepository.getMetaInfo(link, LinkInternalPointsInfo.class).getInternalPoints();

        if (internalPoints.isEmpty()) {
            return getDefaultLabelCenter();
        } else {
            LinkPoint chosenInternalPoint = internalPoints.get(internalPoints.size() / 2);
            return metaInfoRepository.getMetaInfo(chosenInternalPoint, LinkPointPositionInfo.class).getPoint();
        }
    }

    @Override
    public <TEvent extends Event> void addEventHandlerForLinkSegments(EventType<TEvent> eventType, EventHandler<? super TEvent> eventHandler) {
        for (Line linkSegment : linkSegments) {
            linkSegment.addEventFilter(eventType, eventHandler);
        }

        linkSegmentsEventHandlers.add(new Pair<>(eventType, eventHandler));
    }

    @Override
    public <TEvent extends Event> void removeEventHandlerForLinkSegments(EventType<TEvent> eventType, EventHandler<? super TEvent> eventHandler) {
        for (Line linkSegment : linkSegments) {
            linkSegment.removeEventHandler(eventType, eventHandler);
        }

        linkSegmentsEventHandlers.remove(new Pair<>(eventType, eventHandler));
    }

    @Override
    public <TEvent extends Event> void addEventHandlerForLabel(EventType<TEvent> eventType, EventHandler<? super TEvent> eventHandler) {
        linkLabel.addEventHandler(eventType, eventHandler);
    }

    @Override
    public <TEvent extends Event> void removeEventHandlerForLabel(EventType<TEvent> eventType, EventHandler<? super TEvent> eventHandler) {
        linkLabel.removeEventHandler(eventType, eventHandler);
    }

    @Override
    public boolean isInRectangle(Rectangle rectangle) {
        boolean intersectionPossible = false;

        for (Line line : linkSegments) {
            if (line.intersects(rectangle.getBoundsInParent())) {
                intersectionPossible = true;
                break;
            }
        }

        if (!intersectionPossible) {
            return false;
        }

        GeometryFactory geometryFactory = new GeometryFactory();

        Coordinate[] rectangleCoordinates = new Coordinate[]{
                new Coordinate(rectangle.getX(), rectangle.getY()),
                new Coordinate(rectangle.getX() + rectangle.getWidth(), rectangle.getY()),
                new Coordinate(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight()),
                new Coordinate(rectangle.getX(), rectangle.getY() + rectangle.getHeight()),
                new Coordinate(rectangle.getX(), rectangle.getY()),};

        LinearRing rectangleRing = new LinearRing(new CoordinateArraySequence(rectangleCoordinates), geometryFactory);

        Polygon rectanglePolygon = new Polygon(rectangleRing, null, geometryFactory);

        for (Line line : linkSegments) {
            Coordinate lineStart = new Coordinate(line.getStartX(), line.getStartY());
            Coordinate lineEnd = new Coordinate(line.getEndX(), line.getEndY());

            Geometry lineGeometry = geometryFactory.createLineString(new Coordinate[]{lineStart, lineEnd});

            if (RectangleIntersects.intersects(rectanglePolygon, lineGeometry)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Point2D getDefaultLabelCenter() {
        Point2D firstCenter = getFirstCenter();
        Point2D secondCenter = getSecondCenter();

        double resultX = Math.min(firstCenter.getX(), secondCenter.getX()) + (Math.abs(secondCenter.getX() - firstCenter.getX()) / 2);
        double resultY = Math.min(firstCenter.getY(), secondCenter.getY()) + (Math.abs(secondCenter.getY() - firstCenter.getY()) / 2);

        return new Point2D(resultX, resultY);
    }

    private void resetInternalPointRenderers(LinkInternalPointsInfo linkInternalPointsInfo) {
        for (DefaultLinkPointRenderer internalPointRenderer : internalPointRenderers) {
            getChildren().remove(internalPointRenderer);
        }

        internalPointRenderers.clear();

        for (LinkPoint internalLinkPoint : linkInternalPointsInfo.getInternalPoints()) {
            DefaultLinkPointRenderer internalPointRenderer = new DefaultLinkPointRenderer(metaInfoRepository, link, internalLinkPoint);

            for (Pair<? extends EventType<?>, ? extends EventHandler<?>> eventHandlerPair : internalPointRenderersEventHandlers) {
                EventType eventType = eventHandlerPair.getKey();
                EventHandler eventHandler = eventHandlerPair.getValue();

                internalPointRenderer.addEventHandler(eventType, eventHandler);
            }

            internalPointRenderers.add(internalPointRenderer);
        }
    }

    @Override
    public <TEvent extends Event> void addEventHandlerForInternalPointRenderers(EventType<TEvent> eventType, EventHandler<? super TEvent> eventHandler) {
        for (LinkPointRenderer internalPointRenderer : internalPointRenderers) {
            internalPointRenderer.addEventHandler(eventType, eventHandler);
        }

        internalPointRenderersEventHandlers.add(new Pair<>(eventType, eventHandler));
    }

    @Override
    public <TEvent extends Event> void removeEventHandlerForInternalPointRenderers(EventType<TEvent> eventType, EventHandler<? super TEvent> eventHandler) {
        for (LinkPointRenderer internalPointRenderer : internalPointRenderers) {
            internalPointRenderer.removeEventHandler(eventType, eventHandler);
        }

        internalPointRenderersEventHandlers.remove(new Pair<>(eventType, eventHandler));
    }

    @Override
    public LinkPoint getFirstCenterLinkPoint() {
        return firstCenterLinkPoint;
    }

    @Override
    public LinkPoint getSecondCenterLinkPoint() {
        return secondCenterLinkPoint;
    }

}
