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

import info.gianlucacosta.arcontes.fx.rendering.LinkPoint;
import info.gianlucacosta.helios.metainfo.MetaInfo;

import java.util.List;

/**
 * Metainfo containing the list of internal points to be rendered for a link
 */
public interface LinkInternalPointsInfo extends MetaInfo {

    List<LinkPoint> getInternalPoints();

}
