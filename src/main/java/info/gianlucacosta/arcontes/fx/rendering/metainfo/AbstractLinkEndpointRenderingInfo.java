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

package info.gianlucacosta.arcontes.fx.rendering.metainfo;

/**
 * Basic implementation of LinkEndpointRenderingInfo
 */
public abstract class AbstractLinkEndpointRenderingInfo implements LinkEndpointRenderingInfo {

    private boolean visible;
    private double angle;
    private double size;
    private double relativePositionX;
    private double relativePositionY;

    public AbstractLinkEndpointRenderingInfo() {
        visible = true;
        angle = Math.PI / 6;
        size = 15;
        relativePositionX = 3.0 / 4;
        relativePositionY = 3.0 / 4;
    }

    public AbstractLinkEndpointRenderingInfo(LinkEndpointRenderingInfo source) {
        this.visible = source.isVisible();
        this.angle = source.getAngle();
        this.size = source.getSize();
        this.relativePositionX = source.getRelativePositionX();
        this.relativePositionY = source.getRelativePositionY();
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        if (angle < 0 || angle >= 2 * Math.PI) {
            throw new IllegalArgumentException();
        }

        this.angle = angle;
    }

    @Override
    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }

        this.size = size;
    }

    @Override
    public double getRelativePositionX() {
        return relativePositionX;
    }

    public void setRelativePositionX(double relativePositionX) {
        if (relativePositionX < 0 || relativePositionX > 1) {
            throw new IllegalArgumentException();
        }

        this.relativePositionX = relativePositionX;
    }

    @Override
    public double getRelativePositionY() {
        return relativePositionY;
    }

    public void setRelativePositionY(double relativePositionY) {
        if (relativePositionY < 0 || relativePositionY > 1) {
            throw new IllegalArgumentException();
        }

        this.relativePositionY = relativePositionY;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LinkEndpointRenderingInfo)) {
            return false;
        }

        LinkEndpointRenderingInfo other = (LinkEndpointRenderingInfo) obj;

        return (visible == other.isVisible())
                && (angle == other.getAngle())
                && (size == other.getSize())
                && (relativePositionX == other.getRelativePositionX())
                && (relativePositionY == other.getRelativePositionY());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
