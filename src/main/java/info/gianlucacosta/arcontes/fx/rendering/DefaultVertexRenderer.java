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

import info.gianlucacosta.arcontes.fx.rendering.metainfo.VertexCenterInfo;
import info.gianlucacosta.arcontes.fx.rendering.metainfo.VertexRenderingInfo;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.arcontes.graphs.metainfo.LabelInfo;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Basic implementation of VertexRenderer
 */
class DefaultVertexRenderer extends Group implements VertexRenderer {

    private final MetaInfoRepository metaInfoRepository;
    private final EventHandler<MouseEvent> dragStarter;
    private final Vertex vertex;
    private final Rectangle vertexBox;
    private final Text labelTextBox;

    public DefaultVertexRenderer(MetaInfoRepository metaInfoRepository, Vertex vertex) {
        this.metaInfoRepository = metaInfoRepository;
        this.vertex = vertex;

        dragStarter = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                startFullDrag();
            }

        };

        vertexBox = new Rectangle();
        vertexBox.addEventHandler(MouseEvent.DRAG_DETECTED, dragStarter);
        getChildren().add(vertexBox);

        labelTextBox = new Text();
        labelTextBox.addEventHandler(MouseEvent.DRAG_DETECTED, dragStarter);
        labelTextBox.setTextOrigin(VPos.TOP);
        getChildren().add(labelTextBox);

        addEventHandler(Event.ANY, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                event.consume();
            }

        });
    }

    @Override
    public Vertex getVertex() {
        return vertex;
    }

    @Override
    public void render() {
        VertexRenderingInfo vertexRenderingInfo = metaInfoRepository.getMetaInfo(vertex, VertexRenderingInfo.class);

        Point2D vertexCenter = metaInfoRepository.getMetaInfo(vertex, VertexCenterInfo.class).getPoint();

        String vertexLabel = metaInfoRepository.getMetaInfo(vertex, LabelInfo.class).getLabel();

        labelTextBox.setFont(vertexRenderingInfo.getFont());
        labelTextBox.setText(vertexLabel);
        labelTextBox.setFill(vertexRenderingInfo.getFontColor());
        labelTextBox.setStrokeWidth(0);

        Bounds textBounds = labelTextBox.getBoundsInParent();
        double labelWidth = textBounds.getWidth();
        double labelHeight = textBounds.getHeight();
        double labelX = vertexCenter.getX() - labelWidth / 2;
        double labelY = vertexCenter.getY() - labelHeight / 2;
        labelTextBox.setX(labelX);
        labelTextBox.setY(labelY);

        double padding = vertexRenderingInfo.getPadding();

        vertexBox.setFill(vertexRenderingInfo.getBackgroundColor());
        vertexBox.setStrokeWidth(vertexRenderingInfo.getBorderSize());
        vertexBox.setStroke(vertexRenderingInfo.getBorderColor());

        vertexBox.setWidth(labelWidth + 2 * padding);
        vertexBox.setHeight(labelHeight + 2 * padding);
        vertexBox.setLayoutX(labelX - padding);
        vertexBox.setLayoutY(labelY - padding);
        vertexBox.setArcWidth(2 * padding);
        vertexBox.setArcHeight(2 * padding);
    }

    @Override
    public boolean isInRectangle(Rectangle rectangle) {
        return rectangle.intersects(getBoundsInParent());
    }

}
