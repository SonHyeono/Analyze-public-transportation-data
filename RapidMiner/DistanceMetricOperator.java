package com.rapidminer.extension.operator;

import java.util.List;
import java.util.logging.Level;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeAttribute;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.tools.LogService;
import com.rapidminer.tools.Ontology;

public class DistanceMetricOperator extends Operator {

    private InputPort exampleSetInput = getInputPorts().createPort("example set");
    private InputPort exampleSet2Input = getInputPorts().createPort("example set 2");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");
    public static final String STARTING_POINT_LAT = "Starting_Point_lat";
    public static final String STARTING_POINT_LNG = "Starting_Point_lng";
    public static final String DESTINATION_LAT = "Destination_lat";
    public static final String DESTINATION_LNG = "Destination_lng";

    public DistanceMetricOperator(OperatorDescription description) {
        super(description);
        getTransformer().addPassThroughRule(exampleSetInput, exampleSetOutput);
    }
    @Override
    public void doWork() throws OperatorException {

        ExampleSet exampleSet = exampleSetInput.getData(ExampleSet.class);
        ExampleSet exampleSet2 = exampleSet2Input.getData(ExampleSet.class);
        String AttributeName = getParameterAsString(STARTING_POINT_LAT);
        String AttributeName2 = getParameterAsString(STARTING_POINT_LNG);
        String AttributeName3 = getParameterAsString(DESTINATION_LAT);
        String AttributeName4 = getParameterAsString(DESTINATION_LNG);
        Attributes attributes = exampleSet.getAttributes();
        Attributes attributes2 = exampleSet2.getAttributes();
        Attribute attribute = attributes.get(AttributeName);
        Attribute attribute2 = attributes.get(AttributeName2);
        Attribute attribute3 = attributes2.get(AttributeName3);
        Attribute attribute4 = attributes2.get(AttributeName4);


        try {
            String newName = "기존환승센터들과_거리의합";
            Attribute targetAttribute = AttributeFactory.createAttribute(newName, Ontology.REAL);
            attributes.addRegular(targetAttribute);
            exampleSet.getExampleTable().addAttribute(targetAttribute);
            for (Example example : exampleSet) {
                double sum = 0.0;
                for (Example example1 : exampleSet2) {
                    double R = 6373.0;
                    double s_lat = example.getValue(attribute);
                    double s_lng = example.getValue(attribute2);
                    double e_lat = example1.getValue(attribute3);
                    double e_lng = example1.getValue(attribute4);

                    s_lat = s_lat * Math.PI / 180.0;
                    s_lng = deg2rad(s_lng);
                    e_lat = deg2rad(e_lat);
                    e_lng = deg2rad(e_lng);
                    double d = Math.pow(Math.sin((e_lat - s_lat) / 2), 2) + Math.cos(s_lat) * Math.cos(e_lat) * Math.pow(Math.sin((e_lng - s_lng) / 2), 2);
                    double distance = 2 * R * Math.asin(Math.sqrt(d));
                    sum += distance;
                }
                example.setValue(targetAttribute, sum);
            }
        } catch (Exception e) {
            LogService.getRoot().log(Level.WARNING, e.toString());
        }
        exampleSetOutput.deliver(exampleSet);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeAttribute(STARTING_POINT_LAT,
                "The attribute which contains the names of the annotations to be created. Should be unique.",
                exampleSetInput, false ));
        types.add(new ParameterTypeAttribute(STARTING_POINT_LNG,
                "The attribute which contains the names of the annotations to be created. Should be unique.",
                exampleSetInput, false ));
        types.add(new ParameterTypeAttribute(DESTINATION_LAT,
                "The attribute which contains the names of the annotations to be created. Should be unique.",
                exampleSet2Input, false ));
        types.add(new ParameterTypeAttribute(DESTINATION_LNG,
                "The attribute which contains the names of the annotations to be created. Should be unique.",
                exampleSet2Input, false ));
        return types;
    }
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
}

