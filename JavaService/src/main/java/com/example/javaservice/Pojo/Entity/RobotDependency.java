package com.example.javaservice.Core;

import java.io.Serializable;
import java.util.Map;

public class RobotDependency implements Serializable {
    private int dependencyId;
    private Map<Integer, TransferList> transMap;
}
