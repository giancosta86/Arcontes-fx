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

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Basic implementation of LinkPoint
 */
public class DefaultLinkPoint implements LinkPoint, Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id = UUID.randomUUID();

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LinkPoint)) {
            return false;
        }

        LinkPoint other = (LinkPoint) o;

        return Objects.equals(id, other.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
