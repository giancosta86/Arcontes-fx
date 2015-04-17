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

import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.helios.fx.events.EventHandlersTarget;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

/**
 * Renders a link
 */
public interface LinkRenderer extends EventHandlersTarget {

    Link getLink();

    Point2D getDefaultLabelCenter();

    boolean isInRectangle(Rectangle rectangle);

    void render();

    <TEvent extends Event> void addEventHandlerForLinkSegments(EventType<TEvent> eventType, EventHandler<? super TEvent> eventHandler);

    <TEvent extends Event> void removeEventHandlerForLinkSegments(EventType<TEvent> eventType, EventHandler<? super TEvent> eventHandler);

    <TEvent extends Event> void addEventHandlerForLabel(EventType<TEvent> eventType, EventHandler<? super TEvent> eventHandler);

    <TEvent extends Event> void removeEventHandlerForLabel(EventType<TEvent> eventType, EventHandler<? super TEvent> eventHandler);

    <TEvent extends Event> void addEventHandlerForInternalPointRenderers(EventType<TEvent> eventType, EventHandler<? super TEvent> eventHandler);

    <TEvent extends Event> void removeEventHandlerForInternalPointRenderers(EventType<TEvent> eventType, EventHandler<? super TEvent> eventHandler);

    LinkPoint getFirstCenterLinkPoint();

    LinkPoint getSecondCenterLinkPoint();

}
