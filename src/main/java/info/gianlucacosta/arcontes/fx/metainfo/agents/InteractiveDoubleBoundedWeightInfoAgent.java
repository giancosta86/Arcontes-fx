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

package info.gianlucacosta.arcontes.fx.metainfo.agents;

import info.gianlucacosta.arcontes.formatting.CommonDecimalFormat;
import info.gianlucacosta.arcontes.graphs.metainfo.CapacityInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.DefaultBoundedWeightInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.WeightInfo;
import info.gianlucacosta.helios.application.io.CommonInputService;
import info.gianlucacosta.helios.conditions.Condition;
import info.gianlucacosta.helios.conditions.PredicateBasedCondition;
import info.gianlucacosta.helios.metainfo.AbstractMetaInfoAgent;
import info.gianlucacosta.helios.metainfo.MetaInfoException;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import info.gianlucacosta.helios.predicates.DoubleRangePredicate;

import java.text.DecimalFormat;

/**
 * Sets the double bounded weight metainfo of the given handle, after asking its
 * values to the user and validating them
 */
public class InteractiveDoubleBoundedWeightInfoAgent<THandle> extends AbstractMetaInfoAgent<THandle> {

    private final CommonInputService commonInputService;
    private final String weightInputPrompt;
    private final String capacityInputPrompt;
    private final double minimumWeight;
    private final double maximumCapacity;

    public InteractiveDoubleBoundedWeightInfoAgent(CommonInputService commonInputService) {
        this(commonInputService, "Weight:", "Capacity:");
    }

    public InteractiveDoubleBoundedWeightInfoAgent(CommonInputService commonInputService, String weightInputPrompt, String capacityInputPrompt) {
        this(commonInputService, weightInputPrompt, capacityInputPrompt, 0, Double.POSITIVE_INFINITY);
    }

    public InteractiveDoubleBoundedWeightInfoAgent(CommonInputService commonInputService, String weightInputPrompt, String capacityInputPrompt, double minimumWeight, double maximumCapacity) {
        super(true);
        this.commonInputService = commonInputService;
        this.weightInputPrompt = weightInputPrompt;
        this.capacityInputPrompt = capacityInputPrompt;
        this.minimumWeight = minimumWeight;
        this.maximumCapacity = maximumCapacity;
    }

    @Override
    protected boolean doAct(MetaInfoRepository metaInfoRepository, THandle handle) {
        double currentWeight;
        try {
            WeightInfo<Double> weightInfo = metaInfoRepository.getMetaInfo(handle, WeightInfo.class);
            currentWeight = weightInfo.getWeight();
        } catch (MetaInfoException ex) {
            currentWeight = minimumWeight;
        }

        Double newWeight = commonInputService.askForDouble(
                weightInputPrompt,
                currentWeight,
                getWeightInputCondition(handle));

        if (newWeight == null) {
            return false;
        }

        Double currentCapacity;
        try {
            CapacityInfo<Double> capacityInfo = metaInfoRepository.getMetaInfo(handle, CapacityInfo.class);
            currentCapacity = capacityInfo.getCapacity();

            if (currentCapacity.compareTo(newWeight) < 0) {
                currentCapacity = newWeight;
            }
        } catch (MetaInfoException ex) {
            currentCapacity = newWeight;
        }

        Double newCapacity = commonInputService.askForDouble(
                capacityInputPrompt,
                currentCapacity,
                getCapacityInputCondition(handle, newWeight));

        if (newCapacity == null) {
            return false;
        }

        DefaultBoundedWeightInfo<Double> boundedWeightInfo = new DefaultBoundedWeightInfo<>(newWeight, newCapacity);
        metaInfoRepository.putMetaInfo(handle, boundedWeightInfo);

        return true;
    }

    /**
     * @param handle the handle
     * @return the condition checked by the input dialog related to the weight
     */
    protected Condition<Double> getWeightInputCondition(THandle handle) {
        DecimalFormat decimalFormat = CommonDecimalFormat.getDecimalFormat();

        return new PredicateBasedCondition<>(
                new DoubleRangePredicate(minimumWeight, maximumCapacity),
                String.format("Invalid weight value! It must be between %s and %s", decimalFormat.format(minimumWeight), decimalFormat.format(maximumCapacity)));
    }

    /**
     * @param handle        the handle
     * @param currentWeight the current weight
     * @return the condition checked by the input dialog related to the capacity
     */
    protected Condition<Double> getCapacityInputCondition(THandle handle, double currentWeight) {
        DecimalFormat decimalFormat = CommonDecimalFormat.getDecimalFormat();

        return new PredicateBasedCondition<>(
                new DoubleRangePredicate(currentWeight, maximumCapacity),
                String.format("Invalid capacity value! It must be between %s and %s", decimalFormat.format(currentWeight), decimalFormat.format(maximumCapacity)));
    }

}
