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

package info.gianlucacosta.arcontes.fx.algorithms;

import info.gianlucacosta.arcontes.algorithms.AbstractAlgorithmOutput;
import javafx.scene.control.TextArea;

/**
 * Implementation of AlgorithmOutput writing to a JavaFX text area
 */
public class TextAreaAlgorithmOutput extends AbstractAlgorithmOutput {

    private final TextArea textArea;

    public TextAreaAlgorithmOutput(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void print(Object output) {
        textArea.appendText(output.toString());
    }

    @Override
    public void println() {
        textArea.appendText("\n");
    }

}
