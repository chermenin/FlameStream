package com.spbsu.flamestream.example.bl.text_classifier.ops;

import com.spbsu.flamestream.core.graph.SerializableFunction;
import com.spbsu.flamestream.example.bl.text_classifier.model.containers.ClassifierOutput;
import com.spbsu.flamestream.example.bl.text_classifier.model.Prediction;

import java.util.stream.Stream;

public class PredictionFilter implements SerializableFunction<ClassifierOutput, Stream<Prediction>> {

    @Override
    public Stream<Prediction> apply(ClassifierOutput output) {
        if (output instanceof Prediction) {
            return Stream.of((Prediction) output);
        } else {
            return Stream.of();
        }
    }
}